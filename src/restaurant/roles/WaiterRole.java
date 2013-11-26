package restaurant.roles;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.MyCustomer;
import restaurant.CustomerState;
import restaurant.Restaurant;
import restaurant.WaiterAgent.myState;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.*;
import roles.Role;
import agent.RestaurantMenu;

public class WaiterRole extends Role implements Waiter
{
	
	//Lists and Other Agents
	public List<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	public Host host;
	public Cook cook;
	public Cashier cashier;
	public WaiterGui waiterGui;
	
	public RestaurantAnimationPanel copyOfAnimPanel;
	
	//List of foods remaining
	public List<Boolean> foodsAvailable = new ArrayList<Boolean>();
	
	//Variables
	protected String name;
	public Boolean isOnBreak = false;
	public myState state = myState.none;
	
	//Menu
	RestaurantMenu Menu;
	Restaurant rest;
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public WaiterRole()
	{
		super();
		this.name = "Default Daniel";
		print("initialized waiter");
		
		//set all items of food available
		for (int i = 0; i<4; i++)
		{
			foodsAvailable.add(true);
		}
	}
	public WaiterRole(String name, Restaurant rest) {
		super();
		this.rest = rest;
		this.name = name;
	}

//UTILITIES***************************************************
	public void recalculateInventory(List<Boolean> temp)
	{
		foodsAvailable.clear();
		for (int i = 0; i<temp.size(); i++)
		{
			foodsAvailable.add(temp.get(i));
		}
	}
	
	public void setAnimPanel(RestaurantAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
	
	public void setHost(Host host) {
		this.host = host;
	}
	
	public void setCook(Cook cook) {
		this.cook = cook;
	}
	
	public void setCashier(Cashier cashier)
	{
		this.cashier = cashier;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getMyCustomers() {
		return myCustomers;
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}
	
	public WaiterGui getGui() {
		return waiterGui;
	}

//CLASSES/ENUMS**********************************************
	
	public enum myState
	{
		none, wantBreak, askedForBreak, onBreak
	}

//MESSAGES****************************************************

	public void msgSetOffBreak()
	{/*
		print(this.name + " is now available.");
		state = myState.none;
		waiterGui.setBreak(false);*/
		isOnBreak = false;
	}
	
	public void msgSetOnBreak()
	{
		/*
		print(this.name + " wants a break.");
		state = myState.wantBreak;
		myPerson.stateChanged();*/
		isOnBreak = true;
	}
	
	public void msgBreakGranted(Boolean permission)
	{
		if (permission == false)
		{
			//state = myState.none;
			isOnBreak = false;
			//waiterGui.setBreak(false);
		}
		else if (permission == true)
		{
			//state = myState.onBreak;
			isOnBreak = true;
			//waiterGui.setBreak(true);
		}
	}
	
	public void msgSitAtTable(Customer c, int table)
	{
		myCustomers.add(new MyCustomer(c, table, CustomerState.waiting, null));
		print("Got message to sit customer " + c.getName());
		myPerson.stateChanged();
	}

	public void msgReadyToOrder(Customer c) 
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.c == c) {
				mc.s = CustomerState.readyToOrder;
				print("Received message that " + c.getName() + " wants to order.");
				myPerson.stateChanged();
			}
		}
	}
	
	public void msgHereIsMyOrder(Customer c, String choice)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.c == c) {
				mc.s = CustomerState.finishedOrdering;
				mc.choice = choice;
				print("Received message that " + c.getName() + "'s order is " + choice);
				myPerson.stateChanged();
			}
		}
	}

	public void msgOutOfFood(List<Boolean> foods, String choice, int table)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.table == table) {
				recalculateInventory(foods);
				mc.s = CustomerState.reordering;
				print("Received message that we are out of " + choice + ".");
				myPerson.stateChanged();
			}
		}
	}
	
	public void msgOrderIsReady(String choice, int table)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.table == table) {
				mc.s = CustomerState.orderReady;
				print("Received message that " + choice + " is ready.");
				myPerson.stateChanged();
			}
		}
	}
	public void msgCheckIsComputed(Customer c, String choice, float owed)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.c == c) 
			{
				mc.s = CustomerState.checkComputed;
				mc.price = owed;
				print("Received message that check is computed for " + c.getName() + " and it is $" + owed);
			}
		}
	}
	
	public void msgDoneEating(Customer c)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.c == c) {
				mc.s = CustomerState.needsCheck;
				print("Received message that customer " + c.getName() + " needs a check.");
				myPerson.stateChanged();
			}
		}
	}
	
	public void msgPayingAndLeaving(Customer c)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.c == c) {
				mc.s = CustomerState.leaving;
				print("Received message that customer " + c.getName() + " is leaving.");
				myPerson.stateChanged();
			}
		}
	}


