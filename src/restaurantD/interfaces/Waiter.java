package restaurantD.interfaces;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Daniel Silva
 *
 */
public interface Waiter {
	public abstract String getName();
	
	public abstract boolean isOnBreak();
	
	//Messages

	public void SeatCustomer(Customer cust, int table);

	public void reSeatCustomer(Customer cust, int table);

	public void customerSeated(Customer Cust);

	public void imReadyToOrder(Customer Cust);

	public void hereIsMyChoice(Customer Cust, String Choice);

	public void orderIsReady(Customer Cust, String Choice);

	public void msgLeavingTable(Customer Cust) ;

	public void msgFree();

	//=========V2
	public void goOnBreak(boolean x);

	public void outOfOrder(Customer Cust,String choice);

	public void iCantAffordThis(Customer cust);

	public void askCheck(Customer cust);

	public void itCost(Customer cust,double num);


}