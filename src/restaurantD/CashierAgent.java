package restaurantD;

import agent.Agent;
import restaurantD.CookAgent.OrderState;
import restaurantD.CustomerAgent.AgentEvent;
import restaurantD.WaiterAgent.CustomerState;
import restaurantD.gui.WaiterGui;
import restaurantD.interfaces.*;
import restaurantD.test.mock.EventLog;
import restaurantD.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierAgent extends Agent implements Cashier{
	public EventLog log;
	
	private String name;
	
	Timer timer = new Timer();
	public List<Check> checks
	= new ArrayList<Check>();
	public List<MarketCheck> marketChecks
	= new ArrayList<MarketCheck>();
	public enum CheckState{placed,calculating,done,payed,dishes,resolved}
	private double money;
	
	//Menu which will be static right now
	private HashMap<Integer,String> menu = new HashMap<Integer,String> ();
	private HashMap<String,Double> menuPrice = new HashMap<String,Double> ();
	
	public WaiterGui hostGui = null;

	public CashierAgent(String name) {
		super();
		this.name = name;
		setmenu();
		log = new EventLog();
		if(name.equals("Broke")){
			this.money = 0.0;
		}
		else
			this.money = 200.0;
	}
	public void setmenu(){
		menu.put(1, "Steak");
		menu.put(2, "Chicken");
		menu.put(3, "Salad");
		menu.put(4, "Pizza");
		menuPrice.put("Steak",15.99);
		menuPrice.put("Chicken",10.99);
		menuPrice.put("Salad",5.99);
		menuPrice.put("Pizza",8.99);
	}
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages
	public void getCheck(Customer c,String choice,Waiter w){
		checks.add(new Check(c,choice,CheckState.placed,w));
		stateChanged();
	}
	public void pay(Customer C, double num){
		print("Customer payed: " + num);
		for(int i=0;i<checks.size();i++){
			if(checks.get(i).getCustomer()==C){
				checks.get(i).setState(CheckState.payed);
			}
		}
		
	}
	public void cantPay(Customer C){
		for(int i=0;i<checks.size();i++){
			if(checks.get(i).getCustomer()==C){
				checks.get(i).setState(CheckState.dishes);
			}
		}
	}
	public void marketBill(Market M,double num){
		marketChecks.add(new MarketCheck(M,num,CheckState.placed));
		stateChanged();
	}
	// market for mock  market
	// market agent add ordercost() when food is delivered
	// give message to market from cashier about payed. Can use same class states 
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		for(int i=0;i<checks.size();i++){
			if(checks.get(i).getState() == CheckState.placed){
				checks.get(i).setState(CheckState.calculating);
				calculateCost(checks.get(i));
				return true;
			}
		}
		for(int i=0;i<checks.size();i++){
			if(checks.get(i).getState() == CheckState.done){
				giveCheckToWaiter(checks.get(i));
				return true;
			}
		}
		for(int i=0;i<checks.size();i++){
			if(checks.get(i).getState() == CheckState.payed){
				checkPayed(checks.get(i));
				return true;
			}
		}
		for(int i=0;i<checks.size();i++){
			if(checks.get(i).getState() == CheckState.dishes){
				washDishes(checks.get(i));
				return true;
			}
		}
		for(int i=0;i<marketChecks.size();i++){
			if(marketChecks.get(i).getState() == CheckState.placed){
				payMarket(marketChecks.get(i));
				return true;
			}
		}
		return false;
	}

	// Actions
	private void payMarket(MarketCheck C){
		log.add(new LoggedEvent("Money sent to Market with " + C.getNum()));
		C.getMarket().marketPayment(C.getNum());
		C.setState(CheckState.resolved);
	}
	private void checkPayed(Check C){
		C.getCustomer().youPayed();
		C.setState(CheckState.resolved);
	}
	private void washDishes(Check C){
		C.getCustomer().doDishes();
		C.setState(CheckState.resolved);
	}
	
	public void calculateCost(Check C){
		log.add(new LoggedEvent("Calculated Cost"));
		if(C.getChoice().equals("Steak")){
			C.setNum(15.99);
		}
		if(C.getChoice().equals("Chicken")){
			C.setNum(10.99);
		}
		if(C.getChoice().equals("Salad")){
			C.setNum(5.99);
		}
		if(C.getChoice().equals("Pizza")){
			C.setNum(8.99);
		}
		//C.timeCalc(); removed for unit testing will make it instantanious
		C.setState(CheckState.done);
	}
	
	public void giveCheckToWaiter(Check c){
		log.add(new LoggedEvent("Check sent to Waiter"));
		c.getWaiter().itCost(c.getCustomer(),c.getNum());
		//checks.remove(c);
	}
	

	//utilities

	public class Check{
		Customer cust;
		String choice;
		CheckState S; 
		Waiter w;
		double num;
		
		
		Check(Customer cust,String choice,CheckState S,Waiter w){
			this.cust = cust;
			this.choice = choice;
			this.S = S;
			this.w = w;
		}
		
		public void timeCalc(){
			timer.schedule(new TimerTask() { //HACK for now one timer for all foods, later will have different times for different objects
				Object cookie = 1;
				public void run() {
					print("Done calculating check= " + cookie);
					S=CheckState.done;
					stateChanged();
				}
			},
			5000);
		}
		public double getNum(){
			return num;
		}
		public void setNum(double num){
			this.num = num;
		}
		public void setState(CheckState S){
			this.S = S;
		}
		public CheckState getState(){
			return S;
		}
		public Waiter getWaiter(){
			return w;
		}
		public String getChoice(){
			return choice;
		}
		public Customer getCustomer(){
			return cust;
		}
	}
	
	public class MarketCheck{
		Market M;
		CheckState S; 
		double num;
		
		
		MarketCheck(Market M,double num,CheckState S){
			this.M = M;
			this.num = num;
			this.S = S;
		}
		
		public double getNum(){
			return num;
		}
		public void setNum(double num){
			this.num = num;
		}
		public void setState(CheckState S){
			this.S = S;
		}
		public CheckState getState(){
			return S;
		}
		
		public Market getMarket(){
			return M;
		}
	}
}

