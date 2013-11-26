package city.test.mock;


import java.util.List;

import agent.RestaurantMenu;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.*;
import restaurant.test.mock.*;

public class MockPerson extends Mock implements Customer {

    public Cashier cashier;
    public EventLog log;

    public MockPerson(String name) 
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
	
	public void WaitForAnimation()
	{
		//
	}
	
	public void DoneWithAnimation()
	{
		//
	}

	@Override
	public void setHost(Host host) {
		//
	}

	@Override
	public void setCashier(Cashier cashier) {
		//
	}

	@Override
	public void setGui(CustomerGui g) {
		//
	}

	@Override
	public void setAnimPanel(RestaurantAnimationPanel animationPanel) {
		//
	}

	@Override
	public CustomerGui getGui() {
		//
		return null;
	}

	@Override
	public void startThread() {
		//
	}

	@Override
	public void pauseAgent() {
		//
	}

	@Override
	public void resumeAgent() {
		//
	}

}