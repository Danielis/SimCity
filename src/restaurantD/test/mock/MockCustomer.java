package restaurantD.test.mock;


import java.util.HashMap;

import restaurantD.interfaces.Cashier;
import restaurantD.interfaces.Customer;
import restaurantD.interfaces.Waiter;
import restaurantD.test.mock.EventLog;
/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public Waiter waiter;
	public String name;
	
	public EventLog log;
	
	public MockCustomer(String name) {
		super(name);
		this.name=name;
		//A log to be used be used
		log = new EventLog();
	}
	
	public String getName(){ return super.getName();}
	
	public Waiter getWaiter(){ return waiter;}
	
	//Messages

	public void gotHungry() {};

	public void msgSitAtTable(Waiter waiter,HashMap<Integer,String> menu,HashMap<String,Double> menuPrice,int table) {}
	public void msgAnimationFinishedGoToSeat(){} 
	
	public void whatWouldYouLike(){}
	
	public void hereIsYourFood(String choice){}
	
	public void msgAnimationFinishedLeaveRestaurant(){} 
	
	//--------V2-------//
	
	public void outOfOrder(String Choice){}
	
	public void hereIsYourCheck(double num){
		log.add(new LoggedEvent("Received hereIsYourCheck from Waiter. Total = "+ num));
	
	}
	
	public void youPayed(){
		log.add(new LoggedEvent("Received youPayed from cashier."));
	}
	
	public void doDishes(){
		log.add(new LoggedEvent("Received doDishes from cashier."));
	}

}
