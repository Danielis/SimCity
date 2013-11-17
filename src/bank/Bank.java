package bank;

import java.util.List;

import bank.TellerAgent.loanState;



public class Bank {
	double balance;
	List <Account> accounts;
	List <Loan> loans;
	
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
	enum loanState {unpaid, partiallyPaid, paid};
}


