package restaurantA;

import restaurantA.ProducerConsumerMonitor.Ticket;
import restaurantA.MyCustomer;
import restaurantA.WaiterAgent.*;
import restaurantA.interfaces.*;

public class ModernWaiterAgent extends WaiterAgent implements Waiter{
	
	ProducerConsumerMonitor theMonitor;

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
	
	public ModernWaiterAgent(String name, RestaurantA r, double bal) {
		super(name);
		this.rest = r;
		this.name = name;
		theMonitor = r.theMonitor;
		balance = bal;
		print("I'm a modern waiter from Restaurant A! I love the ticket system.");
	}
	
	public void msgTicketIsReady(Cook cook, String choice, Table table, Customer c)
	{
		for (MyCustomer customer: myCustomers) {
			if (customer.c == c) {
				customer.s = customerState.FOODREADY;
				print("Received message that " + choice + " is ready.");
				stateChanged();
			}
		}
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
	
	
	/* ACTIONS******************************************************/
	


	public void SendOrder(MyCustomer c){
		c.s = customerState.WAITING;
		print("Bringing customer " + c.c.getCustomerName() + "'s order of " + c.choice + " to the cook");
		rest.workingCook.msgHereIsOrder(this, c.choice, c.table, c.c);
	}
	
	public Ticket produce_item(Waiter w, Cook ck, String choice, Table newTable, Customer cu){
		Ticket data;
		data = theMonitor.new Ticket(this, ck, choice, newTable, cu);
		print("Creating new ticket for table " + data.table + " with order of " + data.choice);
		cook.msgHereIsMonitor(theMonitor);
		return data;
	}
	
	protected void PlaceTicket(MyCustomer c)
	{
		c.s = customerState.WAITING;
		print("Placing " + c.c.getCustomerName() + "'s order of " + c.choice + " as a ticket.");
		Ticket data = produce_item(this, this.cook, c.choice, c.table, c.c);
		theMonitor.insert(data);
	}
}
