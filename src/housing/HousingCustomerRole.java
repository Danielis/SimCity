package housing;

import java.util.Date;
import java.util.Random;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import roles.Role;
import city.PersonAgent;
import city.PersonAgent.Item;
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
	public HousingCustomerRole(String name2, double b, List<Item> inventory) {
		name = name2;
		balance = b;
		needsLoan = false;
		houseNeedsRepairs = false;
		hungry = false;
		System.out.println("Housing Customer created.");
		this.inventory = inventory;
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
	Boolean leavingHouse = false;
	//how much money owned/owed 
	double balance;
	double bill;

	Timer timer= new Timer();
	//booleans to track loan needs and repairs
	private Boolean needsLoan;
	public Boolean houseNeedsRepairs;
	public Boolean hungry;
	private Boolean leave = false;
	private Boolean sleep = false;
	 List<Item> inventory;
	public TrackerGui trackingWindow;

	public String getName()
	{
		return name;
	}
	
	//-----------------------------------------------
	//------------------Messages---------------------
	//-----------------------------------------------
	//arriving at house
	
	public void msgGetPaid(){
		//balance =+50;
	}
	
	public void enteringHouse() {

	}
	public void msgDoSomething() {
		//print("dosmth");
		stateChanged();
	}
	//sent from landlord.  rent bill
	public void HereIsRentBill(double amount){
		bill = amount;
		stateChanged();
	}
	//sent from landlord.  change from rent bill
	public void HereIsChange(double amount){
		balance += amount;
		stateChanged();
	}
	//sent from landlord.  rent is paid
	public void RentIsPaid(){
		bill = 0;
		stateChanged();
	}
	//sent from landlord.  money is still owed
	public void YouStillOwe(double amount){
		needsLoan = true;
		stateChanged();
	}
	//going be set into action by the user or a criminal or something.  
	public void MyHouseNeedsRepairs(){
		houseNeedsRepairs = true;
		stateChanged();
	}
	//eat at home message
	public void EatAtHome() {
		hungry = true;
		stateChanged();
	}

	//--------------------------------------------------------
	//---------------------Scheduler--------------------------
	//--------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		System.out.println("Tenant scheduler.");
//		if(leave){
//			LeaveApartment();
//			return true;
//		}
		if(sleep){
			GoToSleep();
			return false;
			//return true;
		}
		else if (needsLoan){
			TakeOutLoan();
			return true;
		}
		else if (houseNeedsRepairs){
			CallLandlordRepairs();
			return true;
		}
		else if (bill > 0){
			PayBill();
			return true;
		}
		else if(hungry) {
			GetFood();
			return true;
		}
		else
			LeaveApartment();
		return false;
	}

	private void GoToSleep() {
		gui.DoGoToThreshold();
		gui.DoGoToBed();
		sleep = false;
		//TODO: PLEASE FIX THIS LOL, semaphore?
//		timer.schedule(new TimerTask()
//		{
//			public void run()
//			{
//				
//				print("Woke up!");
//			}
//		}, 5000);
		
		
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
	}
	private void CallLandlordRepairs(){
		print("Calling landlord");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Calling landlord", new Date()));
		gui.DoGoToThreshold();
		gui.DoGoToPhone();
		houseNeedsRepairs = false;
		landlord.MyHouseNeedsRepairs(this);
		gui.DoGoToThreshold();
		//gui.DoGoToBed();	
		System.out.println("Tenant: called landlord for repairs.");

		}

	private void TakeOutLoan(){
		//GoToBank(); //stub;
		needsLoan = false;
		System.out.println("Went to bank.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingCustomerRole", "Went to bank", new Date()));
		balance += 1000;
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
		//gui.DoGoToBed();
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
		myPerson.msgLeavingHome(this, balance);
	}

	public HousingCustomerGui getGui() {
		return gui;
	}

	@Override
	public void setPurpose(String homePurpose) {

		if (homePurpose.equals("Pay Loan"))
			bill = 30;
		if (homePurpose.equals("Call for Repair"))
			houseNeedsRepairs = true;
		if (homePurpose.equals("Cook")) //TODO
			hungry = true;
		if (homePurpose.equals("Sleep"))
			sleep = true;
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

	public void msgLeaveHome() {
		leave = true;
		stateChanged();
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

}
