package housing.guis;



import restaurant.WaiterAgent;
import housing.HousingCustomerAgent;
import housing.HousingWorkerAgent;
import housing.interfaces.HousingCustomer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
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

	public HousingPanel housingPanel = new HousingPanel(this);

	/* personInformationPanel holds information about the clicked customer, if there is one*/
	private JPanel tenantInformationPanel;
	private JPanel workerInformationPanel;
	private JPanel InformationPanel;
	private JPanel buttonPanel;

	private JLabel infoCustomerLabel;
	private JLabel infoWorkerLabel;

	private JCheckBox tenantHungryBox;
	private JCheckBox tenantRepairBox;

	private HousingCustomer currentTenant;
	private HousingWorkerAgent currentWorker;

	private JButton pauseButton;
	private JButton startButton;
	private JPanel ButtonPanel;
	private JButton rentButton;

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

		tenantHungryBox = new JCheckBox();
		tenantHungryBox.setVisible(true);
		tenantHungryBox.addActionListener(this);
		tenantHungryBox.setText("Hungry");
		tenantRepairBox = new JCheckBox();
		tenantRepairBox.setVisible(true);
		tenantRepairBox.addActionListener(this);
		tenantRepairBox.setText("Repair");

		tenantInformationPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoCustomerLabel = new JLabel(); 
		infoCustomerLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
		tenantInformationPanel.add(infoCustomerLabel);
		tenantInformationPanel.add(tenantHungryBox);
		tenantInformationPanel.add(tenantRepairBox);


		//WAITER PANEL INFORMATION
		Dimension infoDimWaiter = new Dimension(WINDOWX, (int) (WINDOWY * .12));
		workerInformationPanel = new JPanel();
		workerInformationPanel.setPreferredSize(infoDimWaiter);
		workerInformationPanel.setMinimumSize(infoDimWaiter);
		workerInformationPanel.setMaximumSize(infoDimWaiter);
		workerInformationPanel.setBorder(BorderFactory.createTitledBorder("Waiters"));


		workerInformationPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoWorkerLabel = new JLabel();
		infoWorkerLabel.setText("<html><pre><i>Click Add to make workers</i></pre></html>");
		workerInformationPanel.add(infoWorkerLabel);
		RestaurantPortion.add(housingPanel, BorderLayout.NORTH);
		InformationPanel.add(tenantInformationPanel, BorderLayout.NORTH);
		rentButton = new JButton("Rent time!");
		rentButton.addActionListener(this);
		ButtonPanel.setLayout(new BorderLayout());
		ButtonPanel.add(rentButton, BorderLayout.CENTER);
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
	public void updateTenantInformationPanel(HousingCustomer temp) {
		//customerStateCheckBox.setVisible(true);
		currentTenant = temp;
		tenantHungryBox.setSelected(currentTenant.hungry);
		tenantHungryBox.setEnabled(!currentTenant.hungry);
		tenantRepairBox.setSelected(currentTenant.houseNeedsRepairs);
		tenantRepairBox.setEnabled(!currentTenant.houseNeedsRepairs);
		infoCustomerLabel.setText(
				"<html><pre>     Name: " + currentTenant.getName() + " </pre></html>");
		tenantInformationPanel.validate();
	}
	public void updateWorkerInformationPanel(HousingWorkerAgent temp) {
		//customerStateCheckBox.setVisible(true);
		currentWorker = temp;
		//HousingCustomerAgent tenant = temp;
		//customerStateCheckBox.setText("Hungry?");
		//customerStateCheckBox.setSelected(currentTenant.getGui().isHungry());
		//customerStateCheckBox.setEnabled(!currentTenant.getGui().isHungry());
		infoWorkerLabel.setText(
				"<html><pre>     Name: " + currentWorker.name + " </pre></html>");
		workerInformationPanel.validate();
	}
	public void updateLastCustomer()
	{
		if (currentTenant != null)
		{	tenantHungryBox.setSelected(currentTenant.hungry);
			tenantHungryBox.setEnabled(!currentTenant.hungry);
			tenantRepairBox.setSelected(currentTenant.houseNeedsRepairs);
			tenantRepairBox.setEnabled(!currentTenant.houseNeedsRepairs);
			tenantInformationPanel.validate();
		}
	}

	public void updateWaiterInformationPanel(HousingWorkerAgent person) {
		currentWorker = person;
		HousingWorkerAgent worker = person;
		infoWorkerLabel.setText(
				"<html><pre>     Name: " + worker.name + " </pre></html>");
		workerInformationPanel.validate();
	}

	/**
	 * Action listener method that reacts to the checkbox being clicked;
	 * If it's the customer's checkbox, it will make him hungry
	 * For v3, it will propose a break for the waiter.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == tenantHungryBox) 
		{
			HousingCustomer c = currentTenant;
			c.EatAtHome();
			tenantHungryBox.setEnabled(false);
		}
		if (e.getSource() == tenantRepairBox) 
		{
			HousingCustomer c = currentTenant;
			c.MyHouseNeedsRepairs();
			tenantRepairBox.setEnabled(false);
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
	}
	/**
	 * Message sent from a customer gui to enable that customer's
	 * "I'm hungry" checkbox.
	 *
	 * @param c reference to the customer
	 */
	public void setCustomerEnabled(HousingCustomerAgent p) {
		HousingCustomer ten = currentTenant;
		if (p.equals(ten)) 
		{
			tenantHungryBox.setEnabled(true);
			tenantHungryBox.setSelected(false);
			tenantRepairBox.setEnabled(true);
			tenantRepairBox.setSelected(false);
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
