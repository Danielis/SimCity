package restaurant;

import agent.Agent;
import restaurant.CookAgent.state;
import restaurant.gui.HostGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;
import restaurant.CookAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

//Cook Agent
public class MarketAgent extends Agent implements Market {
	
	//Lists
	public List<Foods> inventory = Collections.synchronizedList(new ArrayList<Foods>());
	
	//Statics
	static final int numItems = 10;		//Num of items initialized with
	//EDIT HERE*******************************************
	static final int orderTime = 5;		//Time it takes to complete an order
	//EDIT HERE*******************************************

	//Variables
	private Cook cook;					//Copy of cook
	private CashierAgent cashier;
	private String name;					//Name of the market
	private Timer timer;					//A timer	
	private float account;					//How much money the market has
	
	public class Foods
	{
		public String name;					//Name of the food
		public int amount;					//Number of items in stock
		public int amountwanted;			//Number of items wanted by the cook
		public foodstate state;				//State of the item
		
		
		Foods(String s, int ind, int a, foodstate st)
		{
			name = s;
			amount = a;
			state = foodstate.none;
			amountwanted = 0;
		}
		
		public String getName()
		{
			return name;
		}
		int getAmount()
		{
			return amount;
		}
		void setAmount(int a)
		{
			amount = a;
		}
	}
	
	enum foodstate
	{
		none, needsProcessing, processing, processed
	}

	//Constructor
	public MarketAgent(String name) {
		super();
		this.name = name;
		
		//Initialize Inventory
		if (this.name != "Market 3")
		{
			inventory.add(new Foods("Steak", 0, numItems, foodstate.none));
			inventory.add(new Foods("Chicken", 1, numItems, foodstate.none));
			inventory.add(new Foods("Salad", 2, numItems, foodstate.none));
			inventory.add(new Foods("Pizza", 3, numItems, foodstate.none));
		}
		else
		{
			inventory.add(new Foods("Steak", 0, 100, foodstate.none));
			inventory.add(new Foods("Chicken", 1, 100, foodstate.none));
			inventory.add(new Foods("Salad", 2, 100, foodstate.none));
			inventory.add(new Foods("Pizza", 3, 100, foodstate.none));
		}
		
		timer = new Timer();
		account = 50.0f;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setCook(CookAgent c){
		cook = c;
	}
	
	public void setCashier(CashierAgent c){
		cashier = c;
	}
	
//UTILITIES**************************************************

	//None for now
	
//MESSAGES****************************************************

	public void msgHereIsAPayment(float amount)
	{
		account += amount;
	}
	
	public void msgIWantToOrder(String choice, int amount)
	{
		synchronized(inventory)
		{
			for (Foods f : inventory)
			{
				if (f.name == choice)
				{
					f.state = foodstate.needsProcessing;
					f.amountwanted = amount;
					stateChanged();
				}
			}
		}
	}

//SCHEDULER****************************************************
	
	protected boolean pickAndExecuteAnAction() 
	{
		synchronized(inventory)
		{
			for (Foods f : inventory)
			{
				if (f.state == foodstate.needsProcessing)
				{
					ProcessOrder(f);
					return true;
				}
			}
		}
		return false;
	}

//ACTIONS********************************************************

	private void ProcessOrder(Foods newF)
	{
		print("Processing order of " + newF.name + " for " + orderTime + " seconds.");
		newF.state = foodstate.processed;
		final Foods f = newF;
		
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				CompleteOrder(f);
			}
		}, orderTime * 1000);
		
	}
	
	private void CompleteOrder(Foods f)
	{
		print("Completing order.");
		//If we have enough
		if (f.amount > f.amountwanted)
		{
			f.amount -= f.amountwanted;
			print("Giving cook his order. We have enough " + f.name);
			cook.msgHereIsYourOrder(this, f.name, f.amountwanted, false);
			cashier.msgHereAMarketOrder(this, f.name, f.amountwanted);
		}
		
		//If we have exact
		else if (f.amount == f.amountwanted)
		{
			print("Giving cook his order. We have the exact amount of " + f.name);
			cook.msgHereIsYourOrder(this,  f.name,  f.amount,  true);
			cashier.msgHereAMarketOrder(this, f.name, f.amount);
			f.amount = 0;
		}
		
		//We don't have enough
		else if (f.amount < f.amountwanted)
		{
			print("Giving cook his order. Do not have enough " + f.name);
			cook.msgHereIsYourOrder(this,  f.name,  f.amount,  true);
			cashier.msgHereAMarketOrder(this, f.name, f.amount);
			f.amount = 0;
		}
	}
}

