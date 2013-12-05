package bank;

import agent.Agent;
import roles.Role;
import agent.RestaurantMenu;
import bank.gui.BankAnimationPanel;
import bank.gui.TellerGui;
import bank.interfaces.*;
import bank.Bank;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;
import bank.BankCustomerRole.customerPurpose;

import java.util.*;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;

//Waiter Agent
public class TellerRole extends Role implements Teller {
	
	//Lists and Other Agents
	double balance;
	List <MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	public List <Transaction> transactions = new ArrayList<Transaction>();
	
	public BankAnimationPanel copyOfAnimPanel;
	
	public BankHost host;
	public TellerGui waiterGui;
	Boolean leave = false;
	//Variables
	private int tableNum;
	private String name;
	private Bank bank;
	public Boolean isOnBreak = false;
	public myState state = myState.none;
	private int loanAccountThreshold = 500;
	private Boolean hasGun = false;
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public TellerRole()
	{
		super();
		this.name = "Default Daniel";
		print("initialized teller");
		//host.addMe(this);
	}
	public TellerRole(String name) {
		super();
		this.name = name;
		//print("initialized teller");
	}
	
//UTILITIES***************************************************

	public void setTableNum(int index){
		tableNum = index;
	}
	
	public void setHost(BankHost host) {
		this.host = host;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getMyCustomers() {
		return myCustomers;
	}
	
	public void setGui(TellerGui gui) {
		waiterGui = gui;
	}
	
	public TellerGui getGui() {
		return waiterGui;
	}
	public void setAnimPanel(BankAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
//CLASSES/ENUMS**********************************************
		
	public class Transaction{
	    public Transaction(Loan loan2, double amount2, transactionType type, BankCustomer c) {
	    	loan = loan2;
			amount = amount2;
			this.type = type;
			status = transactionStatus.unresolved;
			this.c = c;
	    }
	    
		public Transaction(Account acct, double amount2, transactionType type, BankCustomer c) {
		setAccount(acct);
		amount = amount2;
		this.type = type;
		status = transactionStatus.unresolved;
		this.c = c;
		}
		
		public Transaction(double amount2, transactionType t, BankCustomer c) {
		amount = amount2;
		type = t;
		this.c = c;
		if (t.equals(transactionType.robbery))
			status = transactionStatus.unresolved;
		else
			status = transactionStatus.noAccount;
		}
		
		
		
		public Transaction(BankCustomer c2) {
		c = c2;
		status = transactionStatus.noLoan;
		}


		
		public Account getAccount() {
			return account;
		}

		public void setAccount(Account account) {
			this.account = account;
		}
		
		public BankCustomer getCust(){
			return c;
		}



		public double amount;
	    private Account account;
	    Loan loan;
	   public transactionType type;
	    public transactionStatus status;
	    BankCustomer c;
	}
	public enum transactionType {withdrawal, deposit, newAccount, newLoan, loanPayment, robbery};
	public enum transactionStatus {unresolved, resolved, noAccount, waiting, noLoan};

private class MyCustomer{
	    BankCustomer c;
	}

	
public enum myState
{
	none, free, wantBreak, askedForBreak, onBreak
}

//MESSAGES****************************************************
public void msgGetPaid(){
	balance =+50;
	//print("Person was Paid =====================================");
}
@Override
public void msgLeaveWork() {
	leave = true;
	stateChanged();
}

public void IAmRobbing(BankCustomer c, double amount){
	print("msg rec i am being robbed");
    transactions.add(new Transaction(amount, transactionType.robbery, c));
    stateChanged();
}


public void IWantAccount(BankCustomer c, double amount){
    Account acct = bank.createAccount(c);
    transactions.add(new Transaction(acct, amount, transactionType.newAccount, c));
    stateChanged();
}

public void DepositMoney(BankCustomer c, int accountID, double amount){
	print("Looking for account...");
	for (Account a : bank.accounts){
		if (a.id == accountID){
			print("Found account.");
			Account acct = a;
			 transactions.add(new Transaction(acct, amount, transactionType.deposit, c));
			 stateChanged();
			 return;
		}
	}
	waiterGui.setSpeechBubble("noacct");
	print("No account found.");
	transactions.add(new Transaction(amount, transactionType.deposit, c));
	stateChanged();
	
}

public void WithdrawMoney(BankCustomer c, int accountID, double amount){
	for (Account a : bank.accounts){
		if (a.id == accountID){
			Account acct = a;
			transactions.add(new Transaction(acct, amount, transactionType.withdrawal, c));
			stateChanged();
			return;
		}
	}
	waiterGui.setSpeechBubble("noacct");
	print("No account found");
	transactions.add(new Transaction(amount, transactionType.withdrawal, c));
	stateChanged();
}

public void IWantLoan(BankCustomer c, double amount){
    Loan loan = bank.createLoan(c, amount);
    transactions.add(new Transaction(loan, amount, transactionType.newLoan, c));
    stateChanged();
}

public void PayMyLoan(BankCustomer c, double amount, Loan loan){
    for (Loan l : bank.loans){
		if (l == loan){
			transactions.add(new Transaction(l, amount, transactionType.loanPayment, c));
			stateChanged();
			return;
		}
	}
    transactions.add(new Transaction(c));
    waiterGui.setSpeechBubble("noloanteller");
    print("No loan found");
    stateChanged();
}

	public void IAmLeaving(){
		state = myState.free;
		stateChanged();
	}

	public void msgSetOffBreak()
	{/*
		print(this.name + " is now available.");
		state = myState.none;
		waiterGui.setBreak(false);*/
		isOnBreak = false;
		stateChanged();
	}
	
	public void msgSetOnBreak()
	{
		/*
		print(this.name + " wants a break.");
		state = myState.wantBreak;
		stateChanged();*/
		isOnBreak = true;
	}
	
	public void msgBreakGranted(Boolean permission)
	{
		if (permission == false)
		{
			//state = myState.none;
			isOnBreak = false;
			//waiterGui.setBreak(false);
		}
		else if (permission == true)
		{
			//state = myState.onBreak;
			isOnBreak = true;
			//waiterGui.setBreak(true);
		}
	}
	
	


//SCHEDULER****************************************************
	
	public boolean pickAndExecuteAnAction() 
	{		
		try
		{
			for (Transaction t : transactions){
				print("status: "+ t.status);
				if (t.status == transactionStatus.noAccount){
					HandleNoAccount(t);
					return true;
				}
				if (t.status == transactionStatus.noLoan){
					HandleNoLoan(t);
					return true;
				}
				if (t.status == transactionStatus.unresolved){
					HandleTransaction(t);
					return true;
				}
			}

			if (state == myState.free){
				TellHostFree();
			}

			if (state == myState.wantBreak)
			{
				AskForBreak();
				return true;
			}
			waiterGui.DoGoToHomePosition();
			if (leave && canLeave())
				LeaveWork();
			return false;
		}
		catch(ConcurrentModificationException e)
		{
			waiterGui.DoGoToHomePosition();
			return false;
		}
	}

private boolean canLeave() {
	for (Transaction t : transactions){
		if (t.status == transactionStatus.noAccount || t.status == transactionStatus.unresolved || t.status == transactionStatus.noLoan)
			return false;
	}
	return true;
	
}
//ACTIONS********************************************************
	
	private void LeaveWork() {
		bank.imLeaving(this);
		waiterGui.setDone();
		myPerson.msgLeftWork(this, balance);
		
	}
	
	private void HandleNoLoan(Transaction t){
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "You do not have a loan here", new Date()));
		print("You do not have a loan here.");
		t.status = transactionStatus.resolved;
		t.c.NoLoan();
	}
	
