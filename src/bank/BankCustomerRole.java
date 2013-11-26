package bank;

import bank.Bank.*;
import bank.gui.BankAnimationPanel;
import bank.gui.CustomerGui;
import bank.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import logging.TrackerGui;
import roles.Role;

//Customer Agent
//It still is a finite state machine, instead of events it still uses the state enum.
//I used this design from the designs drawn from class.

public class BankCustomerRole extends Role implements BankCustomer {
	
	//To show icon
	public enum iconState
	{
		none, question, steak, chicken, salad, pizza,
	};
	
	//EDIT HERE******************************
	static final float initialMoney = 20.00f;
	//EDIT HERE******************************
	
//VARIABLES*************************************************
	BankHost h;
	bankCustomerState state;
	public BankAnimationPanel copyOfAnimPanel; // for gui
	Teller t;
	double balance;
	customerPurpose purpose;
	double amount; //amount they want to deposit, withdraw, pay loan off of, or take loan out of
	int accountID;
	private CustomerGui customerGui;
	public Semaphore animSemaphore = new Semaphore(0, true);
	public String name;
	Timer timer = new Timer();
	Loan loan;
	Account acct;
	
	Boolean isHappy = true;
	
	public TrackerGui trackingWindow;

	//Constructor
	public BankCustomerRole(String name, String type, double bankAmount, double money){
		super();
		this.name = name;
		state = bankCustomerState.outside;
		amount = bankAmount;
		balance = money;
		
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
		else{
			purpose = customerPurpose.none;
			state = bankCustomerState.done;
		}
	}

//UTILITIES**************************************************

	private Boolean reduceBalance(){
		if (enoughBalance()){
			balance -= amount;
			return true;
		}
		return false;
	}
	
	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}
	
	private Boolean enoughBalance(){
		if (amount > balance){
			print("I just realized I do not have enough money");
			state = bankCustomerState.done;
			isHappy = false;
			stateChanged();
			return false;
		}
		else
			return true;
	}
	
	
//CLASSES/ENUMS**********************************************

	enum customerPurpose {createAccount, withdraw, deposit, takeLoan, payLoan, none};
	enum bankCustomerState {outside, entered, waiting, assigned, atCounter, done, exited};
	
//MESSAGES*************************************************

	public void BankIsClosed(){
		state = bankCustomerState.done;
		isHappy = false;
		stateChanged();
	}
	
	public void NoLoan() {
	state = bankCustomerState.done;	
	stateChanged();
	}
	
	public void test(String type, double temp){
		state = bankCustomerState.outside;
		stateChanged();
	}
	
public void msgWantsTransaction(){
		state = bankCustomerState.outside;
		//print("rec msg");
		stateChanged();
	}

public void	WantsToDo(String visitPurpose, int quantity){ //called from Person agent
	    //purpose = convert(visitPurpose);
	    this.state = bankCustomerState.entered;
	    stateChanged();
	}

public void	GoToTeller(Teller t){
	//print("received teller info");
	    this.t = t;
	    state = bankCustomerState.assigned;
	    stateChanged();
	}

