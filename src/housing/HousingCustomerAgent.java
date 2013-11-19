package housing;

import agent.Agent;

public class HousingCustomerAgent extends Agent{
	// HOUSING CUSTOMER DATA
	LandlordAgent landlord;
	double balance;
	double bill;
	Boolean needsLoan;
	Boolean houseNeedsRepairs;

	// HOUSING CUSTOMER MESSAGES
	public void HereIsRentBill(double amount){
		bill = amount;
		stateChanged();
	}

	public void HereIsChange(double amount){
		balance += amount;
		stateChanged();
	}

	public void RentIsPaid(){
		bill = 0;
		stateChanged();
	}

	public void YouStillOwe(double amount){
		needsLoan = true;
		stateChanged();
	}

	public void MyHouseNeedsRepairs(){
		houseNeedsRepairs = true;
		stateChanged();
	}

	//--------------------------------------------------------
	//---------------------Scheduler--------------------------
	//--------------------------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction() {
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
		return false;
	}

	//ACTIONS
	private void PayBill(){
		if (balance > bill){
			balance -= bill;
			landlord.HereIsRent(this, bill);
			bill = 0;
		}
		else{
			landlord.HereIsRent(this, balance);
			bill -= balance;
			balance = 0;
		}
	}

	private void CallLandlordRepairs(){
		houseNeedsRepairs = false;
		landlord.MyHouseNeedsRepairs(this);
	}

	private void TakeOutLoan(){
		//GoToBank(); //stub;
		needsLoan = false;
	}
}
