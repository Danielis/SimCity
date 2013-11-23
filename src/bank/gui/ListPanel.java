package bank.gui;

import bank.BankCustomerRole;
import bank.HostAgent;
import bank.TellerAgent;

import javax.swing.*;

import roles.Role;
import bank.interfaces.BankCustomer;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {
    
    //CUSTOMER STUFF
    public JScrollPane customerPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewForCustomer = new JPanel();
    private JPanel topPart_customer = new JPanel();
    private JPanel bottomPart_customer = new JPanel();
    private List<JButton> listForCustomer = new ArrayList<JButton>();
    private JButton addCustomerButton = new JButton("Add");
    private JTextField nameFieldForCustomer = new JTextField("");
    public JCheckBox customerHungryCheckBox = new JCheckBox("Make Hungry");
    private BankCustomer currentCustomer;
    
    //WAITER STUFF
    public JScrollPane waiterPane = 
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewForWaiter = new JPanel();
    private JPanel topPart_waiter = new JPanel();
    private JPanel bottomPart_waiter = new JPanel();
    private List<JButton> listForWaiter = new ArrayList<JButton>();
    private JButton addWaiterButton = new JButton("Add");
    private JTextField nameFieldForWaiter = new JTextField("");
    private JCheckBox waiterBreakCheckBox = new JCheckBox("Set Breaks Below");
    private TellerAgent currentWaiter;
    
    private BankCustomerRole lastCustomerClicked;
    private TellerAgent lastWaiterClicked;

    //GENERAL STUFF
    private BankPanel restPanel;
    String type;
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(BankPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        //setLayout(new GridLayout(0,1,1,1));
        topPart_customer.setLayout(new BorderLayout());
        topPart_waiter.setLayout(new BorderLayout());
        bottomPart_customer.setLayout(new BorderLayout());
        bottomPart_waiter.setLayout(new BorderLayout());
        
        setLayout(new BorderLayout());
        JLabel name = new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>");
        name.setAlignmentY(CENTER_ALIGNMENT);
        
        if (type == "Customers"){
        	topPart_customer.add(name, BorderLayout.NORTH);
            addCustomerButton.addActionListener(this);
            topPart_customer.add(nameFieldForCustomer, BorderLayout.CENTER);
            customerHungryCheckBox.addActionListener(this);
           topPart_customer.add(customerHungryCheckBox, BorderLayout.SOUTH);
            customerHungryCheckBox.setMinimumSize(new Dimension(250,100));
            viewForCustomer.setLayout(new BoxLayout((Container) viewForCustomer, BoxLayout.Y_AXIS));
            customerPane.setViewportView(viewForCustomer);
            bottomPart_customer.add(addCustomerButton, BorderLayout.NORTH);
            bottomPart_customer.add(customerPane, BorderLayout.CENTER);
            add(topPart_customer, BorderLayout.NORTH);
            add(bottomPart_customer, BorderLayout.CENTER);
        }
        
        if (type == "Tellers"){
        	topPart_waiter.add(name, BorderLayout.NORTH);
        	addWaiterButton.addActionListener(this);  
        	topPart_waiter.add(nameFieldForWaiter, BorderLayout.CENTER);
        	//waiterBreakCheckBox.setEnabled(false);
        	//topPart_waiter.add(waiterBreakCheckBox, BorderLayout.SOUTH);
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
        if (e.getSource() == addCustomerButton) 
        {
           // addCustomer(nameFieldForCustomer.getText());
        }
        else if (e.getSource() == addWaiterButton)
        {
        	addTeller(nameFieldForWaiter.getText());
        }
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
        			restPanel.showWaiterInfo(temp2.getText());
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
    
    public void addTeller(String name) {
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
            restPanel.addTeller(name);
            restPanel.showWaiterInfo(name);
            validate();
        }
    }
    
    public void addCustomer(BankCustomer customer) {
        if (customer != null) {
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
            restPanel.addCustomer(customer);
            restPanel.showCustomerInfo(customer.getName());
            validate();
           
        }
    }
    
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
           // restPanel.addCustomer(name);
            restPanel.showCustomerInfo(name);
            validate();
           
        }
    }
    
    public void updateCustomerInfoPanel(BankCustomerRole person) {
    	this.lastCustomerClicked = person;
        customerHungryCheckBox.setVisible(true);
        currentCustomer = person;
        BankCustomerRole customer = person;
        customerHungryCheckBox.setText("Hungry?");
        customerHungryCheckBox.setSelected(customer.getGui().isHungry());
        customerHungryCheckBox.setEnabled(!customer.getGui().isHungry());
    }
    public void updateCustomerPanel()
    {
        customerHungryCheckBox.setSelected(lastCustomerClicked.getGui().isHungry());
        customerHungryCheckBox.setEnabled(!lastCustomerClicked.getGui().isHungry());
    }
    public void updateWaiterInfoPanel(TellerAgent person)
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
    
    public void updateCustomer(BankCustomer temp)
    {
   
    	currentCustomer = temp;
        	if (customerHungryCheckBox.isSelected())
        	{
        		customerHungryCheckBox.setSelected(false);
        		BankCustomer c = currentCustomer;
        		c.getGui().setAction();
        	}
    }
    
    
    public void updateWaiter(TellerAgent person)
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
