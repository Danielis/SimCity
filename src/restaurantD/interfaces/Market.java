package restaurantD.interfaces;

import java.util.HashMap;

import restaurantD.CookAgent;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Daniel Silva
 *
 */
public interface Market {
	public abstract String getName();
	public abstract double getDebt();
	
	//Messages

	public  abstract void hereIsAnOrder(CookAgent cook, int steak, int chicken, int salad, int pizza);
	
	public abstract void marketPayment(double num);
	
}