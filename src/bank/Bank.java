package bank;

import java.util.*;

import bank.interfaces.*;

import javax.swing.JFrame;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import roles.Building;
import roles.Coordinate;
import bank.Bank.Account;
import bank.gui.BankGui;
import bank.gui.BankPanel;



public class Bank extends Building{
	double balance;
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	private List <Teller> workingTellers = new ArrayList<Teller>();
//	List <TellerTable> tellerTables = new ArrayList<TellerTable>();
	int idIncr = 0;
	
	public BankGui gui;
	public BankPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
    public BankHostRole host;
    
	public TrackerGui trackingWindow;
    int tableLastAssigned = 0;
	
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
        
       // TellerTable table1 = new TellerTable(1);
        //TellerTable table2 = new TellerTable(2);
       // TellerTable table3 = new TellerTable(3);
	}
	
	
//	class TellerTable{
//		Teller t;
//		int num;
//		Boolean occupied = false;
//		
//		public TellerTable(int i){
//			System.out.println("added table " + i);
//			num = i;
//		}
//		
////		public void addTeller(Teller t){
////			this.t = t;
////			occupied = true;
////		}
//		
//		public Boolean getOccupied(){
//			return occupied;
//		}
//		
//	}
	
	
	
	public void addTeller(Teller t){
		workingTellers.add(t);
	}
	
	
	public int setTableNum(){
		assignTable();
		return tableLastAssigned;
	}
	
	public void assignTable(){
		tableLastAssigned++;
		if (tableLastAssigned > 3)
			tableLastAssigned = 1;
	}
	
	
	
//	private void setOccupied(Teller t) {
//		int i = getUnoccupiedTableNumber();
//		setTableOccupied(i);
//		t.setTableNum(i); 
//	}

	public Boolean isOpen(){
		return (host != null);
	}
	
//	public int getUnoccupiedTableNumber(){
//		for(TellerTable t : tellerTables){
//			if (!t.getOccupied()){
//				System.out.println("unoccupd table " + t.num);
//				return t.num;
//			}
//		}
//		return 0;
//	}
//	
//	public int setTellerTableNumber(Teller x){
//		for(TellerTable t : tellerTables){
//			if (t == x){
//				System.out.println("get table " + t.num);
//				t.occupied = true;
//				return t.num;
//			}
//		}
//		return 0;
//	}
//	
//	public void setTableOccupied(int x){
//		for(TellerTable t : tellerTables){
//			if (t.num == x)
//				t.occupied = true;
//		}
//	}
	
	public List<Teller> getTellers(){
		return workingTellers;
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

	public void Leaving() {
		host = null;
	}

	public void removeMe(BankHostRole b) {
		workingTellers.remove(b);
	}


	public List <Teller> getWorkingTellers() {
		return workingTellers;
	}


	public void setWorkingTellers(List <Teller> workingTellers) {
		this.workingTellers = workingTellers;
	}

	
}


