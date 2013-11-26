package housing;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import roles.Role;
import city.PersonAgent;
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
	public HousingCustomerRole(String name2, double b) {
		name = name2;
		balance = b;
		needsLoan = false;
		houseNeedsRepairs = false;
		hungry = false;
		System.out.println("Housing Customer created.");
	}
	
	public PersonAgent getPerson() {
		return myPerson;
	}
	
	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
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
	public Semaphore animSemaphore = new Semaphore(0, true);
	//landlord agent for the customer
	private LandlordAgent landlord;

	//how much money owned/owed 
	double balance;
	double bill;

	//booleans to track loan needs and repairs
	private Boolean needsLoan;
	public Boolean houseNeedsRepairs;
	public Boolean hungry;
	private Boolean leave = false;

	public TrackerGui trackingWindow;

	public String getName()
	{
		return name;
	}
	
	//-----------------------------------------------
	//------------------Messages---------------------
	//-----------------------------------------------
	//arriving at house
	
	public void msgLeaveHouse() {
		leave = true;
		myPerson.stateChanged();
	}
	
	public void enteringHouse() {

	}
	public void msgDoSomething() {
		//print("dosmth");
		myPerson.stateChanged();
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
		if(leave){
			LeaveApartment();
			return true;
		}
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
			gui.DoGoToThreshold();
			gui.DoGoToBed();
		//}
		return false;
	}

	//------------------------------------------------
	//----------------Actions-------------------------
	//------------------------------------------------
	private void PayBill(){
		print("Going to pay bill");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Going to pay bill", new Date()));

		gui.DoGoToLandlord();
		if (balance > bill){
			balance -= bill;
			landlord.HereIsRent(this, bill);
			bill = 0;
			System.out.println("Bill paid in full.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Bill paid in full", new Date()));
		}
		else{
			landlord.HereIsRent(this, balance);
			bill -= balance;
			balance = 0;
			System.out.println("This is all I have.  I'm out of money.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "This is all I have.  I'm out of money", new Date()));
		}
		gui.DoGoToThreshold();
		gui.DoGoToBed();
	}
	private void CallLandlordRepairs(){
		print("Calling landlord");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Calling landlord", new Date()));
		gui.DoGoToThreshold();
		gui.DoGoToPhone();
		houseNeedsRepairs = false;
		landlord.MyHouseNeedsRepairs(this);
		gui.DoGoToThreshold();
		gui.DoGoToBed();	
		}

	private void TakeOutLoan(){
		//GoToBank(); //stub;
		needsLoan = false;
		System.out.println("Went to bank.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Went to bank", new Date()));
	}
	private void GetFood() {
		print("Going to cook food.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Going to cook food", new Date()));
	//	gui.DoGoToThreshold();
		gui.DoGoToKitchen();
		gui.DoGoToFridge();
		PickItem();
		gui.DoGoToTable();
		hungry = false;
		gui.DoGoToKitchen();
		gui.DoGoToThreshold();
		gui.DoGoToBed();
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Done Eating", new Date()));
		System.out.println("Done Eating.");
	}
	private void PickItem() {
		Random i = new Random();
		int j = i.nextInt(myPerson.getInvetory().size());
		if(myPerson.getInvetory().get(j).getType().equals("Car")) {
			PickItem();
		}
		else {
			myPerson.getInvetory().get(j).removeItem();
			System.out.println("Consumed " + myPerson.getInvetory().get(j).getType());
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Consumed " + myPerson.getInvetory().get(j).getType(), new Date()));
		}
	}
	
	private void LeaveApartment(){
		houseNeedsRepairs = false;
		hungry = false;
		leave = false;
		gui.DoWalkOut();
		gui.setDone();
		this.myPerson.msgLeavingHome(this, balance);
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
		if (homePurpose.equals("Cook")) //TODO
			hungry = true;
		//if (homePurpose.equals("Sleep"))
		//	hungry = true;
	}

	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}

	public void WaitForAnimation()
	{
		try
		{
			this.animSemaphore.acquire();	
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:", e);
        }
	}

}
