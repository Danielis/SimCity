package market;

import market.gui.MarketAnimationPanel;
import market.gui.MarketCustomerGui;
import market.interfaces.*;
import agent.Agent;
import agent.RestaurantMenu;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant.gui.RestaurantAnimationPanel;

//Customer Agent
//It still is a finite state machine, instead of events it still uses the state enum.
//I used this design from the designs drawn from class.

public class MarketCustomerAgent extends Agent implements MarketCustomer {
	
	//To show icon
	public enum iconState
	{
		none, question, steak, chicken, salad, pizza,
	};
	
	//EDIT HERE******************************
	static final float initialMoney = 20.00f;
	//EDIT HERE******************************
	
//VARIABLES*************************************************
	MarketHost h;
	bankCustomerState state;
	public MarketAnimationPanel copyOfAnimPanel; // for gui
	MarketTeller t;
	double balance = 1000;
	customerPurpose purpose;
	double amount; //amount they want to deposit, withdraw, pay loan off of, or take loan out of
	int accountID;
	private MarketCustomerGui customerGui;
	public Semaphore animSemaphore = new Semaphore(0, true);
	public String name;
	Timer timer = new Timer();
	
	Boolean isHappy = true;
	
	//Constructor
	public MarketCustomerAgent(String name, MarketHost h){
		super();
		this.name = name;
		this.h = h;
		state = bankCustomerState.outside;
		amount = 400;
	}

//UTILITIES**************************************************

	private Boolean enoughBalance(){
		if (amount > balance){
			print("I just realized I do not have enough money");
			state = bankCustomerState.done;
			stateChanged();
			return false;
		}
		else{
			balance -= amount;
			return true;
		}
	}
	
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

public void	GoToTeller(MarketTeller t){
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
	    	if (enoughBalance()){
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
	        
	    	if (enoughBalance())
	    		t.PayMyLoan(this, amount);
	    }


	    
	}



private void LeaveBank(){
		print("Thank you. I now have $" + balance);
		t.IAmLeaving();

		if (t.getTableNum() == 1){
			getCustomerGui().DoGoToWaitingRoom();
			getCustomerGui().DoExitRestaurant();
		}
		else if (t.getTableNum() == 2){
			getCustomerGui().DoGoToTopMiddle();
			getCustomerGui().DoGoToTopLeft();
			getCustomerGui().DoGoToBotLeft();
			getCustomerGui().DoGoToWaitingRoom();
			getCustomerGui().DoExitRestaurant();
		}
		else if (t.getTableNum() == 3){
			getCustomerGui().DoGoToTopRight();
			getCustomerGui().DoGoToBotRight();
			getCustomerGui().DoGoToWaitingRoom();
			getCustomerGui().DoExitRestaurant();
		}
		
		
	    state = bankCustomerState.exited;  
	    
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

	public void setGui(MarketCustomerGui g) {
		setCustomerGui(g);
	}

	public MarketCustomerGui getGui() {
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
	
	public void setAnimPanel(MarketAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}

	public MarketCustomerGui getCustomerGui() {
		return customerGui;
	}

	public void setCustomerGui(MarketCustomerGui customerGui) {
		this.customerGui = customerGui;
	}

	
}


