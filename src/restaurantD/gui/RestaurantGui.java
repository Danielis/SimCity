package restaurantD.gui;

import restaurantD.CustomerAgent;
import restaurantD.MarketAgent;
import restaurantD.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    private JPanel leftBox = new JPanel();
    private JPanel rightBox = new JPanel();
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	int WINDOWX = 500;
        int WINDOWY = 450;
        
        //animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel); 
        Dimension rboxDim = new Dimension(WINDOWX, WINDOWY);
        rightBox.setLayout(new GridLayout(0,1));
        rightBox.setPreferredSize(rboxDim);
        rightBox.setMinimumSize(rboxDim);
        rightBox.setMaximumSize(rboxDim);
        rightBox.add(animationPanel);
        
    	setBounds(50, 50, WINDOWX*2, WINDOWY);
    	setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));
    	
        leftBox.setLayout(new GridLayout(0,2));
        Dimension lboxDim = new Dimension(WINDOWX, WINDOWY);
        leftBox.setLayout(new GridLayout(0,1));
        leftBox.setPreferredSize(lboxDim);
        leftBox.setMinimumSize(lboxDim);
        leftBox.setMaximumSize(lboxDim);
        
        
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        leftBox.add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>There are no Customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        leftBox.add(infoPanel);
    
        add(leftBox);
        add(rightBox);
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
               "<html><pre>Name: " + customer.getName() + " Money: " + customer.getMoney() + "</pre></html>");
        }
        if (person instanceof WaiterAgent) {
            WaiterAgent waiter = (WaiterAgent) person;
            stateCB.setText("Break?");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.isOnBreak());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!waiter.isOnBreak());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        if (person instanceof MarketAgent) {
            MarketAgent market = (MarketAgent) person;
            stateCB.setVisible(false);
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>Name: " + market.getName() + "<br>ST: " + market.getSteak() + " CH: " + market.getChicken() + " SA: " + market.getSalad() + " PI: " + market.getPizza() + "<br>Cook: "
                 + market.getCook().getName()+ "<br>ST: " + market.getCook().getSteak() + " CH: " + market.getCook().getChicken() + " SA: " + market.getCook().getSalad() + " PI: " + market.getCook().getPizza() + "<br>Debt: " + market.getDebt() +"</pre></html>");
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
                if(stateCB.isSelected())
                	w.getHost().iWouldLikeToGoOnBreak(w);
                else
                	w.getHost().iAmOffBreak(w);
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
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
}
