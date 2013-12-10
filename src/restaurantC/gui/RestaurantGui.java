package restaurantC.gui;

import javax.swing.*;

import restaurantC.CustomerRole;
import restaurantC.RestaurantC;
import restaurantC.WaiterRole;

import java.awt.*;
import java.awt.event.*;

import logging.TrackerGui;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public RestaurantPanel restPanel = new RestaurantPanel(this);
    
    //associated restaurant class
    public RestaurantC rest;
    
    
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    //tracking gui for printout statements
    private TrackerGui trackingWindow;
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	//sets window size
        int WINDOWX = 1200;
        int WINDOWY = 700;
    	setBounds(50, 50, WINDOWX, WINDOWY);
     	
        Dimension restDim = new Dimension(500, 700);
        restPanel.setBorder(BorderFactory.createTitledBorder("RestPanel"));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, BorderLayout.EAST);
        
        //add the animation panel
        add(animationPanel, BorderLayout.CENTER);
    }
    
    public void setTrackerGui(TrackerGui t) {
    	trackingWindow = t;
    }
    
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        currentPerson = person;

        if (person instanceof CustomerRole) {
            CustomerRole customer = (CustomerRole) person;
        }
        else if(person instanceof WaiterRole) {
        	WaiterRole waiter = (WaiterRole) person;
        }
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {

    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerRole c) {
        if (currentPerson instanceof CustomerRole) {
            CustomerRole cust = (CustomerRole) currentPerson;
            if (c.equals(cust)) {
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("Chris's Restaurant Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	public void setRestaurant(RestaurantC r) {
		rest = r;
        animationPanel.setRest(rest);
	}
}
