package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.MarketAgent;
import restaurant.interfaces.Customer;

import javax.imageio.ImageIO;
import javax.swing.*;

import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    public HostAgent host = new HostAgent("Oprah");
    public HostGui hostGui = new HostGui(host);
    public CashierAgent cashier = new CashierAgent("Squidward");
    public MarketAgent market1 = new MarketAgent("Market 1");
    public MarketAgent market2 = new MarketAgent("Market 2");
    public MarketAgent market3 = new MarketAgent("Market 3");
    public CookAgent cook = new CookAgent("Gordon Ramsay");
    public CookGui cookGui;
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    public Vector<Customer> customers = new Vector<Customer>();
    public Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
        
    //Image Related
    ImageIcon iconOwner;
    JLabel picOwner;
    ImageIcon iconMenu;
    JLabel picMenu;
    
    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        cookGui = new CookGui(cook, gui);
        host.setGui(hostGui);
        cook.setGui(cookGui);
        
        iconOwner = new ImageIcon(getClass().getResource("/resources/menu_header.png"));
        picOwner = new JLabel(iconOwner);
        iconMenu = new ImageIcon(getClass().getResource("/resources/menu.png"));
        picMenu = new JLabel(iconMenu);
       
        gui.animationPanel.addGui(hostGui);
        gui.animationPanel.addGui(cookGui);
        
        market1.startThread();
        market2.startThread();
        market3.startThread();
        
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        market3.setCashier(cashier);
        
        market1.setCook(cook);
        market2.setCook(cook);
        market3.setCook(cook);
        
        cook.setMarkets(market1);
        cook.setMarkets(market2);
        cook.setMarkets(market3);
        
        host.startThread();
        cashier.startThread();
        cook.startThread();
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        /*old
         * 
         * label.setText(
                "<html>"
	                + "<h3><u>Tonight's Staff</u></h3>"
	                + "<table>"
	                	+ "<tr><td>Host:</td><td>" + host.getName() + "</td></tr>"
        			+ "</table>"
	                + "<h3><u> Menu</u></h3>"
	                + "<table>"
		                + "<tr><td>Steak</td><td>$15.99</td></tr>"
		                + "<tr><td>Chicken</td><td>$10.99</td></tr>"
		                + "<tr><td>Salad</td><td>$5.99</td></tr>"
		                + "<tr><td>Pizza</td><td>$8.99</td></tr>"
	                + "</table><br>"
                + "</html>");
         */

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(picMenu, BorderLayout.SOUTH);
        restLabel.add(picOwner, BorderLayout.NORTH);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showCustomerInfo(String name)
    {
        for (int i = 0; i < customers.size(); i++) {
            Customer temp = customers.get(i);
            if (temp.getName() == name)
            {
                customerPanel.updateCustomer(temp);
                gui.updateCustomerInformationPanel(temp);
                customerPanel.updateCustomer(temp);
            }
        }
    }
    
    public void showWaiterInfo(String name) 
    {
        for (int i = 0; i < waiters.size(); i++) {
            WaiterAgent temp = waiters.get(i);
            if (temp.getName() == name)
            {
                waiterPanel.updateWaiter(temp);
                gui.updateWaiterInformationPanel(temp);
                waiterPanel.updateWaiter(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addCustomer(Customer customer) 
    {
		Customer c = customer;
		CustomerGui g = new CustomerGui(c, gui);
		gui.animationPanel.addGui(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		//System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		c.setAnimPanel(gui.animationPanel);
		customers.add(c);
    }
    
    public void addCustomer(String name) 
    {
		Customer c = new CustomerAgent(name);
		CustomerGui g = new CustomerGui(c, gui);
		gui.animationPanel.addGui(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setAnimPanel(gui.animationPanel);
		customers.add(c);
		c.startThread();
    }
    
    public void addWaiter(String name) 
    {
		waiterindex++;
    	WaiterAgent w = new WaiterAgent(name);	
		WaiterGui g = new WaiterGui(w, gui, waiterindex);
		gui.animationPanel.addGui(g);
		w.setHost(host);
		host.msgNewWaiter(w);
		w.setCook(cook);
		w.setCashier(cashier);
		w.setGui(g);
		waiters.add(w);
		w.startThread();
    }
    
    public void pause()
    {
    	host.pauseAgent();
    	cook.pauseAgent();
    	for (Customer c : customers)
    	{
    		c.pauseAgent();
    	}
    	for (WaiterAgent w : waiters)
    	{
    		w.pauseAgent();
    	}
    }
    
    public void resume()
    {
    	host.resumeAgent();
    	cook.resumeAgent();
    	for (Customer c : customers)
    	{
    		c.resumeAgent();
    	}
    	for (WaiterAgent w : waiters)
    	{
    		w.resumeAgent();
    	}
    }

    public void refresh()
    {
    	gui.updateLastCustomer();
    	gui.updateLastWaiter();
    }
}
