package city.guis;



import restaurant.CustomerAgent;
//import bank.gui.BankGui;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class CityGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
	JFrame animationFrame = new JFrame("Restaurant Animation");
	CityAnimationPanel cityAnimationPanel = new CityAnimationPanel();
	JPanel RestaurantPortion = new JPanel();
 
    private CityPanel cityPanel = new CityPanel(this);
    
    /* personInformationPanel holds information about the clicked customer, if there is one*/
    private JPanel personInformationPanel;
    private JPanel InformationPanel;
    private JPanel buttonPanel;
    
    private JLabel infoCustomerLabel;
    private JLabel infoWaiterLabel;

    private JCheckBox personHungryCheckBox;
    //private JCheckBox waiterBreakCheckBox;
    private JButton waiterON = new JButton("Go On Break");
    private JButton waiterOFF = new JButton("Go Off Break");

    private PersonAgent currentPerson;
    private WaiterAgent currentWaiter;

    private JButton pauseButton;
    private JButton refreshButton;
    private JPanel ButtonPanel;
    private JButton MrKrabsButton;
    private ImageIcon MrKrabs;
    private JButton RamsayButton;
    private ImageIcon Ramsay;
    
    Boolean isPaused = false;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CityGui() {
        int WINDOWX = 500;
        int WINDOWY = 500;
        
        ButtonPanel = new JPanel();
        MrKrabs = new ImageIcon(getClass().getResource("/resources/MrKrabs.png"));
        Ramsay = new ImageIcon(getClass().getResource("/resources/Ramsay.png"));
        
        RestaurantPortion.setLayout(new BorderLayout());
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBounds(25, 25, WINDOWX+700, WINDOWY+150);
    	setVisible(true);

        setLayout(new BorderLayout());
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .86));
        cityPanel.setPreferredSize(restDim);
        cityPanel.setMinimumSize(restDim);
        cityPanel.setMaximumSize(restDim);
        
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(this);
        refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(this);
        
//CUSTOMER PANEL INFORMATION
        Dimension infoDimCustomer = new Dimension(WINDOWX, (int) (WINDOWY * .30));
        personInformationPanel = new JPanel();
        personInformationPanel.setPreferredSize(infoDimCustomer);
        personInformationPanel.setMinimumSize(infoDimCustomer);
        personInformationPanel.setMaximumSize(infoDimCustomer);
        personInformationPanel.setBorder(BorderFactory.createTitledBorder("Customers"));
       
        personHungryCheckBox = new JCheckBox();
        personHungryCheckBox.setVisible(false);
        personHungryCheckBox.addActionListener(this);
        
        personInformationPanel.setLayout(new BorderLayout());
        
        infoCustomerLabel = new JLabel(); 
        infoCustomerLabel.setText("<html><p><p>Click Add to make customers</p></p></html>");
        personInformationPanel.add(infoCustomerLabel, BorderLayout.NORTH);
        personInformationPanel.add(personHungryCheckBox, BorderLayout.SOUTH);
        
//WAITER PANEL INFORMATION
        Dimension infoDimWaiter = new Dimension(WINDOWX, (int) (WINDOWY * .12));

        waiterON.addActionListener(this);
        waiterOFF.addActionListener(this);
        
        infoWaiterLabel = new JLabel();
        infoWaiterLabel.setText("<html><pre><i>Click Add to make waiters</i></pre></html>");
        waiterON.setVisible(false);
        waiterOFF.setVisible(false);
        RestaurantPortion.add(cityPanel, BorderLayout.NORTH);
        InformationPanel.add(personInformationPanel, BorderLayout.NORTH);
        RestaurantPortion.add(InformationPanel, BorderLayout.CENTER);
        buttonPanel.add(pauseButton, BorderLayout.CENTER);
        buttonPanel.add(refreshButton, BorderLayout.EAST);
        RestaurantPortion.add(buttonPanel, BorderLayout.SOUTH);

        add(cityAnimationPanel, BorderLayout.CENTER);
        add(RestaurantPortion, BorderLayout.EAST);

    }
    /**
     * updatepersonInformationPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param temp customer (or waiter) object
     */
    public void updatePersonInformationPanel(PersonAgent temp) {
        personHungryCheckBox.setVisible(true);
        currentPerson = temp;
        PersonAgent person = temp;
        personHungryCheckBox.setText("Hungry?");
        personHungryCheckBox.setSelected(person.getGui().isHungry());
        personHungryCheckBox.setEnabled(!person.getGui().isHungry());
        infoCustomerLabel.setText(
           "<html><pre>     Name: " + person.getName() + " </pre></html>");
        personInformationPanel.validate();
    }
    public void updateLastCustomer()
    {
    	if (currentPerson != null)
    	{
	        personHungryCheckBox.setSelected(currentPerson.getGui().isHungry());
	        personHungryCheckBox.setEnabled(!currentPerson.getGui().isHungry());
	        personInformationPanel.validate();
    	}
    }
    public void updateLastWaiter()
    {
    	//empty
    }
    
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == personHungryCheckBox) 
        {
            PersonAgent c = (PersonAgent) currentPerson;
            c.getGui().setHungry();
            personHungryCheckBox.setEnabled(false);
        }
        if (e.getSource() == waiterON)
        {
        	WaiterAgent w = currentWaiter;
        	w.getGui().AskForBreak();
        }
        if (e.getSource() == waiterOFF)
        {
        	WaiterAgent w = currentWaiter;
        	w.getGui().AskGoOffBreak();
        }
        if (e.getSource() == MrKrabsButton)
        {
        	this.cityPanel.cashier.setBalance(0.0f);
        	System.out.println("Mr. Krabs has collected all his cash. The cashier is out of money.");
        }
        if (e.getSource() == RamsayButton)
        {
        	System.out.println("Gordon Ramsay threw a fit and threw all the food away. Inventory is now 0.");
        	for (int i = 0; i < 4; i++)
        		this.cityPanel.cook.inventory.get(i).amount = 0;
        }
        if (e.getSource() == pauseButton)
        {
        	if (isPaused)
        	{
        		pauseButton.setText("PAUSE");
        		cityPanel.resume();
        		isPaused = false;
        	}
        	else if(isPaused == false)
        	{
        		pauseButton.setText("RESUME");
        		cityPanel.pause();
        		isPaused = true;
        	}
        }
        if (e.getSource() == refreshButton)
        {
        	cityPanel.refresh();
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(PersonAgent p) {
        PersonAgent per = currentPerson;
        if (p.equals(per)) 
        {
            personHungryCheckBox.setEnabled(true);
            personHungryCheckBox.setSelected(false);
        }
}

    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {    
        RestaurantGui gui2 = new RestaurantGui();
        gui2.setTitle("Norman's Restaurant");
        gui2.setVisible(true);
        gui2.setResizable(false);
        gui2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
    	
        CityGui gui = new CityGui();
        gui.cityPanel.setRestPanel(gui2.restPanel);
        gui.setTitle("Team 05's City");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*        
        BankGui gui3 = new BankGui();
        gui3.setTitle("Aleena's Bank");
        gui3.setVisible(true);
        gui3.setResizable(false);
        gui3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   */
    }
}
