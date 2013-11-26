package restaurant;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import bank.BankCustomerRole;
import bank.BankHostRole;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;
import bank.interfaces.Teller;
import agent.RestaurantMenu;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.*;
import roles.Building;
import roles.Coordinate;
import roles.Building.buildingType;

public class Restaurant extends Building{
	
	List <Waiter> workingWaiters = new ArrayList<Waiter>();
	int idIncr = 0;
	
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public Host host;
	public Cashier cashier;
	public Cook cook;
	public String name; //Name of the restaurant
    public Coordinate location;
    public ProducerConsumerMonitor theMonitor;
    public int numWaitersWorking = 0;
    
    //public CookAgent cook;
    //public CashierAgent cashier;
    //public String customerRole; //value is something like "Restaurant1CustomerRole"
    //public RestaurantMenu menu;
    //public String type;
    
    public Restaurant(RestaurantGui gui, String name)
    {
    	this.gui = gui;
    	this.panel = gui.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
        gui.setTitle(name);
        gui.setVisible(false);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       theMonitor = new ProducerConsumerMonitor();
    }
	
	public void addWaiter(Waiter w){
		workingWaiters.add(w);
		numWaitersWorking ++;
	}
	
	public Boolean isOpen(){
		return (host != null && cashier != null && cook != null);
	}
	public int getWaiterNumber(){
		return numWaitersWorking;
	}
}