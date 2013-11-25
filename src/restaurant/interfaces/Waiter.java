package restaurant.interfaces;

import java.awt.image.ImageObserver;
import java.util.List;

import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.*;
import restaurant.HostAgent;
import restaurant.CustomerState;

public interface Waiter 
{
	
	RestaurantAnimationPanel copyOfAnimPanel = null;

	public HostAgent host = null;

	public boolean isOnBreak = false;
	
	public abstract void msgSetOffBreak();
	
	public abstract void msgSetOnBreak();
	
	public abstract void msgBreakGranted(Boolean permission);
	
	public abstract void msgSitAtTable(Customer c, int table);

	public abstract void msgReadyToOrder(Customer c);
	
	public abstract void msgHereIsMyOrder(Customer c, String choice);

	public abstract void msgOutOfFood(List<Boolean> foods, String choice, int table);
	
	public abstract void msgOrderIsReady(String choice, int table);
	
	public abstract void msgCheckIsComputed(Customer c, String choice, float owed);
	
	public abstract void msgDoneEating(Customer c);
	
	public abstract void msgPayingAndLeaving(Customer c);

	public abstract String getName();

	public abstract void DoneWithAnimation();

	public abstract void WaitForAnimation();

}