package restaurant.interfaces;

import java.awt.image.ImageObserver;
import java.util.List;

import restaurant.CashierAgent;
import restaurant.CustomerAgent.iconState;
import restaurant.HostAgent;
import restaurant.Restaurant;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.*;
import agent.RestaurantMenu;

public interface Customer 
{
	public RestaurantAnimationPanel copyOfAnimPanel = null;
	public iconState icon = null;
	public int mySeat = 0;

	public abstract String getName();
	
	public abstract void msgGotHungry();

	public abstract void msgRestaurantIsFull(Boolean b);
	
	public abstract void msgFollowMe(Waiter w, int tableNum, RestaurantMenu m);
	
	public abstract void msgWhatWouldYouLike();
	
	public abstract void msgSorryOutOfFood(List<Boolean> temp);

	public abstract void msgHereIsYourFood(String choice);

	public abstract void msgHereIsYourCheck(float check);
	
	public void WaitForAnimation();
	
	public void DoneWithAnimation();

	public abstract void setHost(Host host);

	public abstract void setCashier(Cashier cashier);

	public abstract void setGui(CustomerGui g);

	public abstract void setAnimPanel(RestaurantAnimationPanel animationPanel);

	public abstract CustomerGui getGui();

	public abstract void startThread();

	public abstract void pauseAgent();

	public abstract void resumeAgent();

	public abstract void setRestaurant(Restaurant r);

}