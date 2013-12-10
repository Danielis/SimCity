package housing.interfaces;

import housing.LandlordRole.HousingComplex;

public interface Landlord {

	public abstract void HereIsRent(HousingCustomer housingCustomer, double balance);
	public abstract void MyHouseNeedsRepairs(HousingCustomer housingCustomerAgent);
	public abstract void RepairsCompleted(HousingComplex c, double bill);
}
