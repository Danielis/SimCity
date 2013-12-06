package restaurantA.interfaces;

import restaurantA.CookAgent;
import restaurantA.MarketAgent.MyOrder;

public interface Market {
	boolean OutOfStock = false;
	public void msgHereIsPayment(int amount);
	public void msgNeedFood(CookAgent c, String f, int quantity);
    public abstract String getName();
	public int getIndex();

}