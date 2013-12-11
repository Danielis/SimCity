package city.test;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import bank.BankCustomerRole;
import bank.BankHostRole;
import bank.interfaces.Teller;
import agent.RestaurantMenu;
import restaurant.ProducerConsumerMonitor;
import restaurant.gui.CookGui;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.*;
import roles.Building;
import roles.Coordinate;
import roles.Building.buildingType;
import city.*;

public class MockRestaurant extends RestBase{
	
	List <Waiter> workingWaiters = new ArrayList<Waiter>();
	int idIncr = 0;
	
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
    public ProducerConsumerMonitor theMonitor;

    public int numWaitersWorking = 0;
    public Boolean hasHost = false;
    public Boolean hasCook = false;
    public Boolean hasCashier = false;
    public int numWaiters = 0;

    
    int numCustomers = 0;
    
    public MockRestaurant(RestaurantGui gui, String name)
    {
    	//this.gui = gui;
    	//this.panel = gui.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
//        gui.setTitle(name);
//        gui.setVisible(true);
//        gui.setState(Frame.NORMAL);
//        gui.setResizable(false);
//        gui.setAlwaysOnTop(true);
//        gui.setAlwaysOnTop(false);
//        gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       theMonitor = new ProducerConsumerMonitor();
       owner = "Norman";
    }
	
	public void addWaiter(Waiter w){
		panel.waiters.add(w);
	}
	
	public boolean canLeave()
	{
		if (panel.host == null) return true;
		if (panel.host.getCustomers().size() == 0 && panel.host.areTablesEmpty()) return true;
		
		return false;
	}
	
	public Boolean isOpen(){
		return (panel.host != null && panel.cashier != null && panel.cook != null);
	}
	
	public int getWaiterNumber(){
		return panel.waiters.size();
	}

	public void removeme(Waiter w)
	{
		panel.removeWaiters(w);
	}
	
	public void nullifyHost()
	{
		panel.host = null;
	}
	public void nullifyCook(CookGui cookGui)
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
	public boolean needsHost() {
		return !hasHost;
	}

	public void sethost() {
		hasHost = true;
	}

	public boolean needsCook() {
		return !hasCook;
	}

	public void setCook() {
		hasCook = true;
	}

	public boolean needsCashier() {
		return !hasCashier;
	}

	public void setCashier() {
		hasCashier = true;
	}

	public boolean needsWaiter() {
		return (numWaiters < 3);
	}

	public void setWaiter() {
		numWaiters++;
	}
}