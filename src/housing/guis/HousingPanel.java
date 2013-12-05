package housing.guis;

import housing.HousingWorkerRole;
import housing.LandlordRole;
import housing.interfaces.HousingCustomer;
import housing.interfaces.HousingWorker;
import housing.interfaces.Landlord;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.PersonAgent;

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
public class HousingPanel extends JPanel {

    //declare agents.  for now one landlord, one worker, and one customer
	public LandlordRole landlord;
        
    private Vector<HousingCustomer> tenants = new Vector<HousingCustomer>();
    public Vector<HousingWorker> workers = new Vector<HousingWorker>();
    
    private JPanel restLabel = new JPanel();
    public HousingListPanel tenantPanel = new HousingListPanel(this, "Tenants");
    public HousingListPanel workerPanel = new HousingListPanel(this, "Workers");
    private JPanel group = new JPanel();
        
    
    private HousingGui gui; //reference to main gui

    public HousingPanel(HousingGui gui) {
        this.gui = gui;
      
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        
        group.add(tenantPanel);
        group.add(workerPanel);
        
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
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showTenantInfo(String name)
    {
    	for (HousingCustomer temp:tenants) {
    		if (temp.getName().equals(name))
    		{
    			tenantPanel.updateTenant(temp);
    			gui.updateTenantInformationPanel(temp);
    		}
    	}
    }
    public void showWorkerInfo(String name)
    {
    	for (HousingWorker temp:workers) {
    		if (temp.name.equals(name))
    		{
    			workerPanel.updateWorker(temp);
    			gui.updateWorkerInformationPanel(temp);
    		}
    	}
    }

    /**
     * Adds a customer or waiter to the appropriate list
     * @param homePurpose 
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
*/
    
    /*
    public void addTenant(String name, int n) 
    {
		HousingCustomerAgent p = new HousingCustomerAgent(name);
		HousingCustomerGui g = new HousingCustomerGui(p, gui, n);
		gui.housingAnimationPanel.addGui(g);
		p.setGui(g);
        landlord.addCustomer(p);
        p.setLandlord(landlord);
    	//p.setAnimationPanel(gui.cityAnimationPanel);
		tenant = p;
		tenants.add(tenant);
		System.out.println("Entry in tenants has name: " + tenants.get(0).name);
		tenant.startThread();
    }*/
    
    public void addTenant(HousingCustomer c, int n, String homePurpose) 
    {
		//HousingCustomerAgent p = new HousingCustomerAgent(name);
    	HousingCustomer hc = c;
    	c.setPurpose(homePurpose);
		HousingCustomerGui g = new HousingCustomerGui(hc, gui, n);
		gui.housingAnimationPanel.addGui(g);
		hc.setGui(g);
		g.setAction();
        landlord.addCustomer(hc);
        hc.setLandlord(landlord);
    	//p.setAnimationPanel(gui.cityAnimationPanel);
		tenants.add(hc);
		System.out.println("Entry in tenants has name: " + tenants.get(0).getName());
		//tenant.startThread();
    }
    
    public void addWorker(HousingWorker w) 
    {
		HousingWorkerGui g = new HousingWorkerGui(w, gui);
		gui.housingAnimationPanel.addGui(g);
		w.setGui(g);
        landlord.addWorker(w);
        w.setLandlord(landlord);
    	//p.setAnimationPanel(gui.cityAnimationPanel);
		workers.add(w);
    }
    
    public void addLandlord(LandlordRole l2) {
    	landlord = l2;
    }
    
    public void pause()
    {
    
    }
    
    public void resume()
    {

    }

    public void startThreads()
    {

    }
}
