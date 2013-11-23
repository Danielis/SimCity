package roles;

import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.CustomerGui;
import restaurant.interfaces.*;
import agent.Agent;
import agent.RestaurantMenu;

import java.util.*;
import java.util.concurrent.Semaphore;

//Customer Agent
//It still is a finite state machine, instead of events it still uses the state enum.
//I used this design from the designs drawn from class.

public class CustomerRole extends Role implements Customer {
	
	//To show icon
	public enum iconState
	{
		none, question, steak, chicken, salad, pizza,
	};
	
	//EDIT HERE******************************
	static final float initialMoney = 20.00f;
	//EDIT HERE******************************
	
//VARIABLES*************************************************
	private String name;						//Name
	private int hungerLevel = 7;        		//Length of Meal
	private int choosingTime = 6;				//Time it takes to choose
	Timer timer;								//Timer Class
	public int mySeat;							//Seat Number, for mapping
	private CustomerGui customerGui;			//GUI Class
	private HostAgent host;						//Host
	private CashierAgent cashier;				//Cashier
	private Waiter waiter;						//Waiter
	private myState state = myState.none;		//State
	private float myMoney = initialMoney;		//Money
	private float bill;							//Price to pay
	Random random;								//Random Utility
	String choice;								//Choice
	RestaurantMenu menu;						//Copy of the menu
	public iconState icon = iconState.none;		//For animation
	public RestaurantAnimationPanel copyOfAnimPanel;	//For drawing icons
	Boolean imusthavethisitem = false;			//To bypass re-selection
	Boolean reordering = false;					//To check if he's reordering
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	//List of foods available
	private List<Boolean> foodOptions = new ArrayList<Boolean>();
	
	//Constructor
	public CustomerRole(String name){
		super();
		this.name = name;
		random = new Random();
		timer = new Timer();

		bill = 0;
		
		for (int i = 0; i<4; i++)
		{
			foodOptions.add(true);
		}
	}

//UTILITIES**************************************************
	public iconState getIconState()
	{
		return icon;
	}
	public void setAnimPanel(RestaurantAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
	//Set the host
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter){
		this.waiter = waiter;
	}
	
	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	
	public String getChoice(){
		return choice;
	}
	
//CLASSES/ENUMS**********************************************
	private class Food
	{
		//stub
	}
	public enum myState
	{
		none, hungry, waiting, beingSeated, 
		seated, readyToOrder, ordering, finishedOrdering,
		readyToEat, eating, readyToPay, paid, finished,
		waitingForBill, waitingForASpot, gotASpot, IllJustLeave,
	};
	
//MESSAGES*************************************************

	public void msgGotHungry() 
	{
		for (int i = 0; i<4; i++)
		{
			foodOptions.add(true);
		}
		//print("I'm hungry.");
		this.icon = iconState.none;
		this.imusthavethisitem = false;
		reordering = false;
		state = myState.hungry;
		reordering = false;
		stateChanged();
	}

	public void msgRestaurantIsFull(Boolean b)
	{
		int a = random.nextInt()%2;
		if (a<0) a=a*(-1);
		if (b == true)
		{
			if (a==0)
			{
				//print("Restaurant is full. Leaving the restaurant");
				state = myState.IllJustLeave;
				stateChanged();
			}
			else if (a==1)
			{
				//print("Restaurant is full. I still want to wait.");
				state = myState.gotASpot;
				stateChanged();
			}
		}
		else if (b == false)
		{
			state = myState.gotASpot;
			stateChanged();
		}
	}
	
