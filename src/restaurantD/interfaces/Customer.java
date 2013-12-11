package restaurantD.interfaces;

import java.util.HashMap;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Daniel Silva
 *
 */
public interface Customer {
	public abstract String getName();
	
	public abstract Waiter getWaiter();
	
	//Messages

	public abstract void gotHungry() ;

	public abstract void msgSitAtTable(Waiter waiter,HashMap<Integer,String> menu,HashMap<String,Double> menuPrice,int table) ;

	public abstract void msgAnimationFinishedGoToSeat() ;
	
	public abstract void whatWouldYouLike();
	
	public abstract void hereIsYourFood(String choice);
	
	public abstract void msgAnimationFinishedLeaveRestaurant() ;
	
	//--------V2-------//
	
	public abstract void outOfOrder(String Choice);
	
	public abstract void hereIsYourCheck(double num);
	
	public abstract void youPayed();
	
	public abstract void doDishes();

}