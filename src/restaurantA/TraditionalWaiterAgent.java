package restaurantA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurantA.RestaurantA.MyMenuItem;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.WaiterGui;
import restaurantA.interfaces.*;

public class TraditionalWaiterAgent extends WaiterAgent implements Waiter{

	public TraditionalWaiterAgent(String name, HostAgent host, CookAgent cook, CashierAgent cashier) {
		super(name, host, cook, cashier);
		this.name = name;
	    this.host = host;
	    this.tables = host.tables;
	    this.cook = cook;
	    print("menu: " + rest.menu);
	    this.menu = rest.menu;
	    this.cashier = cashier;
	}
	
	public void msgReadyToOrder(Customer c)
	{
		synchronized(myCustomers){
			for (MyCustomer customer : myCustomers) 
			{
				if (customer.c == c)
				{
					customer.s = customerState.READY;
					stateChanged();
				}
			}
		}
	}

	public void msgHereIsChoice(Customer c, String choice)
	{
		synchronized(myCustomers)
		{
			for (MyCustomer customer : myCustomers) 
			{
				if (customer.c == c)
				{
					customer.s = customerState.ORDERED;
					customer.choice = choice;
					stateChanged();
				}
			}
		}
	}

	public void msgOrderDone(Cook cook, String choice, Table table, Customer c){
		synchronized(myCustomers)
		{
			for (MyCustomer customer : myCustomers) 
			{
				if (customer.c == c)
				{
					customer.s = customerState.FOODREADY;
					stateChanged();
				}
			}
		}
	}

	//SCHEDULER
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
		
		if (onBreak)
		{
			boolean temp = true;
			for (MyCustomer c : myCustomers)
			{
				if (c.s != customerState.DONE)
					temp = false;
			}
			if (temp)
			{
				waiterGui.DoReturnHome();
				enjoyBreak();
				return false;
			}
		}
		
		if (!waiterGui.atHome())
		{
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
	}

	public void SendOrder(MyCustomer c){
		//DoSendOrder(); //animation
		c.s = customerState.WAITING;
		print("Bringing customer " + c.c.getCustomerName() + "'s order of " + c.choice + " to the cook");
		rest.workingCook.msgHereIsOrder(this, c.choice, c.table, c.c);
	}
}