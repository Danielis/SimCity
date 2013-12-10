package restaurantC.interfaces;

import restaurant.CustomerAgent;

public interface Waiter {
	public abstract void readyToOrder(Customer cust);
	public abstract void order(Customer cust, String choice);
	public abstract void doneWithFood(Customer cust);
	public abstract void checkPlease(Customer cust);
	public abstract void hereIsCheck(int t, double p);
	public abstract void msgLeavingTable(Customer cust);
}
