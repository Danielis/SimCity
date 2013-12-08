package restaurantA;

import java.awt.Frame;
import java.util.*;

import javax.swing.JFrame;





//import restaurantA.ProducerConsumerMonitor;
import restaurantA.gui.RestaurantGui;
import restaurantA.gui.RestaurantPanel;
import roles.*;
import roles.Building.*;
import city.*;

public class RestaurantA extends RestBase{
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
   // public ProducerConsumerMonitor theMonitor;
    public int numWaitersWorking = 0;
	public Boolean hasHost = false;
    public Boolean hasCook = false;
    public Boolean hasCashier = false;
    public int numWaiters = 0;
    
	public HostAgent workingHost = null;
	public CashierAgent workingCashier = null;
	public CookAgent workingCook = null;
	public List<WaiterAgent> workingWaiters = new ArrayList<WaiterAgent>();
	
	
	public RestaurantA(restaurantA.gui.RestaurantGui rg, String name)
    {
    	this.gui = rg;
    	this.panel = rg.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
        rg.setTitle(name);
        rg.setVisible(true);
        rg.setState(Frame.NORMAL);
        rg.setResizable(false);
        rg.setAlwaysOnTop(true);
        rg.setAlwaysOnTop(false);
        rg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        owner = "Aleena";
      // theMonitor = new ProducerConsumerMonitor();
    }

	public boolean hostIsHere() {
		return hasHost;
	}

	@Override
	public Boolean isOpen() {
		return true;
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
		return (numWaiters < 2);
	}

	public void setWaiter() {
		numWaiters++;
	}

	public void setWorkingHost(HostAgent c) {
		workingHost = c;
	}
}
