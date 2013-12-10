package restaurantC;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurantC.gui.CustomerGui;
import restaurantC.interfaces.Customer;
import roles.Role;
/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role implements Customer {
	public String name;					//name.  public variable
	Timer timer = new Timer();			//timer for timed scenarios 
	private CustomerGui customerGui;    //gui backed by agent
	private int custTableNumber;
	private String choice;				//string choice
	public Menu myMenu;					//menu that will be passed in by the waiter
	//amount owed
	private double amountOwed;			//amount the customer owes
	private double spendingMoney;		//amount the customer has

	//semaphore that will manage the agent's interaction with the gui
	private Semaphore waitingForAnimation = new Semaphore(0); 
	
	// agent(role) correspondents
	private HostRole host;
	private WaiterRole waiter;
	CashierRole cashier;
	
	//enums that manage customer.  state machine
	//states
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, WaitingForFood, Eating, DoneEating, WaitingForCheck, Paying, 
		Leaving};
	private AgentState state = AgentState.DoingNothing;
	//events
	public enum AgentEvent 
	{none, gotHungry, followHost, seated, readyToOrder, foodIsHere, doneEating, AnythingElse, ReceivedCheck, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;
	
	//Constructor
	public CustomerRole(String name){
		super();
		this.name = name;					//sets name
		this.choice = "none";				//sets choice to none
		amountOwed = 0.00;						//owes no money right now
		//hack for non-normative scenarios
		if(name.equals("Thief") || name.equals("Broke")) {
			spendingMoney = 2.00;
		}
		else {
			spendingMoney = 25.00;
		}
		System.out.println(name + ": I have $" + spendingMoney);
	}

	//Setters and getters
	public void setHost(HostRole host) {
		this.host = host;
	}
	
	public void setWaiter(WaiterRole waiter) {
		this.waiter = waiter;
	}

	public String getCustomerName() {
		return name;
	}
	
	public void setCashier(CashierRole c) {
		cashier = c;
	}
	
	public int getX() {
		return customerGui.getX();
	}
	
	public int getY() {
		return customerGui.getY();
	}
	
	//---------------------------------------------------
	// -----------------Messages-------------------------
	//---------------------------------------------------
	//message from animation saying "I'm hungry."  Starts the chain of reaction.
	public void gotHungry() {
		System.out.println(name + ": I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	public void restaurantFull() {
		if((Math.random() * 2) > 1) {
			//do nothing
			System.out.println(name + ": Okay, I'll wait.");
			customerGui.DoGoWait();
		}
		else{
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			System.out.println(name + ": Nah, not worth the wait.");
			customerGui.DoExitRestaurant();
			stateChanged();
		}

	}
	//message from host to sit at a table
	public void msgSitAtTable(int tableNumber, Menu m) {
		custTableNumber = tableNumber;
		myMenu = m;
		event = AgentEvent.followHost;
		stateChanged();
	}
	//message from the gui, saying that the customergui is at the table
	public void msgAtTable() {
		event = AgentEvent.seated;
		state = AgentState.Seated;
		stateChanged();
	}
	//message from waiter, asking what the order is
	public void whatDoYouWant() {
		event = AgentEvent.readyToOrder;
		stateChanged();
	}
	//message from waiter, the food is at the table
	public void hereIsFood(String choice){
		event = AgentEvent.foodIsHere;
		this.choice = choice;
		stateChanged();
	}
	//message from waiter, there is none of the food choice, cycles back around to ready to order
	public void outOfFood() {
		state = AgentState.Seated;
		event = AgentEvent.seated;
		for(int i = myMenu.foods.size()-1; i>=0; i--) {
			if(myMenu.foods.get(i).name.equals(choice)) {
				myMenu.foods.remove(i);
			}
		}
		stateChanged();
	}
	//message from waiter, asks if customer needs anything else
	public void anythingElse() {
		event = AgentEvent.AnythingElse;
		stateChanged();
	}
	//message from waiter, here is check
	public void hereIsCheck(double p) {
		state = AgentState.Paying;
		event = AgentEvent.ReceivedCheck;
		amountOwed += p;
		stateChanged();
	}
	//sent from cashier, done paying
	public void hereIsChange(double change) {
		spendingMoney += change;
		System.out.println(name + ": I now have $" + spendingMoney);
		event = AgentEvent.donePaying;
		stateChanged();
	}
	public void msgLeftRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	public void msgAtCashier() {
		waitingForAnimation.release();
	}

	//---------------------------------------------------------
	//-----------------Scheduler-------------------------------
	//---------------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		//handles when the customer first gets hungry.  should be invoked after IsHungry is called
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			event = AgentEvent.none;
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		//handles when the customer is told to sit at a table by waiter.  should be invoked after msgSitAtTable
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			event = AgentEvent.none;
			SitDown(custTableNumber);
			return true;
		}
		//handles when the customer is seated.  should be invoked after msgAtTable
		if(state == AgentState.Seated && event == AgentEvent.seated) {
			event = AgentEvent.none;
			ThinkAboutOrder();
			return true;
		}
		//handles when customer has been asked for order.  should be invoked after whatDoYouWant
		if(state == AgentState.Seated && event == AgentEvent.readyToOrder) {
			event = AgentEvent.none;
			placeOrder();
			return true;
		}
		//food arrives.  should be invoked after foodIsHere
		if(state == AgentState.WaitingForFood && event == AgentEvent.foodIsHere) {
			event = AgentEvent.none;
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		//done eating.  should be invoked after eatFood is done in actions
		if(state == AgentState.Eating && event == AgentEvent.doneEating) {
			event = AgentEvent.none;
			state = AgentState.DoneEating;
			doneWithFood();
			return true;
		}
		//waiter and customer are talking again.  should be invoked after anythingElse
		if(state == AgentState.DoneEating && event == AgentEvent.AnythingElse) {
			event = AgentEvent.none;
			thinkAboutMore();
			return true;
		}
		//customer needs to go to pay the cashier.  should be invoked after hereIsCheck
		if(state == AgentState.Paying && event == AgentEvent.ReceivedCheck) {
			event = AgentEvent.none;
			payCashier();
			return true;
		}
		//customer is done paying.  should be invoked after donePaying
		if(state == AgentState.Paying && event == AgentEvent.donePaying) {
			event = AgentEvent.none;
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			//no action
			return true;
		}

		return false;
	}

	//--------------------------------------------------------
	//---------------------Actions----------------------------
	//--------------------------------------------------------
	//going to restaurant.  sending host hunger message.
	private void goToRestaurant() {
		System.out.println(name + ": Going to restaurant");
		host.IWantFood(this);
	}
	//sitting down at the table.  has the host go to seat
	private void SitDown(int tableNumber) {
		System.out.println(name + ": Being seated. Going to table");
		customerGui.DoGoToSeat(tableNumber);
	}
	//thinking about the order while at the table
	private void ThinkAboutOrder() {
		System.out.println(name + ": Thinking about order...");
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				chooseOrder();
				ReadyToOrder();
			}
		},
		5000);
	}
	//called directly above...alerts waiter of ready to order
	private void ReadyToOrder() {
		System.out.println(name + ": Ready to order.");
		waiter.readyToOrder(this);
	}
	//also called above...chooses order
	private void chooseOrder() {
		for(int i = myMenu.foods.size()-1; i>=0; i--) {
			if(myMenu.foods.get(i).price > spendingMoney) {
				myMenu.foods.remove(i);
			}
		}
		//if the menu is empty, customer either leaves or picks cheapest item
		if(myMenu.foods.isEmpty()) {
			if(name.equals("Thief")) {
				choice = "Salad";
				return;
			}
			else {
				choice = "none";
				return;
			}
		}
		double rand = Math.random() * ((double) myMenu.foods.size());
		if(rand<1)
			choice = myMenu.foods.get(0).name;
		else if (rand<2)
			choice = myMenu.foods.get(1).name;
		else if (rand<3)
			choice = myMenu.foods.get(2).name;
		else
			choice = myMenu.foods.get(3).name;
	}
	//placing order
	public void placeOrder(){
		if(!choice.equals("none")) {
			state = AgentState.WaitingForFood;
			System.out.println(name + ": I want " + choice + ".");
			//shows the order text
			waiter.order(this, choice);
		}
		else {
			System.out.println(name + "Actually, I'm leaving.  There's nothing I want.");
			state = AgentState.Paying;
			event = AgentEvent.donePaying;
		}
	}
	//eating food
	private void EatFood() {
		System.out.println(name + ": Eating Food");
		customerGui.finalizeOrder(choice);
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		5000);
	}
	//done with food
	private void doneWithFood() {
		waiter.doneWithFood(this);
	}
	//thinks about getting something else, but for now just orders the check
	private void thinkAboutMore(){
		System.out.println("Customer: hmm let me think.");
		System.out.println("Check please.");
		waiter.checkPlease(this);
	}
	//goes to pay cashier
	private void payCashier() {
		customerGui.removeOrder();
		customerGui.DoGoToCashier();
		double checkPaymentMoney = 0.00;
		try {
			waitingForAnimation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(amountOwed<=spendingMoney) {
			checkPaymentMoney = calculateCheckPayment();
			System.out.println(name + ": paying cashier $ " + checkPaymentMoney);
			spendingMoney -= checkPaymentMoney;
			cashier.hereIsMoney(checkPaymentMoney, this);
			amountOwed = 0;
		}
		else {
			System.out.println(name + ": paying cashier $ " + spendingMoney);
			cashier.hereIsMoney(spendingMoney, this);
			spendingMoney = 0;
			amountOwed -= spendingMoney;
		}

	}
	//function that figures out how much money to give to the cashier
	private double calculateCheckPayment() {
		if(spendingMoney >= 20.00)
			return 20.00;
		else if(spendingMoney >= 10.00 && amountOwed < 10.00)
			return 10.00;
		else
		return amountOwed;
	}
	//leaving table
	private void leaveTable() {
		System.out.println("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
		spendingMoney += 25;
	}
	

	// Accessors, etc.
	public String getName() {
		return name;
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

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub
		
	}

}