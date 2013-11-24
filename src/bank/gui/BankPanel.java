package bank.gui;

import bank.Bank;
import bank.BankCustomerRole;
import bank.HostAgent;
import bank.TellerAgent;
import bank.interfaces.BankCustomer;

import javax.imageio.ImageIO;
import javax.swing.*;

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
public class BankPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Oprah");
    private HostGui hostGui = new HostGui(host);
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    private Vector<BankCustomer> customers = new Vector<BankCustomer>();
    private Vector<TellerAgent> waiters = new Vector<TellerAgent>();

    private JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Tellers");
    private JPanel group = new JPanel();
        
    
    
    private BankGui gui; //reference to main gui
    Bank b;
    
    public BankPanel(BankGui gui) {
        this.gui = gui;
        host.setGui(hostGui);
        host.setAnimPanel(gui.animationPanel);
        
       
       
        gui.animationPanel.addGui(hostGui);
        
       
        
        host.startThread();
        
        
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
        
          
//         label.setText(
//                "<html>"
//	                + "<h3><u>Tonight's Staff</u></h3>"
//	                + "<table>"
//	                	+ "<tr><td>Host:</td><td>" + host.getName() + "</td></tr>"
//        			+ "</table>"
//	                + "<h3><u> Menu</u></h3>"
//	                + "<table>"
//		                + "<tr><td>Steak</td><td>$15.99</td></tr>"
//		                + "<tr><td>Chicken</td><td>$10.99</td></tr>"
//		                + "<tr><td>Salad</td><td>$5.99</td></tr>"
//		                + "<tr><td>Pizza</td><td>$8.99</td></tr>"
//	                + "</table><br>"
//                + "</html>");
         

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
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
            BankCustomer temp = customers.get(i);
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
            TellerAgent temp = waiters.get(i);
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
    public void addCustomer(BankCustomer c) 
    {
    	//System.out.println("bankpanel addcust");
		CustomerGui g = new CustomerGui(c, gui);
		gui.animationPanel.addGui(g);
		c.setHost(host);
		c.setGui(g);
		c.setAnimPanel(gui.animationPanel);
		customers.add(c);
		//c.startThread();
    }
    
//    public void addCustomer(String name) 
//    {
//		BankCustomerRole c = new BankCustomerRole(name);	
//		CustomerGui g = new CustomerGui(c, gui);
//		gui.animationPanel.addGui(g);
//		c.setHost(host);
//		c.setGui(g);
//		c.setAnimPanel(gui.animationPanel);
//		customers.add(c);
//		c.startThread();
//    }
    
    public void addTeller(String name) 
    {
		waiterindex++;
    	TellerAgent w = new TellerAgent(name, waiterindex);	
		TellerGui g = new TellerGui(w, gui, waiterindex);
		w.setBank(b);
		gui.animationPanel.addGui(g);
		w.setHost(host);
		w.setAnimPanel(gui.animationPanel);
		host.msgNewTeller(w);
		w.setGui(g);
		waiters.add(w);
		w.startThread();
    }
    
    public void pause()
    {
    	host.pauseAgent();
 
    	for (BankCustomer c : customers)
    	{
    		c.pauseAgent();
    	}
    	for (TellerAgent w : waiters)
    	{
    		w.pauseAgent();
    	}
    }
    
    public void resume()
    {
    	host.resumeAgent();
    
    	for (BankCustomer c : customers)
    	{
    		c.resumeAgent();
    	}
    	for (TellerAgent w : waiters)
    	{
    		w.resumeAgent();
    	}
    }

    public void refresh()
    {
    	gui.updateLastCustomer();
    	gui.updateLastWaiter();
    }

	public void setBank(Bank bank) {
		this.b = bank;
		
	}
}
