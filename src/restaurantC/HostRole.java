package restaurantC;

import java.util.*;
import java.util.concurrent.Semaphore;

import roles.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role {
	//number of tables
	static final int NTABLES = 3;

	//list that tracks the customers
	public List<CustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<CustomerRole>());

	//collection of tables
	public Collection<Table> tables;

	//class that keeps track of waiters
	private class myWaiter{
		WaiterRole w;
		CustomerRole c;
		Table t;
		boolean occupied;
		boolean onBreak;
		public myWaiter(WaiterRole waiter){
			w = waiter;
			occupied = false;
			onBreak = false;
		}
	};

	public List<myWaiter> waiters = Collections.synchronizedList(new ArrayList<myWaiter>());

	private String name;

	//constructor
	public HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}

	}


	//------------getters and setters----------------------
	public void setWaiter(WaiterRole waiter) {
		waiters.add(new myWaiter(waiter));
	}

	public String getMaitreDName() {
		return name;
	}
	public String getName() {
		return name;
	}
	public List getWaitingCustomers() {
		return waitingCustomers;
	}
	public Collection getTables() {
		return tables;
	}

	//-----------------------------------------------------------
	//----------------------Messages-----------------------------
	//-----------------------------------------------------------
	//customer asked for food.  add him to waiting customers thing
	public void IWantFood(CustomerRole cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}
	//comes from waiter, customer left table
	public void msgLeavingTable(CustomerRole cust, WaiterRole w){
		synchronized(tables) {
			for (Table table : tables) {
				if (table.getOccupant() == cust) {
					print(cust + " leaving " + table);
					table.setUnoccupied();
					synchronized(waiters) {
						for(myWaiter waiter:waiters) {
							if(waiter.c == cust) {
								waiter.occupied = false;
							}
						}
					}
					stateChanged();
				}
			}
		}
	}
	//comes from waiter, asking for break
	public void askForBreak(WaiterRole w) {
		synchronized(waiters) {
			for(myWaiter waiter:waiters) {
				if(waiter.w == w) {
					if(waiters.size() <= 1) {
						System.out.println(name + ": No, you are the only one working.");
						waiter.w.noBreak();
						waiter.onBreak = false;
					}
					else if(waiter.occupied == false){
						System.out.println("Yes. Go on break.");
						waiter.onBreak = true;
						w.goOnBreak();
					}
					else
					{
						waiter.onBreak = true;
						System.out.println("You're busy. Once you finish with your customers.");
					}
				}
			}
		}
	}

	public void onBreak(WaiterRole w) {
		synchronized(waiters) {
			for(myWaiter waiter:waiters) {
				if(waiter.w == w) {
					waiter.onBreak = true;
				}
			}
		}
	}

	//------------------------------------------------------------------
	//----------------Scheduler-----------------------------------------
	//------------------------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		if(waiters.isEmpty()) {
			return false;
		}
		synchronized(tables) {
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						synchronized(waiters) {
							for(myWaiter mw:waiters) {
								if(mw.occupied == false && mw.onBreak == false) {
									pairCustomer(waitingCustomers.get(0), table, mw);
									return true;
								}
							}
						}
						synchronized(waiters) {
							for(myWaiter mw:waiters) {
								if(!mw.onBreak) {
									pairCustomer(waitingCustomers.get(0), table, mw);
									return true;
								}

							}
						}
					}
				}
			}
		}
		if(!waitingCustomers.isEmpty()) {
			restaurantFull(waitingCustomers.get(waitingCustomers.size() - 1));
		}
		return false;
	}

	//----------------------Actions-------------------------------------
	//assigns customer to a waiter
	//right now there's only one waiter
	private void pairCustomer(CustomerRole customer, Table table, myWaiter waiter) {
		waiter.occupied = true;
		waiter.c = customer;
		waiter.t = table;
		customer.setWaiter(waiter.w);
		table.setOccupant(customer);
		waiter.w.seatCustomer(customer, table.tableNumber);
		waitingCustomers.remove(customer);
		System.out.println("Host: customer has been assigned to a waiter.");
	}
	private void restaurantFull(CustomerRole customer) {
		System.out.println(name + ": No tables available at this time.");
		customer.restaurantFull();
	}


	//TABLE CLASS
	private class Table {
		CustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
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

