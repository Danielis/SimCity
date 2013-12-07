package restaurant.roles;

import agent.Agent;
import agent.RestaurantMenu;
import restaurant.CustomerState;
import restaurant.MyCustomer;
import restaurant.Restaurant;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.*;

import java.awt.Menu;
import java.util.*;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;

//Waiter Agent
public class TraditionalWaiterRole extends WaiterRole implements Waiter {
	
	public WorkState myWorkState = WorkState.none;
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public TraditionalWaiterRole(String string, Restaurant r, double cash)
	{
		super();
		this.rest = r;
		this.name = string;
		balance = cash;
		
		//set all items of food available
		for (int i = 0; i<4; i++)
		{
			foodsAvailable.add(true);
		}
	}
	public TraditionalWaiterRole(String name) {
		super();

		this.name = name;
		print("I'm a traditional waiter! I hate the ticket system.");
	}


//MESSAGES****************************************************

	public void msgLeaveWork()
	{
		myWorkState = WorkState.needToLeave;
		stateChanged();
	}
	
	public void msgHereIsMyOrder(Customer c, String choice)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.c == c) {
				mc.s = CustomerState.finishedOrdering;
				mc.choice = choice;
				print("Received message that " + c.getName() + "'s order is " + choice);
				stateChanged();
			}
		}
	}

	
	public void msgOrderIsReady(String choice, int table)
	{
		for (MyCustomer mc: myCustomers) {
			if (mc.table == table) {
				mc.s = CustomerState.orderReady;
				print("Received message that " + choice + " is ready.");
				stateChanged();
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
			
			if (myWorkState == WorkState.needToLeave)
			{
				if(rest.canLeave())
				{
					LeaveWork();
					return true;
				}
			}
			waiterGui.DoGoToHomePosition();
			return false;
		}
		catch(ConcurrentModificationException e)
		{
			waiterGui.DoGoToHomePosition();
			return true;
		}
	}

//ACTIONS********************************************************

	protected void PlaceOrder(MyCustomer mc)
	{
		print("Placing the order for " + mc.choice);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "TraditionalWaiterRole", "Placing the order for " + mc.choice, new Date()));

		waiterGui.DoGoToCook();
		mc.s = CustomerState.hasOrdered;
		//print("c: "+ mc.choice +" t: " + mc.table);
		cook.msgHereIsAnOrder(this, mc.choice, mc.table);
	}
	
	public void LeaveWork()
	{
		rest.removeme(this);
		waiterGui.Disable();
		myWorkState = WorkState.leaving;
		print("TraditionalWaiterRole: Called to leave work.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "TraditionalWaiterRole", "Called to leave work.", new Date()));
		myPerson.msgLeftWork(this, this.balance);
	}

}

