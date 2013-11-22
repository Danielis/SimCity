package housing.guis;



import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.PersonAgent;
import city.guis.CityGui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class HousingGui extends JFrame implements ActionListener {
	/* The GUI has two frames, the control frame (in variable gui) 
	 * and the animation frame, (in variable animationFrame within gui)
	 */

	JFrame animationFrame = new JFrame("Housing Animation");
	HousingAnimationPanel housingAnimationPanel = new HousingAnimationPanel();
	JPanel RestaurantPortion = new JPanel();

	private HousingPanel housingPanel = new HousingPanel(this);

	/* personInformationPanel holds information about the clicked customer, if there is one*/
	private JPanel tenantInformationPanel;
	private JPanel workerInformationPanel;
	private JPanel InformationPanel;
	private JPanel buttonPanel;

	private JLabel infoCustomerLabel;
	private JLabel infoWaiterLabel;

	private JCheckBox customerStateCheckBox;

	private PersonAgent currentPerson;
	private WaiterAgent currentWaiter;

	private JButton pauseButton;
	private JButton startButton;
	private JPanel ButtonPanel;
	private JButton rentButton;
	private JButton hungryButton;
	private JButton breakStuffButton;

	Boolean isPaused = false;
	
	//constructor
	public HousingGui() {
		//dimensions
		int WINDOWX = 600;
		int WINDOWY = 500;

		//button panel at bottom
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
		housingPanel.setPreferredSize(restDim);
		housingPanel.setMinimumSize(restDim);
		housingPanel.setMaximumSize(restDim);

		pauseButton = new JButton("PAUSE");
		pauseButton.addActionListener(this);
		startButton = new JButton("START");
		startButton.addActionListener(this);

		//CUSTOMER PANEL INFORMATION
		Dimension infoDimCustomer = new Dimension(WINDOWX, (int) (WINDOWY * .12));
		tenantInformationPanel = new JPanel();
		tenantInformationPanel.setPreferredSize(infoDimCustomer);
		tenantInformationPanel.setMinimumSize(infoDimCustomer);
		tenantInformationPanel.setMaximumSize(infoDimCustomer);
		tenantInformationPanel.setBorder(BorderFactory.createTitledBorder("Customers"));

		customerStateCheckBox = new JCheckBox();
		customerStateCheckBox.setVisible(false);
		customerStateCheckBox.addActionListener(this);

		tenantInformationPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoCustomerLabel = new JLabel(); 
		infoCustomerLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
		tenantInformationPanel.add(infoCustomerLabel);
		tenantInformationPanel.add(customerStateCheckBox);

		//WAITER PANEL INFORMATION
		Dimension infoDimWaiter = new Dimension(WINDOWX, (int) (WINDOWY * .12));
		workerInformationPanel = new JPanel();
		workerInformationPanel.setPreferredSize(infoDimWaiter);
		workerInformationPanel.setMinimumSize(infoDimWaiter);
		workerInformationPanel.setMaximumSize(infoDimWaiter);
		workerInformationPanel.setBorder(BorderFactory.createTitledBorder("Waiters"));


		workerInformationPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoWaiterLabel = new JLabel();
		infoWaiterLabel.setText("<html><pre><i>Click Add to make waiters</i></pre></html>");
		workerInformationPanel.add(infoWaiterLabel);
		RestaurantPortion.add(housingPanel, BorderLayout.NORTH);
		InformationPanel.add(tenantInformationPanel, BorderLayout.NORTH);
		rentButton = new JButton("Rent time!");
		breakStuffButton = new JButton("Break Stuff!");
		hungryButton = new JButton("Get Hungry");
		rentButton.addActionListener(this);
		breakStuffButton.addActionListener(this);
		hungryButton.addActionListener(this);
		ButtonPanel.setLayout(new BorderLayout());
		ButtonPanel.add(rentButton, BorderLayout.WEST);
		ButtonPanel.add(breakStuffButton, BorderLayout.EAST);
		ButtonPanel.add(hungryButton, BorderLayout.CENTER);
		InformationPanel.add(ButtonPanel, BorderLayout.SOUTH);
		InformationPanel.add(workerInformationPanel, BorderLayout.CENTER);
		RestaurantPortion.add(InformationPanel, BorderLayout.CENTER);
		buttonPanel.add(pauseButton, BorderLayout.CENTER);
		buttonPanel.add(startButton, BorderLayout.EAST);
		RestaurantPortion.add(buttonPanel, BorderLayout.SOUTH);

		add(housingAnimationPanel, BorderLayout.CENTER);
		add(RestaurantPortion, BorderLayout.EAST);

	}
	/**
	 * updatepersonInformationPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 *
	 * @param temp customer (or waiter) object
	 */
	public void updatePersonInformationPanel(PersonAgent temp) {
		customerStateCheckBox.setVisible(true);
		currentPerson = temp;
		PersonAgent person = temp;
		customerStateCheckBox.setText("Hungry?");
		customerStateCheckBox.setSelected(person.getGui().isHungry());
		customerStateCheckBox.setEnabled(!person.getGui().isHungry());
		infoCustomerLabel.setText(
				"<html><pre>     Name: " + person.getName() + " </pre></html>");
		tenantInformationPanel.validate();
	}
	public void updateLastCustomer()
	{
		if (currentPerson != null)
		{
			customerStateCheckBox.setSelected(currentPerson.getGui().isHungry());
			customerStateCheckBox.setEnabled(!currentPerson.getGui().isHungry());
			tenantInformationPanel.validate();
		}
	}
	public void updateLastWaiter()
	{
		//empty
	}

	public void updateWaiterInformationPanel(WaiterAgent person) {
		currentWaiter = person;
		WaiterAgent waiter = person;
		infoWaiterLabel.setText(
				"<html><pre>     Name: " + waiter.getName() + " </pre></html>");
		workerInformationPanel.validate();
	}

	/**
	 * Action listener method that reacts to the checkbox being clicked;
	 * If it's the customer's checkbox, it will make him hungry
	 * For v3, it will propose a break for the waiter.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == customerStateCheckBox) 
		{
			PersonAgent c = (PersonAgent) currentPerson;
			c.getGui().setHungry();
			customerStateCheckBox.setEnabled(false);
		}
		if (e.getSource() == pauseButton)
		{
			if (isPaused)
			{
				pauseButton.setText("PAUSE");
				housingPanel.resume();
				isPaused = false;
			}
			else if(isPaused == false)
			{
				pauseButton.setText("RESUME");
				housingPanel.pause();
				isPaused = true;
			}
		}
		if (e.getSource() == startButton)
		{
			housingPanel.startThreads();
		}
		if(e.getSource() == rentButton) {
			housingPanel.landlord.EveryoneOwesRent();
		}
		if(e.getSource() == breakStuffButton) {
			housingPanel.tenant.MyHouseNeedsRepairs();
		}
		if(e.getSource() == hungryButton) {
			housingPanel.tenant.EatAtHome();
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
			customerStateCheckBox.setEnabled(true);
			customerStateCheckBox.setSelected(false);
		}
	}

	/**
	 * Main routine to get gui started
	 */
//	public static void main(String[] args) {
//		HousingGui gui = new HousingGui();
//		gui.setTitle("Housing View");
//		gui.setVisible(true);
//		gui.setResizable(false);
//		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		/*
//        CityGui gui3 = new CityGui();
//        gui3.setTitle("Team 05's City");
//        gui3.setVisible(true);
//        gui3.setResizable(false);
//        gui3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        RestaurantGui gui2 = new RestaurantGui();
//        gui2.setTitle("Norman's Restaurant");
//        gui2.setVisible(true);
//        gui2.setResizable(false);
//        gui2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		 */
//	}
}
