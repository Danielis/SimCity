package bank;

import java.util.*;

import bank.Bank.Account;



public class Bank {
	double balance;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	int idIncr = 0;
	
	public Bank(){
		balance = 50000;
		accounts.add(new Account(1, 500));
	}
	
	
	public class Account {
		int id; // auto increment
	    CustomerAgent c;
	    double balance;
	    
	    
	    public Account(CustomerAgent c) {
			this.c = c;
			balance = 0;
			id = ++idIncr;
		}
	    
	    public Account(int t, int b) {
			balance = b;
			id = t;
		}
	}
	
	public class Loan {
		CustomerAgent c;
	    double balanceOwed;
	    double balancePaid;
	    double rate;
	    int dayCreated;
	    int dayOwed;
	    loanState s;
	    
	    Loan(CustomerAgent c2, double amount){
	    c = c2;
	    rate = .08;
	    balanceOwed = amount * rate;
	    }
	}
	enum loanState {unpaid, partiallyPaid, paid}
	
	public Account createAccount(CustomerAgent c) {
		Account acct = new Account(c);
		return acct;
	}

	public Loan createLoan(CustomerAgent c, double amount) {
		Loan loan = new Loan(c, amount);
		return loan;
	}

	
}


