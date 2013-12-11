package restaurantD;

import agent.Agent;
import restaurantD.gui.CustomerGui;
import restaurantD.gui.RestaurantGui;
import restaurantD.interfaces.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	private int tableNumber;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private double money;
	private double check;
	public HashMap<Integer,String> menu;
	public HashMap<String,Double> menuPrice;
	
	// agent correspondents
	private HostAgent host;
	private Waiter waiter = null;
	private Cashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, ReadyToOrder, Ordered, Eating,CheckPlease, Paying, Leaving,OrderedAgain};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, ordering, orderBrought,doneEating, doneLeaving,reordering,rereordering, gotCheck, payed, cantPay};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		money = 20;
		if(name.equals("Salad")){
			money =5.99;
		}
		if(name.equals("Broke")){
			money =0.0;
		}
		if(name.equals("IDK")){
			money =0.0;
		}
	}
	// Hack to set the menu for Cook
	public void setmenu(HashMap<Integer,String> menu,HashMap<String,Double> menuPrice){
		this.menu= new HashMap<Integer,String>(menu);
		this.menuPrice= new HashMap<String,Double>(menuPrice);
	}
	/**
	 * hack to establish connection to Host agent 
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}
	public void setCashier(Cashier c){
		this.cashier = c;
	}
	public Waiter getWaiter(){
		return waiter;
	}
	public String getCustomerName() {
		return name;
	}
	public double getMoney(){
		return money;
	}
	public void setMoney(double num){
		this.money = num;
	}
	// Messages

	public void gotHungry() {//from animation
		//print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgSitAtTable(Waiter waiter,HashMap<Integer,String> menu,HashMap<String,Double> menuPrice,int table) {
		//tableNumber = number; removing since it is not necessary for customer to know
		//print("Received msgSitAtTable");
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		this.menu = new HashMap<Integer,String>(menu);
		this.menuPrice= new HashMap<String,Double>(menuPrice);
		this.tableNumber = table;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
		print("Finished going to seat");
	}
	
	public void whatWouldYouLike(){
		event = AgentEvent.ordering;
		stateChanged();
	}
	
	public void hereIsYourFood(String choice){
		event = AgentEvent.orderBrought;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	//--------V2-------//
	public void outOfOrder(String Choice){
		if(state==AgentState.Ordered)
			event = AgentEvent.reordering;
		if(state==AgentState.OrderedAgain)
			event = AgentEvent.rereordering;
		stateChanged();
	}
	public void hereIsYourCheck(double num){
		check = num;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	public void youPayed(){
		event = AgentEvent.payed;
		stateChanged();
	}
	public void doDishes(){
		event = AgentEvent.cantPay;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.ReadyToOrder;
			imReadyToOrder();
			return true;
		}
		
		if (state == AgentState.ReadyToOrder && event == AgentEvent.ordering){
			state = AgentState.Ordered;
			hereIsMyChoice();
			return true;
		}
		//--------V2 addition here//
		if (state == AgentState.Ordered && event == AgentEvent.reordering){
			state = AgentState.OrderedAgain;
			hereIsAnotherChoice();
			return true;
		}
		     //adding a state for if multiple dishes were out and the customer has to order again.
			// In this scenario the customer will just leave the table not having eaten anything.
		if (state == AgentState.OrderedAgain && event == AgentEvent.rereordering){
			state= AgentState.Leaving;
			//print("This is horrible service, GoodBYE!!!");
			leaveTable();
			return true;
		}
		
		if ((state == AgentState.Ordered || state== AgentState.OrderedAgain) && event == AgentEvent.orderBrought){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.CheckPlease;
			askCheck();
			return true;
		}
		if (state == AgentState.CheckPlease && event == AgentEvent.gotCheck){
			state = AgentState.Paying;
			goPay();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.cantPay){///////
			washDishes();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.payed){///////
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableNumber);//hack; only one table
	}
	
	private void imReadyToOrder(){
		print(this.name + " calling Waiter");
		waiter.imReadyToOrder(this);
	}
	
	private void hereIsMyChoice(){
		if(name.equals("IDC")){
			waiter.hereIsMyChoice(this,menu.get(1));
		}
		else if(name.equals("Salad")){
			waiter.hereIsMyChoice(this, menu.get(3));
		}
		else if(name.equals("Broke")){
			print("I'm sorry, but I can't afford this. GoodBye.");
			waiter.iCantAffordThis(this);
			leaveTable();
		}
		else
			waiter.hereIsMyChoice(this,menu.get(1));
		
	}
	private void hereIsAnotherChoice(){
		//print(waiter.getName() + " I would like " + this.menu.get(2) );
		waiter.hereIsMyChoice(this,menu.get(2)); //Hack since person will always choose chicken for now
	}
	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	private void askCheck(){
		waiter.askCheck(this);
	}
	private void goPay(){
		customerGui.DogoPay();
		if(check<=money){
			cashier.pay(this,check);
			money-=check;
		}
		else
			cashier.cantPay(this);
	}
	private void washDishes(){
		customerGui.DoWashDishes();
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.payed;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);
	}
	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

