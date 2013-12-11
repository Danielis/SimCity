package restaurantD;

import agent.Agent;
import restaurantD.MarketAgent;
import restaurantD.CustomerAgent.AgentEvent;
import restaurantD.WaiterAgent.CustomerState;
import restaurantD.gui.CookGui;
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
public class CookAgent extends Agent {
	public List<Order> chefOrders
	= new ArrayList<Order>();
	public enum OrderState{placed,cooking,done,out}
	
	private String name;
	private Vector<MyMarketAgent> markets = new Vector<MyMarketAgent>();
	private boolean orderedFromMarkets;
	private int stockInInventory = 10;
	
	Timer timer = new Timer();
	
	//Menu which will be static right now
	private HashMap<Integer,String> menu = new HashMap<Integer,String> ();
	private HashMap<String,Double> menuPrice = new HashMap<String,Double> ();
	
	private Food inventory = new Food();
	
	private CookGui hostGui = null;

	public CookAgent(String name) {
		super();
		this.name = name;
		setmenu();
		orderedFromMarkets = false;
	}
	private void setmenu(){
		menu.put(1, "Steak");
		menu.put(2, "Chicken");
		menu.put(3, "Salad");
		menu.put(4, "Pizza");
		menuPrice.put("Steak",15.99);
		menuPrice.put("Chicken",10.99);
		menuPrice.put("Salad",5.99);
		menuPrice.put("Pizza",8.99);
	}
	public void addMarket(MarketAgent m){
		markets.add(new MyMarketAgent(m));
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

	public String getName() {
		return name;
	}

	// Messages
	public void hereIsAnOrder(Waiter w,Customer c,String choice){
		print("Chef got the order: " + choice);
		chefOrders.add(new Order(c,choice,OrderState.placed,w));
		stateChanged();
	}
	public void marketOrderUp(MarketAgent m,int st, int ch, int sa, int pi){
		if(!markets.isEmpty()){
			print("Food was delivered to the cook");
			inventory.setSteak(inventory.getSteak()+st);
			inventory.setChicken(inventory.getChicken()+ch);
			inventory.setSalad(inventory.getSalad()+sa);
			inventory.setPizza(inventory.getPizza()+pi);
			for(int i=0;i<markets.size();i++){
				if(m == markets.get(i).m){
					markets.get(i).ordered=false;
				}
			}
		}
	}
	
	public void unableToCompleteOrder(int st, int ch, int sa, int pi){
		if(!markets.isEmpty() && (st!=0 || ch!=0 || sa!=0 || pi!=0)){
			for(int i=0;i<markets.size();i++){
				if(markets.get(i).ordered==false){
					markets.get(i).m.hereIsAnOrder(this,st,ch, sa,pi);
					markets.get(i).ordered=true;
					break; // once you find a market order and break for loop so you don't order same thing from multiple places
				}
			}
		}
		else 
			print("No need to order more since the market had enough or no more markets to order from");
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(inventory.getLow() && orderedFromMarkets == false){
			orderSomeFood();
			print("ORering food since it is low");
			return true;
		}
		for (int i=0;i<chefOrders.size();i++){
			if (chefOrders.get(i).getState() == OrderState.placed) { // If waiting
				letsFry(chefOrders.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		for (int i=0;i<chefOrders.size();i++){
			if (chefOrders.get(i).getState() == OrderState.done) { // If waiting
				orderUP(chefOrders.get(i));
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		boolean checking=true;;
		for(int i=0;i<markets.size();i++){
			if(markets.get(i).ordered == true){
				checking = false;
			}
		}
		if(checking == true)
			orderedFromMarkets = false;
		return false;
	}

	// Actions
	public void letsFry(Order O){
		if(inventory.getQuantity(O.getChoice())==0){
			O.setState(OrderState.out);
			O.getWaiter().outOfOrder(O.getCustomer(),O.getChoice());
		}
		else{
			O.setState(OrderState.cooking);
			O.timeCook();
			inventory.setUsed(O.getChoice());
			inventory.setLow(inventory.isLow());
		}
	}
	public void orderUP(Order O){
		print("Order Cooked: " + O.getChoice());
		O.getWaiter().orderIsReady(O.getCustomer(), O.getChoice());
		O.setState(OrderState.out);
	}
	private void orderSomeFood(){
		if(!markets.isEmpty()){
			orderedFromMarkets = true;
			markets.get(0).m.hereIsAnOrder(this, (stockInInventory-inventory.steak), (stockInInventory-inventory.chicken), (stockInInventory-inventory.salad), (stockInInventory-inventory.pizza));
			markets.get(0).ordered = true;
		}
	}

	//utilities

	public void setGui(CookGui gui) {
		hostGui = gui;
	}

	public CookGui getGui() {
		return hostGui;
	}
	public int getSteak(){
		return inventory.steak;
	}
	public int getChicken(){
		return inventory.chicken;
	}
	public int getSalad(){
		return inventory.salad;
	}
	public int getPizza(){
		return inventory.pizza;
	}

	/**
	 * MyMarketAgent class which holds a boolean for each market displaying if an order is in progress
	 */
	private class MyMarketAgent{
		MarketAgent m;
		boolean ordered;
		
		MyMarketAgent(MarketAgent m){
			this.m = m;
			this.ordered = false;
		}
	}
	/**
	 * Food Class
	 * This class acts as an inventory for the cook.
	 * When ordering he asks for each item individually in one order giving an int per item, 0 if no item is needed
	 */
	private class Food{
		int steak;
		int chicken;
		int salad;
		int pizza;
		boolean low;
		boolean ordered;
		
		Food(){
			steak=1;chicken=1;salad=5;pizza=10;low=false;ordered=false;
		}
		public boolean isLow(){
			if(steak < 3 || chicken < 3 || salad < 3 || pizza < 3){
				return true;
			}
			return false;
		}
		public boolean getLow(){
			return low;
		}
		public void setLow(boolean logic){
			this.low=logic;
		}
		public int getQuantity(String choice){
			if(choice.equals("Steak")){
				return steak;
			}
			else if(choice.equals("Chicken")){
				return chicken;
			}
			else if(choice.equals("Salad")){
				return salad;
			}
			else 
				return pizza;
		}
		public void setUsed(String choice){
			if(choice.equals("Steak")){
				steak--;
			}
			else if(choice.equals("Chicken")){
				chicken--;
			}
			else if(choice.equals("Salad")){
				salad--;
			}
			else 
				pizza--;
		}
		
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
		
		public void setSteak(int num){
			this.steak=num;
		}
		public void setChicken(int num){
			this.chicken=num;
		}
		public void setSalad(int num){
			this.salad=num;
		}
		public void setPizza(int num){
			this.pizza=num;
		}
			
	}
	
	
	/**
	 * Order Class
	 * This class is used to take orders from the waiter and have a timer cook them depending on the choice
	 *	HACK: Need to implement times for the food items, as of right now it waits a set amount
	 */
	private class Order{
		Customer cust;
		String choice;
		OrderState S; 
		Waiter w;
		
		
		Order(Customer cust,String choice,OrderState S,Waiter w){
			this.cust = cust;
			this.choice = choice;
			this.S = S;
			this.w = w;
		}
		
		public void timeCook(){
			timer.schedule(new TimerTask() { //HACK for now one timer for all foods, later will have different times for different objects
				Object cookie = 1;
				public void run() {
					print("Done eating, cookie=" + cookie);
					S=OrderState.done;
					stateChanged();
				}
			},
			5000);
		}
		
		public void setState(OrderState S){
			this.S = S;
		}
		public OrderState getState(){
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
}

