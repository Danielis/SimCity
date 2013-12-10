package restaurantA;

import java.util.Timer;

import roles.*;

import java.util.TimerTask;

import agent.Agent;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.HostGui;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Role {
	public static final int NTABLES = 2;//a global for the number of tables.
	public static final int NWAITERS = 1;
	Timer timer;
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<CustomerAgent> waitingCustomers 
	= Collections.synchronizedList( new ArrayList<CustomerAgent>());
	public ArrayList<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public RestaurantA rest;
	private String name;
	public List<WaiterAgent> waiters;
	public List<MyWaiter> myWaiters;
	public AnimationPanel copyOfAnimPanel = null;
	private double balance = 0;
	private Boolean leave = false;
	double salary;
	class MyWaiter{
		WaiterAgent w;
		waiterState s;
	
			MyWaiter(WaiterAgent w){
				this.w = w;
				this.s = waiterState.WORKING;
			}
	}
	
	enum waiterState {WORKING, WANTSBREAK, ONBREAK};

	
	private Semaphore atTable = new Semaphore(0,true);

	public HostGui hostGui = null;

	public HostAgent(String name) {
		super();
			
		this.name = name;
				
		waiters = Collections.synchronizedList( new ArrayList<WaiterAgent>(NWAITERS));
		myWaiters = Collections.synchronizedList( new ArrayList<MyWaiter>(NWAITERS));
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
	
	public void msgIWantBreak(Waiter waiter){
		print("received message that " + waiter + " wants a break");
		
		synchronized(myWaiters){
		for (MyWaiter w: myWaiters){
			print(w.w.getName());
			if (w.w == waiter){
				w.s = waiterState.WANTSBREAK;
				stateChanged();
				return;
			}
		}
		}
	}
	
	public void msgDoneWithBreak(Waiter waiter) {
		print("received message that " + waiter + " is done with break");
		synchronized(myWaiters){
		for (MyWaiter w: myWaiters){
			print(w.w.getName());
			if (w.w == waiter){
				w.s = waiterState.WORKING;
				stateChanged();
				return;
			}
		}
		}
	}
	
	public void waiterAdded(){
		setWaiters();
		stateChanged();
	}
	
	public void setWaiters(){
		for (WaiterAgent w : waiters){
			Boolean found = false;
			for (MyWaiter mw : myWaiters){
				if (mw.w.equals(w))
					found = true;
			}
			if (!found)
				myWaiters.add(new MyWaiter(w));
		}
	}

	public void msgIWantFood(CustomerAgent cust) {
		waitingCustomers.add(cust);
		updateCustpost();
		stateChanged();
		print("Customer " + cust.getCustomerName() + " has arrived.");
	}

	public void msgLeavingTable(CustomerAgent cust) {
		synchronized(tables){
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				//print(cust + " leaving " + table);
				table.setUnoccupied();
			}
		}
		}
		print("Good-bye " + cust.getCustomerName());
		stateChanged();
	}
	
	public void msgNotWaiting(Customer c){
		synchronized(waitingCustomers){
		for (Customer cust :waitingCustomers){
			if (cust == c)
				waitingCustomers.remove(cust);
		}
		}
		
		updateCustpost();
		stateChanged();
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		//print("message at table sent");
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//print("scheduler called");
		//print("num of waiting cust: " + waitingCustomers.size());
		synchronized(myWaiters){
		for (MyWaiter w: myWaiters){
			if (w.s == waiterState.WANTSBREAK){
				if (CheckBreakOK())
					PutOnBreak(w);
				else
					PutBackToWork(w);
			}
		}
		}
		
		if (!waitingCustomers.isEmpty()){
			synchronized(tables){
				for (Table table : tables) {
					//print("Checking table " + table.tableNumber + " to see if empty");
						if (!table.isOccupied() && !waiters.isEmpty()) {
							WaiterAgent waiterTemp = selectWaiter();
								seatCustomer(waitingCustomers.get(0), table, waiterTemp);//the action
								return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
			TellWaitingCustomersFull();
		}
		
		if (leave){
			if (waitingCustomers.isEmpty() && rest.currentCustomers.isEmpty())
				LeaveWork();
			else
				return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	private void LeaveWork() {
		print("Leaving work");
		rest.noHost();
		myPerson.msgLeftWork(this, balance);
	}

	// Actions
	private void TellWaitingCustomersFull(){
		print("Restaurant is full. Do you mind waiting?");
		for (CustomerAgent c :waitingCustomers){
			c.msgRestaurantFull();
		}
	}
	private void PutBackToWork(MyWaiter w){
		w.s = waiterState.WORKING;
		w.w.msgNoBreak();
		print(w.w.getName() + " cannot go on break.");
	}
	
	private void PutOnBreak(MyWaiter w){
		w.s = waiterState.ONBREAK;
		w.w.msgGoOnBreak();
		print("**************" + w.w.getName() + " can go on break after finishing current customers.");
	}
	
	private boolean CheckBreakOK(){
		int temp = 0;
		print("************** Checking if waiter can go on break");
		for (MyWaiter w: myWaiters){
			if (w.s != waiterState.ONBREAK)
				temp++;
		}
		
		return (temp > 1);
	}

	private WaiterAgent selectWaiter() {
		// TODO Auto-generated method stub
		
		if (myWaiters.size() != 0){
		MyWaiter tempWait = myWaiters.get(0);
		
		for (MyWaiter w : myWaiters){
		if (tempWait.w.numCust > w.w.numCust && w.s != waiterState.ONBREAK)
			tempWait = w;
	
		}
		
		return tempWait.w;
		}
		return null;
		//return null;
	}

	private void seatCustomer(CustomerAgent customer, Table table, WaiterAgent waiter) {
		//gui here
		table.setOccupant(customer);
		waitingCustomers.remove(0);
		updateCustpost();
		waiter.msgBringCustomerToTable(this, customer, table);
	}

	// The animation DoXYZ() routines
	


	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public void setWaiterAgent(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	public void addWaiter(WaiterAgent w){
			waiters.add(w);//how you add to a collections
			print("Waiter " + w.getName() + " added");
			myWaiters.add( new MyWaiter(w) );
			stateChanged();
	}
	
	public void updateCustpost(){
		for (int i =0; i <waitingCustomers.size(); i++){
			waitingCustomers.get(i).customerGui.shuffle(0, 50 + i*30);
		}
	}

	@Override
	public void msgLeaveWork() {
		print("rec mesg");
		leave = true;
		stateChanged();
	}

	@Override
	public void msgGetPaid() {
		balance += this.rest.takePaymentForWork(salary);		
	}

	public void setRestaurant(RestaurantA rest) {
		this.rest = rest;
		waiters = rest.workingWaiters;
	}

	public void setAnimPanel(AnimationPanel animationPanel) {
		copyOfAnimPanel = animationPanel;
	}

	public void setSalary(double i) {
		salary = i;
		
	}

	

}

