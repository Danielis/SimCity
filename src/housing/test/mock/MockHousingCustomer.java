package housing.test.mock;


import housing.LandlordAgent;
import housing.guis.HousingAnimationPanel;
import housing.guis.HousingCustomerGui;
import housing.interfaces.HousingCustomer;

import java.util.List;



public class MockHousingCustomer extends Mock implements HousingCustomer {

	public EventLog log = new EventLog();

	public MockHousingCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void HereIsChange(double d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void RentIsPaid() {
		log.add(new LoggedEvent("RentIsPaid"));;
	}

	@Override
	public void YouStillOwe(double d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void HereIsRentBill(double amountOwed) {
		log.add(new LoggedEvent("HereIsRentBill"));;
	}

	@Override
	public HousingAnimationPanel copyOfAnimationPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGui(HousingCustomerGui g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLandlord(LandlordAgent landlord) {
		// TODO Auto-generated method stub

	}

	@Override
	public void EatAtHome() {
		// TODO Auto-generated method stub

	}

	@Override
	public void MyHouseNeedsRepairs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPurpose(String homePurpose) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgDoSomething() {
		// TODO Auto-generated method stub

	}

	@Override
	public void DoneWithAnimation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void WaitForAnimation() {
		// TODO Auto-generated method stub

	}



}