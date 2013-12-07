package restaurant.interfaces;

import java.awt.image.ImageObserver;
import java.util.List;

import restaurant.CustomerAgent;
import restaurant.TraditionalWaiterAgent;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantAnimationPanel;

public interface Host {

	public RestaurantAnimationPanel copyOfAnimPanel = null;

	public abstract void msgCheckForASpot(Customer customer);

	public abstract void msgIdLikeToGoOnBreak(Waiter waiter);

	public abstract void msgTableIsFree(Waiter waiter, int table);

	public abstract void msgIWantFood(Customer cust, boolean b);

	public abstract void setGui(HostGui hostGui);

	public abstract void startThread();

	public abstract void resumeAgent();

	public abstract void pauseAgent();

	public abstract void msgNewWaiter(Waiter w);

	public abstract void WaitForAnimation();
	
	public abstract void DoneWithAnimation();

	public abstract void setAnimPanel(RestaurantAnimationPanel animationPanel);

	public abstract void msgLeaveWork();

	public abstract void msgRemoveWaiter(Waiter waiter);

	public abstract List getCustomers();
	
	public abstract boolean areTablesEmpty();

}
