package restaurantD;

import agent.Agent;
import restaurantD.gui.WaiterGui;
import restaurantD.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent implements Waiter{
	
	public List<MyCustomer> wCustomers
	= new ArrayList<MyCustomer>();
	
	public enum CustomerState{waiting,seated,readyToOrder,asked,ordered,orderPlaced,orderReady,served,done,gone,outOfOrder,askedCheck,waitingForCheck,recievingCheck, Paying}
	
	private String name;
	private Semaphore busy = new Semaphore(0,true);
	private boolean onBreak=false;

	
	//Hack menu is asked for from cook at moment waiter needs it to give to customer
	private HashMap<Integer,String> menu;
	private HashMap<String,Double> menuPrice;
	private HostAgent host = null;
	private WaiterGui gui = null;
	private Cashier cashier;

	//Setting the Cook for the waiter
	public CookAgent cook = null;
	
	public WaiterAgent(String name) {
		super();
		this.name=name;
	}
	
	
	public void setCashier(Cashier c){
		this.cashier = c;
	}
	// Hack to set the menu for Cook
	public void setmenu(HashMap<Integer,String> menu,HashMap<String,Double> menuPrice){
		this.menu= new HashMap<Integer,String>(menu);
		this.menuPrice= new HashMap<String,Double>(menuPrice);
	}
	public HashMap<Integer,String> getMenu(){
		return menu;
	}
	public HashMap<String,Double> getMenuPrice(){
		return menuPrice;
	}
	public String getMaitreDName() {
		return name;
	}
	public HostAgent getHost(){
		return host;
	}
	public String getName() {
		return name;
	}
	
	public boolean isOnBreak(){
		return onBreak;
	}

	// Messages
	public void SeatCustomer(Customer cust, int table){
		//print(this.getName() + " Got the customer" + "   Size: " + wCustomers.size()); //Working
		wCustomers.add(new MyCustomer(cust,table,CustomerState.waiting));
		//print("Size: " + wCustomers.size());//Working
		stateChanged();
	}
	public void reSeatCustomer(Customer cust, int table){
		//print(this.getName() + " Got the customer AGAIN" + "   Size: " + wCustomers.size()); //Working
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == cust) {
				wCustomers.get(i).setState(CustomerState.waiting);
				stateChanged();
			}
		}
		//print("Size: " + wCustomers.size());//Working
		stateChanged();
	}
	
	public void customerSeated(Customer Cust){///////////////////
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == Cust) { // Don't think == works need c1.equals(c2)
				wCustomers.get(i).setState(CustomerState.seated);
				stateChanged();
			}
		}
	}
	public void imReadyToOrder(Customer Cust){
		for (int i=0;i<wCustomers.size();i++){
			print("@");
			if (wCustomers.get(i).getCustomer() == Cust) {
				wCustomers.get(i).setState(CustomerState.readyToOrder);
				stateChanged();
				//print("Asked " + wCustomers.get(i).getCustomer().getName() +" for their order");
			}
			//print("#");
		}
	}
	public void hereIsMyChoice(Customer Cust, String Choice){
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == Cust) {
				wCustomers.get(i).setChoice(Choice);
				wCustomers.get(i).setState(CustomerState.ordered);
				stateChanged();
				//print(Cust.getName() + " will be having " + Choice);
			}
		}
		
	}
	public void orderIsReady(Customer Cust, String Choice){
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == Cust) {
				wCustomers.get(i).setState(CustomerState.orderReady);
				stateChanged();
				//print("Here is your order " + Cust.getName());
			}
		}
	}
	public void msgLeavingTable(Customer Cust) {
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == Cust) {
				wCustomers.get(i).setState(CustomerState.done);
				stateChanged();
				//print(Cust.getName() +" is leaving!");
			}
		}
	}

	public void msgFree() {//from animation
		//print("release called");
		busy.release();// = true;
	}
	
	//=========V2
	public void goOnBreak(boolean x){
		onBreak=x;
	}
	
	public void outOfOrder(Customer Cust,String choice){
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == Cust) {
				wCustomers.get(i).setState(CustomerState.outOfOrder);
				stateChanged();
				print(Cust.getName() + ", Please re-Order something");
			}
		}
	}
	public void iCantAffordThis(Customer cust){
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == cust) {
				wCustomers.get(i).setState(CustomerState.done);
				stateChanged();
				print(cust.getName() + ", was broke so he left");
			}
		}
	}
	public void askCheck(Customer cust){
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == cust) {
				wCustomers.get(i).setState(CustomerState.askedCheck);
				stateChanged();
				print("Customer is getting the check");
			}
		}
	}
	public void itCost(Customer cust,double num){
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomer() == cust) {
				wCustomers.get(i).setCheck(num);
				wCustomers.get(i).setState(CustomerState.recievingCheck);
				stateChanged();
				print("Giving check to customer");
			}
		}
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.waiting) { // If waiting
				seatCustomer(wCustomers.get(i));//the action
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<wCustomers.size();i++){
			//print("%");
			if (wCustomers.get(i).getCustomerState() == CustomerState.readyToOrder) { // readyToOrder
				whatWouldYouLike(wCustomers.get(i));
				//print(wCustomers.get(i).getCustomer().getName() +", What would you like?");
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
			//print("*");
		}
		//print("________________");
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.ordered) { // If waiting
				hereIsAnOrder(wCustomers.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.orderReady) { // If orderReady
				hereIsYourFood(wCustomers.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.done) { // If waiting
				doneAndLeaving(wCustomers.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.outOfOrder) { // If waiting
				outOfOrder(wCustomers.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.askedCheck) { // If waiting
				getCheck(wCustomers.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<wCustomers.size();i++){
			if (wCustomers.get(i).getCustomerState() == CustomerState.recievingCheck) { // If waiting
				giveCheck(wCustomers.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}

		gui.goHome();
		try {
			//print(" try go home     acquire called");

			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(MyCustomer C) {
		if(this.name.equals("OnBreak")){
			host.iWouldLikeToGoOnBreak(this);
		}
		gui.goHome();
		try {
			//print(" try go home     acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C.getCustomer().msgSitAtTable(this,menu,menuPrice,C.getTable());//HACK
		DoSeatCustomer(C.getCustomer(), C.getTable());
		try {
			//print("seatcustomer    acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//print("Gui Message:Customer seated at table");
		//Now that Customer is seated send message to self that he is seated
		C.setState(CustomerState.seated);
		
	}
	private void hereIsYourFood(MyCustomer C ){
		gui.doGoToTable(C.getTable());
		try {
			//print("hereisyourfood     acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C.getCustomer().hereIsYourFood(C.getChoice());
		C.setState(CustomerState.served);
		stateChanged();
	}
	private void whatWouldYouLike(MyCustomer C){
		gui.doGoToTable(C.getTable());
		try {
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C.setState(CustomerState.asked);
		C.getCustomer().whatWouldYouLike();
		stateChanged();
	}
	private void hereIsAnOrder(MyCustomer C){
		cook.hereIsAnOrder(this,C.getCustomer(),C.getChoice());
		C.setState(CustomerState.orderPlaced);
		gui.goToChef();
		try {
			print("hereisanorder      acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stateChanged();
	}
	private void doneAndLeaving(MyCustomer C){
		host.msgLeavingTable(C.getCustomer());
		C.setState(CustomerState.gone);
		stateChanged();
	}
	//---------V2--------//
	private void outOfOrder(MyCustomer C ){
		gui.doGoToTable(C.getTable());
		try {
			//print("OutOfOrder     acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C.getCustomer().outOfOrder(C.getChoice());
		C.setState(CustomerState.asked);
		stateChanged();
	}
	private void getCheck(MyCustomer C ){
		gui.goHome();
		try {
			//print("OutOfOrder     acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashier.getCheck(C.cust,C.Choice,this);
		C.setState(CustomerState.waitingForCheck);
		stateChanged();
	}
	private void giveCheck(MyCustomer C ){
		gui.doGoToTable(C.getTable());
		try {
			//print("OutOfOrder     acquire called");
			
			busy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C.getCustomer().hereIsYourCheck(C.getCheck());
		C.setState(CustomerState.Paying);
		stateChanged();
	}
	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		//print("Seating " + customer + " at " + table);
		gui.DoBringToTable(customer,table); //Hack
	}

	//utilities
	public void setCook(CookAgent cook){
		this.cook=cook;
	}
	public void setHost(HostAgent host){
		this.host=host;
	}
	public void setGui(WaiterGui gui) {
		this.gui = gui;
	}

	public WaiterGui getGui() {
		return gui;
	}

	private class MyCustomer{
		Customer cust;
		int table;
		String Choice;
		CustomerState S;
		double check;

		MyCustomer(Customer cust,int table,CustomerState S){
			this.cust=cust;
			this.table=table;
			this.S=S;
		}
		
		void Table(int tableNumber) {
			this.table = tableNumber;
		}
		public void setCheck(double num){
			this.check=num;
		}
		public double getCheck(){
			return check;
		}
		public Customer getCustomer() {
			return cust;
		}
		public CustomerState getCustomerState(){
			return S;
		}
		public int getTable() {
			return table;
		}
		public String getChoice(){
			return Choice;
		}
		public void setChoice(String S){
			Choice = S;
		}
		public void setState(CustomerState S){
			this.S=S;
		}
		
		public String toString() {
			return "table " + table;
		}
	}
}

