package restaurantA.gui;

import restaurantA.CustomerAgent;
import restaurantA.RestaurantA;
import restaurantA.WaiterAgent;
import restaurantA.interfaces.Customer;

import javax.swing.*;

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
	private boolean paused;
	//JFrame animationFrame = new JFrame("Restaurant Animation");
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
    public JButton pauseButton;
    public JButton orderButton;
    public JButton addMarket;
    private JCheckBox stateCB;//part of infoLabel
    public RestaurantA rest;
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	paused = false;
        int WINDOWX = 600;
        int WINDOWY = 500;
    	
    	setBounds(50, 50, WINDOWX + 650, WINDOWY + 170);
        setLayout(new BorderLayout());
       

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .4));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, BorderLayout.EAST);
        
        addMarket = new JButton();
        addMarket.setVisible(true);
        addMarket.setText("Add Market");
        addMarket.addActionListener(this);
        Dimension buttonDim = new Dimension(150, 50);
        addMarket.setPreferredSize(buttonDim);
        addMarket.setMinimumSize(buttonDim);
        addMarket.setMaximumSize(buttonDim);
       // add(addMarket);
        
        pauseButton = new JButton();
        pauseButton.setVisible(true);
        pauseButton.setText("p a u s e");
        pauseButton.addActionListener(this);
        pauseButton.setPreferredSize(buttonDim);
        pauseButton.setMinimumSize(buttonDim);
        pauseButton.setMaximumSize(buttonDim);
        //add(pauseButton);
        
        orderButton = new JButton();
        orderButton.setVisible(true);
        orderButton.setText("Order Food");
        orderButton.addActionListener(this);
        orderButton.setPreferredSize(buttonDim);
        orderButton.setMinimumSize(buttonDim);
        orderButton.setMaximumSize(buttonDim);
       // add(orderButton);
        
        Dimension animDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        add(animationPanel, BorderLayout.WEST);
   
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(450, (int) (100));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Selected Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        restPanel.restLabel.add(infoPanel);
     
        
        
    
        
       
        
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

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person;
            stateCB.setText("Break?");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.getGui().isOnBreak());
          //Is customer hungry? Hack. Should ask customerGui
           stateCB.setEnabled(!waiter.getGui().isOnBreak());
          // Hack. Should ask customerGui 
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            if (currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson;
                w.getGui().WantsBreak();
                stateCB.setEnabled(false);
            }
        }
        
        else if (e.getSource() == pauseButton){
//        	if(paused) {
//        		paused = false;
//        		pauseButton.setText("p a u s e");
//        		restPanel.host.unpause();
//            	for (WaiterAgent w : restPanel.host.waiters) {
//            		w.unpause();
//            		w.cook.unpause();
//            	}
//            	for (CustomerAgent c : restPanel.customers) {
//            		c.unpause();
//            	}
//            	System.out.println("resume button pressed");
//        	} else {
//        		paused = true;
//        		pauseButton.setText("r e s u m e");
//        		restPanel.host.pause();
// 
//            	for (WaiterAgent w : restPanel.host.waiters) {
//            		w.pause();
//            		w.cook.pause();
//            	}
//            	for (CustomerAgent c : restPanel.customers) {
//            		c.pause();
//            	}
//            	System.out.println("pause button pressed");
//        	}	
        }
        else if (e.getSource() == addMarket){
        	restPanel.cook.addMarket();
        }
        else if (e.getSource() == orderButton){
        	restPanel.cook.CheckStock();
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Customer c) {
        if (currentPerson instanceof CustomerAgent) {
            Customer cust = (Customer) currentPerson;
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
	public void setTrackerGui(TrackerGui trackingWindow) {
		// TODO Auto-generated method stub
		
	}
	public void setRestaurant(RestaurantA r) {
		rest = r;
        animationPanel.setRest(rest);
	}
}
