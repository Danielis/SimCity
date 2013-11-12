package restaurant.interfaces;

import java.util.List;

import restaurant.interfaces.*;
import restaurant.WaiterAgent.CustomerState;
import restaurant.WaiterAgent.MyCustomer;

public interface Waiter 
{
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

}