package restaurant.interfaces;

import restaurant.MarketAgent;
import restaurant.gui.CashierGui;
import restaurant.gui.RestaurantAnimationPanel;

public interface Cashier 
{
	RestaurantAnimationPanel copyOfAnimPanel = null;
	public abstract void msgHereIsACheck(Waiter newW, Customer newC, String newChoice);
	public abstract void msgHereIsMyPayment(Customer newC, float payment);
	public abstract void msgHereAMarketOrder(Market market,
			String name, int amountwanted);
	public abstract void startThread();
	public abstract void setBalance(float f);
	public abstract void DoneWithAnimation();
	public abstract void WaitForAnimation();
	public abstract void setGui(CashierGui g);
	public abstract void setAnimPanel(RestaurantAnimationPanel animationPanel);
	public abstract void msgLeaveWork();
}