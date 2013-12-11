package housing.test.mock;

import housing.LandlordRole;
import housing.LandlordRole.HousingComplex;
import housing.guis.HousingWorkerGui;
import housing.interfaces.HousingWorker;
import housing.interfaces.Landlord;

import java.util.List;

import agent.RestaurantMenu;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;
import restaurant.test.mock.*;

public class MockHousingWorker extends Mock implements HousingWorker {

    public Cashier cashier;
    public EventLog log;

    public MockHousingWorker(String name) 
    {
            super(name);
            log = new EventLog();
    }

    /*public String getName(){
    	return this.getName();
    }*/
    
	public void msgHereIsAPayment(float amount)
	{
		log.add(new LoggedEvent("Received payment from cashier. Total = "+ amount));
	}
	
	public void msgIWantToOrder(String choice, int amount)
	{
		//None for now, does not interact with cashier
	}

	@Override
	public void HereIsMoney(HousingComplex complex, double bill) {
		log.add(new LoggedEvent("HereIsMoney"));
		
	}

	@Override
	public void GoRepair(HousingComplex complex) {
		log.add(new LoggedEvent("GoRepair"));
	}

	@Override
	public void setGui(HousingWorkerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLandlord(LandlordRole landlord) {
		// TODO Auto-generated method stub
		
	}

}