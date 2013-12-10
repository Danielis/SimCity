package restaurantA.gui;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

import restaurantA.CashierAgent;
import restaurantA.CookAgent;
import restaurantA.CustomerAgent;
import restaurantA.HostAgent;
import restaurantA.Table;
import restaurantA.WaiterAgent;
import restaurantA.interfaces.Customer;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;



/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    public HostAgent host ;//= new HostAgent("Aleena");
   // public CashierAgent cashier = new CashierAgent("Cashier");
   // public CookAgent cook = new CookAgent("Chef", cashier);
 
	
    
   // private HostGui hostGui = new HostGui(host);
    private int numTables = 4;
    public ArrayList<Table> tables;

    public Vector<Customer> customers = new Vector<Customer>();
    public Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    
    JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
    	
    	setTables();
        this.gui = gui;
        gui.animationPanel.setHost(host);
        gui.animationPanel.setTables(tables);
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new FlowLayout());

        group.add(customerPanel);
        
        add(group);
    }

    private void setTables() {
    	int startingX = 130;
		int startingY = 250;
		int spacerY = 200;
		int spacerX = spacerY + 40;
				
		// make some tables
		tables = new ArrayList<Table>(numTables);
		for (int ix = 0; ix < numTables/2; ix++) {
			Table tempTable = new Table(ix, startingX+spacerX*ix, startingY);
			tables.add(tempTable);//how you add to a collections
		}
		for (int ix = 0; ix < numTables/2; ix++) {
			Table tempTable = new Table(ix, startingX+spacerX*ix, startingY + spacerY);
			tables.add(tempTable);
		}
	}

    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
            	Customer temp = customers.get(i);
                
                if (name.isEmpty())
        			name = "Customer";
                
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        
        if (type.equals("Waiters")) {
            for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
                
                if (name.isEmpty())
        			name = "Waiter";
                
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

	public void addCustomer(String type, CustomerAgent c, boolean b) {
		CustomerGui g = new CustomerGui(c, gui);

		gui.animationPanel.addGui(g);
		c.setHost(host);
		c.setGui(g);
		customers.add(c);
		c.setAnimPanel(gui.animationPanel);
		g.setHungry();
	}

	public void addHost(HostAgent c) {
		this.host = c;
		host.setRestaurant(gui.rest);
		gui.rest.setWorkingHost(c);
		host.setAnimPanel(gui.animationPanel);
		host.tables = tables;
		c.setWaiters();
	}

	public void addWaiter(WaiterAgent c) {

		c.rest = gui.rest;
		c.menu = gui.rest.menu;
		gui.rest.setWorkingWaiter(c);
		c.setAnimPanel(gui.animationPanel);
		c.tables = tables;
		
		WaiterGui waiterGui = new WaiterGui(c, c.tables, 120 + 30 * waiters.size(), 0);
		c.setGui(waiterGui);
		if (gui.rest.workingHost != null)
			gui.rest.workingHost.waiterAdded();
		waiters.add(c);
		gui.animationPanel.addGui(waiterGui);
	}

	public void addCook(CookAgent c) {
		c.setRestaurant(gui.rest);
		gui.rest.setWorkingCook(c);
		CookGui g = new CookGui(c, gui);
	    c.setGui(g);
	    gui.animationPanel.addGui(g);// dw
	    c.setAnimPanel(gui.animationPanel);
	}

	public void addCashier(CashierAgent c) {
		c.setRestaurant(gui.rest);
		gui.rest.setWorkingCashier(c);
	}

}
