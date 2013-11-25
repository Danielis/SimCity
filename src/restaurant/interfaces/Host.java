package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.TraditionalWaiterAgent;
import restaurant.gui.HostGui;

public interface Host {

	public abstract void msgCheckForASpot(Customer customer);

	public abstract void msgIdLikeToGoOnBreak(Waiter waiter);

	public abstract void msgTableIsFree(Waiter waiter, int table);

	public abstract void msgIWantFood(Customer cust, boolean b);

	public abstract void setGui(HostGui hostGui);

	public abstract void startThread();

	public abstract void resumeAgent();

	public abstract void pauseAgent();

	public abstract void msgNewWaiter(Waiter w);

}
