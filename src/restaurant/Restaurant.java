package restaurant;

import java.awt.Frame;
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
	public String name; //Name of the restaurant
    public Coordinate location;
    public ProducerConsumerMonitor theMonitor;
    
    public Restaurant(RestaurantGui gui, String name)
    {
    	this.gui = gui;
    	this.panel = gui.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
        gui.setTitle(name);
        gui.setVisible(true);
        gui.setState(Frame.NORMAL);
        gui.setResizable(false);
        gui.setAlwaysOnTop(true);
        gui.setAlwaysOnTop(false);
        gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       theMonitor = new ProducerConsumerMonitor();
    }
	
	public void addWaiter(Waiter w){
		panel.waiters.add(w);
	}
	
	public Boolean isOpen(){
		return (panel.host != null && panel.cashier != null && panel.cook != null);
	}
	public int getWaiterNumber(){
		return panel.waiters.size();
	}

	public void removeme(Waiter w)
	{
		panel.waiters.remove(w);
	}
	
	public void nullifyHost()
	{
		panel.host = null;
	}
	public void nullifyCook()
	{
		panel.cook = null;
	}
	public void nullifyCashier()
	{
		panel.cashier = null;
	}
	
	public double takePaymentForWork(double amount)
	{
		this.PaymentFund -= amount;
		return amount;
	}
}