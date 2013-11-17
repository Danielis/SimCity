package bank;

import bank.gui.BankAnimationPanel;
import bank.gui.CustomerGui;
import bank.interfaces.*;
import agent.Agent;
import agent.RestaurantMenu;

import java.util.*;
import java.util.concurrent.Semaphore;

//Customer Agent
//It still is a finite state machine, instead of events it still uses the state enum.
//I used this design from the designs drawn from class.

public class CustomerAgent extends Agent implements Customer {
	
	//To show icon
	public enum iconState
	{
		none, question, steak, chicken, salad, pizza,
	};
	
	//EDIT HERE******************************
	static final float initialMoney = 20.00f;
	//EDIT HERE******************************
	
//VARIABLES*************************************************
	HostAgent h;
	bankCustomerState state;
	TellerAgent t;
	double balance;
	customerPurpose purpose;
	double amount; //amount they want to deposit, withdraw, pay loan off of, or take loan out of
	int accountID;
	private CustomerGui customerGui;
	public Semaphore animSemaphore = new Semaphore(0, true);
	//List of foods available
	
	//Constructor
	public CustomerAgent(String name){
		super();
	}

//UTILITIES**************************************************

	
//CLASSES/ENUMS**********************************************

	enum customerPurpose {createAccount, withdraw, deposit, takeLoan, payLoan};
	enum bankCustomerState {outside, entered, waiting, assigned, atCounter, done, exited};
	
//MESSAGES*************************************************

public void	WantsToDo(String visitPurpose, int quantity){ //called from Person agent
	    //purpose = convert(visitPurpose);
	    this.state = bankCustomerState.entered;
	}

public void	GoToTeller(TellerAgent t){
	    this.t = t;
	    state = bankCustomerState.assigned;
	}

public void	AccountCreated(){
	    state = bankCustomerState.done;
	}

public void	MoneySuccesfullyDeposited(){
	    state = bankCustomerState.done;
	}

public void	LoanCreated(){
	    balance += amount;
	    state = bankCustomerState.done;
	}

public void	CannotCreatLoan(){
	    state = bankCustomerState.done;
	}

public void	CreditNotGoodEnough(){
	    state = bankCustomerState.done;
	}

public void	YourLoanIsPaidOff(){
	    state = bankCustomerState.done;
	}

public void	YouStillOwe(double d, int i){
	    state = bankCustomerState.done;
	}

public void	HereIsWithdrawal(double amount){
	    balance += amount;
	    state = bankCustomerState.done;
	}

public void	HereIsPartialWithdrawal(double amount){
	    balance += amount;
	    state = bankCustomerState.done;
	}

public void	NoMoney(){ // in account
	    state = bankCustomerState.done;
	}

	
//SCHEDULER*************************************************
	protected boolean pickAndExecuteAnAction() 
	{
		if (state == bankCustomerState.outside){
			GoToBank();
		}
		if (state == bankCustomerState.entered){
		    TellHost();
		    return true;
		}

		if (state == bankCustomerState.assigned){
		    WalkToTeller();
		}
		
		if (state == bankCustomerState.atCounter){
			AskForAssistance();
		    return true;
		}

		if (state == bankCustomerState.done){
		    LeaveBank();
		}

		return false;
	}

//ACTIONS*************************************************

private void GoToBank() {
		print("Going to bank");
		this.customerGui.DoGoToWaitingRoom();
		state = bankCustomerState.entered;
}
	
private void TellHost(){
	   // DoEnterBank();
	    h.IWantService(this);
	    state = bankCustomerState.waiting;
}

private void AskForAssistance(){
	   // DoGiveOrder();

	    if (purpose == customerPurpose.createAccount){
	        balance -= amount;
	        t.IWantAccount(this, amount);
	    }
	    if (purpose == customerPurpose.withdraw)
	        t.WithdrawMoney(this, accountID, amount);
	    if (purpose == customerPurpose.deposit){
	        balance -= amount;
	        t.DepositMoney(this, accountID, amount);
	    }
	    if (purpose == customerPurpose.takeLoan)
	        t.IWantLoan(this, amount);
	    if (purpose == customerPurpose.payLoan){
	        balance -= amount;
	        t.PayMyLoan(this, amount);
	    }


	    state = bankCustomerState.waiting;
	}



private void LeaveBank(){
		print("I'm going to leave the restaurant.");
		customerGui.DoExitRestaurant();
	    state = bankCustomerState.exited;
}
	
	
	
	
	private void WalkToTeller() 
	{
		print("Being seated. Going to table " + 2);
		customerGui.DoGoToSeat(2);
		state = bankCustomerState.atCounter;
		stateChanged();
	}
	
	
	

//UTILITIES*************************************************

	

	public String toString() {
		return getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
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
	
	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}
}

