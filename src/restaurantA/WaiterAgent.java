package restaurantA;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurantA.CookAgent;
import restaurantA.Table;
import restaurantA.RestaurantA.*;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.WaiterGui;
import restaurantA.interfaces.Cook;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Waiter;

import java.util.*;
import roles.*;

public class WaiterAgent extends Role implements Waiter {
	public static final int NTABLES = 3;
	public Semaphore atTable = new Semaphore(0,true);
	public Semaphore atCook = new Semaphore(0,true);
	public Semaphore atOrigin = new Semaphore(0,true);
	public Boolean imHere = true;
	public Boolean leave = false;
	public Semaphore waitForOrder = new Semaphore(0);
	public ArrayList<Table> tables;
	public String name;
	public double balance = 0;
	public List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
    //public CookAgent cook = new CookAgent("Chef"); // ask cameron where i should be storing this lol
    public int numCust = 0;
    public HostAgent host;
	public WaiterGui waiterGui = null;
	public CookAgent cook;
	public CashierAgent cashier;
	public Collection<MyMenuItem> menu;
	public RestaurantA rest;
	Timer timer = new Timer();
	double salary;
	public boolean isAssigned = false;

	public AnimationPanel copyOfAnimPanel;
	public WaiterAgent(String name, HostAgent host, CookAgent cook, CashierAgent cashier) {
		super();
		this.name = name;
	    
	    this.host = host;
	    this.tables = host.tables;
	    this.cook = cook;
	    print("menu: " + rest.menu);
	    this.menu = rest.menu;
	    this.cashier = cashier;
	}
	public WaiterAgent(String name) {
		super();
		this.name = name;
	}
	
	public void setAssigned()
	{
		isAssigned = true;
	}
	
	public boolean isAssigned()
	{
		return isAssigned;
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
		this.host = host;
		this.numCust ++;
		print("cust: " + customer + " table: " + table + " host: " + host);
		print("person " + myPerson);
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
	
	public void msgAtTable() {
		atTable.release();
		stateChanged();
	}
	
	public void msgAtCook() {
		atCook.release();
		stateChanged();
	}
	
	public void msgAtOrigin() {
		
		if (imHere == true){
			//print("msg at origin flicker called");
			imHere = false;
			atOrigin.release();
			stateChanged();
			}
		
	}

	public boolean pickAndExecuteAnAction() {

		for (MyCustomer customer : myCustomers)
		{
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
		
		if (!waiterGui.atHome()){
			waiterGui.DoReturnHome();
			return false;
		}
		
		if (leave){
				if (canLeave())
					LeaveWork();
				else
					return true;
		}
		
		return false;
	}
	
	private Boolean canLeave(){
		 if(rest.currentCustomers.isEmpty()){
		 boolean temp = true;
			for (MyCustomer c : myCustomers){
				if (c.s != customerState.DONE)
					temp = false;
			}
			return (temp);
		 }
		 else return false;
	}

	public void LeaveWork() {
		//print("sizE: " + rest.workingWaiters.size());
		rest.removeMe(this);
	//	print("sizE: " + rest.workingWaiters.size());
		waiterGui.setDone();
		myPerson.msgLeftWork(this, balance);
		
	}
	public void enjoyBreak() {
	print("***** Enjoying break");
	timer.schedule(new TimerTask() {
		Object cookie = 1;
		public void run() {
			waiterGui.SetOffBreak();
			print("******Done with break");
		}
	},
	15000);
}

	public void escortCustomer(MyCustomer c){
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

	public void WalkCustomerToTable(MyCustomer c, Table table) {
		print("Walking customer " + c.c.getCustomerName() + " to table " + table.tableNumber);
		
		c.s = customerState.WAITINGFORESCORT;
		waiterGui.DoReturnOrigin();
		table.setOccupant(c.c);
		host.updateCustpost();
		
		imHere = true;
		
		try {
			atOrigin.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public void multiStepWaiter(MyCustomer c) {
		
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
    	stateChanged();
    }
	// The animation DoXYZ() routines
	
	public void DeliverOrder(MyCustomer c){
		waiterGui.DoSendOrder();
		print("Picking up order for " + c.c.getCustomerName());
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rest.workingCook.msgPickedUpFood(c.c);
		
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

	public void TakeOrder(MyCustomer c){
		print("Taking order from customer " + c.c.getCustomerName());
		
		c.s = customerState.ORDERING;
		c.c.msgWhatOrder(this);
		waiterGui.DoGoToTable(c.c, c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void SendOrder(MyCustomer c){
		c.s = customerState.WAITING;
		print("Bringing customer " + c.c.getCustomerName() + "'s order of " + c.choice + " to the cook");
		rest.workingCook.msgHereIsOrder(this, c.choice, c.table, c.c);
	}

	public void GiveFood(MyCustomer c){
		c.s = customerState.EATING;
		c.c.waitForFood().release();  
		c.c.msgHereIsFood(this, c.choice);
	}
	
	public void RequestCheck(MyCustomer c){
		print("Cashier, please prepare check for " + c.c.getCustomerName());
		c.s = customerState.NEEDSCHECK;
		rest.workingCashier.msgRequestCheck(this, c.c);
	}
	
	public void GiveCheck(MyCustomer c){
		print("Here is your check");
		c.s = customerState.RECEIVEDCHECK;
		c.c.msgHereIsCheck(c.check, rest.workingCashier);
	}
	

	public void ClearTable(MyCustomer c){
		c.s = customerState.DONE;
		print(c.c.getCustomerName() + " is leaving table " + c.table.tableNumber);
		
		rest.workingHost.msgLeavingTable(c.c);
	}
	
	
	
	public boolean isBusy() {
		return false;
	}

	public void setHost(HostAgent host) {
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

	 public Semaphore waitForOrder() {
	    	return waitForOrder;
	    }

	public void msgGoOffBreak() {
		waiterGui.SetOffBreak();
	}

	public void setAnimPanel(AnimationPanel animationPanel)
	{
		copyOfAnimPanel = animationPanel;
	}

	@Override
	public void msgLeaveWork() {
		leave = true;
		stateChanged();
	}
	
	public void setSalary(double sal)
	{
		salary = sal;
	}

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub
		
	}

}