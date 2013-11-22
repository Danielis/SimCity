package bank.test.mock;


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

	@Override
	public void msgGotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantIsFull(Boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(Waiter w, int tableNum, RestaurantMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSorryOutOfFood(List<Boolean> temp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourCheck(float check) {
		// TODO Auto-generated method stub
		
	}
    
	

}