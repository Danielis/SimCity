package restaurantA.test.mock;


import restaurantA.HostAgent;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.CustomerGui;
import restaurantA.interfaces.Cashier;
import restaurantA.interfaces.Customer;

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
    public EventLog log = new EventLog();

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgGetOut() {
		log.add(new LoggedEvent("Received message msgGetOut from cashier"));
		
	}

	@Override
	public void addMoneyAmountOwed(double amountOwed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouAreGoodToGo() {
		log.add(new LoggedEvent("Received message msgYouAreGoodToGo from cashier"));
		
	}

	@Override
	public void msgHereIsChange(double amountChange) {
		log.add(new LoggedEvent("Received message msgHereIsChange from cashier"));

	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHost(HostAgent host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimPanel(AnimationPanel animationPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

}
