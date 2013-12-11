package restaurantD.test.mock;


import restaurantD.interfaces.*;
import restaurantD.test.mock.EventLog;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter{

	private boolean onBreak=false;
	public EventLog log;
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockWaiter(String name) {
		super(name);
		log = new EventLog();
	}
	public String getName(){ return super.getName();}
	
	public boolean isOnBreak(){ return onBreak;}

	public void SeatCustomer(Customer cust, int table){}

	public void reSeatCustomer(Customer cust, int table){}

	public void customerSeated(Customer Cust){}

	public void imReadyToOrder(Customer Cust){}

	public void hereIsMyChoice(Customer Cust, String Choice){}

	public void orderIsReady(Customer Cust, String Choice){}

	public void msgLeavingTable(Customer Cust) {}

	public void msgFree(){}

	//=========V2
	public void goOnBreak(boolean x){}

	public void outOfOrder(Customer Cust,String choice){}

	public void iCantAffordThis(Customer cust){}

	public void askCheck(Customer cust){}

	public void itCost(Customer cust,double num){
		log.add(new LoggedEvent("Received Check of " + num));
	}


}
