package city.Interfaces;

import java.util.ConcurrentModificationException;

import market.MarketCustomerRole;
import roles.Role;
import transportation.BusStopAgent;
import bank.BankCustomerRole;
import city.BankDatabase.*;



public interface Person {
/*****************************************************************************
	 MESSAGES
******************************************************************************/

	public void msgWakeUp();

	public void msgGetPaid();

	//TODO ADD THIS MSG TO ALL WORKER ROLES
	public void msgLeaveWork() ;

	public void msgLeftWork(Role r, double balance) ;

	public void msgLeavingHome(Role r, double balance);

	public void msgGoToWork() ;



	//Housing
	public void msgGoToHome(String purpose);



	//Restaurant
	public void msgGoToRestaurant();

	public void msgLeavingRestaurant(Role r, float myMoney);

	public void msgGoToBank(String purpose, double amt);

	public void msgLeavingBank(BankCustomerRole r, double balance) ;



	public void msgNewAccount(BankCustomerRole bankCustomerRole, Account acct) ;

	public void msgNewLoan(BankCustomerRole bankCustomerRole, Loan loan) ;

	//Transportation
	public void msgAtBusStop();

	public void setDestinationStop(BusStopAgent P);

	public BusStopAgent getDestinationBusStop();

	public void setPosition(int X, int Y);

	public void msgGoToMarket(String purpose, double quantity);

	public void msgLeavingMarket(MarketCustomerRole r, double balance, String item, int quantRec) ;

	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

/*****************************************************************************
	SCHEDULER
******************************************************************************/

	public boolean pickAndExecuteAnAction();

}