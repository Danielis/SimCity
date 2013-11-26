package transportation.test.mock;
import java.util.List;

import restaurant.HostAgent;
import restaurant.CustomerState;
import restaurant.MyCustomer;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

public class MockTransportationCompany extends Mock implements Waiter {

		public HostAgent host;
        public Cashier cashier;
        public EventLog log;

        public MockTransportationCompany(String name) 
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

		@Override
		public void DoneWithAnimation() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void WaitForAnimation() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public WaiterGui getGui() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void resumeAgent() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setHost(Host host) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAnimPanel(RestaurantAnimationPanel animationPanel) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setCook(Cook cook) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setCashier(Cashier cashier) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setGui(WaiterGui g) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void pauseAgent() {
			// TODO Auto-generated method stub
			
		}
}