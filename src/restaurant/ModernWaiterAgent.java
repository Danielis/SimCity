package restaurant;


import restaurant.ProducerConsumerMonitor.Ticket;
import restaurant.interfaces.*;
import roles.Restaurant;

import java.util.*;
import java.util.concurrent.Semaphore;

//Waiter Agent
public class ModernWaiterAgent extends WaiterAgent implements Waiter {
	
	//Lists and Other Agents

	//List of foods remaining
    private int itemCount = 0;
    
	//Variables
	private ProducerConsumerMonitor theMonitor;
	
	//Menu
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public ModernWaiterAgent()
	{
		super();
		this.name = "Default Daniel";
		
		//set all items of food available
		for (int i = 0; i<4; i++)
		{
			foodsAvailable.add(true);
		}
		//theMonitor = ;
	}
	public ModernWaiterAgent(String name, Restaurant r) {
		super();
		this.rest = r;
		this.name = name;
		theMonitor = r.theMonitor;
		print("I'm a modern waiter! I love the ticket system.");
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

	
	
	protected void PlaceTicket(MyCustomer mc)
	{
		print("Placing the Ticket for " + mc.choice);
		waiterGui.DoGoToCook();
		mc.s = CustomerState.hasOrdered;		
		//cook.msgHereIsAnTicket(this, mc.choice, mc.table);
		Ticket data = produce_item(this, mc.choice, mc.table);
        print("Placed ticket for table " + data.table
                + " with order of " + data.choice);
        theMonitor.insert(data);
        
        //try{sleep(1000);}
        //catch(InterruptedException ex){};
	}
	
	private Ticket produce_item(ModernWaiterAgent w, String choice, int tb){
		Ticket data;
        //try{sleep(1000);}
        //catch(InterruptedException ex){};
        data = theMonitor.new Ticket(this, cook, choice, tb);
        itemCount++;
        print("Creating new ticket for table " + data.table
                + " with order of " + data.choice);
        cook.msgHereIsMonitor(theMonitor);
        return data;
    }
    
	


   

}

