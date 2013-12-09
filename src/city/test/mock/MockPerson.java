package city.test.mock;


import java.util.List;

import market.MarketCustomerRole;
import bank.BankCustomerRole;
import city.BankDatabase.*;
import city.Interfaces.Person;
import agent.RestaurantMenu;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.Restaurant;
import restaurant.WaiterAgent;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.*;
import restaurant.test.mock.*;
import roles.Role;
import transportation.BusStopAgent;
import transportation.Interfaces.BusStop;

public class MockPerson extends Mock implements Person {

	BusStopAgent stop = new BusStopAgent("Stop1");
	
    public Cashier cashier;
    public EventLog log;

    public MockPerson(String name) 
    {
            super(name);
            log = new EventLog();
    }
    
	

	public void msgHereIsYourCheck(float check)
	{
    	log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ check));

       
	}
	
	/*****************************************************************************
	 MESSAGES
******************************************************************************/

	public void msgWakeUp(){
		// Enter this message into all to log into the events, change string to accomodate. This is used to check if Person has recieved a message
		// Other mocks or tests can check this to test MockPerson. Feel free to add code to accomodate your tests.
		log.add(new LoggedEvent("Person recieved the message to wake up"));
		
	}

	public void msgGetPaid(){}

	//TODO ADD THIS MSG TO ALL WORKER ROLES
	public void msgLeaveWork() {}

	public void msgLeftWork(Role r, double balance) {}

	public void msgLeavingHome(Role r, double balance){}

	public void msgGoToWork() {}



	//Housing
	public void msgGoToHome(String purpose){}



	//Restaurant
	public void msgGoToRestaurant(){}

	public void msgLeavingRestaurant(Role r, float myMoney){}

	public void msgGoToBank(String purpose, double amt){}

	public void msgLeavingBank(BankCustomerRole r, double balance) {}



	public void msgNewAccount(BankCustomerRole bankCustomerRole, Account acct) {}

	public void msgNewLoan(BankCustomerRole bankCustomerRole, Loan loan) {}

	//Transportation
	public void msgAtBusStop(){}

	public void setDestinationStop(BusStopAgent P){}

	public BusStopAgent getDestinationBusStop(){ return stop;}

	public void setPosition(int X, int Y){}

	public void msgGoToMarket(String purpose, double quantity){}

	public void msgLeavingMarket(MarketCustomerRole r, double balance, String item, int quantRec) {}

	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

/*****************************************************************************
	SCHEDULER
******************************************************************************/

	public boolean pickAndExecuteAnAction(){ 
		//Add code for the AI or states that this should respond to and log certain calls before returning true EX:
		//		if(State){
		//			log.add(new LoggedEvent("Person recieved the message to wake up"));
		//  		return true;
		//		}
		return false;
	}
	
	public void setBuilding(Restaurant r) {
		// TODO Auto-generated method stub
	}

}