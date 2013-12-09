package bank;

import java.util.*;

import bank.interfaces.*;

import javax.swing.JFrame;

import city.TimeManager;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import roles.Building;
import roles.Coordinate;
import bank.gui.BankGui;
import bank.gui.BankPanel;
import city.TimeManager.*;



public class Bank extends Building{
	double balance;
//	List <Account> accounts = new ArrayList<Account>();
//	List <Loan> loans = new ArrayList<Loan>();
	private List <Teller> workingTellers = new ArrayList<Teller>();
//	List <TellerTable> tellerTables = new ArrayList<TellerTable>();

	public BankGui gui;
	public BankPanel panel;
    public Coordinate location;
    public BankHostRole workingHost;
    private Boolean hasHost = false;
    private int numTellers = 0;
    
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
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setAlwaysOnTop(true);
        gui.setAlwaysOnTop(false);
        gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.name = name;
	}
	
	public Bank(){
		balance = 50000;
		type = buildingType.bank;
	}
	

	
	public void addTeller(Teller t){
		workingTellers.add(t);
	}
	
	public void imLeaving(Teller t){
		workingTellers.remove(t);
	}
	
	
	public int setTableNum(){
		assignTable();
		return tableLastAssigned;
	}
	
	public void tellTellers(BankHost h){
		for (Teller t: workingTellers){
			t.setHost(h);
		}
	}
	
	public void assignTable(){
		tableLastAssigned++;
		if (tableLastAssigned > 3)
			tableLastAssigned = 1;
	}
	
	public int setTable(){
		return (workingTellers.size());
	}
	
	
	
//	private void setOccupied(Teller t) {
//		int i = getUnoccupiedTableNumber();
//		setTableOccupied(i);
//		t.setTableNum(i); 
//	}

	public Boolean isOpen(){
		if (forceClosed || TimeManager.getInstance().getDay().equals(Day.sunday) || TimeManager.getInstance().getDay().equals(Day.saturday) || workingHost == null)
			return false;
		else
			return true;
	}
	
	public double getBalance(){
		return balance;
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
	
	

	public void Leaving() {
		workingHost = null;
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
	

	
	public Boolean needsHost(){
		return !hasHost;
	}

	public Boolean needsTeller(){
		return (numTellers < 3);
	}

	public void setTeller(){
		numTellers++;
	}
	
	public void sethost(){
		hasHost = true;
	}
	
	public double takePaymentForWork(double amount)
	{
		this.PaymentFund -= amount;
		return amount;
	}

	
}


