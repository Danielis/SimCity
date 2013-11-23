package market.gui;

import market.MarketCustomerAgent;
import market.MarketTellerAgent;
import market.MarketHostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
	JFrame animationFrame = new JFrame("Restaurant Animation");
	MarketAnimationPanel animationPanel = new MarketAnimationPanel();
	JPanel RestaurantPortion = new JPanel();
	
 
    public MarketPanel restPanel = new MarketPanel(this);
    
    /* customerInformationPanel holds information about the clicked customer, if there is one*/
    private JPanel customerInformationPanel;
    private JPanel waiterInformationPanel;
    private JPanel InformationPanel;
    private JPanel buttonPanel;
    
    private JLabel infoCustomerLabel;
    private JLabel infoWaiterLabel;

    private JCheckBox customerStateCheckBox;
    private JTextField amountInput = new JTextField("");
    
    String[] transactions = { "New Account", "Deposit", "Withdraw", "New Loan", "Pay Loan" };
    JComboBox transactionList = new JComboBox(transactions);
    //private JCheckBox waiterBreakCheckBox;
    private JButton waiterON = new JButton("Go On Break");
    private JButton waiterOFF = new JButton("Go Off Break");

    private MarketCustomerAgent currentCustomer;
    private MarketTellerAgent currentWaiter;

    private JButton pauseButton;
    private JButton refreshButton;
    private JPanel ButtonPanel;

    
    Boolean isPaused = false;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public MarketGui() {
        int WINDOWX = 600;
        int WINDOWY = 500;
        
        ButtonPanel = new JPanel();
       
        RestaurantPortion.setLayout(new BorderLayout());
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBounds(25, 25, WINDOWX+650, WINDOWY+170);
    	setVisible(true);

        setLayout(new BorderLayout());
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .86));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(this);
        refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(this);
        
//CUSTOMER PANEL INFORMATION
        Dimension infoDimCustomer = new Dimension(WINDOWX, (int) (WINDOWY * .12));
        customerInformationPanel = new JPanel();
        customerInformationPanel.setPreferredSize(infoDimCustomer);
        customerInformationPanel.setMinimumSize(infoDimCustomer);
        customerInformationPanel.setMaximumSize(infoDimCustomer);
        customerInformationPanel.setBorder(BorderFactory.createTitledBorder("Customers"));
       
        customerStateCheckBox = new JCheckBox();
        customerStateCheckBox.setVisible(false);
        customerStateCheckBox.addActionListener(this);
        
        
      
        
        customerInformationPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoCustomerLabel = new JLabel(); 
        infoCustomerLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        customerInformationPanel.add(infoCustomerLabel);
        customerInformationPanel.add(customerStateCheckBox);
        customerInformationPanel.add(amountInput);
        customerInformationPanel.add(transactionList);
        
//WAITER PANEL INFORMATION
        Dimension infoDimWaiter = new Dimension(WINDOWX, (int) (WINDOWY * .12));
        waiterInformationPanel = new JPanel();
        waiterInformationPanel.setPreferredSize(infoDimWaiter);
        waiterInformationPanel.setMinimumSize(infoDimWaiter);
        waiterInformationPanel.setMaximumSize(infoDimWaiter);
        waiterInformationPanel.setBorder(BorderFactory.createTitledBorder("Tellers"));

        waiterON.addActionListener(this);
        waiterOFF.addActionListener(this);
        
        waiterInformationPanel.setLayout(new GridLayout(1, 2, 30, 0));

        infoWaiterLabel = new JLabel();
        infoWaiterLabel.setText("<html><pre><i>Click Add to make waiters</i></pre></html>");
        waiterInformationPanel.add(infoWaiterLabel);
        waiterON.setVisible(false);
        waiterOFF.setVisible(false);
        waiterInformationPanel.add(waiterON);
        waiterInformationPanel.add(waiterOFF);
        RestaurantPortion.add(restPanel, BorderLayout.NORTH);
        InformationPanel.add(customerInformationPanel, BorderLayout.NORTH);
        ButtonPanel.setLayout(new BorderLayout());
        InformationPanel.add(ButtonPanel, BorderLayout.SOUTH);
        InformationPanel.add(waiterInformationPanel, BorderLayout.CENTER);
        RestaurantPortion.add(InformationPanel, BorderLayout.CENTER);
        buttonPanel.add(pauseButton, BorderLayout.CENTER);
        buttonPanel.add(refreshButton, BorderLayout.EAST);
        RestaurantPortion.add(buttonPanel, BorderLayout.SOUTH);

        add(animationPanel, BorderLayout.CENTER);
        add(RestaurantPortion, BorderLayout.EAST);

    }
    /**
     * updateCustomerInformationPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateCustomerInformationPanel(MarketCustomerAgent person) {
        customerStateCheckBox.setVisible(true);
        currentCustomer = person;
        MarketCustomerAgent customer = person;
        customerStateCheckBox.setText("Start?");
        customerStateCheckBox.setSelected(customer.getGui().isHungry());
        customerStateCheckBox.setEnabled(!customer.getGui().isHungry());
        transactionList.setSelectedIndex(0);
        transactionList.addActionListener(this);
        
        infoCustomerLabel.setText(
           "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        customerInformationPanel.validate();
    }
    public void updateLastCustomer()
    {
    	if (currentCustomer != null)
    	{
	        customerStateCheckBox.setSelected(currentCustomer.getGui().isHungry());
	        customerStateCheckBox.setEnabled(!currentCustomer.getGui().isHungry());
	        customerInformationPanel.validate();
    	}
    }
    public void updateLastWaiter()
    {
    	//empty
    }
    
    public void updateWaiterInformationPanel(MarketTellerAgent person) {
    	waiterON.setVisible(true);
    	waiterOFF.setVisible(true);
        currentWaiter = person;
        MarketTellerAgent waiter = person;
    	infoWaiterLabel.setText(
                "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        waiterInformationPanel.validate();
    }
    
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == customerStateCheckBox) 
        {
            MarketCustomerAgent c = (MarketCustomerAgent) currentCustomer;
            c.getGui().setAction(transactionList.getSelectedItem().toString(), amountInput.getText());
            customerStateCheckBox.setEnabled(false);
        }
//        if (e.getSource() == transactionList)
//        {
//        	restPanel.refresh();
//        }
        if (e.getSource() == waiterON)
        {
        	MarketTellerAgent w = currentWaiter;
        	w.getGui().AskForBreak();
        }
        if (e.getSource() == waiterOFF)
        {
        	MarketTellerAgent w = currentWaiter;
        	w.getGui().AskGoOffBreak();
        }
      
        if (e.getSource() == pauseButton)
        {
        	if (isPaused)
        	{
        		pauseButton.setText("PAUSE");
        		restPanel.resume();
        		isPaused = false;
        	}
        	else if(isPaused == false)
        	{
        		pauseButton.setText("RESUME");
        		restPanel.pause();
        		isPaused = true;
        	}
        }
        if (e.getSource() == refreshButton)
        {
        	restPanel.refresh();
        }
        
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(MarketCustomerAgent c) {
        MarketCustomerAgent cust = currentCustomer;
        if (c.equals(cust)) 
        {
            customerStateCheckBox.setEnabled(true);
            customerStateCheckBox.setSelected(false);
        }
}

    /**
     * Main routine to get gui started
     */
//    public static void main(String[] args) {
//        RestaurantGui gui = new RestaurantGui();
//        gui.setTitle("Norman's Restaurant");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
