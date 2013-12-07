package city;

import java.util.ArrayList;
import java.util.List;


import bank.interfaces.BankCustomer;

public class BankDatabase {
	int idIncr = 0;
	int loanIncr = 0;
	private static BankDatabase instance = null;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();

public static synchronized BankDatabase getInstance() {
    if (instance == null) {
            instance = new BankDatabase();
    }
    return instance;
}

public List<Account> getAccounts(){
	return accounts;
}

public void addAccount(Account a){
	accounts.add(a);
}

public Account createAccount(BankCustomer c) {
	Account acct = new Account(c);
	return acct;
}

public Loan createLoan(BankCustomer c, double amount) {
	Loan loan = new Loan(c, amount);
	return loan;
}

public List<Loan> getLoans(){
	return loans;
}

public void addLoan(Loan l){
	loans.add(l);
}

public class Account {
	private int id; // auto increment
    public BankCustomer c;
    private double balance;
    
    
    public Account(BankCustomer c) {
		this.c = c;
		setBalance(0);
		id = ++idIncr;
	}
    
    public Account(int t, int b) {
		setBalance(b);
		id = t;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public BankCustomer getCustomer(){
		return c;
	}
	
	public int getID(){
		return id;
	}

	

	public void setId(int id) {
		this.id = id;
	}
}

public class Loan {
	public BankCustomer c;
    public double balanceOwed;
    public double balancePaid;
    double rate;
    public int dayCreated;
    public int dayOwed;
    public loanState s;
    int id;
    
    Loan(BankCustomer c2, double amount){
    c = c2;
    rate = 1.08;
    balanceOwed = Math.round(amount * rate * 100) / 100.0d;
    balancePaid = 0;
	id = ++loanIncr;
    }
    
    public double getAmountOwed(){
    	return balanceOwed;
    }
    public double getAmountPaid(){
    	return balancePaid;
    }
}
public enum loanState {unpaid, partiallyPaid, paid}



}