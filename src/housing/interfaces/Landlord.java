package housing.interfaces;

import housing.LandlordAgent.HousingComplex;

public interface Landlord {

	void HereIsRent(HousingCustomer housingCustomer, double balance);
	void MyHouseNeedsRepairs(HousingCustomer housingCustomerAgent);
}
