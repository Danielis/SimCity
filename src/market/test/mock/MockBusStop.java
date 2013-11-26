package market.test.mock;


import java.util.List;

import agent.RestaurantMenu;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;
import restaurant.test.mock.*;

public class MockBusStop extends Mock implements Market {

    public Cashier cashier;
    public EventLog log;

    public MockBusStop(String name) 
    {
            super(name);
            log = new EventLog();
    }

    /*public String getName(){
    	return this.getName();
    }*/
    
	public void msgHereIsAPayment(float amount)
	{
		log.add(new LoggedEvent("Received payment from cashier. Total = "+ amount));
	}
	
	public void msgIWantToOrder(String choice, int amount)
	{
		//None for now, does not interact with cashier
	}

}