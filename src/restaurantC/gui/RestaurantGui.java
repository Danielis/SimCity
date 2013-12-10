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
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    //associated restaurant class
    public RestaurantC rest;
    
    //panel for creation of a customer
    private JPanel myPanel;
    
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
        
        //add the animation panel
        add(animationPanel);
 
    	//use a gridlayout.  2 rows, 4 columns, 5 pixel spacing
    	setLayout(new GridLayout(2,2,5,5));
    	
    	//adding a panel for customer creation
    	Dimension myDim = new Dimension((int) (WINDOWX * .3), (int) (WINDOWY * .25));
        myPanel = new JPanel();
        myPanel.setPreferredSize(myDim);
        myPanel.setMinimumSize(myDim);
        myPanel.setMaximumSize(myDim);
        myPanel.setBorder(BorderFactory.createTitledBorder("Add"));
        myPanel.setLayout(new BorderLayout());
        myPanel.setVisible(false);
        add(myPanel);
        
    	//--------------stuff I changed-----------------------------------
        Dimension restDim = new Dimension((int)(WINDOWX*.5), (int)(WINDOWY * .5));
        restPanel.setBorder(BorderFactory.createTitledBorder("RestPanel"));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension((int) (WINDOWX), (int) (WINDOWY * .15));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        //state hungry checkbox
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        //sets the layout of the info panel
        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel);
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
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerRole) {
            CustomerRole customer = (CustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        else if(person instanceof WaiterRole) {
        	WaiterRole waiter = (WaiterRole) person;
        	stateCB.setText("Break?");
        	stateCB.setSelected(waiter.wantToGoOnBreak);
        	stateCB.setEnabled(!waiter.wantToGoOnBreak);
        	infoLabel.setText("<html><pre>     Name: " + waiter.name + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB && stateCB.getText().equals("Hungry?")) {
            if (currentPerson instanceof CustomerRole) {
                CustomerRole c = (CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        else if(e.getSource() == stateCB && stateCB.getText().equals("Break?")) {
        	 if (currentPerson instanceof WaiterRole) {
                 WaiterRole w = (WaiterRole) currentPerson;
                 w.wantToGoOnBreak();
                stateCB.setEnabled(false);
             }
        }
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
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	public void setRestaurant(RestaurantC r) {
		rest = r;
        animationPanel.setRest(rest);
	}
}
