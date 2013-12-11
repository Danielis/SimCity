package restaurantD.test.mock;


import restaurantD.CookAgent;
import restaurantD.interfaces.*;
import restaurantD.test.mock.EventLog;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Daniel Silva
 *
 */
public class MockMarket extends Mock implements Market{

	
	public EventLog log;
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	double debt=100;
	
	public MockMarket(String name) {
		super(name);
		log = new EventLog();
	}
	public double getDebt(){return this.debt;}
	public String getName(){ return super.getName();}
	
	public void hereIsAnOrder(CookAgent cook, int steak, int chicken, int salad, int pizza){};
	
	public void marketPayment(double num){ 
		debt-=num;
		log.add(new LoggedEvent("Received Cashier Payment of "+ num));
	};

}
