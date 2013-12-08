package bank.gui;

import bank.Bank;
import bank.BankCustomerRole;
import bank.BankHostRole;
import bank.interfaces.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class BankPanel extends JPanel {

    private BankHost host = null;
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    private Vector<BankCustomer> customers = new Vector<BankCustomer>();
    private Vector<Teller> waiters = new Vector<Teller>();

    private JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Tellers");
    private JPanel group = new JPanel();
        
    public ImageIcon iconDescription;
    public JLabel picDescription;
    
    private BankGui gui; //reference to main gui
    
    Bank b;
    
    public BankPanel(BankGui gui) {
        this.gui = gui;
        
        iconDescription = new ImageIcon(getClass().getResource("/resources/img_bank.png"));
        picDescription = new JLabel(iconDescription);
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        //group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    private void initRestLabel() {
        restLabel.setLayout(new BorderLayout());
     
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        
        restLabel.add(picDescription, BorderLayout.CENTER);
    }

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
        	Teller temp = waiters.get(i);
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
		CustomerGui g = new CustomerGui(c, gui);
		gui.animationPanel.addGui(g);
		c.setHost(host);  
		c.setGui(g);
		g.setAction();
		c.setAnimPanel(gui.animationPanel);
		customers.add(c);
    }
    
    public void addHost(BankHost host) {
		HostGui g = new HostGui(host, gui);
		gui.animationPanel.addGui(g);
		host.setBank(b);
		host.setTellers(b.getWorkingTellers());
		b.tellTellers(host);
		host.setGui(g);
		host.setAnimPanel(gui.animationPanel);
		this.host = host;
	}
    
    public void addTeller(Teller c) 
    {
		Teller w = c;	
		b.addTeller(w);
		c.setTableNum(b.setTable()); 
		TellerGui g = new TellerGui(w, gui, b.setTable());
		w.setBank(b);
		if (host != null)
			host.addMe(w);
		gui.animationPanel.addGui(g);
		w.setHost(host);  
		w.setAnimPanel(gui.animationPanel);
		w.setGui(g);
		waiters.add(w);
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
