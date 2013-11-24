package housing.interfaces;

import housing.LandlordAgent;
import housing.guis.HousingAnimationPanel;
import housing.guis.HousingCustomerGui;

import javax.swing.Icon;

public interface HousingCustomer {

	boolean hungry = false;

	public abstract void HereIsChange(double d);

	public abstract void RentIsPaid();

	public abstract void YouStillOwe(double d);

	public abstract void HereIsRentBill(double amountOwed);

	public abstract String getName();
	
	public HousingAnimationPanel copyOfAnimationPanel();

	public abstract void setGui(HousingCustomerGui g);

	public abstract void setLandlord(LandlordAgent landlord);


}
