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
	int accountID;
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
		amount = 400;
	}

//UTILITIES**************************************************

	
//CLASSES/ENUMS**********************************************

	enum customerPurpose {createAccount, withdraw, deposit, takeLoan, payLoan};
	enum bankCustomerState {outside, entered, waiting, assigned, atCounter, done, exited};
	
//MESSAGES*************************************************

	public void NoLoan() {
	state = bankCustomerState.done;	
	stateChanged();
	}
	
public void msgWantsTransaction(String type, double temp){
		if (type.equals("New Account"))
			purpose = customerPurpose.createAccount;
		else if (type.equals("Withdraw"))
			purpose = customerPurpose.withdraw;
		else if (type.equals("Deposit"))
			purpose = customerPurpose.deposit;
		else if (type.equals("New Loan"))
			purpose = customerPurpose.takeLoan;
		else if (type.equals("Pay Loan"))
			purpose = customerPurpose.payLoan;
		state = bankCustomerState.outside;
		amount = temp;
		stateChanged();
	}

public void	WantsToDo(String visitPurpose, int quantity){ //called from Person agent
	    //purpose = convert(visitPurpose);
	    this.state = bankCustomerState.entered;
	    stateChanged();
	}

public void	GoToTeller(TellerAgent t){
	//print("received teller info");
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

public void	LoanCreated(double temp){
	    balance += temp;
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

public void	YourLoanIsPaidOff(double change){
		if (change > 0){
		balance += change;
		print("Received received change of $" + change);
		print("Now I have $" + balance);
		}
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
	if (purpose.equals(customerPurpose.withdraw))
		amount = 0;
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
	    state = bankCustomerState.waiting;
	    h.IWantService(this);
}

private void AskForAssistance(){
	state = bankCustomerState.waiting;
	timer.schedule( new TimerTask()
	{
		public void run()
		{				
			GiveRequest();
		}
	},  100);
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
		t.IAmLeaving();

		if (t.getTableNum() == 1){
			customerGui.DoGoToWaitingRoom();
			customerGui.DoExitRestaurant();
		}
		else if (t.getTableNum() == 2){
			customerGui.DoGoToTopLeft();
			customerGui.DoGoToBotLeft();
			customerGui.DoGoToWaitingRoom();
			customerGui.DoExitRestaurant();
		}
		else if (t.getTableNum() == 3){
			customerGui.DoGoToTopRight();
			customerGui.DoGoToBotRight();
			customerGui.DoGoToWaitingRoom();
			customerGui.DoExitRestaurant();
		}
		
		
	    state = bankCustomerState.exited;  
	    
	    customerGui.finishedTransaction();
}
	
	
	
	
	private void WalkToTeller() 
	{
		print("Directed to teller.");
		
		if (t.getTableNum() == 1){
		customerGui.DoGoToSeat(t.getTableNum());
		}
		else if (t.getTableNum() == 2){
			customerGui.DoGoToBotLeft();
			customerGui.DoGoToTopLeft();
			customerGui.DoGoToSeat(t.getTableNum());
		}
		else if (t.getTableNum() == 3){
			customerGui.DoGoToBotRight();
			customerGui.DoGoToTopRight();
			customerGui.DoGoToSeat(t.getTableNum());
		}
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

