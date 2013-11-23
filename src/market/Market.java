package market;

import java.util.*;

import bank.Bank.Account;



public class Market {
	double balance;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	int idIncr = 0;
	
	public Market(){
		balance = 50000;
		//accounts.add(new Account(1, 500));
	}
	
	
	public class Account {
		int id; // auto increment
	    MarketCustomerAgent c;
	    double balance;
	    
	    
	    public Account(MarketCustomerAgent c) {
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
		MarketCustomerAgent c;
	    double balanceOwed;
	    double balancePaid;
	    double rate;
	    int dayCreated;
	    int dayOwed;
	    loanState s;
	    
	    Loan(MarketCustomerAgent c2, double amount){
	    c = c2;
	    rate = 1.08;
	    balanceOwed = Math.round(amount * rate * 100) / 100.0d;
	    balancePaid = 0;
	    }
	}
	enum loanState {unpaid, partiallyPaid, paid}
	
	public Account createAccount(MarketCustomerAgent c) {
		Account acct = new Account(c);
		return acct;
	}

	public Loan createLoan(MarketCustomerAgent c, double amount) {
		Loan loan = new Loan(c, amount);
		return loan;
	}

	
}


