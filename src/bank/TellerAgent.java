package bank;

import agent.Agent;
import agent.RestaurantMenu;
import bank.gui.BankAnimationPanel;
import bank.gui.TellerGui;
import bank.interfaces.*;
import bank.Bank;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;
import bank.CustomerAgent.customerPurpose;

import java.util.*;
import java.util.concurrent.Semaphore;

//Waiter Agent
public class TellerAgent extends Agent implements Teller {
	
	//Lists and Other Agents
	double balance;
	List <MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	List <Transaction> transactions = new ArrayList<Transaction>();
	
	public BankAnimationPanel copyOfAnimPanel;
	
	public HostAgent host;
	public TellerGui waiterGui;
	
	//Variables
	private int tableNum;
	private String name;
	private Bank bank;
	public Boolean isOnBreak = false;
	public myState state = myState.none;
	private int loanAccountThreshold = 500;
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public TellerAgent()
	{
		super();
		this.name = "Default Daniel";
		print("initialized teller");
		
	}
	public TellerAgent(String name, int index) {
		super();
		this.name = name;
		this.tableNum = index;
	}
	

//UTILITIES***************************************************
	
	public void setHost(HostAgent host) {
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
		
	private class Transaction{
	    public Transaction(Loan loan2, double amount2, transactionType type, CustomerAgent c) {
	    	loan = loan2;
			amount = amount2;
			this.type = type;
			status = transactionStatus.unresolved;
			this.c = c;
	    }
	    
		public Transaction(Account acct, double amount2, transactionType type, CustomerAgent c) {
		account = acct;
		amount = amount2;
		this.type = type;
		status = transactionStatus.unresolved;
		this.c = c;
		}
		
		public Transaction(double amount2, transactionType t, CustomerAgent c) {
		amount = amount2;
		type = t;
		this.c = c;
		status = transactionStatus.noAccount;
		}
		
		
		
		public Transaction(CustomerAgent c2) {
		c = c2;
		status = transactionStatus.noLoan;
		}



		double amount;
	    Account account;
	    Loan loan;
	    transactionType type;
	    transactionStatus status;
	    CustomerAgent c;
	}
	enum transactionType {withdrawal, deposit, newAccount, newLoan, loanPayment};
	enum transactionStatus {unresolved, resolved, noAccount, waiting, noLoan};

private class MyCustomer{
	    CustomerAgent c;
	}

	
public enum myState
{
	none, free, wantBreak, askedForBreak, onBreak
}

//MESSAGES****************************************************

public void IWantAccount(CustomerAgent c, double amount){
    Account acct = bank.createAccount(c);
    transactions.add(new Transaction(acct, amount, transactionType.newAccount, c));
    stateChanged();
}

public void DepositMoney(CustomerAgent c, int accountID, double amount){
	print("Looking for account...");
	for (Account a : bank.accounts){
		if (a.c == c){
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

public void WithdrawMoney(CustomerAgent c, int accountID, double amount){
	for (Account a : bank.accounts){
		if (a.c == c){
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

public void IWantLoan(CustomerAgent c, double amount){
    Loan loan = bank.createLoan(c, amount);
    transactions.add(new Transaction(loan, amount, transactionType.newLoan, c));
    stateChanged();
}

public void PayMyLoan(CustomerAgent c, double amount){
    for (Loan l : bank.loans){
		if (l.c == c){
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
	
	protected boolean pickAndExecuteAnAction() 
	{		
		try
		{
			for (Transaction t : transactions){
				//print("status: "+ t.status);
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
			return false;
		}
		catch(ConcurrentModificationException e)
		{
			waiterGui.DoGoToHomePosition();
			return false;
		}
	}

//ACTIONS********************************************************

	private void HandleNoLoan(Transaction t){
		print("You do not have a loan here.");
		t.status = transactionStatus.resolved;
		t.c.NoLoan();
	}
	
	private void HandleNoAccount(Transaction t){
		//print("handling no account");
		if (t.type == transactionType.deposit){
			print("You do not have an account at this bank. Would you like to create one?");
			t.status = transactionStatus.waiting;
			t.c.WantAccount();
		}
		if (t.type == transactionType.withdrawal){
			print("You do not have an account at this bank. Would you like to create one for future withdrawals?");
			t.status = transactionStatus.waiting;
			t.amount = 0;
			t.c.WantAccount();
		}
	}
	
	private void HandleTransaction(Transaction t){
		print("Looking into transaction...");
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
	
	private void Deposit(Transaction t){
		waiterGui.setSpeechBubble("thnxteller");
		print("Depositing $" + t.amount + " into account #" + t.account.id);
	    t.account.balance += t.amount;
	    print("New account balance is $" + t.account.balance);
	    bank.balance += t.amount;
	    print("New bank cash balance is $" + bank.balance);
	    t.status = transactionStatus.resolved;
	    
	   
	    t.c.MoneySuccesfullyDeposited();
	}

	private void Withdraw(Transaction t){
		t.status = transactionStatus.resolved;
	    if (t.account.balance >= t.amount){
	    	waiterGui.setSpeechBubble("withdrawteller");
	    	print("Withdrawing $" + t.amount + " from account #" + t.account.id);
	        t.account.balance -= t.amount;
	        print("New account balance is $" + t.account.balance);
	        bank.balance -= t.amount;
	        print("New bank cash balance is $" + bank.balance);
	        t.c.HereIsWithdrawal(t.amount);
	    }
	    else if (t.account.balance > 0){
	    	waiterGui.setSpeechBubble("withdrawteller");
	    	double temp = t.account.balance;
	    	print("You are low on money. Withdrawing only $" + temp + " from account #" + t.account.id);
	        t.account.balance -= temp;
	        print("New account balance is $" + t.account.balance);
	        bank.balance -= temp;
	        print("New bank cash balance is $" + bank.balance);
	        t.c.HereIsPartialWithdrawal(temp);
	    }
	    else{
	    	waiterGui.setSpeechBubble("banknomoney");
	    	print("You do not have any money in that account.");
	        t.c.NoMoney();
	    }  
	}

	private void NewAccount(Transaction t){
		
	   	waiterGui.setSpeechBubble("newacctteller");
	    
		print("Creating new account...");
	    t.account.balance += t.amount;
	    bank.balance += t.amount;
	    bank.accounts.add(t.account);
	    t.status = transactionStatus.resolved;
	    t.account.c.AccountCreated();
	    print("Your new account ID is " + t.account.id + " with balance of $" + t.account.balance);
	    print("Bank cash balance is $" + bank.balance);
	}

	private void CreateLoan(Transaction t){
		//print("reached createloan function");
	    t.status = transactionStatus.resolved;
	    if (HasGoodCredit(t.loan.c) && EnoughFunds(t.loan.balanceOwed)){ //stub function to see if bank has enough funds
	    	waiterGui.setSpeechBubble("loanteller");
	    	print("Created loan. Here is $" + t.amount + ". You owe $" + t.loan.balanceOwed);
	    	bank.loans.add(t.loan);
	    	bank.balance -= t.amount;
	        t.loan.c.LoanCreated(t.amount);
	        print("Bank cash balance is $" + bank.balance);
	    }
	    else if (HasGoodCredit(t.loan.c) && !EnoughFunds(t.loan.balanceOwed)){
	    	waiterGui.setSpeechBubble("banknomoney");
	    	print("Sorry we do not have enough money");
	        t.loan.c.CannotCreateLoan();
	    }
	    else { // bad credit
	    	waiterGui.setSpeechBubble("noloanteller");
	    	print("Your credit is not good enough");
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
	        t.loan.s = loanState.paid;
	        double change = t.loan.balancePaid - t.loan.balanceOwed;
	        t.loan.c.YourLoanIsPaidOff(change);
	    }
	    else{
	    	waiterGui.setSpeechBubble("stilloweloanteller");
	    	print("You still owe $" + (t.loan.balanceOwed - t.loan.balancePaid));
	        t.loan.c.YouStillOwe(t.loan.balanceOwed - t.loan.balancePaid, t.loan.dayCreated - t.loan.dayOwed);
	    }
	}
	
	private void TellHostFree(){
		print("I am ready for next customer.");
		host.IAmFree(this);
	}
	
	private void AskForBreak()
	{
		print("Asking host for a break");
		state = myState.askedForBreak;
		host.msgIdLikeToGoOnBreak(this);
	}
	
	private Boolean HasGoodCredit(CustomerAgent c){
		Boolean hasLoan = false;
		Boolean goodAccount = true;
		
		for (Loan l : bank.loans){
			if (l.c == c && l.s != loanState.paid){
				print("Customer already has a loan to pay off.");
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