	private void HandleNoAccount(Transaction t){
		//print("handling no account");
		if (t.type == transactionType.deposit){
			print("You do not have an account at this bank. Would you like to create one?");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "You do not have an account at this bank.  Would you like to create one?", new Date()));
			t.status = transactionStatus.waiting;
			t.c.WantAccount();
		}
		if (t.type == transactionType.withdrawal){
			print("You do not have an account at this bank. Would you like to create one for future withdrawals?");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "You do not have an account at this bank.  Would you like to create one for future withdrawrals?", new Date()));
			t.status = transactionStatus.waiting;
			t.amount = 0;
			t.c.WantAccount();
		}
	}
	
	private void HandleTransaction(Transaction t){
		
		if (t.type == transactionType.robbery){
			print("dealing with robbery");
			DealWithRobbery(t);
		}
		else{
		print("Looking into transaction...");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Looking into transaction...", new Date()));
		if (t.type == transactionType.deposit){
			Deposit(t);
		}
		else if (t.type == transactionType.withdrawal){
			Withdraw(t);
		}
		else if (t.type == transactionType.newAccount){
			NewAccount(t);
		}
		else if (t.type == transactionType.newLoan){
			CreateLoan(t);
		}
		else if (t.type == transactionType.loanPayment){
			LoanPayBack(t);
		}
		}
	}
	
	private void DealWithRobbery(Transaction t){
		if (hasGun){
			print("I have a gun! You better get out.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "I have a gun! You better get out.", new Date()));
		   
			t.c.GetOut();
		}
		else{
			print("Ok ;_;! Here's $" + t.amount);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Ok ;_;! Here's $" + t.amount, new Date()));
			t.c.OkHereIsMoney(t.amount);
			}
	}
	
	private void Deposit(Transaction t){
		waiterGui.setSpeechBubble("thnxteller");
		print("Depositing $" + t.amount + " into account #" + t.getAccount().id);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Depositing $" + t.amount + " into account #" + t.getAccount().id, new Date()));
	    t.getAccount().setBalance(t.getAccount().getBalance()
				+ t.amount);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "New account balance is $" + t.getAccount().getBalance(), new Date()));
	    print("New account balance is $" + t.getAccount().getBalance());
	    bank.balance += t.amount;
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "New bank cash balance is $" + bank.balance, new Date()));	    
	    print("New bank cash balance is $" + bank.balance);
	    t.status = transactionStatus.resolved;
	    
	   
	    t.c.MoneySuccesfullyDeposited();
	}

	private void Withdraw(Transaction t){
		t.status = transactionStatus.resolved;
	    if (t.getAccount().getBalance() >= t.amount){
	    	waiterGui.setSpeechBubble("withdrawteller");
	    	print("Withdrawing $" + t.amount + " from account #" + t.getAccount().id);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Withdrawing $" + t.amount + " from account #" + t.getAccount().id, new Date()));	    
	        t.getAccount().setBalance(t.getAccount().getBalance()
					- t.amount);
	        print("New account balance is $" + t.getAccount().getBalance());
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "New account balance is $" + t.getAccount().getBalance(), new Date()));	    	        
	        bank.balance -= t.amount;
	        print("New bank cash balance is $" + bank.balance);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "New bank cash balance is $" + bank.balance, new Date()));	    	        
	        t.c.HereIsWithdrawal(t.amount);
	    }
	    else if (t.getAccount().getBalance() > 0){
	    	waiterGui.setSpeechBubble("withdrawteller");
	    	double temp = t.getAccount().getBalance();
	    	print("You are low on money. Withdrawing only $" + temp + " from account #" + t.getAccount().id);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "You are low on money. Withdrawing only $" + temp + " from account #" + t.getAccount().id, new Date()));	    	        
	        t.getAccount().setBalance(t.getAccount().getBalance() - temp);
	        print("New account balance is $" + t.getAccount().getBalance());
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "New account balance is $" + t.getAccount().getBalance(), new Date()));	    	        
	        bank.balance -= temp;
	        print("New bank cash balance is $" + bank.balance);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "New bank cash balance is $" + bank.balance, new Date()));	    	        
	        t.c.HereIsPartialWithdrawal(temp);
	    }
	    else{
	    	waiterGui.setSpeechBubble("banknomoney");
	    	print("You do not have any money in that account.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "You do not have any money in that account", new Date()));	    	        
	        t.c.NoMoney();
	    }  
	}

	private void NewAccount(Transaction t){
		
	   	waiterGui.setSpeechBubble("newacctteller");
	    
		print("Creating new account...");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Creating new account...", new Date()));	    	        
	    t.getAccount().setBalance(t.getAccount().getBalance()
				+ t.amount);
	    bank.balance += t.amount;
	    bank.addAccount(t.getAccount());
	    t.status = transactionStatus.resolved;
	    t.getAccount().c.AccountCreated(t.getAccount());
	    print("Your new account ID is " + t.getAccount().id + " with balance of $" + t.getAccount().getBalance());
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Your new account ID is " + t.getAccount().id + " with balance of $" + t.getAccount().getBalance(), new Date()));	    	        
	    print("Bank cash balance is $" + bank.balance);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Bank cash balance is $" + bank.balance, new Date()));	    	        
	}

	private void CreateLoan(Transaction t){
		//print("reached createloan function");
	    t.status = transactionStatus.resolved;
	    if (HasGoodCredit(t.loan.c) && EnoughFunds(t.loan.balanceOwed)){ //stub function to see if bank has enough funds
	    	waiterGui.setSpeechBubble("loanteller");
	    	print("Created loan. Here is $" + t.amount + ". You owe $" + t.loan.balanceOwed);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Created loan. Here is $" + t.amount + ". You owe $" + t.loan.balanceOwed, new Date()));	    	        
	    	bank.loans.add(t.loan);
	    	bank.balance -= t.amount;
	        t.loan.c.LoanCreated(t.amount, t.loan);
	        print("Bank cash balance is $" + bank.balance);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Bank cash balance is $" + bank.balance, new Date()));	    	        
	    }
	    else if (HasGoodCredit(t.loan.c) && !EnoughFunds(t.loan.balanceOwed)){
	    	waiterGui.setSpeechBubble("banknomoney");
	    	print("Sorry we do not have enough money");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Sorry we do not have enough money", new Date()));	    	        
	        t.loan.c.CannotCreateLoan();
	    }
	    else { // bad credit
	    	waiterGui.setSpeechBubble("noloanteller");
	    	print("Your credit is not good enough");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Your credit is not good enough", new Date()));	    	        
	        t.loan.c.CreditNotGoodEnough();
	    }
	}

	private void LoanPayBack(Transaction t){
	    t.loan.balancePaid += t.amount;
	    bank.balance += t.amount;
	    t.status = transactionStatus.resolved;
	    if (t.loan.balancePaid >= t.loan.balanceOwed){
	    	waiterGui.setSpeechBubble("thnxteller");
	    	print("Your loan is paid off!");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Your loan is paid off!", new Date()));	    	        
	        t.loan.s = loanState.paid;
	        double change = t.loan.balancePaid - t.loan.balanceOwed;
	        t.loan.balancePaid -= change;
	        bank.balance -= change;
	        t.loan.c.YourLoanIsPaidOff(change);
	    }
	    else{
	    	waiterGui.setSpeechBubble("stilloweloanteller");
	    	print("You still owe $" + (t.loan.balanceOwed - t.loan.balancePaid));
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "You still owe $" + (t.loan.balanceOwed - t.loan.balancePaid), new Date()));	    	        
	        t.loan.c.YouStillOwe(t.loan.balanceOwed - t.loan.balancePaid, t.loan.dayCreated - t.loan.dayOwed);
	    }
	}
	
	private void TellHostFree(){
		print("I am ready for next customer.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "I am ready for next customer", new Date()));	    	        
		host.IAmFree(this);
	}
	
	private void AskForBreak()
	{
		print("Asking host for a break");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Asking host for a break", new Date()));	    	        
		state = myState.askedForBreak;
		host.msgIdLikeToGoOnBreak(this);
	}
	
	private Boolean HasGoodCredit(BankCustomer c){
		Boolean hasLoan = false;
		Boolean goodAccount = true;
		
		for (Loan l : bank.loans){
			if (l.c == c && l.s != loanState.paid){
				print("Customer already has a loan to pay off.");
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "TellerRole", "Customer already has a loan to pay off", new Date()));	    	        
				return false;
			}
		}
		
//		for (Account a : bank.accounts){
//			if (a.c == c && a.balance > loanAccountThreshold){
//				return false;
//			}
//		}
		
		return true;
	}
	
	private Boolean EnoughFunds(double amount){
		return true;
	}

	public void WaitForAnimation()
	{
		try
		{
			animSemaphore.acquire();		
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:", e);
        }
	}
	
	public void DoneWithAnimation()
	{
		animSemaphore.release();
	}
	public int getTableNum() {
		return tableNum;
	}
	public void setBank(Bank b) {
		this.bank = b;
	}
}

