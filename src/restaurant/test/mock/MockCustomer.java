package restaurant.test.mock;


import java.util.List;

import agent.RestaurantMenu;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;
import restaurant.test.mock.*;

public class MockCustomer extends Mock implements Customer {

    public Cashier cashier;
    public EventLog log;

    public MockCustomer(String name) 
    {
            super(name);
            log = new EventLog();
    }
    
	public void msgGotHungry() 
	{
		//Empty
	}

	public void msgRestaurantIsFull(Boolean b)
	{
		//Empty
	}
	
	public void msgFollowMe(Waiter w, int tableNum, RestaurantMenu m) 
	{
		//Empty
	}
	
	public void msgWhatWouldYouLike()
	{
		//Empty
	}
	
	public void msgSorryOutOfFood(List<Boolean> temp)
	{
		//Empty
	}

	public void msgHereIsYourFood(String choice)
	{
		//Empty
	}

	public void msgHereIsYourCheck(float check)
	{
    	log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ check));

        if(this.getName().toLowerCase().contains("thief")){
                //test the non-normative scenario where the customer has no money if their name contains the string "theif"
                cashier.msgHereIsMyPayment(this, 0);
        }else{
                //test the normative scenario
        		System.out.println(check);
                cashier.msgHereIsMyPayment(this, check);
        }
	}

}