package bank;

import java.util.*;
import bank.interfaces.*;

import javax.swing.JFrame;

import roles.Building;
import roles.Coordinate;
import bank.Bank.Account;
import bank.gui.BankGui;
import bank.gui.BankPanel;



public class Bank extends Building{
	double balance;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	List <Teller> workingTellers = new ArrayList<Teller>();
	int idIncr = 0;
	
	public BankGui gui;
	public BankPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
    public int numTellersWorking = 0;
	
	public Bank(BankGui gui, String name){
		//super();
		balance = 50000;
		type = buildingType.bank;
		this.gui = gui;
    	this.panel = gui.restPanel;
    	gui.restPanel.setBank(this);
        gui.setTitle(name);
        gui.setVisible(false);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void addTeller(Teller t){
		workingTellers.add(t);
		numTellersWorking ++;
	}
	
	public int getTellerNunmber(){
		return numTellersWorking;
	}
	
	public class Account {
		int id; // auto increment
	    BankCustomerRole c;
	    private double balance;
	    
	    
	    public Account(BankCustomerRole c) {
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
	}
	
	public class Loan {
		BankCustomerRole c;
	    double balanceOwed;
	    double balancePaid;
	    double rate;
	    int dayCreated;
	    int dayOwed;
	    public loanState s;
	    
	    Loan(BankCustomerRole c2, double amount){
	    c = c2;
	    rate = 1.08;
	    balanceOwed = Math.round(amount * rate * 100) / 100.0d;
	    balancePaid = 0;
	    }
	}
	public enum loanState {unpaid, partiallyPaid, paid}
	
	public Account createAccount(BankCustomerRole c) {
		Account acct = new Account(c);
		return acct;
	}

	public Loan createLoan(BankCustomerRole c, double amount) {
		Loan loan = new Loan(c, amount);
		return loan;
	}

	
}


