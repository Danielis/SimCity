package market.gui;

import market.Market;
import market.MarketCustomerRole;
import market.MarketHostAgent;
import market.MarketWorkerAgent;
import market.interfaces.MarketCustomer;

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
public class MarketPanel extends JPanel {

    //Host, cook, waiters and customers
    private MarketHostAgent host = new MarketHostAgent("Oprah");
    private MarketHostGui hostGui = new MarketHostGui(host);
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    private Vector<MarketCustomer> customers = new Vector<MarketCustomer>();
    private Vector<MarketWorkerAgent> waiters = new Vector<MarketWorkerAgent>();

    private JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Tellers");
    private JPanel group = new JPanel();
        
    
    private Market market;
    private MarketGui gui; //reference to main gui
    Market b;// = new Market();
    
    public MarketPanel(MarketGui gui) {
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
     * @param string name of person
     */
    public void showCustomerInfo(String inp)
    {
        for (int i = 0; i < customers.size(); i++) {
        	MarketCustomer temp = customers.get(i);
            if (temp.getName() == inp)
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
            MarketWorkerAgent temp = waiters.get(i);
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
     * @param c2 name of person
     */
    public void addCustomer(MarketCustomer c) 
    {
    	c.setHost(host);
		MarketCustomerGui g = new MarketCustomerGui(c, gui);
		gui.animationPanel.addGui(g);
		c.setGui(g);
		c.setAnimPanel(gui.animationPanel);
		customers.add(c);
		c.getGui().setAction();
		//c.startThread();
    }
    
    public void addTeller(String name) 
    {
		waiterindex++;
    	MarketWorkerAgent w = new MarketWorkerAgent(name, waiterindex);	
		MarketTellerGui g = new MarketTellerGui(w, gui, waiterindex);
		w.setMarket(b);
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
 
    	for (MarketCustomer c : customers)
    	{
    		c.pauseAgent();
    	}
    	for (MarketWorkerAgent w : waiters)
    	{
    		w.pauseAgent();
    	}
    }
    
    public void resume()
    {
    	host.resumeAgent();
    
    	for (MarketCustomer c : customers)
    	{
    		c.resumeAgent();
    	}
    	for (MarketWorkerAgent w : waiters)
    	{
    		w.resumeAgent();
    	}
    }

    public void refresh()
    {
    	gui.updateLastCustomer();
    	gui.updateLastWaiter();
    }

	public void setMarket(Market market) {
		this.market = market;
	}
}
