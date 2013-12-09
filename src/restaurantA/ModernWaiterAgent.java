package restaurantA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurantA.RestaurantA.MyMenuItem;
import restaurantA.MyCustomer;
import restaurantA.WaiterAgent.*;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.WaiterGui;
import restaurantA.interfaces.*;

public class ModernWaiterAgent extends WaiterAgent implements Waiter{

	public List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public ModernWaiterAgent(String name, HostAgent host, CookAgent cook, CashierAgent cashier) {
		super(name, host, cook, cashier);
		this.name = name;

		this.host = host;
		this.tables = host.tables;
		this.cook = cook;
		print("menu: " + rest.menu);
		this.menu = rest.menu;
		this.cashier = cashier;
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

	public boolean pickAndExecuteAnAction() 
	{
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
			return false;
		}

		if (leave){
			boolean temp = true;
			for (MyCustomer c : myCustomers){
				if (c.s != customerState.DONE)
					temp = false;
			}
			if (temp)
				LeaveWork();
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	public void LeaveWork() {
		rest.removeMe(this);
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

	public void DeliverOrder(MyCustomer c){
		waiterGui.DoSendOrder(); //animation pick up and deliver food
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

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub

	}

}
