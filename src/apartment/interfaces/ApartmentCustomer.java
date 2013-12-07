package apartment.interfaces;

import javax.swing.Icon;

import apartment.LandlordRole;
import apartment.guis.ApartmentAnimationPanel;
import apartment.guis.ApartmentCustomerGui;

public interface ApartmentCustomer {

	boolean hungry = false;
	boolean houseNeedsRepairs = false;

	public abstract void HereIsChange(double d);

	public abstract void RentIsPaid();

	public abstract void YouStillOwe(double d);

	public abstract void HereIsRentBill(double amountOwed);

	public abstract String getName();
	
	public ApartmentAnimationPanel copyOfAnimationPanel();

	public abstract void setGui(ApartmentCustomerGui g);

	public abstract void setLandlord(LandlordRole landlord);

	public abstract void EatAtHome();

	public abstract void MyHouseNeedsRepairs();

	public abstract void setPurpose(String homePurpose);

	public abstract void msgDoSomething();

	public abstract void DoneWithAnimation();

	public abstract void WaitForAnimation();


}
