package restaurant.roles;

import agent.Agent;
import restaurant.CustomerAgent.iconState;
import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import restaurant.ProducerConsumerMonitor.Ticket;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.CookGui;
import restaurant.gui.CustomerGui;
import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Waiter;
import restaurant.MarketAgent;
import roles.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;

//Cook Agent
public class CookRole extends Role implements Cook 
{

	public WorkState myState = WorkState.none;
	
	//Lists
	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Foods> inventory = Collections.synchronizedList(new ArrayList<Foods>());
	List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());
		
	//Variables
	private String name;
	Timer timer;
	double accountBalance = 0;
	double salary;

	//How many items the cook starts with
	static final int numItemsInInventory = 5;
	
	//How many items the cook orders when he's low
	static final int numItemsToOrder = 2;
	
	//The threshhold required for the cook to order
	static final int numIntemsForThreshhold = 4;
	
	//Variables
	private Map<String, Foods> foods = new HashMap<String, Foods>();
	private int time;
	public double balance;
	
	//Anim Stuff
	public List<myIcon> icons = Collections.synchronizedList(new ArrayList<myIcon>());
	public RestaurantAnimationPanel copyOfAnimPanel;						//For drawing icons
	public Semaphore animSemaphore = new Semaphore(0, true);
	public Map<String, iconState> iconMap = new HashMap<String, iconState>();
	public CookGui cookGui = null;
	public Restaurant rest;
	ProducerConsumerMonitor theMonitor;

	public enum iconState
	{
		none, steak, chicken, salad, pizza,
	};

	//Constructor
	public CookRole(String name, double cash) {
		super();
		this.name = name;
		
		accountBalance = cash;
		
		//Set the timer
		timer = new Timer();
		
		//Map name to foods
		foods.put("Steak",  new Foods(5,0));
		foods.put("Chicken", new Foods(6,1));
		foods.put("Salad", new Foods(3,2));
		foods.put("Pizza", new Foods(8,3));
		
		//Initialize Inventory
		inventory.add(new Foods("Steak", numItemsInInventory, 0));
		inventory.add(new Foods("Chicken", numItemsInInventory, 1));
		inventory.add(new Foods("Salad", numItemsInInventory, 2));
		inventory.add(new Foods("Pizza", numItemsInInventory, 3));
		
		iconMap.put("Steak", iconState.steak);
		iconMap.put("Chicken", iconState.chicken);
		iconMap.put("Salad", iconState.salad);
		iconMap.put("Pizza", iconState.pizza);
		
		//Order Food If Inventory is low
		for (int i = 0; i < inventory.size(); i++)
		{
			if ((inventory.get(i).amount <= numIntemsForThreshhold && 
					inventory.get(i).state == OrderState.none) ||
				(inventory.get(i).amount <= numIntemsForThreshhold && 
					inventory.get(i).state == OrderState.ordered))
			{
				print("There are " + inventory.get(i).amount + " " + inventory.get(i).name);
				print("Scheduling order for " + inventory.get(i).name);
				inventory.get(i).state = OrderState.needsToOrder;
				stateChanged();
			}
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
//UTILITIES**************************************************
	
	public void setMarkets(MarketAgent m)
	{
		markets.add(new MyMarket(m));
	}
	
	public void setAnimPanel(RestaurantAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
	
	public void setGui(CookGui g) {
		cookGui = g;
	}
	
	public CookGui getGui(){
		return cookGui;
	}
	
	public class myIcon
	{
		public Waiter w;
		public iconState state;
		
		myIcon(Waiter newW, iconState newState)
		{
			w = newW;
			state = newState;
		}
	}

	public class Foods
	{
		public String name;
		public int amount;
		public int cookingTime;
		int index;
		public OrderState state;
		Foods(int a, int i)
		{
			cookingTime = a;
			index = i;
			state = OrderState.none;
		}
		Foods(String s, int a, int i)
		{
			name = s;
			amount = a;
			index = i;
			state = OrderState.none;
		}
		String getName()
		{
			return name;
		}
		int getCookingTime()
		{
			return cookingTime;
		}
		int getAmount()
		{
			return amount;
		}
		void setAmount(int a)
		{
			amount = a;
		}
		void decrement()
		{
			amount--;
		}
	}
	private class Order
	{
		Waiter w;
		String choice;
		int table;
		state s;
		
		//Constructor
		Order(Waiter newWaiter, String newChoice, int newTable)
		{
			w = newWaiter;
			choice = newChoice;
			table = newTable;
			s = state.needsProcessing;
		}
		
		//Class Methods
		Waiter getWaiter()
		{
			return w;
		}
		
		String getChoice()
		{
			return choice;
		}
		
		int getTable(){
			return table;
		}
		
		void setState(state newState)
		{
			s = newState;
		}
	}
	
	private class MyMarket
	{
		public MarketAgent m;
		public List<Boolean> outOfItems = new ArrayList<Boolean>();
		
		MyMarket(MarketAgent newM)
		{
			m = newM;
			//For as many items as we have, set as not out of that item
			for (int i=0; i<inventory.size(); i++)
			{
				outOfItems.add(false);
			}
		}
		
		public void setOutOfItem(int index)
		{
			outOfItems.set(index, true);
		}
	}
	
	//enum State
	enum state
	{
		needsProcessing, pending, cooking, done, complete
	};
	
	enum OrderState
	{
		none, needsToOrder, ordering, ordered
	};


//MESSAGES****************************************************
	public void msgGetPaid(){
		balance =+ this.rest.takePaymentForWork(salary);
	}


	public void msgLeaveWork()
	{
		myState = WorkState.needToLeave;
		stateChanged();
	}
	
	public void msgTakingItem(Waiter w)
	{
		synchronized(icons)
		{
			for (myIcon icon : icons)
			{
				if (icon.w == w)
				{
					icon.state = iconState.none;
					stateChanged();
				}
			}
		}
	}
	
	public void msgNotEmpty(){
		stateChanged();
	}
	public void msgHereIsAnOrder(Waiter w, String choice, int table)
	{
		orders.add(new Order(w, choice, table));
		print("Received order to prepare " + choice);
		stateChanged();
	}

	public void msgHereIsYourOrder(MarketAgent m, String name, int count, boolean out)
	{
		print("Got order from " + m.getName() + ": " + count + " " + name);
		//Find the market giving you the order
		synchronized(markets)
		{
			for (MyMarket mm : markets)
			{
				if (mm.m == m)
				{
					//If the market is out of this item
					if (out == true)
					{
						mm.setOutOfItem(foods.get(name).index);
					}
					
					//add items to inventory
					inventory.get(foods.get(name).index).amount += count;
					
					//change the state
					inventory.get(foods.get(name).index).state = OrderState.ordered;
				}
			}
		}
		
		for (int i = 0; i < inventory.size(); i++)
		{
			if ((inventory.get(i).amount <= numIntemsForThreshhold && 
					inventory.get(i).state == OrderState.none) ||
				(inventory.get(i).amount <= numIntemsForThreshhold && 
					inventory.get(i).state == OrderState.ordered))
			{
				print("There are " + inventory.get(i).amount + " " + inventory.get(i).name);
				print("Scheduling order for " + inventory.get(i).name);
				inventory.get(i).state = OrderState.needsToOrder;
				stateChanged();
			}
		}
		
		int steakCounter = 0;
		int chickenCounter = 0;
		int saladCounter = 0;
		int pizzaCounter = 0;
		
		synchronized(markets)
		{
			for (MyMarket mm : markets)
			{
				if (mm.outOfItems.get(0) == true)
				{
					steakCounter++;
				}
				if (mm.outOfItems.get(1) == true)
				{
					chickenCounter++;
				}
				if (mm.outOfItems.get(2) == true)
				{
					saladCounter++;
				}
				if (mm.outOfItems.get(3) == true)
				{
					pizzaCounter++;
				}
			}
		}
		
		if (steakCounter == markets.size() && name == "Steak")
		{
			print("All markets are out of steak");
		}
		if (chickenCounter == markets.size() && name == "Chicken")
		{
			print("All markets are out of chicken");
		}
		if (saladCounter == markets.size() && name == "Salad")
		{
			print("All markets are out of salad");
		}
		if (pizzaCounter == markets.size() && name == "Pizza")
		{
			print("All markets are out of pizza");
		}
	}

//SCHEDULER****************************************************
	
	public boolean pickAndExecuteAnAction() 
	{
		try
		{
			synchronized(icons)
			{
				for (myIcon icon : icons)
				{
					if (icon.state == iconState.none){
						icons.remove(icon);
					}
				}
			}
			//For all markets
			synchronized(markets)
			{
				for (MyMarket mm : markets)
				{
					//For as many items that we have in the menu
					for (int i = 0; i<inventory.size(); i++)
					{
						//If we are below the threshhold for that item
						if (inventory.get(i).amount <= numIntemsForThreshhold)
						{
							//If this particular market has not run out of this item
							if (mm.outOfItems.get(i) == false)
							{
								//If we didn't order this item before from another market
								if(inventory.get(i).state == OrderState.needsToOrder)
								{
									print("Ordering " + inventory.get(i).getName() + " from " + mm.m.getName());
									//Order this item from this market
									OrderFromMarket(i, mm, inventory.get(i).name, numItemsToOrder);
									return true;
								}
							}
						}
					}
				}
			}
			
			
			synchronized(orders)
			{
				for (Order o : orders)
				{
					if (o.s == state.needsProcessing)
					{
						ProcessOrder(o);
						return true;
					}
				}
			}
			
			synchronized(orders)
			{
				for (Order o : orders)
				{
					if (o.s == state.pending) 
					{
						CookOrder(o);
						return true;
					}
				}
			}
	
			synchronized(orders)
			{
				for (Order o : orders) 
				{
					if (o.s == state.done)
					{
						PlateOrder(o);
						return true;
					}
				}
			}
			
			if (theMonitor != null && theMonitor.getCount() > 0){
				TakeTicket();
				return true;
			}
			
			if (myState == WorkState.needToLeave)
			{
				//if there's no customers in the restaurant and the cook has no orders
				if(this.orders.size() == 0 && 
						noItemsOrdered() && this.rest.canLeave())
				{
					LeaveWork();
					return true;
				}
			}
	
			return false;
		}
		catch (ConcurrentModificationException e)
		{
			return true;
		}
	}

//ACTIONS********************************************************

	private void ProcessOrder(Order o)
	{
		print("Processing order of " + o.choice);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Processing order of " + o.choice, new Date()));
		List<Boolean> foods = new ArrayList<Boolean>();
		
		//Constructing a list of what foods are available. False = out of food that item
		synchronized(inventory)
		{
			for (Foods f : inventory)
			{
				if (f.getAmount() == 0)
				{
					foods.add(false);
				}
				else
				{
					foods.add(true);
				}
			}
		}

		synchronized(inventory)
		{
			for (Foods f : inventory)
			{
				if (f.getName() == o.choice)
				{
					if (f.amount == 0)
					{
						print("Telling waiter we are out of " + f.getName());
						trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Telling waiter we are out of " + f.getName(), new Date()));
						o.w.msgOutOfFood(foods, o.choice, o.table);
						orders.remove(o);
					}
					else
					{
						o.s = state.pending;
						CookOrder(o);
					}
				
				}
			}
		}
		
		//For all items in the inventory, if the item is below threshhold, say we didn't order it
		//Then run scheduler
		for (int i = 0; i < inventory.size(); i++)
		{
			if ((inventory.get(i).amount <= numIntemsForThreshhold && 
					inventory.get(i).state == OrderState.none) ||
				(inventory.get(i).amount <= numIntemsForThreshhold && 
					inventory.get(i).state == OrderState.ordered))
			{
				print("There are " + inventory.get(i).amount + " " + inventory.get(i).name);
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "There are " + inventory.get(i).amount + " " + inventory.get(i).name, new Date()));
				print("Scheduling order for " + inventory.get(i).name);
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Scheduling order for " + inventory.get(i).name, new Date()));
				inventory.get(i).state = OrderState.needsToOrder;
				stateChanged();
			}
		}
		
		int steakCounter = 0;
		int chickenCounter = 0;
		int saladCounter = 0;
		int pizzaCounter = 0;
		
		synchronized(markets)
		{
			for (MyMarket mm : markets)
			{
				if (mm.outOfItems.get(0) == true)
				{
					steakCounter++;
				}
				if (mm.outOfItems.get(1) == true)
				{
					chickenCounter++;
				}
				if (mm.outOfItems.get(2) == true)
				{
					saladCounter++;
				}
				if (mm.outOfItems.get(3) == true)
				{
					pizzaCounter++;
				}
			}
		}

		if (steakCounter == markets.size())
		{
			print("All markets are out of steak");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "All markets are out of steak.", new Date()));			
		}
		if (chickenCounter == markets.size())
		{
			print("All markets are out of chicken");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "All markets are out of chicken.", new Date()));

		}
		if (saladCounter == markets.size())
		{
			print("All markets are out of salad");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "All markets are out of salad.", new Date()));
		}
		if (pizzaCounter == markets.size())
		{
			print("All markets are out of pizza");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "All markets are out of pizza.", new Date()));
		}
		
	}
	public void CookOrder(Order o)
	{
		//stub DoCooking;
		o.s = state.cooking;
		
		synchronized(inventory)
		{
			for (Foods f : inventory)
			{
				if (f.getName() == o.choice)
				{
					f.decrement();
					print("There are " + f.getAmount() + " " + f.getName()+ "s remaining");
					trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "There are " + f.getAmount() + " " + f.getName()+ "s remaining", new Date()));

				}
			}
		}
		time = foods.get(o.choice).getCookingTime();
		
		cookGui.DoGoToFridge();
		cookGui.DoGoToGrill();
		print("Cooking " + o.choice + " for " + time + " seconds.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Cooking " + o.choice + " for " + time + " seconds.", new Date()));
		final Order temp = o;
		
		//finish cooking food after a certain cooking time
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				temp.s = state.done;
				print("The order of " + temp.choice + " has finished cooking.");
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "The order of " + temp.choice + " has finished cooking.", new Date()));
				stateChanged();
			}
		}, time * 1000);
	}
	
	private void PlateOrder(Order o)
	{
		cookGui.DoGoToGrill();
		cookGui.DoGoToPlatingArea();
		print("The order of " + o.choice + " has been plated.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "The order of " + o.choice + " has been plated.", new Date()));
		o.w.msgOrderIsReady(o.choice,  o.table);
		Waiter newW = o.w;
		icons.add(new myIcon(newW, iconMap.get(o.choice)));
		orders.remove(o);
	}
	
	private void OrderFromMarket(int index, MyMarket mm, String name, int numToOrder)
	{
		inventory.get(index).state = OrderState.ordering;
		mm.m.msgIWantToOrder(inventory.get(index).name, numItemsToOrder);
	}
	
	private void TakeTicket(){
		print("Picking up ticket");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Picking up ticket", new Date()));
               Ticket data = theMonitor.remove();
               consume_item(data);
       }
       
       private void consume_item(Ticket data){
    	   orders.add(new Order(data.w, data.choice, data.table));
           print("Creating new order from ticket left by " + name 
                               + " for order of " + data.choice);
   		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Creating new order from ticket left by " + name 
                + " for order of " + data.choice, new Date()));
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
	
	public boolean noItemsOrdered()
	{
		int counter = 0;
		for(int i = 0; i<inventory.size(); i++)
		{
			if(inventory.get(i).state == OrderState.none ||
					inventory.get(i).state == OrderState.ordered)
				counter++;
		}
		if (counter == 4)
			return true;
		else 
			return false;
	}
	
	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}
	
	public void forceScheduler()
	{
		stateChanged();
	}

	public void setBuilding(Restaurant restaurant) {
		this.rest = restaurant;
		theMonitor = restaurant.theMonitor;
	}

	public void msgHereIsMonitor(ProducerConsumerMonitor theMonitor2) {
		theMonitor = theMonitor2;
	}

	
	public void LeaveWork()
	{
		cookGui.Disable();
		myState = WorkState.leaving;
		print("CookRole: Called to leave work.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CookRole", "Called to leave work.", new Date()));
		myPerson.msgLeftWork(this, this.accountBalance);
		rest.nullifyCook(this.cookGui);
	}

	public void setSalary(double i) {
		salary = i;
	}
}

