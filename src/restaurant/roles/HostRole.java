package restaurant.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import restaurant.HostAgent.customerState;
import restaurant.HostAgent.waiterState;
import restaurant.Restaurant;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import roles.Role;

public class HostRole extends Role implements Host{
	
	public double balance;
	public int NTABLES = 4;
	public Semaphore animSemaphore = new Semaphore(0, true);
	public RestaurantAnimationPanel copyOfAnimPanel;
	
	public WorkState myState = WorkState.none;
	public double salary;
	
	Restaurant r;
	
	//Lists
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public List<Table> tables = Collections.synchronizedList(new ArrayList<Table>());

	//Other Variables
	private String name;
	//private Semaphore atTable = new Semaphore(0,true); for GUI
	public HostGui hostGui = null;
	
//CONSTRUCTOR
	public HostRole(String name, double cash) {
		super();

		balance = cash;
		
		this.name = name;
		
		//Make the tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, false));
		}
	}

//UTILITIES************************************************************
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getCustomers() {
		return customers;
	}

	public Collection getTables() {
		return tables;
	}
	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	//How to Select a Waiter: Check the list for the least busy waiter, and return it
	public MyWaiter PickWaiter()
	{
		int least = 0;
		int index = 0;
		for (int i = 0; i< waiters.size(); i++)
		{
			if (waiters.get(i).isOnBreak == false)
			{
				least = waiters.get(i).getNumCustomersServing();
			}
		}

		for (int i = 0; i< waiters.size(); i++)
		{
			if (waiters.get(i).isOnBreak == false)
			{
				if (waiters.get(i).getNumCustomersServing() <= least)
				{
					index = i;
					least = waiters.get(i).getNumCustomersServing();	
				}
			}
		}
		print("Picked waiter " + waiters.get(index).w.getName());
		return waiters.get(index);
	}
	
//CLASSES****************************************************
		public enum waiterState
		{
			none, wantsBreak, onbreak,
		};
		
		public enum customerState
		{
			notAssigned, waitingToSeeIfFull, waitingForReply, assigned,
			removeFromList, leftRestaurant
		};
		//Table Class
		private class Table {
			Boolean isOccupied;
			int tableNumber;
			
			Table(int tableNumber, Boolean s)
			{
				this.isOccupied = s;
				this.tableNumber = tableNumber;
			}

			void setUnoccupied() 
			{
				isOccupied = false;
			}
			
			void setOccupied()
			{
				isOccupied = true;
			}

			boolean getOccupied() 
			{
				return isOccupied;
			}
			
			int getTableNumber()
			{
				return tableNumber;
			}

			public String toString() 
			{
				return "table " + tableNumber;
			}
		}
		
		//MyCustomer Class
		private class MyCustomer
		{
			Customer c;
			customerState state;
		
			//Constructor	
			MyCustomer(Customer newCustomer, customerState newState)
			{
				c = newCustomer;
				state = newState;
			}
		}
		
		//MyWaiter Class
		private class MyWaiter
		{
			Waiter w;
			int numCustomersServing;
			Boolean isOnBreak = false;
			waiterState state;
			
			MyWaiter(Waiter newWaiter, int num, Boolean b)
			{
				w = newWaiter;
				numCustomersServing = num;
				isOnBreak = b;
				state = waiterState.none;
			}
			
			Waiter getWaiter()
			{
				return w;
			}
			
			int getNumCustomersServing()
			{
				return numCustomersServing;
			}
			
			Boolean getisOnBreak()
			{
				return isOnBreak;
			}
		}

//MESSAGES****************************************************
		public void msgGetPaid(){
			balance += this.r.takePaymentForWork(salary);
		}

	public void msgLeaveWork()
	{
		myState = WorkState.needToLeave;
		stateChanged();
	}

	public void msgNewWaiter(Waiter w)
	{
		waiters.add(new MyWaiter(w, 0, false));	
		stateChanged();
	}
	
	public void msgRemoveWaiter(Waiter w)
	{
		waiters.remove(w);
	}
	
	public void msgCheckForASpot(Customer cust)
	{
		customers.add(new MyCustomer(cust, customerState.waitingToSeeIfFull));
		stateChanged();
	}
	
	public void msgIWantFood(Customer cust, boolean b) 
	{		
		//If Customer wants food, assign him
		if (b)
		{
			synchronized(customers)
			{
				for (MyCustomer mc : customers)
				{
					if (mc.c == cust)
					{
						mc.state = customerState.notAssigned;
					}
				}
				stateChanged();
			}
		}
		
		//Else remove him
		else
		{
			synchronized(customers)
			{
				for (MyCustomer mc : customers)
				{
					if (mc.c == cust)
					{
						mc.state = customerState.removeFromList;
					}
				}
			}
		}
	}

	public void msgTableIsFree(Waiter w, int tableNum) {		
		//Find the table that is free
		for (Table table : tables) {
			if (table.getTableNumber() == tableNum) {
				print("Table " + tableNum + " is now unoccupied.");
				table.setUnoccupied();
				DeLoad(w);
				stateChanged();
			}
		}
	}
	
	public void msgIdLikeToGoOnBreak(Waiter waiter)
	{
		print("Received message that " + waiter.getName() + " wants to go on break.");
		for (MyWaiter mw : waiters)
		{
			if (mw.w == waiter)
			{
				mw.state = waiterState.wantsBreak;
				stateChanged();
			}
		}
	}
	
	public void msgIdLikeToGetOffBreak(Waiter agent)
	{
		print("Received message that " + agent.getName() + " wants to go off break.");
		for (MyWaiter mw : waiters)
		{
			if (mw.w == agent)
			{
				if (mw.isOnBreak == false)
				{
					print(agent.getName() + " is already off break.");
				}
				mw.isOnBreak = false;
				mw.state = waiterState.none;
				stateChanged();
			}
		}
	}


