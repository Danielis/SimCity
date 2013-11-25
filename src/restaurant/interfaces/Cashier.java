package restaurant.interfaces;

import restaurant.MarketAgent;

public interface Cashier 
{
	public abstract void msgHereIsACheck(Waiter newW, Customer newC, String newChoice);
	public abstract void msgHereIsMyPayment(Customer newC, float payment);
	public abstract void msgHereAMarketOrder(Market market,
			String name, int amountwanted);
	public abstract void startThread();
	public abstract void setBalance(float f);
}