package bank;

import java.util.*;



public class Bank {
	double balance;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	
	public class Account {
	    public Account(CustomerAgent c2, double amount) {
			// TODO Auto-generated constructor stub
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
	
	public Account createAccount(CustomerAgent c, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	public Loan createLoan(CustomerAgent c, double amount) {
		// TODO Auto-generated method stub
		return null;
	};
}