//SCHEDULER****************************************************
	
	public boolean pickAndExecuteAnAction() {
		try
		{
			synchronized(customers)
			{
				for (MyCustomer mc : customers)
				{
					if (mc.state == customerState.removeFromList)
					{
						RemoveCustomer(mc);
						return true;
					}
				}
			}

			synchronized(customers)
			{
				for (MyCustomer mc : customers) 
				{
					//If the customer is waiting for the host to tell him if the restaurant is full
					if(mc.state == customerState.waitingToSeeIfFull)
					{
						int counter = 0;
						for (Table t: tables)
						{
							if(t.getOccupied() == true)
								counter++;
						}

						//If we are full, tell him we are
						if (counter == tables.size())
						{
							print("Telling customer restaurant is full");
							TellCustomerWhetherFull(mc, true);
							return true;
						}

						//If we are not, tell him we are not full
						else
						{
							print("Telling customer there is a seat");
							TellCustomerWhetherFull(mc, false);
							return true;
						}
					}
				}
			}

			synchronized(customers)
			{
				//Check for an unassigned customer and free table, then assign them
				for (MyCustomer mc : customers) 
				{
					if (mc.state == customerState.notAssigned) 
					{
						for (Table t : tables)
						{
							if (t.getOccupied() == false)
							{
								if (waiters.size() != 0)
								{
									for (MyWaiter w : waiters)
									{
										if (w.getisOnBreak() == false)
										{
											AssignCustomer(mc, t);
											return true;
										}
									}
								}
								else
								{
									print("There are no waiters. Please create one.");
									return false;
								}
							}
						}
					}
				}
			}

			synchronized(waiters)
			{
				//Check if waiter can go on break
				for (MyWaiter mw : waiters)
				{
					//If a waiter Wants a break
					if (mw.state == waiterState.wantsBreak)
					{
						//If there's more than one waiter
						if (waiters.size() > 1)
						{
							//if there are other waiters who are not on break
							for (MyWaiter mw_other : waiters)
							{
								if (mw_other.state == waiterState.none)
								{
									if (mw.isOnBreak == false)
									{
										//Grant Break
										GrantBreak(mw, true);
										return true;
									}
									else
									{
										print(mw.w.getName() + " is already on break.");
									}
								}
							}
							print("There's only one waiter available right now.");
						}
						else
						{
							//Deny break if only one waiter
							print("There is only one waiter. Break denied.");
							GrantBreak(mw, false);
						}
					}
				}
			}

		//Check if this host can leave
		if (myState == WorkState.needToLeave)
		{
			if(r.canLeave())
			{
				LeaveWork();
				return true;
			}
		}

		return false;
		} catch(ConcurrentModificationException e)
		{ 
			return true; 
		}	
	}

//ACTIONS********************************************************
	
	private void RemoveCustomer(MyCustomer mc)
	{
		mc.state = customerState.leftRestaurant;
		customers.remove(mc);
	}
	
	private void TellCustomerWhetherFull(MyCustomer mc, Boolean b)
	{
		mc.state = customerState.waitingForReply;
		mc.c.msgRestaurantIsFull(b);
	}

	private void DeLoad(Waiter w)
	{
		synchronized(waiters)
		{
			for (MyWaiter mw : waiters)
			{
				if (mw.w == w)
				{
					mw.numCustomersServing--;
					print(mw.w.getName() + " has one less customer for a total of " + mw.numCustomersServing);
					print("Press refresh to update the information panel. Select the customer from the list to reactivate.");
				}
			}
		}
	}
	
	private void AssignCustomer(MyCustomer mc, Table t) {
		//Assign waiter, message waiter, take care of the table
		MyWaiter w = PickWaiter();
		t.setOccupied();
		mc.state = customerState.assigned;
		print(w.w.getName() + " is the least busy, handling " + w.getNumCustomersServing() + " customers." );
		w.numCustomersServing++;
		print("Assigning " + mc.c.getName() + " to " + t + " and waiter " + w.w.getName() );
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "HostRole", "Assigning " + mc.c.getName() + " to " + t + " and waiter " + w.w.getName(), new Date()));
		w.w.msgSitAtTable(mc.c, t.tableNumber);
		customers.remove(mc);
	}
	
	private void GrantBreak(MyWaiter mw, Boolean b)
	{
		if (b == true)
		{
			print("Granting break for " + mw.w.getName() + " when customers are done. Will stop sending him customers.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "HostRole", "Granting break for " + mw.w.getName() + " when customers are done. Will stop sending him customers.", new Date()));
			mw.state = waiterState.onbreak;
			mw.isOnBreak = true;
			mw.w.msgBreakGranted(true);
		}
		else if (b == false)
		{
			print("Break rejected for " + mw.w.getName());
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "HostRole", "Break rejected for " + mw.w.getName(), new Date()));
			mw.state = waiterState.none;
			mw.isOnBreak = false;
			mw.w.msgBreakGranted(false);
		}
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

	@Override
	public void setAnimPanel(RestaurantAnimationPanel animationPanel) {
		this.copyOfAnimPanel = animationPanel;
		
	}
	
	public boolean areTablesEmpty()
	{
		for (int i = 0; i<4; i++)
		{
			if(tables.get(i).isOccupied)
				return false;
		}
		System.out.println("Tables are empty");
		return true;
	}
	
	public void LeaveWork()
	{
		hostGui.Disable();
		myState = WorkState.leaving;
		print("HostRole: Called to leave work.");
		myPerson.msgLeftWork(this, balance);
		r.nullifyHost();
	}

	public void setBuilding(Restaurant rr)
	{
		r = rr;
	}

	public void setSalary(double i) {
		salary = i;
	}
}

