package restaurantD;

import agent.Agent;
import restaurantD.gui.WaiterGui;
import restaurantD.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 2;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers
	= new ArrayList<Customer>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public List<Waiter> waiters
	= new ArrayList<Waiter>();
	
    private int waitercount = 0; //Used to determine which waiter to use
	
    private String name;
    
    public CookAgent cook = null;
	
	//Menu which will be static right now
	public HashMap<Integer,String> menu = new HashMap<Integer,String> ();
	
	public WaiterGui hostGui = null;

	public HostAgent(String name,CookAgent c) {
		super();
		
		this.name = name;
		this.cook = c;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}
	
	//Hack for waiter to get into gui right now //////////////////////////////////////////
	public void addWaiter(Waiter w){
		waiters.add(w);
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
	
	// Messages

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingTable(Customer cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void iWouldLikeToGoOnBreak(Waiter w){
		int waitersonbreak=0;
		for(int i=0;i<waiters.size();i++){
			if(waiters.get(i).isOnBreak()){
				waitersonbreak++;
				print("+++++++++++=============");
			}
		}
		if(waitersonbreak==waiters.size()-1){
			w.goOnBreak(false);
		}
		else
			w.goOnBreak(true);print("OnBreak assigned true");
	}
	public void iAmOffBreak(Waiter w){
		for(int i=0;i<waiters.size();i++){
			if(waiters.get(i)==w){
				waiters.get(i).goOnBreak(false);
			}
		}
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()){
					if(!(waitingCustomers.get(0).getWaiter() == null)){
						for(int i=0;i<waiters.size();i++){
							if(waiters.get(i) == waitingCustomers.get(0).getWaiter()){
								reSeatCustomer(waitingCustomers.get(0), table, waiters.get(i));//the action
							}
						}
					}
					else{
						for(int i=0;i<waiters.size();i++){
							if(waitercount==waiters.size())
								waitercount=0;
							if(!waiters.get(waitercount).isOnBreak())
								break;
							waitercount++;
						}
						print("ASSIGNED " + waitingCustomers.get(0).getName() + " to " + waiters.get(waitercount).getName());
						seatCustomer(waitingCustomers.get(0), table, waiters.get(waitercount));//the action
						waitercount++;
						//if(waitercount == waiters.size()) waitercount=0;
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(Customer customer, Table table,Waiter w) {
		w.SeatCustomer(customer, table.getTable());
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}
	private void reSeatCustomer(Customer customer, Table table,Waiter w) {
		w.reSeatCustomer(customer, table.getTable());
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}

	//utilities

	public void setGui(WaiterGui gui) {
		hostGui = gui;
	}
	public void setCook(CookAgent cook){
		this.cook=cook;
	}
	public WaiterGui getGui() {
		return hostGui;
	}

	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		public int getTable(){
			return tableNumber;
		}
	}
}

