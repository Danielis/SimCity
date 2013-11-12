package restaurant.interfaces;

import java.util.List;

import restaurant.interfaces.*;
import agent.RestaurantMenu;

public interface Customer 
{
	public abstract String getName();
	
	public abstract void msgGotHungry();

	public abstract void msgRestaurantIsFull(Boolean b);
	
	public abstract void msgFollowMe(Waiter w, int tableNum, RestaurantMenu m);
	
	public abstract void msgWhatWouldYouLike();
	
	public abstract void msgSorryOutOfFood(List<Boolean> temp);

	public abstract void msgHereIsYourFood(String choice);

	public abstract void msgHereIsYourCheck(float check);
}