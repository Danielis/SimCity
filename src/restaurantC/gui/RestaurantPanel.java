package restaurantC.gui;

import javax.swing.*;

import restaurantC.CashierRole;
import restaurantC.CookRole;
import restaurantC.CustomerRole;
import restaurantC.HostRole;
import restaurantC.MarketAgent;
import restaurantC.RestaurantC;
import restaurantC.WaiterRole;
import roles.Building;
import roles.Role;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	
	//int values to keep track of the number of waiters
	private int waiterGuiCount = 0;
	private int customerGuiCount = 0;
	
	//Agents
	//host
	private HostRole host;
	//waiters
	private Vector<WaiterRole> waiters = new Vector<WaiterRole>();
	//customer
	private Vector<CustomerRole> customers = new Vector<CustomerRole>();
	//cook
	private CookRole cook;
	//cashier
	private CashierRole cashier;
	//markets
	private Vector<MarketAgent> markets = new Vector<MarketAgent>();


	private JPanel restLabel = new JPanel();
	public JPanel inputPanel = new JPanel();

	//stuff for adding customers/waiters
	public JLabel customerLabel = new JLabel("Customer");
	public JTextField inputText = new JTextField(10);
	public JButton inputButton = new JButton("Enter");
	public JCheckBox inputHunger = new JCheckBox("Hungry");
	public JLabel waiterLabel = new JLabel("Waiter");
	public JTextField waiterText = new JTextField(10);
	public JButton waiterButton = new JButton("Enter");
	
	//pause
	public JButton pauseButton = new JButton("Pause");

	//customer panel
	public ListPanel customerPanel = new ListPanel(this, "Customers");
	private JPanel group = new JPanel();
	//waiter panel

	private RestaurantGui gui;
	
	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;
		
		//add three markets
		addMarket("Ralph's");
		addMarket("Safeway");
		addMarket("Food 4 Less");
		
		
		//layout stuff
		setLayout(new GridLayout(1, 4, 5, 5));
		group.setLayout(new GridLayout(1, 2, 5, 5));
		group.setBorder(BorderFactory.createTitledBorder("Add Customers"));
		inputPanel.setBorder(BorderFactory.createTitledBorder("Add"));
		group.add(customerPanel);
		inputPanel.add(customerLabel);
		inputPanel.add(inputText);
		inputPanel.add(inputHunger);
		inputPanel.add(inputButton);
		inputPanel.add(waiterLabel);
		inputPanel.add(waiterText);
		inputPanel.add(waiterButton);
		inputPanel.setVisible(false);
		initRestLabel();
		add(restLabel);
		add(group);
		add(inputPanel);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		restLabel.setLayout(new BorderLayout());
		label.setText(
				"<html><h3><u>Staff</u></h3><table><tr><td>host:</td><td>" + "Sarah" + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label, BorderLayout.CENTER);
		restLabel.add(pauseButton, BorderLayout.NORTH);
		restLabel.add(new JLabel(" "), BorderLayout.EAST);
		restLabel.add(new JLabel(" "), BorderLayout.WEST);
	}

	//shows info about a customer/waiter when it is clicked
	public void showInfo(String type, String name) {

		if (type.equals("Customers")) {
			for (int i = 0; i < customers.size(); i++) {
				CustomerRole temp = customers.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
	}
	
	//shows info about a waiter
	public void showWaiterInfo(String type, String name) {
		for(int i = 0; i < waiters.size(); i++) {
			WaiterRole temp = waiters.get(i);
			if(temp.name == name) {
				gui.updateInfoPanel(temp);
			}
		}
	}

	//adds customer
	public void addCustomer(CustomerRole c2, RestaurantC r) {
			CustomerRole c = (CustomerRole)c2;	
			CustomerGui g = new CustomerGui(c, gui, customerGuiCount);
			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			c.setGui(g);
			customers.add(c);
			c.getGui().setHungry();
			customerGuiCount++;
	}
	
	//adds customer
	public void addWaiter(WaiterRole c2, RestaurantC r) {
			WaiterRole w = (WaiterRole) c2;
			WaiterGui wgui = new WaiterGui(w, gui, waiterGuiCount);
			w.setGui(wgui);
			gui.animationPanel.addGui(wgui);
			w.setAnimationPanel(this.gui.animationPanel);
			waiters.add(w);
			waiterGuiCount++;
	}
	
	//add market
	public void addMarket(String name) {
		MarketAgent market = new MarketAgent(name);
		markets.add(market);
		market.startThread();
	}
	
	public void askBreak() {
		waiters.lastElement().wantToGoOnBreak();
	}

	public void pause()
	{

	}

	public void addHost(restaurantC.HostRole c) {
		HostRole hr = c;
		hr.building = gui.rest;
	}
	public void addCook(restaurantC.CookRole c) {
		CookRole cr = c;
		cr.building = gui.rest;
		CookGui cg = new CookGui(cr, this.gui);
		cr.setAnimationPanel(this.gui.animationPanel);
		gui.animationPanel.addGui(cg);
		cr.setGui(cg);
	}
	public void addCashier(restaurantC.CashierRole c) {
		CashierRole cr = c;
		cr.building = gui.rest;
		gui.animationPanel.hasCashier = true;
	}
}

