package restaurantA.test.mock;


import restaurantA.CookAgent;
import restaurantA.MarketAgent.MyOrder;
import restaurantA.interfaces.Cashier;
import restaurantA.interfaces.Cook;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCook extends Mock implements Cook {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
    public EventLog log = new EventLog();

	public MockCook(String name) {
		super(name);

	}

	@Override
	public void msgNotEmpty() {
		// TODO Auto-generated method stub
		
	}

	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}


}