	public void msgFollowMe(Waiter w, int tableNum, RestaurantMenu m) 
	{
		//print("Received message to follow waiter to table " + tableNum);
		waiter = w;
		mySeat = tableNum;
		menu = m;
		state = myState.beingSeated;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike()
	{
		state = myState.ordering;
		//print("I want to order the " + choice);
		stateChanged();
	}
	
	public void msgSorryOutOfFood(List<Boolean> temp)
	{
		reordering = true;
		state = myState.seated;
		print("Got message to reorder.");
		recalculateOptions(temp);
		stateChanged();
	}

	public void msgHereIsYourFood(String choice)
	{
		print("Got my food.");
		state = myState.readyToEat;
		stateChanged();
	}

	public void msgHereIsYourCheck(float check)
	{
		print("Got my check.");
		state = myState.readyToPay;
		bill = check;
		stateChanged();
	}

//SCHEDULER*************************************************
	public boolean pickAndExecuteAnAction() 
	{
		if (state == myState.readyToPay){
			PayForMeal();
			return true;
		}
		if (state == myState.readyToEat){
			EatFood();
			return true;
		}
		if (state == myState.ordering){
			TellWaiterOrder();
			return true;
		}
		if (state == myState.seated){
			PickAnOrder();
			return true;
		}
		if (state == myState.beingSeated){
			FollowWaiter();
			return true;
		}
		if (state == myState.gotASpot)
		{
			DecideToGo();
			return true;
		}

		if (state == myState.hungry)
		{
			GoToRestaurant();
			return true;
		}
		if (state == myState.IllJustLeave)
		{
			LeaveBecauseFull();
			return true;
		}
		return false;
	}

//ACTIONS*************************************************

	private void GoToRestaurant() 
	{
		//print("Going to restaurant");
		this.customerGui.DoGoToWaitingRoom();
		host.msgCheckForASpot(this);
		state = myState.waitingForASpot;
	}
	
	private void DecideToGo()
	{
		host.msgIWantFood(this, true);
		state = myState.waiting;
	}
	
	private void FollowWaiter() 
	{
		print("Being seated. Going to table " + mySeat);
		customerGui.DoGoToSeat(mySeat);
		state = myState.seated;
		stateChanged();
	}
	
	private void TellWaiterOrder()
	{
		print("Telling waiter I'm ready to order");
		waiter.msgHereIsMyOrder(this,  choice);
		state = myState.finishedOrdering;
		
	}
	private void PickAnOrder()
	{
		print("Deciding what to order.");	

		//Out of food case
		int foodCounter = 4;
		for (int i = 0; i<foodOptions.size(); i++)
		{
			if (foodOptions.get(i) == false)
			{
				foodCounter--;
			}
		}
		if (foodCounter == 0)
		{
			print("There is no more food in the restaurant. Leaving.");
			Leave();
		}
		else
		{
			//Selection integer
			int temp = random.nextInt() %4;
			if (temp < 0) temp = temp * -1;

			//This is before the customer re-orders
			int aram = random.nextInt()%2; //random value
			if (aram < 0) aram = aram * -1;
			
			//Money cases: Does not have enough money to pay
			if (myMoney < 5.99 && reordering == false)
			{
				//50% chance customer leaves if he's out of money.

				if (aram == 0)
				{
					print("I randomly chose that I don't have enough money for anything. I will leave");
					Leave();
					return;
				}
			}
			//if the customer doesn't have enough money to pay for all options
			//50% of the time he will decide he doesn't care and will order anything
			//50% he will care and only order that item and if he doesn't get it he leaves

			if (aram == 1 && imusthavethisitem == false && myMoney < 15.99 && reordering == false)
			{
				print("I choose not to limit my options because of how much money I have");
			}
			else if (reordering == false)
			{
				if (myMoney < 8.99 && myMoney > 5.99)
				{
					print("I only have enough for salad. Picking salad.");
					imusthavethisitem = true;
					choice = "Salad";
				}
				else if (myMoney < 10.99 && myMoney > 8.99)
				{
					print("I only have enough for salad or pizza.");
					imusthavethisitem = true;
					int a = random.nextInt()%2;
					if (a<0) a*=-1;
					if (a==0) choice = "Salad";
					else if (a==1) choice = "Pizza";
				}
				else if (myMoney < 15.99 && myMoney > 10.99)
				{
					print("I don't have enough money for steak. Picking something else.");
					imusthavethisitem = true;
					int a = random.nextInt()%3;
					if (a<0) a*=-1;
					if (a==0) choice = "Salad";
					else if (a==1) choice = "Pizza";
					else if (a==2) choice = "Chicken";

				}
			}
			if(imusthavethisitem == true)
			{
				int index;
				
				if (choice.equals("Steak"))
					index = 0;
				else if (choice.equals("Chicken"))
					index = 1;
				else if (choice.equals("Salad"))
					index = 2;
				else if (choice.equals("Pizza"))
					index = 3;
				else
					index = 0;
				
				if(foodOptions.get(index) == false)
				{
					print("They don't have the item I want. Going to leave.");
					Leave();
				}
				//If customer needs that item and they have it
				else
				{
					choice = menu.menu.get(index);

					state = myState.readyToOrder;
					timer.schedule( new TimerTask()
					{
						public void run()
						{				
							CallWaiter();
						}
					}, choosingTime * 1000);
				}
			}
			//if the customer doesn't need to have that item
			else
			{
				//So that the customer can reorder
				Boolean flag;					//Kicks the choosing loop
				do
				{
					flag = false;
					if (this.foodOptions.get(temp) == false) //While that food is out
					{
						temp = random.nextInt() %4; //Reorder integer
						if (temp < 0)
						{
							temp = temp * -1;		//Negative case
						}
						flag = true;				//Kick out
					}
				} while(flag == true);
				//can add a hack to order a single thing

				choice = menu.menu.get(temp);

				state = myState.readyToOrder;
				timer.schedule( new TimerTask()
				{
					public void run()
					{				
						CallWaiter();
					}
				}, choosingTime * 1000);
			}
		}
	}

	private void CallWaiter()
	{
		print("I am ready to order, calling waiter.");
		waiter.msgReadyToOrder(this);
		icon = iconState.question;
	}
	private void EatFood() {
		state = myState.eating;
		print("Eating my " + choice);
		
		if (choice.equals("Steak"))
			icon = iconState.steak;
		else if (choice.equals("Chicken"))
			icon = iconState.chicken;
		else if (choice.equals("Salad"))
			icon = iconState.salad;
		else if (choice.equals("Pizza"))
			icon = iconState.pizza;
		
		timer.schedule(new TimerTask() {
			public void run() {
				DoneEating();
			}
		},
		getHungerLevel() * 1000);
	}

	private void DoneEating() 
	{
		icon = iconState.none;
		print("Telling waiter I'm finished eating.");
		state = myState.waitingForBill;
		waiter.msgDoneEating(this);
	}
	
	private void PayForMeal()
	{
		state = myState.paid;
		print("Going to the cashier to pay");
		customerGui.DoGoToCashier();
		if (bill > myMoney)
		{
			print("I don't have enough money. Giving everything to cashier.");
			cashier.msgHereIsMyPayment(this, myMoney);
		}
		else
		{
			print("I have enough money. Paying giving $" + bill + " to cashier.");
			myMoney -= bill;
			cashier.msgHereIsMyPayment(this, bill);
		}
		Leave();

	}
	
	private void Leave()
	{
		icon = iconState.none;
		print("I'm going to leave the restaurant.");
		waiter.msgPayingAndLeaving(this);
		customerGui.DoExitRestaurant();
		state = myState.finished;
		customerGui.setNotHungry();
		//NEW LINE
		System.out.println(myPerson);
		this.myPerson.msgLeavingRestaurant(this);
	}
	
	private void LeaveBecauseFull()
	{
		icon = iconState.none;
		host.msgIWantFood(this, false);
		print("I'm going to leave the restaurant.");
		this.customerGui.DoExitRestaurant();
		state = myState.finished;
		customerGui.setNotHungry();
	}

//UTILITIES*************************************************

	public void recalculateOptions(List<Boolean> temp)
	{
		foodOptions.clear();
		for (int i = 0; i<temp.size(); i++)
		{
			foodOptions.add(temp.get(i));
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
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
	
	public void WaitForAnimation()
	{
		try
		{
			this.animSemaphore.acquire();	
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:", e);
        }
	}
	
	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}

}

