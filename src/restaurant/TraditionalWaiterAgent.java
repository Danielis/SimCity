package restaurant;

import agent.Agent;
import agent.RestaurantMenu;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.*;

import java.awt.Menu;
import java.util.*;
import java.util.concurrent.Semaphore;

//Waiter Agent
public class TraditionalWaiterAgent extends WaiterAgent implements Waiter {
	
	//Lists and Other Agents

	//List of foods remaining
	
	//Variables
	
	//Menu
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public TraditionalWaiterAgent()
	{
		super();
		this.name = "Default Daniel";
		
		//set all items of food available
		for (int i = 0; i<4; i++)
		{
			foodsAvailable.add(true);
		}
	}
	public TraditionalWaiterAgent(String name) {
		super();

		this.name = name;
		print("I'm a traditional waiter! I hate the ticket system.");
	}

//UTILITIES***************************************************
	
//CLASSES/ENUMS**********************************************
		
	
//MESSAGES****************************************************

	
	
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
	
	protected boolean pickAndExecuteAnAction() 
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

	
	
	protected void PlaceOrder(MyCustomer mc)
	{
		print("Placing the order for " + mc.choice);
		waiterGui.DoGoToCook();
		mc.s = CustomerState.hasOrdered;		
		cook.msgHereIsAnOrder(this, mc.choice, mc.table);
	}
	
	



}

