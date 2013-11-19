package bank;

import bank.gui.BankAnimationPanel;
import bank.gui.CustomerGui;
import bank.interfaces.*;
import agent.Agent;
import agent.RestaurantMenu;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant.gui.RestaurantAnimationPanel;

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
	public BankAnimationPanel copyOfAnimPanel; // for gui
	TellerAgent t;
	double balance;
	customerPurpose purpose;
	double amount; //amount they want to deposit, withdraw, pay loan off of, or take loan out of
	int accountID = 1;
	private CustomerGui customerGui;
	public Semaphore animSemaphore = new Semaphore(0, true);
	public String name;
	Timer timer = new Timer();
	
	//Constructor
	public CustomerAgent(String name, HostAgent h){
		super();
		this.name = name;
		this.h = h;
		state = bankCustomerState.outside;
		purpose = customerPurpose.createAccount;
		amount = 400;
	}

//UTILITIES**************************************************

	
//CLASSES/ENUMS**********************************************

	enum customerPurpose {createAccount, withdraw, deposit, takeLoan, payLoan};
	enum bankCustomerState {outside, entered, waiting, assigned, atCounter, done, exited};
	
//MESSAGES*************************************************

public void	WantsToDo(String visitPurpose, int quantity){ //called from Person agent
	    //purpose = convert(visitPurpose);
	    this.state = bankCustomerState.entered;
	    stateChanged();
	}

public void	GoToTeller(TellerAgent t){
	print("received teller info");
	    this.t = t;
	    state = bankCustomerState.assigned;
	    stateChanged();
	}

public void	AccountCreated(){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	MoneySuccesfullyDeposited(){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	LoanCreated(){
	    balance += amount;
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	CannotCreateLoan(){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	CreditNotGoodEnough(){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	YourLoanIsPaidOff(){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	YouStillOwe(double d, int i){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	HereIsWithdrawal(double amount){
	    balance += amount;
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	HereIsPartialWithdrawal(double amount){
	    balance += amount;
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	NoMoney(){ // in account
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void WantAccount(){
	//	state = bankCustomerState.thinking;
		purpose = customerPurpose.createAccount;
		state = bankCustomerState.atCounter;
		stateChanged();
}

	
//SCHEDULER*************************************************
	protected boolean pickAndExecuteAnAction() 
	{
		//print("reached sched");
		if (state == bankCustomerState.outside){
			GoToBank();
			return true;
		}
		if (state == bankCustomerState.entered){
		    TellHost();
		    return true;
		}

		if (state == bankCustomerState.assigned){
		    WalkToTeller();
		    return true;
		}
		
		if (state == bankCustomerState.atCounter){
			AskForAssistance();
		    return true;
		}

		if (state == bankCustomerState.done){
		    LeaveBank();
		    return true;
		}

		return false;
	}

//ACTIONS*************************************************

private void GoToBank() {
		print("Going to bank");
		customerGui.DoGoToWaitingRoom();
		state = bankCustomerState.entered;
}
	
private void TellHost(){
	   // DoEnterBank();
		print("I want service");
	    h.IWantService(this);
	    state = bankCustomerState.waiting;
}

private void AskForAssistance(){
	state = bankCustomerState.waiting;
	timer.schedule( new TimerTask()
	{
		public void run()
		{				
			GiveRequest();
		}
	},  5000);
}

private void GiveRequest(){
		//print("This what I want to do...");
		
		
	    if (purpose == customerPurpose.createAccount){
	    	if (amount > 0)
	    	print("I would like to create an account and deposit $" + amount);
	    	else
	    	print("I would like to create an account.");
	        balance -= amount;
	        t.IWantAccount(this, amount);
	    }
	    
	    if (purpose == customerPurpose.withdraw){
	    	print("I would like to withdraw $" + amount);
	        t.WithdrawMoney(this, accountID, amount);
	    }
	    
	    if (purpose == customerPurpose.deposit){
	    	print("I would like to deposit $" + amount);
	        balance -= amount;
	        t.DepositMoney(this, accountID, amount);
	    }
	    
	    if (purpose == customerPurpose.takeLoan){
	    	print("I would like to take out a loan of $" + amount);
	        t.IWantLoan(this, amount);
	    }
	    
	    if (purpose == customerPurpose.payLoan){
	    	print("I would like to payback $" + amount + " of my loan");
	        balance -= amount;
	        t.PayMyLoan(this, amount);
	    }


	    
	}



private void LeaveBank(){
		print("Thank you. I now have $" + balance);
		
		customerGui.DoExitRestaurant();
	    state = bankCustomerState.exited;
	    t.IAmLeaving();
}
	
	
	
	
	private void WalkToTeller() 
	{
		print("Directed to teller.");
		customerGui.DoGoToSeat(t.getTableNum());
		state = bankCustomerState.atCounter;
		//stateChanged();
	}
	
	
	

//UTILITIES*************************************************

	

	public String toString() {
		return name;
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
	
	public void setAnimPanel(BankAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
}


