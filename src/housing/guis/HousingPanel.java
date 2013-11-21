package housing.guis;

import restaurant.CustomerAgent;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.MarketAgent;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantGui;
import housing.HousingCustomerAgent;
import housing.HousingWorkerAgent;
import housing.LandlordAgent;
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
	private LandlordAgent landlord = new LandlordAgent();
	private HousingCustomerAgent tenant = new HousingCustomerAgent();
	private HousingWorkerAgent worker = new HousingWorkerAgent();
        
    private Vector<PersonAgent> people = new Vector<PersonAgent>();
    

    private JPanel restLabel = new JPanel();
    private HousingListPanel personPanel = new HousingListPanel(this, "People");
    private JPanel group = new JPanel();
        
    
    private HousingGui gui; //reference to main gui

    public HousingPanel(HousingGui gui) {
        this.gui = gui;
      
       // gui.cityAnimationPanel.addGui(hostGui);
        //gui.cityAnimationPanel.addGui(cookGui);
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(personPanel);

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
    public void showPersonInfo(String name)
    {
        for (int i = 0; i < people.size(); i++) {
        	
            PersonAgent temp = people.get(i);
            if (temp.getName() == name)
            {
                personPanel.updatePerson(temp);
                gui.updatePersonInformationPanel(temp);
                personPanel.updatePerson(temp);
            }
        }
    }
    
   

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
*/
    public void addPerson(String name) 
    {
		//PersonAgent p = new PersonAgent(name);	
 //   	PersonGui g = new PersonGui(p, gui);
   // 	gui.cityAnimationPanel.addGui(g);
    //	p.setGui(g);
    	//p.setAnimationPanel(gui.cityAnimationPanel);
    	//people.add(p);
    	//p.startThread();
    }
    
    public void pause()
    {
    
    }
    
    public void resume()
    {

    }

    public void startThreads()
    {
    	landlord.startThread();
    	tenant.startThread();
    	worker.startThread();
    	//gui.updateLastCustomer();
    	//gui.updateLastWaiter();
    }
}
