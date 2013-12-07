package restaurantA;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import agent.Agent;

import java.util.concurrent.Semaphore;

import restaurantA.CookAgent;
import restaurantA.Table;
import restaurantA.CookAgent.MyMenuItem;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.HostGui;
import restaurantA.gui.WaiterGui;
import restaurantA.interfaces.Cook;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Waiter;

import java.util.*;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent implements Waiter {
	public static final int NTABLES = 3;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atOrigin = new Semaphore(0,true);
	private Boolean imHere = true;
	
	public Semaphore waitForOrder = new Semaphore(0);
	public ArrayList<Table> tables;
	private String name;
	private List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
    //public CookAgent cook = new CookAgent("Chef"); // ask cameron where i should be storing this lol
    public int numCust;
    public HostAgent host;
	public WaiterGui waiterGui = null;
	public CookAgent cook;
	public CashierAgent cashier;
	public Collection<MyMenuItem> menu;
	Timer timer = new Timer();

	public AnimationPanel copyOfAnimPanel;
	public WaiterAgent(String name, HostAgent host, CookAgent cook, CashierAgent cashier) {
		super();
		this.name = name;
	    
	    numCust = 0;
	    this.host = host;
	    this.tables = host.tables;
	    this.cook = cook;
	    this.menu = cook.menu;
	    this.cashier = cashier;
	}
	public boolean onBreak = false;

	public void msgGoOnBreak(){
		waiterGui.SetBreak();
		
		onBreak = true;
		stateChanged();
	}
	
	public void msgNoBreak(){
		
	}
	
	public void msgOutOfFood(Cook cook, String choice, Table table, Customer c) {
		synchronized(myCustomers){
		for (MyCustomer customer : myCustomers) { 
			if (customer.c == c){
				customer.s = customerState.REORDER;
				stateChanged();
			}
		}
		}
	}
	
	public void msgBringCustomerToTable(HostAgent host, CustomerAgent customer, Table table) {
		myCustomers.add(new MyCustomer(customer, table, customerState.FOLLOWING));
		//print ("Waiter received message to bring customer " + customer.getCustomerName() + " to table " + table.tableNumber + ".");
		this.numCust ++;
		stateChanged();
	}
	
	public void msgLeavingTable(Customer c) {
		synchronized(myCustomers){
		for (MyCustomer customer : myCustomers) {
			if (customer.c == c){
				//print("msg that customer " + c.getCustomerName() + " is leaving");
				customer.s = customerState.FINISHED;
				stateChanged();
			}
		}
		}
		this.numCust--;
		
		//print("message 1 rec");
	}

	public void msgReadyToOrder(Customer c){
		//MyCustomer cust =  MyCustomer.find(c); ASK CAMERON
		//print("received message ready to order from customer " + c.getCustomerName());
		synchronized(myCustomers){
		for (MyCustomer customer : myCustomers) {
			if (customer.c == c){
				//print("msg that customer " + c.getCustomerName() + " ready to order");
				customer.s = customerState.READY;
				stateChanged();
			}
		}
		}
	//	cust.s = customerState.READY;
		//print("message 2 rec");
	}

	public void msgHereIsChoice(Customer c, String choice){
//		cust = new MyCustomer (myCustomers.find(c));
//		cust.s = customerState.ORDERED;
//		cust.choice = choice;
		synchronized(myCustomers){
		for (MyCustomer customer : myCustomers) { // ask cameron how to do this more elegantly lol
			if (customer.c == c){
			//print("msg that customer " + c.getCustomerName() + " ordered");
				customer.s = customerState.ORDERED;
				customer.choice = choice;
				stateChanged();
			}
		}
		}
		//print("message 3 rec");
	}

	public void msgOrderDone(Cook cook, String choice, Table table, Customer c){
//		cust = new MyCustomer (myCustomers.find(table));
//		cust.s = customerState.FOODREADY;
		synchronized(myCustomers){
		for (MyCustomer customer : myCustomers) { // ask cameron how to do this more elegantly lol
			if (customer.c == c){
				customer.s = customerState.FOODREADY;
				stateChanged();
			}
		}
		}
		//print("message 4 rec");
	}
	
	public void msgPickUpCheck(Check c){
		print("Picked up check");
		synchronized(myCustomers){

		for (MyCustomer customer : myCustomers) { // ask cameron how to do this more elegantly lol
			if (customer.c == c.c){
				customer.s = customerState.CHECKREADY;
				customer.check = c;
				stateChanged();
				return;
			}
		}
		}
	}
	
	public void msgAtTable() {//from animation
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgAtCook() {//from animation
		atCook.release();// = true;
		stateChanged();
	}
	
	public void msgAtOrigin() {//from animation
		// = true;
		
		if (imHere == true){
			//print("msg at origin flicker called");
			imHere = false;
			atOrigin.release();
			stateChanged();
			}
		
	}

//	public void msgDoneEating(CustomerAgent c){
//		cust = new MyCustomer (myCustomers.find(c));
//		cust.s = customerState.FINISHED;
//		
//		print("message 5 rec");
//	}
//	
	
	// scheduler
	// TODO
	protected boolean pickAndExecuteAnAction() {
		
		//print("waiter scheduler called");
		
		
		
		for (MyCustomer customer : myCustomers)
		{
			//print("checking customer " + customer.c.getCustomerName() + " (state: " + customer.s.toString() +")");
		
			if (customer.s == customerState.WAITINGFORESCORT){
				escortCustomer(customer);
				return true;
			}
			if (customer.s == customerState.FOLLOWING){
				WalkCustomerToTable(customer, customer.table);
				return true;
			}
			
		}
		
		
		for (MyCustomer customer : myCustomers)
		{	
			//print("state: " + customer.s);
			
			if (customer.s == customerState.CHECKREADY){
				GiveCheck(customer);
				return true;
			}
			if (customer.s == customerState.EATING){
				RequestCheck(customer);
				return true;
			}
			if (customer.s == customerState.ORDERED){
				SendOrder(customer);
				return true;
			}
			if (customer.s == customerState.FINISHED){
				ClearTable(customer);
				return true;
			}
			if (customer.s == customerState.FOODREADY){
				DeliverOrder(customer);
				return true;
			}
			if (customer.s == customerState.READY || customer.s == customerState.REORDER){
				multiStepWaiter(customer); 
				return true;
			}
			if (customer.s == customerState.FOODTABLE){
				GiveFood(customer); 
				return true;
			}
		
		}
		
		if (onBreak){
		boolean temp = true;
		for (MyCustomer c : myCustomers){
			if (c.s != customerState.DONE)
				temp = false;
		}
		if (temp){
			waiterGui.DoReturnHome();
			enjoyBreak();
			return false;
		}
		}
		
		if (!waiterGui.atHome()){ //if there's nothing to do, return to origin
			waiterGui.DoReturnHome();
			return false; //TODO a more elegant implementation of this lol...
		}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

private void enjoyBreak() {
	print("***** Enjoying break");
	timer.schedule(new TimerTask() {
		Object cookie = 1;
		public void run() {
			waiterGui.SetOffBreak();
			print("******Done with break");
		}
	},
	15000);

	//print("**done with break");
}

private void escortCustomer(MyCustomer c){
	//print("escort*********" + c.c.getCustomerName());
	print("Picking up customer " + c.c.getCustomerName());
	c.c.msgSitAtTable(this, c.table);
	waiterGui.DoGoToTable(c.c, c.table);
	c.s = customerState.SEATED;
	try {
		atTable.acquire();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	private void WalkCustomerToTable(MyCustomer c, Table table) {
		print("Walking customer " + c.c.getCustomerName() + " to table " + table.tableNumber);
		
		c.s = customerState.WAITINGFORESCORT;
		waiterGui.DoReturnOrigin();
		table.setOccupant(c.c);
		host.updateCustpost();
		
		imHere = true;
		
		try {
			atOrigin.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//waiterGui.DoReturnOrigin();
	}

	
	private void multiStepWaiter(MyCustomer c) {
		
		if (c.s == customerState.REORDER){
			print("Sorry " + c.c.getCustomerName() + ", you have to re-order.");
			c.c.msgYouNeedToReOrder();
		}
		
		
		TakeOrder(c);
    	c.c.waitForWaiter().release();
    	
    	try {
    		waitForOrder.acquire();
    	} catch(Exception ex) {
    	}

    		
    	waiterGui.DoSendOrder();
    	
    	try {
    		atCook.acquire();
    	} catch(Exception ex) {
    	}
    	
    	//c.c.waitForFood().release();    
    	
    	stateChanged();
    }
	// The animation DoXYZ() routines
	
	private void DeliverOrder(MyCustomer c){
		//createCheck(c);
		waiterGui.DoSendOrder(); //animation pick up and deliver food
		print("Picking up order for " + c.c.getCustomerName());
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgPickedUpFood(c.c);
		
		waiterGui.DoDeliverOrder(c.c, c.table, c.choice);
		print("Delivering order to " + c.c.getCustomerName());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c.s = customerState.FOODTABLE;
		
		stateChanged();
	}

//	private void createCheck(MyCustomer c) {
//		if (c.check == null)
//		c.check = new Check(c.choice, c.table, c.c);			
//	}

	public void TakeOrder(MyCustomer c){
		print("Taking order from customer " + c.c.getCustomerName());
		
		c.s = customerState.ORDERING;
		c.c.msgWhatOrder(this);
		waiterGui.DoGoToTable(c.c, c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SendOrder(MyCustomer c){
		//DoSendOrder(); //animation
		c.s = customerState.WAITING;
		print("Bringing customer " + c.c.getCustomerName() + "'s order of " + c.choice + " to the cook");
		cook.msgHereIsOrder(this, c.choice, c.table, c.c);
	}

	public void GiveFood(MyCustomer c){
		c.s = customerState.EATING;
		c.c.waitForFood().release();  
		c.c.msgHereIsFood(this, c.choice);
	}
	
	public void RequestCheck(MyCustomer c){
		print("Cashier, please prepare check for " + c.c.getCustomerName());
		c.s = customerState.NEEDSCHECK;
		cashier.msgRequestCheck(this, c.c);
	}
	
	public void GiveCheck(MyCustomer c){
		print("Here is your check");
		c.s = customerState.RECEIVEDCHECK;
		c.c.msgHereIsCheck(c.check, cashier);
	}
	

	public void ClearTable(MyCustomer c){
		c.s = customerState.DONE;
		print(c.c.getCustomerName() + " is leaving table " + c.table.tableNumber);
		
		host.msgLeavingTable(c.c);
	}
	
	
	
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setHost(HostAgent host) {
		// TODO Auto-generated method stub
		this.host = host;
		this.tables = host.tables;
	}
	

	public String getName() {
		return name;
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	private class MyCustomer{
		CustomerAgent c;
		Table table;
		String choice;
		customerState s;
		Check check;
		
		MyCustomer(CustomerAgent c, Table table, customerState s) {
			this.c = c;
			this.table = table;
			this.s = s;
		}

//		private MyCustomer find(CustomerAgent c2) {
//			for (MyCustomer customer : myCustomers) {
//				if (customer.c == c2)
//					return customer;
//			}
//			return null;
//		}
		

	}
	 public Semaphore waitForOrder() {
	    	return waitForOrder;
	    }

	enum customerState {FOLLOWING, WAITINGFORESCORT, FOODTABLE, SEATED, READY, ORDERING,
		ORDERED, WAITING, FOODREADY, EATING, FINISHED, DONE, REORDER, RECEIVEDCHECK, NEEDSCHECK, CHECKREADY}

	public void msgGoOffBreak() {
		waiterGui.SetOffBreak();
	}

	public void setAnimPanel(AnimationPanel animationPanel)
	{
		copyOfAnimPanel = animationPanel;
	}

}