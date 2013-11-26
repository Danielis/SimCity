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
	
	
	
	public HousingCustomerRole(String name2, double b) {
		name = name2;
		balance = b;
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
	public Semaphore animSemaphore = new Semaphore(0, true);
	//landlord agent for the customer
	private LandlordAgent landlord;
	Boolean leavingHouse = false;
	//how much money owned/owed 
	double balance;
	double bill;

	//booleans to track loan needs and repairs
	private Boolean needsLoan;
	public Boolean houseNeedsRepairs;
	public Boolean hungry;
	private Boolean leave = false;

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
		if(!leave) {
			gui.DoGoToThreshold();
			gui.DoGoToBed();
		}
		else
			LeaveApartment();
		return false;
	}

	//------------------------------------------------
	//----------------Actions-------------------------
	//------------------------------------------------
	private void PayBill(){
		print("Going to pay bill");
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
		print("Going to call landlord");
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
		print("Going to cook.");
		
	//	gui.DoGoToThreshold();
		gui.DoGoToKitchen();
		gui.DoGoToFridge();
		gui.DoGoToTable();
		hungry = false;
		gui.DoGoToKitchen();
		gui.DoGoToThreshold();
		gui.DoGoToBed();
		System.out.println("Done Eating.");
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

	public void msgLeaveHome() {
		leave = true;
		stateChanged();
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

}
