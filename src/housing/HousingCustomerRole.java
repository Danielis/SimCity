package housing;

import java.util.concurrent.Semaphore;

import roles.Role;
import city.guis.PersonGui;
import housing.guis.HousingAnimationPanel;
import housing.guis.HousingCustomerGui;
import housing.interfaces.HousingCustomer;
import housing.interfaces.Landlord;
import agent.Agent;

public class HousingCustomerRole extends Role implements HousingCustomer{


	//-----------------------------------------------
	//-----------------Utilities---------------------
	//-----------------------------------------------
	//constructor
	
	
	
	public HousingCustomerRole(String name2) {
		name = name2;
		balance = 10000;
		needsLoan = false;
		houseNeedsRepairs = false;
		hungry = false;
		System.out.println("Housing Customer created.");
	}

	public void setLandlord(LandlordAgent landlord2) {
		landlord = landlord2;
	}

	public HousingAnimationPanel copyOfAnimationPanel() {
		return animationPanel;
	}

	public void setGui(HousingCustomerGui g) {
		gui = g;		
	}


	//-----------------------------------------------
	//--------------------DATA-----------------------
	//-----------------------------------------------
	//name
	public String name;
	//private Semaphore waitingForAnimation = new Semaphore(0);
	private HousingAnimationPanel animationPanel;
	private HousingCustomerGui gui;

	//landlord agent for the customer
	private LandlordAgent landlord;

	//how much money owned/owed 
	double balance;
	double bill;

	//booleans to track loan needs and repairs
	private Boolean needsLoan;
	public Boolean houseNeedsRepairs;
	public Boolean hungry;

	public String getName()
	{
		return name;
	}
	
	//-----------------------------------------------
	//------------------Messages---------------------
	//-----------------------------------------------
	//arriving at house
	public void enteringHouse() {

	}
	//sent from landlord.  rent bill
	public void HereIsRentBill(double amount){
		bill = amount;
		myPerson.stateChanged();
	}
	//sent from landlord.  change from rent bill
	public void HereIsChange(double amount){
		balance += amount;
		myPerson.stateChanged();
	}
	//sent from landlord.  rent is paid
	public void RentIsPaid(){
		bill = 0;
		myPerson.stateChanged();
	}
	//sent from landlord.  money is still owed
	public void YouStillOwe(double amount){
		needsLoan = true;
		myPerson.stateChanged();
	}
	//going be set into action by the user or a criminal or something.  
	public void MyHouseNeedsRepairs(){
		houseNeedsRepairs = true;
		myPerson.stateChanged();
	}
	//eat at home message
	public void EatAtHome() {
		hungry = true;
		myPerson.stateChanged();
	}

	//--------------------------------------------------------
	//---------------------Scheduler--------------------------
	//--------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		System.out.println("Tenant scheduler.");
		if (needsLoan){
			TakeOutLoan();
			return true;
		}
		if (houseNeedsRepairs){
			CallLandlordRepairs();
			return true;
		}
		if (bill > 0){
			PayBill();
			return true;
		}
		if(hungry) {
			GetFood();
			return true;
		}
		//if(!gui.goingSomewhere) {
			gui.DoGoToBed();
		//}
		return false;
	}

	//------------------------------------------------
	//----------------Actions-------------------------
	//------------------------------------------------
	private void PayBill(){
		gui.DoGoToLandlord();
		if (balance > bill){
			balance -= bill;
			landlord.HereIsRent(this, bill);
			bill = 0;
			System.out.println("Bill paid in full.");
		}
		else{
			landlord.HereIsRent(this, balance);
			bill -= balance;
			balance = 0;
			System.out.println("This is all I have.  I'm out of money.");
		}
		gui.DoGoToThreshold();
		gui.DoGoToBed();
	}
	private void CallLandlordRepairs(){
		gui.DoGoToThreshold();
		gui.DoGoToPhone();
		houseNeedsRepairs = false;
		landlord.MyHouseNeedsRepairs(this);
		System.out.println("Tenant: called landlord for repairs.");
		gui.DoGoToThreshold();
		gui.DoGoToBed();	
		}

	private void TakeOutLoan(){
		//GoToBank(); //stub;
		needsLoan = false;
		System.out.println("Went to bank.");
	}
	private void GetFood() {
		hungry = false;
		gui.DoGoToThreshold();
		gui.DoGoToKitchen();
		gui.DoGoToFridge();
		gui.DoGoToTable();
		gui.DoGoToKitchen();
		gui.DoGoToThreshold();
		gui.DoGoToBed();
		System.out.println("Done Eating.");
	}

	public HousingCustomerGui getGui() {
		return gui;
	}

	@Override
	public void setPurpose(String homePurpose) {

		//if (homePurpose.equals("Pay Loan"))
			//needsLoan = true;
		if (homePurpose.equals("Call for Repair"))
			houseNeedsRepairs = true;
		if (homePurpose.equals("Cook"))
			hungry = true;
		//if (homePurpose.equals("Sleep"))
		//	hungry = true;
	}


}