package restaurant.roles;

import restaurant.CustomerState;
import restaurant.MyCustomer;
import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import restaurant.ProducerConsumerMonitor.Ticket;
import restaurant.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;

//Waiter Agent
public class ModernWaiterRole extends WaiterRole implements Waiter {
		
	public WorkState myWorkState = WorkState.none;
	//Lists and Other Agents
	double balance = 0;
	//List of foods remaining
    private int itemCount = 0;
    
	//Variables
	public ProducerConsumerMonitor theMonitor;
	
	//Menu
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public ModernWaiterRole(String name, Restaurant r, double cash) {
		super(name, r);
		this.rest = r;
		this.name = name;
		balance = cash;
		theMonitor = r.theMonitor;
		print("I'm a modern waiter! I love the ticket system.");
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
				print("Received message that " + c.getName() + "'s Ticket is " + choice);
				stateChanged();
			}
		}
	}

	
	public void msgTicketIsReady(String choice, int table)
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
					PlaceTicket(mc);
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
	
	public void PlaceTicket(MyCustomer mc)
	{
		print("Placing the Ticket for " + mc.choice);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "ModernWaiterRole", "Placing the Ticket for " + mc.choice, new Date()));
		waiterGui.DoGoToCook();
		mc.s = CustomerState.hasOrdered;		
		//cook.msgHereIsAnTicket(this, mc.choice, mc.table);
		Ticket data = produce_item(this, mc.choice, mc.table);
        print("Placed ticket for table " + data.table
                + " with order of " + data.choice);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "ModernWaiterRole", "Placed ticket for table " + data.table
                + " with order of " + data.choice, new Date()));

        theMonitor.insert(data);
	}
	
	public Ticket produce_item(Waiter w, String choice, int tb){
		Ticket data;
        data = theMonitor.new Ticket(this, cook, choice, tb);
        itemCount++;
        print("Creating new ticket for table " + data.table
                + " with order of " + data.choice);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "ModernWaiterRole", "Creating new ticket for table " + data.table
                + " with order of " + data.choice, new Date()));

        cook.msgHereIsMonitor(theMonitor);
        return data;
    }
	
	public void LeaveWork()
	{
		rest.removeme(this);
		waiterGui.Disable();
		myWorkState = WorkState.leaving;
		print("ModernWaiterRole: Called to leave work.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "ModernWaiterRole", "Called to leave work", new Date()));
		myPerson.msgLeftWork(this, this.balance);
	}
}

