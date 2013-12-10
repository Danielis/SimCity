package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.Restaurant;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import restaurant.roles.*;
/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {
	
	Restaurant r = null;
    
    //CUSTOMER STUFF
    public JScrollPane customerPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewForCustomer = new JPanel();
    private JPanel topPart_customer = new JPanel();
    private JPanel bottomPart_customer = new JPanel();
    private List<JButton> listForCustomer = new ArrayList<JButton>();
    //private JButton addCustomerButton = new JButton("Add");
    //private JTextField nameFieldForCustomer = new JTextField("Enter Customer Here");
    //public JCheckBox customerHungryCheckBox = new JCheckBox("Make Hungry");
    private Customer currentCustomer;
    
    //WAITER STUFF
    public JScrollPane waiterPane = 
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewForWaiter = new JPanel();
    private JPanel topPart_waiter = new JPanel();
    private JPanel bottomPart_waiter = new JPanel();
    private List<JButton> listForWaiter = new ArrayList<JButton>();
    private JButton addWaiterButton = new JButton("Add");
    private JTextField nameFieldForWaiter = new JTextField("Enter Waiter Here");
    private JCheckBox waiterBreakCheckBox = new JCheckBox("Set Breaks Below");
    private Waiter currentWaiter;
    
    private Customer lastCustomerClicked;
    private Waiter lastWaiterClicked;

    //GENERAL STUFF
    private RestaurantPanel restPanel;
    String type;
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        //setLayout(new GridLayout(0,1,1,1));
        topPart_customer.setLayout(new BorderLayout());
        topPart_waiter.setLayout(new BorderLayout());
        bottomPart_customer.setLayout(new BorderLayout());
        bottomPart_waiter.setLayout(new BorderLayout());
        
        setLayout(new BorderLayout());
        JLabel name = new JLabel("Current Restaurant Customers");
        name.setAlignmentY(CENTER_ALIGNMENT);
        
        if (type == "Customers"){
        	topPart_customer.add(name, BorderLayout.NORTH);
            //addCustomerButton.addActionListener(this);
            //topPart_customer.add(nameFieldForCustomer, BorderLayout.CENTER);
            //customerHungryCheckBox.addActionListener(this);
            //topPart_customer.add(customerHungryCheckBox, BorderLayout.SOUTH);
            //customerHungryCheckBox.setMinimumSize(new Dimension(250,100));
            viewForCustomer.setLayout(new BoxLayout((Container) viewForCustomer, BoxLayout.Y_AXIS));
            customerPane.setViewportView(viewForCustomer);
            //bottomPart_customer.add(addCustomerButton, BorderLayout.NORTH);
            bottomPart_customer.add(customerPane, BorderLayout.CENTER);
            add(topPart_customer, BorderLayout.NORTH);
            add(bottomPart_customer, BorderLayout.CENTER);
        }
        
        if (type == "Waiters"){
        	topPart_waiter.add(name, BorderLayout.NORTH);
        	addWaiterButton.addActionListener(this);  
        	topPart_waiter.add(nameFieldForWaiter, BorderLayout.CENTER);
        	waiterBreakCheckBox.setEnabled(false);
        	topPart_waiter.add(waiterBreakCheckBox, BorderLayout.SOUTH);
            viewForWaiter.setLayout(new BoxLayout((Container) viewForWaiter, BoxLayout.Y_AXIS));
            waiterPane.setViewportView(viewForWaiter);
            bottomPart_waiter.add(addWaiterButton, BorderLayout.NORTH);
            bottomPart_waiter.add(waiterPane, BorderLayout.CENTER);
            add(topPart_waiter, BorderLayout.NORTH);
            add(bottomPart_waiter, BorderLayout.CENTER);
        }

    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        //if (e.getSource() == addCustomerButton) 
        //{
            //addCustomer(nameFieldForCustomer.getText());
        //}
        //else if (e.getSource() == addWaiterButton)
        //{
        	//addWaiter(nameFieldForWaiter.getText());
        //}
    	if(false){}
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp1:listForCustomer){
                if (e.getSource() == temp1)
                {
                    restPanel.showCustomerInfo(temp1.getText());
                }
            }
        	for (JButton temp2:listForWaiter){
        		if (e.getSource() == temp2)
        		{
        			//restPanel.showWaiterInfo(temp2.getText());
        		}
        	}
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    /*
    public void addWaiter(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = waiterPane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 10));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            listForWaiter.add(button);
            viewForWaiter.add(button);
            restPanel.addWaiter(name);
            restPanel.showWaiterInfo(name);
            validate();
        }
    }*/
    /*
    public void addCustomer(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = customerPane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 10));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            listForCustomer.add(button);
            viewForCustomer.add(button);
            restPanel.addCustomer(name);
            restPanel.showCustomerInfo(name);
            validate();
           
        }
    }*/
    
    public void addCustomer(CustomerRole customer, Restaurant r) {
        if (customer.getName() != null) {
            JButton button = new JButton(customer.getName());
            button.setBackground(Color.white);

            Dimension paneSize = customerPane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 10));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            listForCustomer.add(button);
            viewForCustomer.add(button);
            restPanel.addCustomer(customer, r);
            restPanel.showCustomerInfo(customer.getName());
            validate();
           
        }
    }
    
    public void removeCustomer(Customer customer)
    {
    	for(JButton b : listForCustomer)
    	{
    		if(b.getText() == customer.getName())
    		{
    			listForCustomer.remove(b);
    			viewForCustomer.remove(b);
    		}
    	}
    	restPanel.removeCustomer(customer);
    	validate();
    }
    
    
    public void updateCustomerInfoPanel(Customer person) {
    	this.lastCustomerClicked = person;
        //customerHungryCheckBox.setVisible(true);
        currentCustomer = person;
        Customer customer = person;
        //customerHungryCheckBox.setText("Hungry?");
        //customerHungryCheckBox.setSelected(customer.getGui().isHungry());
        //customerHungryCheckBox.setEnabled(!customer.getGui().isHungry());
    }
    public void updateCustomerPanel()
    {
        //customerHungryCheckBox.setSelected(lastCustomerClicked.getGui().isHungry());
        //customerHungryCheckBox.setEnabled(!lastCustomerClicked.getGui().isHungry());
    }
    public void updateWaiterInfoPanel(Waiter person)
    {
    	/*
    	this.lastWaiterClicked = person;
    	waiterBreakCheckBox.setVisible(true);
        currentWaiter = person;
        WaiterAgent waiter = person;
        customerHungryCheckBox.setText("On Break");
        waiterBreakCheckBox.setSelected(waiter.isOnBreak);
        */
    }
    
    public void updateWaiterPanel()
    {
    	//this.restPanel.refresh();
    }
    
    public void updateCustomer(Customer person)
    {
   
    	currentCustomer = person;
    	person.getGui().setHungry();
        	//if (customerHungryCheckBox.isSelected())
        	//{
        	//	customerHungryCheckBox.setSelected(false);
        	//	Customer c = currentCustomer;
        	//	c.getGui().setHungry();
        	//}
    }
    
    
    public void updateWaiter(Waiter person)
    {
    	
    	/*currentWaiter = person;
    	
		if (waiterBreakCheckBox.isSelected() == true)
		{
			waiterBreakCheckBox.setSelected(true);
			WaiterAgent w = currentWaiter;
    		w.getGui().AskForBreak();
		}
		else if (waiterBreakCheckBox.isSelected() == false)
		{
			waiterBreakCheckBox.setSelected(false);
			WaiterAgent w = currentWaiter;
			w.isOnBreak = false;
		}*/
    }
}
