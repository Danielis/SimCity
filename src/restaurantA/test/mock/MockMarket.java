package restaurantA.test.mock;


import restaurantA.CookAgent;
import restaurantA.MarketAgent.MyOrder;
import restaurantA.interfaces.Cashier;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
    public EventLog log = new EventLog();

	public MockMarket(String name) {
		super(name);

	}

public void msgHereIsPayment(int amount){
		log.add(new LoggedEvent("Received message msgHereIsPayment from cashier"));
	}

public void msgNeedFood(CookAgent c, String f, int quantity) {
	
	}

@Override
public int getIndex() {
	// TODO Auto-generated method stub
	return 0;
}
}