public void	AccountCreated(Account a){
		acct = a;
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	MoneySuccesfullyDeposited(){
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	LoanCreated(double temp, Loan t){
		loan = t;
	    balance += temp;
	    state = bankCustomerState.done;
	    stateChanged();
	}

public void	CannotCreateLoan(){
	    state = bankCustomerState.done;
	    isHappy = false;
	    stateChanged();
	}

public void	CreditNotGoodEnough(){
	    state = bankCustomerState.done;
	    isHappy = false;
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
	    isHappy = false;
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
	isHappy = false;
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
	public boolean pickAndExecuteAnAction() 
	{
	//	print("reached sched");
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
			SayThanks();
		    return true;
		}
		
		
		return false;
	}

//ACTIONS*************************************************

	
private void GoToBank() {
		print("Going to bank");
		getCustomerGui().DoGoToWaitingRoom();
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
	
	

    if (purpose == customerPurpose.createAccount){
    	customerGui.setSpeechBubble("newacctq");
    }
    
    else if (purpose == customerPurpose.withdraw){
    	customerGui.setSpeechBubble("withdrawq");
    }
    
    else if (purpose == customerPurpose.deposit){
    	customerGui.setSpeechBubble("deposit");
    }
    
    else if (purpose == customerPurpose.takeLoan){
    	customerGui.setSpeechBubble("newloanq");
    }
    
    else if (purpose == customerPurpose.payLoan){
    	customerGui.setSpeechBubble("payloan");
    }
	
	
	
	timer.schedule( new TimerTask()
	{
		public void run()
		{				
			GiveRequest();
		}
	}, 4000);
}

private void SayThanks(){
	state = bankCustomerState.exited;
	if (isHappy)
	customerGui.setSpeechBubble("thnxcust");
	else
	customerGui.setSpeechBubble("oksadcust");
	
	timer.schedule( new TimerTask()
	{
		public void run()
		{				
			LeaveBank();
		}
	}, 2000);
	
}

private void GiveRequest(){
		//print("This what I want to do...");
		
		
	    if (purpose == customerPurpose.createAccount){
	    	if (amount > 0)
	    	print("I would like to create an account and deposit $" + amount);
	    	else
	    	print("I would like to create an account.");
	    	if (reduceBalance()){
	    		t.IWantAccount(this, amount);
	    	}
	    }
	    
	    if (purpose == customerPurpose.withdraw){
	    	print("I would like to withdraw $" + amount);
	        t.WithdrawMoney(this, accountID, amount);
	    }
	    
	    if (purpose == customerPurpose.deposit){
	    	print("I would like to deposit $" + amount);
	    	
	    	if (enoughBalance())
	    		t.DepositMoney(this, accountID, amount);
	    }
	    
	    if (purpose == customerPurpose.takeLoan){
	    	print("I would like to take out a loan of $" + amount);
	        t.IWantLoan(this, amount);
	    }
	    
	    if (purpose == customerPurpose.payLoan){
	    	print("I would like to payback $" + amount + " of my loan");
	        
	    	if (reduceBalance())
	    		t.PayMyLoan(this, amount);
	    }


	    
	}



private void LeaveBank(){
		print("Thank you. I now have $" + balance);
		if (t != null){
			t.IAmLeaving();

		if (t.getTableNum() == 1){
			getCustomerGui().DoGoToWaitingRoom();
		}
		else if (t.getTableNum() == 2){
			getCustomerGui().DoGoToTopMiddle();
			getCustomerGui().DoGoToTopLeft();
			getCustomerGui().DoGoToBotLeft();
			getCustomerGui().DoGoToWaitingRoom();
		}
		else if (t.getTableNum() == 3){
			getCustomerGui().DoGoToTopRight();
			getCustomerGui().DoGoToBotRight();
			getCustomerGui().DoGoToWaitingRoom();
		}
		}
		
		getCustomerGui().DoExitRestaurant();
		
	    state = bankCustomerState.exited;  
	    customerGui.setDone();
		this.myPerson.msgLeavingBank(this, balance);
		if (acct != null)
			this.myPerson.msgNewAccount(this, acct);
		if (loan != null)
			this.myPerson.msgNewLoan(this,loan);
	    getCustomerGui().finishedTransaction();
}
	
	
	
	
	private void WalkToTeller() 
	{
		print("Directed to teller.");
		
		if (t.getTableNum() == 1){
		getCustomerGui().DoGoToSeat(t.getTableNum());
		}
		else if (t.getTableNum() == 2){
			getCustomerGui().DoGoToBotLeft();
			getCustomerGui().DoGoToTopLeft();
			getCustomerGui().DoGoToTopMiddle();
			getCustomerGui().DoGoToSeat(t.getTableNum());
		}
		else if (t.getTableNum() == 3){
			getCustomerGui().DoGoToBotRight();
			getCustomerGui().DoGoToTopRight();
			getCustomerGui().DoGoToSeat(t.getTableNum());
		}
		state = bankCustomerState.atCounter;
		//stateChanged();
	}
	
	
	

//UTILITIES*************************************************

	

	public String toString() {
		return name;
	}

	public void setGui(CustomerGui g) {
		setCustomerGui(g);
		
	}

	public CustomerGui getGui() {
		return getCustomerGui();
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

	public CustomerGui getCustomerGui() {
		return customerGui;
	}

	public void setCustomerGui(CustomerGui customerGui) {
		this.customerGui = customerGui;
	}

	public void setHost(BankHost host) {
		this.h = host;
	}



	

	
}


