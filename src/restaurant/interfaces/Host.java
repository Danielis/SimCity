package restaurant.interfaces;

import restaurant.CustomerAgent;

public interface Host {

	public abstract void msgCheckForASpot(Customer customer);

	public abstract void msgIdLikeToGoOnBreak(Waiter waiter);

	public abstract void msgTableIsFree(Waiter waiter, int table);

	public abstract void msgIWantFood(Customer cust, boolean b);

}
