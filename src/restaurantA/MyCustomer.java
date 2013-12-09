package restaurantA;

import restaurantA.WaiterAgent.customerState;

public class MyCustomer{
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

}