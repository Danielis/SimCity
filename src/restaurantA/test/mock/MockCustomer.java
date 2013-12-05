package restaurantA.test.mock;


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
	public void addMoneyAmountOwed(int amountOwed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouAreGoodToGo() {
		log.add(new LoggedEvent("Received message msgYouAreGoodToGo from cashier"));
		
	}

	@Override
	public void msgHereIsChange(int amountChange) {
		log.add(new LoggedEvent("Received message msgHereIsChange from cashier"));

	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

}
