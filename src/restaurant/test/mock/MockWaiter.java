package restaurant.test.mock;
import java.util.List;

import restaurant.HostAgent;
import restaurant.WaiterAgent.CustomerState;
import restaurant.WaiterAgent.MyCustomer;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

		public HostAgent host;
        public Cashier cashier;
        public EventLog log;

        public MockWaiter(String name) 
        {
                super(name);
                log = new EventLog();
        }

    	public void msgSetOffBreak()
    	{
    		//EMPTY
    	}
    	
    	public void msgSetOnBreak()
    	{
    		//EMPTY
    	}
    	
    	public void msgBreakGranted(Boolean permission)
    	{
    		//EMPTY
    	}
    	
    	public void msgSitAtTable(Customer c, int table)
    	{
    		//EMPTY
    	}

    	public void msgReadyToOrder(Customer c) 
    	{
    		//EMPTY
    	}
    	
    	public void msgHereIsMyOrder(Customer c, String choice)
    	{
    		//EMPTY
    	}

    	public void msgOutOfFood(List<Boolean> foods, String choice, int table)
    	{
    		//EMPTY
    	}
    	
    	public void msgOrderIsReady(String choice, int table)
    	{
    		//EMPTY
    	}
    	
    	public void msgDoneEating(Customer c)
    	{
    		//EMPTY
    	}
    	
    	public void msgPayingAndLeaving(Customer c)
    	{
    		//EMPTY
    	}
    	
    	public void msgCheckIsComputed(Customer c, String choice, float owed)
    	{
    		log.add(new LoggedEvent("Received msgCheckIsComputed from cashier. Amount = " + owed));
    	}
}