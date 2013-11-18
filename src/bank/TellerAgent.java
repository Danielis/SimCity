package bank;

import agent.Agent;
import agent.RestaurantMenu;
import bank.gui.TellerGui;
import bank.interfaces.*;
import bank.Bank;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;

import java.util.*;
import java.util.concurrent.Semaphore;

//Waiter Agent
public class TellerAgent extends Agent implements Teller {
	
	//Lists and Other Agents
	double balance;
	List <MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	List <Transaction> transactions = new ArrayList<Transaction>();
	
	
	
	public HostAgent host;
	public TellerGui waiterGui;
	
	//Variables
	private String name;
	private Bank bank = new Bank();
	public Boolean isOnBreak = false;
	public myState state = myState.none;
	
	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);
	
	//Constructors
	public TellerAgent()
	{
		super();
		this.name = "Default Daniel";
		print("initialized teller");
		
	}
	public TellerAgent(String name) {
		super();
		this.name = name;
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

//CLASSES/ENUMS**********************************************
		
	private class Transaction{
	    public Transaction(Loan loan2, double amount2, transactionType loanpayment) {
		
	    }
	    
		public Transaction(Account acct, double amount2, transactionType type) {
		account = acct;
		amount = amount2;
		this.type = type;
		status = transactionStatus.unresolved;
		}
		
		public Transaction(double amount2, transactionType t, CustomerAgent c) {
		amount = amount2;
		type = t;
		this.c = c;
		status = transactionStatus.noAccount;
		}
		
		double amount;
	    Account account;
	    Loan loan;
	    transactionType type;
	    transactionStatus status;
	    CustomerAgent c;
	}
	enum transactionType {withdrawal, deposit, newAccount, newLoan, loanPayment};
	enum transactionStatus {unresolved, resolved, noAccount, waiting};

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
    transactions.add(new Transaction(acct, amount, transactionType.newAccount));
    stateChanged();
}

public void DepositMoney(CustomerAgent c, int accountID, double amount){
	print("Looking for account...");
	for (Account a : bank.accounts){
		if (a.id == accountID){
			print("Found account.");
			Account acct = a;
			 transactions.add(new Transaction(acct, amount, transactionType.deposit));
			 stateChanged();
		}
	}
	print("No account found.");
	transactions.add(new Transaction(amount, transactionType.deposit, c));
	stateChanged();
	
}

public void WithdrawMoney(CustomerAgent c, int accountID, double amount){
	for (Account a : bank.accounts){
		if (a.id == accountID){
			Account acct = a;
			transactions.add(new Transaction(acct, amount, transactionType.withdrawal));
			stateChanged();
		}
	}
	transactions.add(new Transaction(amount, transactionType.withdrawal, c));
}

public void IWantLoan(CustomerAgent c, double amount){
    Loan loan = bank.createLoan(c, amount);
    transactions.add(new Transaction(loan, amount, transactionType.newLoan));
    stateChanged();
}

public void PayMyLoan(CustomerAgent c, double amount){
    for (Loan l : bank.loans){
		if (l.c == c){
			Loan loan  = l;
			transactions.add(new Transaction(loan, amount, transactionType.loanPayment));
			stateChanged();
		}
	}
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

	private void HandleNoAccount(Transaction t){
		print("handling no account");
		if (t.type == transactionType.deposit){
			print("You do not have an account at this bank. Would you like to create one?");
			t.status = transactionStatus.waiting;
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
		print("Depositing money");
	    t.account.balance += t.amount;
	    balance += t.amount;
	    t.status = transactionStatus.resolved;
	    t.account.c.MoneySuccesfullyDeposited();
	}

	private void Withdraw(Transaction t){
		t.status = transactionStatus.resolved;
	    if (t.account.balance >= t.amount){
	    	print("Here is your withdrawal");
	        t.account.balance -= t.amount;
	        balance -= t.amount;
	        t.account.c.HereIsWithdrawal(t.amount);
	    }
	    else if (t.account.balance > 0){
	    	print("Here is partial withdrawal");
	        t.account.balance -= t.amount;
	        balance -= t.amount;
	        t.account.c.HereIsPartialWithdrawal(t.amount);
	    }
	    else{
	    	print("You do not have any money");
	        t.account.c.NoMoney();
	    }  
	}

	private void NewAccount(Transaction t){
		print("Creating new account");
	    t.account.balance += t.amount;
	    balance += t.amount;
	    bank.accounts.add(t.account);
	    t.status = transactionStatus.resolved;
	    t.account.c.AccountCreated();
	    print("Your new account ID is " + t.account.id + " with balance of $" + t.account.balance);
	}

	private void CreateLoan(Transaction t){
	    t.status = transactionStatus.resolved;
	    if (HasGoodCredit(t.loan.c) && EnoughFunds(t.loan.balanceOwed)){ //stub function to see if bank has enough funds
	    	print("Created loan");
	    	bank.loans.add(t.loan);
	        t.loan.c.LoanCreated();
	    }
	    else if (HasGoodCredit(t.loan.c) && !EnoughFunds(t.loan.balanceOwed)){
	    	print("Sorry we do not have enough money");
	        t.loan.c.CannotCreatLoan();
	    }
	    else { // bad credit
	    	print("Your credit is not good enough");
	        t.loan.c.CreditNotGoodEnough();
	    }
	}

	private void LoanPayBack(Transaction t){
	    t.loan.balancePaid += t.amount;
	    balance += t.amount;
	    t.status = transactionStatus.resolved;
	    if (t.loan.balancePaid >= t.loan.balanceOwed){
	    	print("Your loan is paid off!");
	        t.loan.s = loanState.paid;
	        t.loan.c.YourLoanIsPaidOff();
	    }
	    else{
	    	print("You still owe");
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

}

