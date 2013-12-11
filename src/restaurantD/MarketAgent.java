package restaurantD;

import agent.Agent;
import restaurantD.CookAgent;
import restaurantD.interfaces.*;

import java.util.*;

/**
 * Restaurant Host Agent
 */

public class MarketAgent extends Agent implements Market {
	public List<Order> chefOrders
	= new ArrayList<Order>();
	public enum OrderState{placed,gathering,done}
	
	private String name;
	private CookAgent cook;
	private CashierAgent cashier;
	//Inventory Items
	int steak;
	int chicken;
	int salad;
	int pizza;
	//debt for cashier
	double debt;
	
	Timer timer = new Timer();
	
	
	
	public MarketAgent(String name) {
		super();
		this.name = name;
		setInventory();
		debt=0;
	}
	public void setCook(CookAgent c){
		this.cook=c;
	}
	public CookAgent getCook(){
		return cook;
	}
	public void setCashier(CashierAgent C){
		this.cashier = C;
	}
	public double getDebt(){
		return this.debt;
	}
	
	public void setInventory(){
		if(name.equals("Market1")){
			this.steak = 0;
			this.chicken = 0;
			this.salad = 0;
			this.pizza = 0;
		}
		else if(name.equals("Market2")){
			this.steak = 0;
			this.chicken = 0;
			this.salad = 10;
			this.pizza = 10;
		}
		else if(name.equals("Market3")){
			this.steak = 10;
			this.chicken = 10;
			this.salad = 10;
			this.pizza = 10;
		}
		else{
			this.steak = 10;
			this.chicken = 10;
			this.salad = 10;
			this.pizza = 10;
		}
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages
	public void hereIsAnOrder(CookAgent cook, int steak, int chicken, int salad, int pizza){
		print(this.name + " got an order /n ST: " + steak + " Chicken: " + chicken + " Salad: " + salad+ " Pizza: " + pizza);
		this.cook = cook;
		chefOrders.add(new Order(steak,chicken,salad,pizza,OrderState.placed,this));
		stateChanged();
	}
	public void marketPayment(double num){
		this.debt -=num;
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		for (int i=0;i<chefOrders.size();i++){
			if (chefOrders.get(i).getState() == OrderState.placed) { // If waiting
				gather(chefOrders.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<chefOrders.size();i++){
			if (chefOrders.get(i).getState() == OrderState.done) { // If waiting
				orderUP(chefOrders.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		
		return false;
	}

	// Actions
	public void gather(Order O){
		O.setState(OrderState.gathering);
		O.doWeHaveEnough();
		print("GATHERING >>>>>>>>>>>>>>>>");
		O.timeCook();
	}
	public void orderUP(Order O){
		print(this.name + " gathered ST: " + O.steak + " Chicken: " + O.chicken + " Salad: " + O.salad+ " Pizza: " + O.pizza);
		O.m.cook.marketOrderUp(this,O.steak,O.chicken,O.salad,O.pizza);
		cashier.marketBill(this, (O.steak*15.99 +O.chicken*10.99+O.salad*5.99+O.pizza*8.99));
		this.debt+=(O.steak*15.99 +O.chicken*10.99+O.salad*5.99+O.pizza*8.99);
		chefOrders.remove(O);
	}

	//utilities
	public int getSteak(){
		return steak;
	}
	public int getChicken(){
		return chicken;
	}
	public int getSalad(){
		return salad;
	}
	public int getPizza(){
		return pizza;
	}
	private class Order{
		int steak;
		int chicken;
		int salad;
		int pizza;
		OrderState S; 
		MarketAgent m;
		
		Order(int steak,int chicken,int salad, int pizza, OrderState s, MarketAgent m){
			this.steak = steak;
			this.chicken = chicken;
			this.salad = salad;
			this.pizza = pizza;
			this.S = s;
			this.m = m;
		}
		public void doWeHaveEnough(){
			int noSt=0;int noCh=0;int noSa=0;int noPi=0;
			int checkSt = m.steak - this.steak;
			int checkCh = m.chicken - this.chicken;
			int checkSa = m.salad - this.salad;
			int checkPi = m.pizza - this.pizza;
			
			if(checkSt < 0){
				noSt = -checkSt;
				steak = m.steak;
				m.steak=0;
			}
			else
				m.steak-=steak;
			if(checkCh < 0){
				noCh = -checkCh;
				chicken = m.chicken;
				m.chicken =0;
			}
			else 
				m.chicken -= chicken;
			if(checkSa < 0){
				noSa = -checkSa;
				salad = m.salad;
				m.salad = 0;
			}
			else
				m.salad -= salad;
			if(checkPi < 0){
				noPi = -checkPi;
				pizza = m.pizza;
				m.pizza = 0;
			}
			else
				m.pizza -= pizza;
		
			if(noSt>0 || noCh>0 || noSa>0 || noPi >0)
				m.cook.unableToCompleteOrder(noSt,noCh,noSa,noPi);
			print(m.name + " unable to complete ST: " + noSt + " Chicken: " + noCh + " Salad: " + noSa+ " Pizza: " + noPi);
		}
		public void timeCook(){
			
			timer.schedule(new TimerTask() {
				Object cookie = 1;
				public void run() {
					S=OrderState.done;
					stateChanged();
					print("GATHERED <<<<<<<<<<<<<<<<<<");
				}
			},
			60000);
		}
		
		public void setState(OrderState S){
			this.S = S;
		}
		public OrderState getState(){
			return S;
		}
		
	}
}

