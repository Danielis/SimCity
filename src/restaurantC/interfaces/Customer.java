package restaurantC.interfaces;

import restaurantC.Menu;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {

	public String name = "";
	
	//message sit at table from waiter
	public abstract void msgSitAtTable(int tableNumber, Menu m);
	
	//waiter asking customer what he/she wants
	public abstract void whatDoYouWant();

	//food being delivered
	public abstract void hereIsFood(String choice);
	
	//out of food message scenario from waiter
	public abstract void outOfFood();
	
	//anything else?
	public abstract void anythingElse();
	
	//here is check.  sent from waiter
	public abstract void hereIsCheck(double total);
	
	//done paying.  sent from cashier
	public abstract void hereIsChange(double change);
	
	public abstract int getX();
	public abstract int getY();

}