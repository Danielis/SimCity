package bank;

import java.util.*;

import bank.Bank.Account;



public class Bank {
	double balance;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	int idIncr = 0;
	
	
	public class Account {
		
	    public Account(CustomerAgent c) {
			this.c = c;
			balance = 0;
			id = ++idIncr;
		}
	    
	    
		int id; // auto increment
	    CustomerAgent c;
	    double balance;
	}
	
	public class Loan {
		CustomerAgent c;
	    double balanceOwed;
	    double balancePaid;
	    int dayCreated;
	    int dayOwed;
	    loanState s;
	    
	    Loan(){
	    	
	    }
	}
	enum loanState {unpaid, partiallyPaid, paid}
	
	public Account createAccount(CustomerAgent c) {
		Account acct = new Account(c);
		return acct;
	}

	public Loan createLoan(CustomerAgent c, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	
}


