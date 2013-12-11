package restaurantD.interfaces;

import restaurantD.CashierAgent.Check;
import restaurantD.CashierAgent.CheckState;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Daniel Silva
 *
 */
public interface Cashier {
	public abstract String getName();
	
	public abstract void getCheck(Customer c,String choice,Waiter w);
	
	public abstract void pay(Customer C, double num);
	
	public abstract void cantPay(Customer C);

}