//SCHEDULER****************************************************
	
	public boolean pickAndExecuteAnAction() 
	{		
		try
		{
			for (MyCustomer mc : myCustomers)
			{
				if (mc.s == CustomerState.leaving) {
					CleanAndNotifyHost(mc);
					return true;
				}
			}
			
			for (MyCustomer mc : myCustomers)
			{
				if (mc.s == CustomerState.needsCheck) {
					PickUpAndGiveCheck(mc);
					return true;
				}
			}	
			
			for (MyCustomer mc : myCustomers) {
				if (mc.s == CustomerState.orderReady) {
					GiveOrderToCustomerAndCheckToCashier(mc);
					return true;
				}
			}
			
			for (MyCustomer mc : myCustomers)
			{
				if (mc.s == CustomerState.reordering) {
					AskAgain(mc);
					return true;
				}
			}
			
			for (MyCustomer mc : myCustomers)
			{
				if (mc.s == CustomerState.finishedOrdering) {
					PlaceOrder(mc);
					return true;
				}
			}
			
			for (MyCustomer mc : myCustomers)
			{
				if (mc.s == CustomerState.readyToOrder) {
					AskOrder(mc);
					return true;
				}
			}
			
			for (MyCustomer mc : myCustomers)
			{
				if (mc.s == CustomerState.waiting) {
					SeatCustomer(mc);
					return true;
				}
			}
			
			if (state == myState.wantBreak)
			{
				AskForBreak();
				return true;
			}
			waiterGui.DoGoToHomePosition();
			return false;
		}
		catch(ConcurrentModificationException e)
		{
			waiterGui.DoGoToHomePosition();
			return false;
		}
	}

//ACTIONS********************************************************

	protected void AskForBreak()
	{
		print("Asking host for a break");
		state = myState.askedForBreak;
		host.msgIdLikeToGoOnBreak(this);
	}
	protected void SeatCustomer(MyCustomer mc) 
	{
		print("Going to host to seat new customer");
		waiterGui.DoGoToWaitingRoom();
		print("Seating " + mc.c.getName() + " at table " + mc.table);
		waiterGui.DoShowTable(mc.table, mc);
		mc.s = CustomerState.seated;
	}

	protected void AskOrder(MyCustomer mc)
	{
		print("Asking " + mc.c.getName() + " for an order.");
		waiterGui.DoGoToTable(mc.table);
		mc.s = CustomerState.ordering;
		mc.c.msgWhatWouldYouLike();
	}
	
	protected void AskAgain(MyCustomer mc)
	{
		print("Asking " + mc.c.getName() + " again for a different order.");
		waiterGui.DoGoToTable(mc.table);
		mc.s = CustomerState.reordered;
		mc.c.msgSorryOutOfFood(foodsAvailable);
	}
	
	protected void PlaceOrder(MyCustomer mc)
	{
		print("Placing the order for " + mc.choice);
		waiterGui.DoGoToCook();
		mc.s = CustomerState.hasOrdered;		
		cook.msgHereIsAnOrder(this, mc.choice, mc.table);
	}
	
	protected void GiveOrderToCustomerAndCheckToCashier(MyCustomer mc)
	{
		print("Getting the order of " + mc.choice + " for " + mc.c.getName());
		waiterGui.DoGoToCook();
		cook.msgTakingItem(this);
		print("Giving " + mc.choice + " to " + mc.c.getName());
		waiterGui.DoGoToTable(mc.table);
		mc.s = CustomerState.eating;
		mc.c.msgHereIsYourFood(mc.choice);
		print("Giving cashier check.");
		waiterGui.DoGoToCashier();
		cashier.msgHereIsACheck(this, mc.c, mc.choice);
	}
	
	protected void PickUpAndGiveCheck(MyCustomer mc)
	{
		print("Picking up check.");
		waiterGui.DoGoToCashier();
		print("Giving check to "+ mc.c.getName());
		waiterGui.DoGoToTable(mc.table);
		mc.s = CustomerState.leaving;
		mc.c.msgHereIsYourCheck(mc.price);
		CleanAndNotifyHost(mc);
	}

	protected void CleanAndNotifyHost(MyCustomer mc)
	{
		print("Cleaning table " + mc.table);
		waiterGui.DoGoToTable(mc.table);
		print("Notifying host that customer " + mc.c.getName() + " is leaving.");
		waiterGui.DoGoToHomePosition();
		mc.s = CustomerState.hasLeft;
		host.msgTableIsFree(this, mc.table);
		myCustomers.remove(mc);

	}

	public void WaitForAnimation()
	{
		try
		{
			animSemaphore.acquire();		
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:", e);
        }
	}
	
	public void DoneWithAnimation()
	{
		animSemaphore.release();
	}

}