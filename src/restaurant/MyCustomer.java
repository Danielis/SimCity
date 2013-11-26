package restaurant;

import restaurant.interfaces.Customer;

	public class MyCustomer
	{
		//Variables
		public Customer c;
		public int table;
		public CustomerState s;
		public String choice;
		public float price;
		
		//Constructor
		public MyCustomer(Customer newCust, int newTable, CustomerState newState, String newChoice)
		{
			c = newCust;
			table = newTable;
			s = newState;
			choice = newChoice;
			price = 0;
		}
	}