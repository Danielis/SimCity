package bank;

import agent.Agent;
import agent.RestaurantMenu;
import bank.gui.TellerGui;
import bank.interfaces.*;
import bank.Bank;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;

import java.awt.Menu;
import java.util.*;
import java.util.concurrent.Semaphore;

//Waiter Agent
public class TellerAgent extends Agent implements Teller {
	
	//Lists and Other Agents
	double balance;
	List <MyCustomer> myCustomers;
	List <Transaction> transactions;
	
	
	
	public HostAgent host;
	public TellerGui waiterGui;
	
	//Variables
	private String name;
	private Bank bank;
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
		public Transaction(Account acct, double amount2, transactionType withdrawal) {
		
		}
		double amount;
	    Account account;
	    Loan loan;
	    transactionType type;
	    transactionStatus status;
	}
	enum transactionType {withdrawal, deposit, newAccount, newLoan, loanPayment};
	enum transactionStatus {unresolved, resolved};

private class MyCustomer{
	    CustomerAgent c;
	}

	
public enum myState
{
	none, wantBreak, askedForBreak, onBreak
}

//MESSAGES****************************************************

public void IWantAccount(CustomerAgent c, double amount){
    Account acct = new Account(c, amount);
    transactions.add(new Transaction(acct, amount, transactionType.newAccount));
}

public void DepositMoney(CustomerAgent c, int accountID, double amount){
	for (Account a : bank.accounts){
		if (a.id == accountID){
			Account acct = a;
			 transactions.add(new Transaction(acct, amount, transactionType.deposit));
		}
	}
}

public void WithdrawMoney(CustomerAgent c, int accountID, double amount){
	for (Account a : bank.accounts){
		if (a.id == accountID){
			Account acct = a;
			transactions.add(new Transaction(acct, amount, transactionType.withdrawal));
		}
	}
}

public void IWantLoan(CustomerAgent c, double amount){
    //int time = generateTime(); // stub
    Loan loan = new Loan(); //TODO change 5 to a time value
    transactions.add(new Transaction(loan, amount, transactionType.newLoan));
}

public void PayMyLoan(CustomerAgent c, double amount){
    for (Loan l : bank.loans){
		if (l.c == c){
			Loan loan  = l;
			transactions.add(new Transaction(loan, amount, transactionType.loanPayment));
		}
	}
}

	public void msgSetOffBreak()
	{/*
		print(this.name + " is now available.");
		state = myState.none;
		waiterGui.setBreak(false);*/
		isOnBreak = false;
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
				if (t.status == transactionStatus.unresolved){
					HandleTransaction(t);
					return true;
				}
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

	private void HandleTransaction(Transaction t){
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
	    t.account.balance += t.amount;
	    balance += t.amount;
	    t.status = transactionStatus.resolved;
	    t.account.c.MoneySuccesfullyDeposited();
	}

	private void Withdraw(Transaction t){
	    if (t.account.balance >= t.amount){
	        t.account.balance -= t.amount;
	        balance -= t.amount;
	        t.account.c.HereIsWithdrawal(t.amount);
	    }
	    else if (t.account.balance > 0){
	        t.account.balance -= t.amount;
	        balance -= t.amount;
	        t.account.c.HereIsPartialWithdrawal(t.amount);
	    }
	    else
	        t.account.c.NoMoney();
	    t.status = transactionStatus.resolved;
	}

	private void NewAccount(Transaction t){
	    t.account.balance += t.amount;
	    balance += t.amount;
	    bank.accounts.add(t.account);
	    t.status = transactionStatus.resolved;
	    t.account.c.AccountCreated();
	}

	private void CreateLoan(Transaction t){
	    t.status = transactionStatus.resolved;
	    if (HasGoodCredit(t.loan.c) && EnoughFunds(t.loan.balanceOwed)){ //stub function to see if bank has enough funds
	        Loan loan = new Loan();
	    	bank.loans.add(loan);
	        t.loan.c.LoanCreated();
	    }
	    else if (HasGoodCredit(t.loan.c) && !EnoughFunds(t.loan.balanceOwed)){
	        t.loan.c.CannotCreatLoan();
	    }
	    else // bad credit
	        t.loan.c.CreditNotGoodEnough();
	}

	private void LoanPayBack(Transaction t){
	    t.loan.balancePaid += t.amount;
	    balance += t.amount;
	    t.status = transactionStatus.resolved;
	    if (t.loan.balancePaid >= t.loan.balanceOwed){
	        t.loan.s = loanState.paid;
	        t.loan.c.YourLoanIsPaidOff();
	    }
	    else
	        t.loan.c.YouStillOwe(t.loan.balanceOwed - t.loan.balancePaid, t.loan.dayCreated - t.loan.dayOwed);
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

