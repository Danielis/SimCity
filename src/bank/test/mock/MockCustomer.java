package bank.test.mock;


import java.util.List;

import agent.RestaurantMenu;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.Restaurant;
import restaurant.WaiterAgent;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantAnimationPanel;
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

	@Override
	public void WaitForAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoneWithAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimPanel(RestaurantAnimationPanel animationPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startThread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseAgent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeAgent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRestaurant(Restaurant r) {
		// TODO Auto-generated method stub
		
	}
    
	

}