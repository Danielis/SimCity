package restaurantA;

import restaurantA.CookAgent.MyMenuItem;
import roles.*;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.CustomerGui;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Waiter;
//import restaurant.gui.RestaurantGui;
import agent.Agent;
import bank.gui.BankAnimationPanel;

import java.util.concurrent.Semaphore;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Role implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	CustomerGui customerGui;
	Table table;
	String choice;
	WaiterAgent waiter;
	Check check;
	public Semaphore waitForWaiter = new Semaphore(0);
    public Semaphore waitForFood = new Semaphore(0);
    public Collection<MyMenuItem> menu;
    private CashierAgent cashier;
    private double money = 0;
	public AnimationPanel copyOfAnimPanel;
	
	// agent correspondents
	private HostAgent host;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, MovingCashier, Paying, WaitingInRestaurant, ReOrder, BeingSeated, Seated, LookingMenu, ReadyOrder, WaitingToOrder, Ordering, WaitingFood, Eating, DoneEating, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, bored, atCashier, paid, followHost, selectedFood, WaiterArrives, FoodArrives, seated, doneEating, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param cash 
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name, double cash){
		super();
		this.name = name;
		money = cash;
		//setMoney();
	}

	public void addMoneyAmountOwed(double amount){
		this.check.setAmountOwed(amount);
		this.money += 40;
		print("Withdrawing $40 to pay back debts");
	}
	
	public void setAnimPanel(AnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
	
	private void setMoney() {
		// rand number between 5 and 15
		
		if (getName().contentEquals("bigSpender"))
			this.money += 5;
		else if (getName().contentEquals("poorMan"))
			this.money = 5;
		else if (getName().contentEquals("7bucks"))
			this.money = 7;
		
		else if (getName().contentEquals("Steak") || getName().contentEquals("steak")){
			this.money += 20;
			print("Found $20 in pocket (hack for steak customer)");
		}
		else if (getName().contentEquals("Chicken") || getName().contentEquals("chicken")){
			this.money += 13;
			print("Found $13 in pocket (hack for chicken customer)");
		}
		else if (getName().contentEquals("Pizza") || getName().contentEquals("pizza")){
			this.money += 9;
			print("Found $9 in pocket (hack for pizza customer)");
		}
		else if (getName().contentEquals("Salad") || getName().contentEquals("salad")){
			this.money += 7;
			print("Found $7 in pocket (hack for salad customer)");
		}
		
		
		
		
		else 
			this.money += 7 + (int )(Math.random() * (25 - 7) + 1);
		
		print("Found money on the ground. Now have: $" + money);
		}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		//print("state: " + state);
		//print("event: " + event);
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	public void msgRestaurantFull(){
		int rand = (int )(Math.random() * 3 + 1);
		
//		if ( ((rand == 0 || rand == 1) && !name.equals("patient")) || name.equals("impatient")){
//			event = AgentEvent.bored;
//			stateChanged();
//		}
//		else
			print("I don't mind waiting.");
	}

	public void msgSitAtTable(WaiterAgent w, Table table) {
		this.table = table;
		this.waiter = w;
		this.menu = waiter.menu;
		event = AgentEvent.followHost;
		stateChanged();
	}
	

	public void msgYouNeedToReOrder() {
		state = AgentState.LookingMenu;
		event = AgentEvent.selectedFood;
		waitForFood.release();
		//print(" recevived message to reorder");
		stateChanged();
	}

	public void msgWhatOrder(Waiter w){
		event = AgentEvent.WaiterArrives;
		stateChanged();
	}

	public void msgHereIsFood(Waiter w, String choice){
		event = AgentEvent.FoodArrives;
		
		stateChanged();
	}
	
	public void msgHereIsCheck(Check check, CashierAgent c){
		this.check = check;
		this.cashier = c;
		stateChanged();
	}
	
	public void msgHereIsChange(double change){
		money += change;
	}
	
	public void msgYouAreGoodToGo(){
		event = AgentEvent.paid;
		stateChanged();
	}
	
	public void msgGetOut(){
		event = AgentEvent.paid;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		setMoney();
		event = AgentEvent.doneLeaving;
		//print("reached origin");
		stateChanged();
	}
	public void msgAnimationFinishedGoToCashier() {
		//from animation
		event = AgentEvent.atCashier;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//System.out.println("Hi Aleena!");
		//print("state: " + state);
		//print("event: " + event);
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			//event = AgentEvent.seated; //remove this when GUI added
			SitDown();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.bored ){
			state = AgentState.Leaving;
			tellHostNotWaiting();
			
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.LookingMenu;
			LookMenu();
			return true;
		}
		if (state == AgentState.LookingMenu && event == AgentEvent.selectedFood ){
			state = AgentState.ReadyOrder;
			multiStepCustomer();
			
			return true;
		}
		if (state == AgentState.ReOrder && event == AgentEvent.selectedFood){
			state = AgentState.Ordering;
			GiveOrder();
			return true;
			}
//		if (state == AgentState.ReadyOrder && event == AgentEvent.WaiterArrives ){
//			state = AgentState.Ordering;
//			GiveOrder();
//			return true;
//		}
		if (state == AgentState.WaitingFood && event == AgentEvent.FoodArrives ){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.MovingCashier;
			goToCashier();
			return true;
		}
		if (state == AgentState.MovingCashier && event == AgentEvent.atCashier){
			state = AgentState.Paying;
			payCashier();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.paid){
			state = AgentState.Leaving;
			leaveRestaurant();
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

	private void tellHostNotWaiting(){
		print("Tired of waiting. Leaving.");
		leaveRestaurant();
		host.msgNotWaiting(this);
	}
	private void goToRestaurant() {
		print("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		print("Following waiter to table.");
		customerGui.DoGoToSeat(table);//hack; only one table
		//event = AgentEvent.seated;
	}

	
	private void LookMenu() {
		print("Looking at menu");
		
		
		//event = AgentEvent.selectedFood;
		
		timer.schedule(new TimerTask() {
			@SuppressWarnings("unused")
			Object cookie = 1;
			public void run() {
				print("Done looking at menu");
				
				//print ("Selected " + choice);
				event = AgentEvent.selectedFood;
				stateChanged();
			}
		},
		500);
	
	
	}
	
	 private void multiStepCustomer() {
		 CallWaiter();
	        
	        try {
	            waitForWaiter.acquire();
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        

	        GiveOrder();
	        waiter.waitForOrder().release();
	        
	        try {
	            waitForFood.acquire();
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        stateChanged();
	    }
	
	private void CallWaiter() {
		print("I am ready to order");
		waiter.msgReadyToOrder(this);
	}

	private void GiveOrder(){
		this.choice = RandomFood();
		
		
		if (!getName().contentEquals("bigSpender")){
		if (!hasEnoughMoney()){
			customerGui.CantOrder();
			
			timer.schedule(new TimerTask() {
				@SuppressWarnings("unused")
				Object cookie = 1;
				public void run() {
					customerGui.DoneGiveOrder();
				}
			},
			3000);
			
			print("I can't afford anything on the menu :(");
			waiter.msgLeavingTable(this);
			//waiter.host.msgLeavingTable(this);
//			state = AgentState.Paying;
//			event = AgentEvent.paid;
			waitForFood.release();
			state = AgentState.Leaving;
			leaveRestaurant();
			//leaveRestaurant();
			//stateChanged();
			return;
		}
			
		while(!FoodEnoughMoney(choice)){
			choice = RandomFood();
		}
		
		}
		
		print("I would like to order " + choice);
		state = AgentState.WaitingFood;
		waiter.msgHereIsChoice(this, choice);
		customerGui.DoGiveOrder(choice);
		
		timer.schedule(new TimerTask() {
			@SuppressWarnings("unused")
			Object cookie = 1;
			public void run() {
				customerGui.DoneGiveOrder();
			}
		},
		3000);
	}
	
	private boolean hasEnoughMoney(){
		boolean flag = false;
		for(MyMenuItem m : menu){
				if (money >= m.price && m.stock > 0)
					flag = true;
		}	
		return flag;
	}
	private boolean FoodEnoughMoney(String choice) {
		for(MyMenuItem m : menu){
			if (m.name == choice){
				if (money >= m.price && m.stock > 0)
					return true;
				else
					return false;
			}
		}		
		return false;
		}

	private void EatFood() {
		print("Eating Food");
		customerGui.isEating(choice);
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
				customerGui.doneEating();
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void goToCashier(){
		print("Done eating. Walking to cashier.");
		waiter.msgLeavingTable(this);
		customerGui.DoGoToCashier();
	}
	
	private void payCashier(){
		check.setAmount(choice);
		print("I owe: $" + check.getAmountOwed());
		int extra = (int )(Math.random() * 4 + 1);
		int paid = (int )(check.getAmountOwed() + extra);
		
		if (money >= paid){ // pays some amount extra
			money -= paid;
			print("Here is $" + paid + ". I need change.");
			cashier.msgHereIsMoney(check, paid);
		}
		else if (money >= check.getAmountOwed()){ // pays exact amount
			money -= check.getAmountOwed() ;
			print("Here is $" + check.getAmountOwed());
			cashier.msgHereIsMoney(check, check.getAmountOwed());
		}
		else {
			print("I don't have enough money... Here is $" + money);
			money = 0;
			cashier.msgICantPay(check, money);
		}
	}
	
	private void leaveRestaurant() {
		print("Leaving.");
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
	
	private String RandomFood(){
		
		
		if (getName().contentEquals("Steak") || getName().contentEquals("steak")){
			return "Steak";
		}
		if (getName().contentEquals("Chicken") || getName().contentEquals("chicken")){
			return "Chicken";
		}
		if (getName().contentEquals("Pizza") || getName().contentEquals("pizza")){
			return "Pizza";
		}
		if (getName().contentEquals("Salad") || getName().contentEquals("salad")){
			return "Salad";
		}
		
		
		int rand = (int )(Math.random() * 4 + 1);
		
		if (rand == 1)
			return "Steak";
		if (rand == 2)
			return "Chicken";
		if (rand == 3)
			return "Salad";
		if (rand == 4)
			return "Pizza";
		else
			return "Steak";
	}
	
	public Semaphore waitForWaiter() {
    	return waitForWaiter;
    }
	
    public Semaphore waitForFood() {
	    	return waitForFood;
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub
		
	}





	

	

	
}

