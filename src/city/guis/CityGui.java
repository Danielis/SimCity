package city.guis;


//Import other packages
import restaurant.CustomerAgent;
import restaurant.Restaurant;
//import bank.gui.BankGui;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customer;
import roles.Apartment;
import roles.Building;
import roles.Building.buildingType;
import housing.guis.HousingGui;
import bank.Bank;
import bank.interfaces.*;
import bank.gui.BankGui;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TracePanel;
import logging.TrackerGui;
import market.Market;
import market.gui.MarketGui;
import city.PersonAgent;
import transportation.BusAgent;
import transportation.BusStopAgent;
import transportation.TransportationCompanyAgent;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;
import city.Government;
import city.Scenario;
import city.TimeManager;




//Import Java utilities
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import restaurantA.*;
import restaurantC.RestaurantC;
import restaurantD.RestaurantD;
import bank.*;
public class CityGui extends JFrame implements ActionListener {

	Restaurant tempRes;
	RestaurantA tempResA;
	RestaurantC tempResC;

	RestaurantD tempResD;
	
	Bank tempBan;
	Apartment tempApa;
	Market tempMar;
	
	//Funds per building to pay their employees
	public double GovernmentFunds = 100000;

	CityGui cityGui = this;

	//Buildings
	public Vector<Building> buildings = new Vector<Building>();

	//Java Structure
	public JFrame animationFrame = new JFrame("Restaurant Animation");

	//The City Animation
	public CityAnimationPanel cityAnimationPanel = new CityAnimationPanel();

	//City Panels with buttons and etc
	public CityPanel cityPanel = new CityPanel(this);

	//Contains a lot of information for the city
	public JPanel RestaurantPortion = new JPanel();

	//City JPanels for information
	private JPanel personInformationPanel;
	private JPanel InformationPanel;
	private JPanel buttonPanel;
	private JLabel infoCustomerLabel;

	// private JPanel functionPanel;
	private JPanel functionPanel = new JPanel();
	private JLabel functionPanelL;

	private JLabel quant;

	//Copy of the current person
	private PersonAgent currentPerson;

	//Functionality buttons
	private JButton pauseButton;
	private JButton refreshButton;

	Boolean isPaused = false;

	public static TrackerGui trackingWindow;

	private Government clock = new Government();

	// ************ START FUNCTION PANEL *********************

	private JPanel bankPanel = new JPanel();
	private String[] transactions = { "New Account", "Deposit", "Withdraw", "New Loan", "Pay Loan", "Rob" };
	private JComboBox transactionList = new JComboBox(transactions);
	private JButton bankGo = new JButton("Go");
	private JTextField amountInput = new JTextField("");

	private JPanel marketPanel = new JPanel();
	String[] marketTransactions = { "Steak", "Pizza", "Salad", "Chicken", "Car" };
	JComboBox marketList = new JComboBox(marketTransactions);
	private JButton marketGo = new JButton("Go");
	private JTextField marketQ = new JTextField("");

	private JPanel housingPanel = new JPanel();
	String[] housingOptions = { "Cook", "Pay Rent", "Call for Repair", "Sleep" };
	JComboBox housingList = new JComboBox(housingOptions);
	String[] foodOptions = { "Pasta", "Chicken", "Eggs" };
	JComboBox foodList = new JComboBox(foodOptions);
	private JButton housingGo = new JButton("Go");

	private JPanel restaurantPanel = new JPanel(); 
	private JButton restaurantGo = new JButton("Go");
	private JPanel otherFunction = new JPanel(); 
	private JButton busGo = new JButton("Bus");

	private JButton scenP = new JButton("Scenario P");
	private JButton scenCrash = new JButton("P Q"); 
	private JButton scenA = new JButton("A");
	private JButton scenB = new JButton("B");
	private JButton scenF = new JButton("F");
	private JButton scenJ = new JButton("J");
	private JButton scenO = new JButton("O");
	private JButton scenR = new JButton("R");
	private JButton workGo = new JButton("Work");
	private JButton noAIGo = new JButton("Turn off all AI");
	private JButton rubric = new JButton("Rubric");

	private JButton tempGo = new JButton("Test");

	private JButton close = new JButton("Close");
	private JButton closeRestN = new JButton("Close RestN");
	private JButton closeRestA = new JButton("Close RestA");
	private JButton closeRestC = new JButton("Close RestC");
	private JButton closeBank = new JButton("Close Banks");
	private JButton closeMarket = new JButton("Close Markets");
	

	private JButton fillBank = new JButton("Fill");
	private JButton fillRestaurant = new JButton("Fill");

	// ************ END FUNCTION PANEL *********************

	//CONSTRUCTOR

	public CityGui() 
	{
		//this.setAlwaysOnTop(true);
		this.setAlwaysOnTop(false);

		clock.startThread();
		clock.setPeople(cityPanel.people);

		int WINDOWX = 500;
		int WINDOWY = 500;

		//Set the City Gui's specifications
		this.setVisible(true);
		setTitle("Team 05's City");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(25, 25, WINDOWX+800, WINDOWY+150);

		setLayout(new GridLayout(1,2));
		// cityPanel.setRestaurants(restaurants);
		//cityPanel.setBanks(banks);
		cityPanel.setBuildings(buildings);

		//Set the layouts of the panels
		RestaurantPortion.setLayout(new BorderLayout());
		InformationPanel = new JPanel();
		InformationPanel.setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		//Set Dimension for RestaurantPortion
		Dimension restDim2 = new Dimension(280, (int) (43 * .86));
		functionPanel.setPreferredSize(restDim2);
		functionPanel.setMinimumSize(restDim2);
		functionPanel.setMaximumSize(restDim2);


		//Set Dimension for CityPanel
		Dimension restDim = new Dimension(250, (int) (WINDOWY * .86));
		cityPanel.setPreferredSize(restDim);
		cityPanel.setMinimumSize(restDim);
		cityPanel.setMaximumSize(restDim);

		//Initiate buttons
		pauseButton = new JButton("PAUSE");
		pauseButton.addActionListener(this);
		refreshButton = new JButton("REFRESH");
		refreshButton.addActionListener(this);


		// ************ START FUNCTION PANEL *********************
		functionPanel.setLayout(new GridLayout(0,1));
		functionPanelL = new JLabel(); 
		functionPanelL.setText("Amount ($):");


		quant = new JLabel(); 
		quant.setText("Quantity:");
		//functionPanel.add(functionPanelL, BorderLayout.NORTH);


		//marketLabel.setText("market");
		//housingLabel.setText("housing");
		//restaurantLabel.setText("rest");
		//bankPanel.add(functionPanelL);
		//marketPanel.add(marketLabel);
		//housingPanel.add(housingLabel);
		//restaurantPanel.add(restaurantLabel);

		//functionPanel.setLayout(new FlowLayout());


		//functionPanel.add(personPanel);


		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder bankTitle = BorderFactory.createTitledBorder(loweredetched, "Bank");

		functionPanel.add(bankPanel);
		bankPanel.setLayout(new FlowLayout());
		bankPanel.setBorder(bankTitle);
		amountInput.setColumns(10);
		bankPanel.add(functionPanelL);
		bankPanel.add(amountInput);
		bankPanel.add(transactionList);
		bankPanel.add(bankGo);
		bankPanel.add(closeBank);
		//bankPanel.add(fillBank);

		TitledBorder marketTitle = BorderFactory.createTitledBorder(loweredetched, "Market");
		functionPanel.add(marketPanel);
		marketPanel.setLayout(new FlowLayout());
		marketPanel.setBorder(marketTitle);
		marketQ.setColumns(10);
		marketPanel.add(quant);
		marketPanel.add(marketQ);
		marketPanel.add(marketList);
		marketPanel.add(marketGo);
		marketPanel.add(closeMarket);

		TitledBorder housingTitle = BorderFactory.createTitledBorder(loweredetched, "Housing");
		functionPanel.add(housingPanel);
		housingPanel.setLayout(new FlowLayout());
		housingPanel.setBorder(housingTitle);
		housingPanel.add(housingList);
		//housingPanel.add(foodList);
		foodList.setEnabled(false);
		housingPanel.add(housingGo);


		TitledBorder restTitle = BorderFactory.createTitledBorder(loweredetched, "Restaurant");
		functionPanel.add(restaurantPanel);
		restaurantPanel.setBorder(restTitle);
		restaurantPanel.add(restaurantGo);
		restaurantPanel.add(closeRestA);
		restaurantPanel.add(closeRestN);
		//restaurantPanel.add(closeRestC);
		//restaurantPanel.add(fillRestaurant);

		TitledBorder funct = BorderFactory.createTitledBorder(loweredetched, "City");
		functionPanel.add(otherFunction);
		otherFunction.setLayout(new FlowLayout());
		otherFunction.setBorder(funct);
		//otherFunction.add(workGo);
		otherFunction.add(busGo);
		otherFunction.add(scenA);
		otherFunction.add(scenB);

		//otherFunction.add(scenP);
		otherFunction.add(scenCrash);
		otherFunction.add(scenF);
		otherFunction.add(scenJ);
		otherFunction.add(scenO);
		otherFunction.add(scenR);
		//otherFunction.add(noAIGo);
		//otherFunction.add(tempGo);
		otherFunction.add(rubric);
		
		busGo.addActionListener(this);
		scenO.addActionListener(this);
		scenA.addActionListener(this);
		scenB.addActionListener(this);

		scenP.addActionListener(this);
		scenCrash.addActionListener(this);

		scenF.addActionListener(this);
		scenJ.addActionListener(this);
		scenR.addActionListener(this);

		bankGo.addActionListener(this);
		marketGo.addActionListener(this);
		housingGo.addActionListener(this);
		restaurantGo.addActionListener(this);
		workGo.addActionListener(this);
		housingList.addActionListener(this);
		noAIGo.addActionListener(this);
		fillBank.addActionListener(this);
		fillRestaurant.addActionListener(this);

		closeRestA.addActionListener(this);
		closeRestN.addActionListener(this);
		closeRestC.addActionListener(this);
		closeMarket.addActionListener(this);
		closeBank.addActionListener(this);
		closeMarket.addActionListener(this);

		tempGo.addActionListener(this);
		
		rubric.addActionListener(this);



		// ************ END FUNCTION PANEL *********************

		Dimension restDim5 = new Dimension(450, (int) (200));
		RestaurantPortion.setPreferredSize(restDim5);
		RestaurantPortion.setMinimumSize(restDim5);
		RestaurantPortion.setMaximumSize(restDim5);

		//Set the information Panel Structures (Java layout for the panels)
		Dimension infoDimCustomer = new Dimension(WINDOWX, (int) (WINDOWY * .30));
		personInformationPanel = new JPanel();
		personInformationPanel.setPreferredSize(infoDimCustomer);
		personInformationPanel.setMinimumSize(infoDimCustomer);
		personInformationPanel.setMaximumSize(infoDimCustomer);
		personInformationPanel.setBorder(BorderFactory.createTitledBorder("Citizens"));
		personInformationPanel.setLayout(new BorderLayout());
		infoCustomerLabel = new JLabel(); 
		infoCustomerLabel.setText("<html><p><p>Click Add to make people</p></p></html>");


		personInformationPanel.add(infoCustomerLabel, BorderLayout.NORTH);
		RestaurantPortion.add(cityPanel, BorderLayout.EAST);
		RestaurantPortion.add(functionPanel, BorderLayout.CENTER);


		//InformationPanel.add(personInformationPanel, BorderLayout.NORTH);
		// cityPanel.add(InformationPanel, BorderLayout.SOUTH);
		buttonPanel.add(pauseButton, BorderLayout.CENTER);
		buttonPanel.add(refreshButton, BorderLayout.EAST);
		//RestaurantPortion.add(personInformationPanel, BorderLayout.SOUTH);
		add(cityAnimationPanel);
		add(RestaurantPortion);



		//City Element Creation
		createRestaurant("Norman's Restaurant", 257, 474);
		createBank("Aleena's Bank - North", 73, 74);
		createBank("Aleena's Bank - South", 16, 619);
		createRestaurantA("Aleena Restaurant", 183, 266);
		createRestaurantC("Chris's Restaurant", 528, 473);
		createRestaurantD("Daniel's Restaurant", 87, 262);
		createMarket("Aleena's Market - East", 577, 81);
		createMarket("Aleena's Market - West", 280, 265);
		createApartment("The Chris Apartment Complex", 319, 90);

		this.setAlwaysOnTop(false);
		//this.setAutoRequestFocus(false);

		//Mouse Listener for the coordinates
		cityAnimationPanel.addMouseListener(new MouseListener() 

		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int x=e.getX();
				int y=e.getY();
				System.out.println(x+","+y);//these co-ords are relative to the component


				for (PersonAgent p : cityPanel.people)
				{
					if (p.getGui().isPresent())
					{
						int xmin, ymin, xmax, ymax, xmintest, xmaxtest, ymintest, ymaxtest;

						xmin = p.getBound_leftx();
						xmax = p.getBound_rightx();
						ymin = p.getBound_topy();
						ymax = p.getBound_boty();

						xmintest = p.getGui().getXPosition()-30;
						xmaxtest = p.getGui().getXPosition()+30;
						ymintest = p.getGui().getYPosition()-30;
						ymaxtest = p.getGui().getYPosition()+30;;

						if ((x<xmaxtest) && (y<ymaxtest) && (x>xmintest) && (y>ymintest))
						{
							System.out.println("Opening Person Frame.");
							createFrame(p);
						}
					}
				}


				if ((x<159) && (y<85) && (x>0) && (y>0))
				{
					for (Building b: buildings){
						if (b.getType() == buildingType.bank && b.name.equals("Aleena's Bank - North")){
							Bank r = (Bank) b;
							tempBan = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempBan.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
									System.out.println("check");
								}
							});
						}
					}   
				}
				
				if ((x<140) && (y<619) && (x>32) && (y>554))
				{
					for (Building b: buildings){
						if (b.getType() == buildingType.bank && b.name.equals("Aleena's Bank - South")){
							Bank r = (Bank) b;
							tempBan = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempBan.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
									System.out.println("check");
								}
							});
						}
					}   
				}


				if ((x<314) && (y<468) && (x>210) && (y>370))
				{
					for (Building b: buildings){
						if (b.getType() == buildingType.restaurant && b.owner.equals("Norman")){
							Restaurant r = (Restaurant) b;
							r.setPaymentFund(GovernmentFunds);
							tempRes = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempRes.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
								}

								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent arg0) {}
								public void mousePressed(MouseEvent arg0) {}
								public void mouseReleased(MouseEvent arg0) {}

							});
						}

					}
				}

				if ((x<239) && (y<264) && (x>145) && (y>190))
				{
					for (Building b: buildings){
						if (b.getType() == buildingType.restaurant && b.owner.equals("Aleena")){
							RestaurantA r = (RestaurantA) b;
							//r.setPaymentFund(GovernmentFunds);
							tempResA = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempResA.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
								}

								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent arg0) {}
								public void mousePressed(MouseEvent arg0) {}
								public void mouseReleased(MouseEvent arg0) {}

							});
						}

					}
				}
				
				if ((x<608) && (y<472) && (x>514) && (y>397))
				{
					for (Building b: buildings){
						if (b.getType() == buildingType.restaurant && b.owner.equals("Chris")){
							RestaurantC r = (RestaurantC) b;
							//r.setPaymentFund(GovernmentFunds);
							tempResC = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempResC.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
								}

								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent arg0) {}
								public void mousePressed(MouseEvent arg0) {}
								public void mouseReleased(MouseEvent arg0) {}

							});
						}

					}
				}
				
				
				if ((x<131) && (y<266) && (x>37) && (y>204))
				{
					for (Building b: buildings){
						if (b.getType() == buildingType.restaurant && b.owner.equals("Daniel")){
							RestaurantD r = (RestaurantD) b;
							//r.setPaymentFund(GovernmentFunds);
							tempResD = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempResD.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
								}

								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent arg0) {}
								public void mousePressed(MouseEvent arg0) {}
								public void mouseReleased(MouseEvent arg0) {}

							});
						}

					}
				}

				if ((x<398) && (y<85) && (x>257) && (y>0))
				{
					for (Building b: buildings)
					{
						if (b.getType() == buildingType.housingComplex)
						{
							Apartment r = (Apartment) b;
							tempApa = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempApa.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									cityGui.setAlwaysOnTop(false);
									setState(Frame.NORMAL);
								}
							});
						}
					}
				}


				if ((x<315) && (y<258) && (x>258) && (y>212))
				{
					
					for (Building b: buildings){
						//System.out.println("t: " + b.getType() + " name" + b.name);
						if (b.getType() == buildingType.market && b.name.equals("Aleena's Market - West")){
							Market r = (Market) b;
							tempMar = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempMar.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									setState(Frame.NORMAL);
								}
							});
						}
					} 
				}
				
				
				if ((x<606) && (y<84) && (x>546) && (y>27))
				{
					
					for (Building b: buildings){
					//	System.out.println("t: " + b.getType() + " name" + b.name);
						if (b.getType() == buildingType.market && b.name.equals("Aleena's Market - East")){
							Market r = (Market) b;
							tempMar = r;
							r.gui.setVisible(true);
							cityGui.setAlwaysOnTop(false);
							r.gui.setAlwaysOnTop(true);
							r.gui.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									tempMar.gui.setAlwaysOnTop(false);
									cityGui.setAlwaysOnTop(true);
									setState(Frame.NORMAL);
								}
							});
						}
					} 
				}
			}


			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}


		});
	}

	/**
	 * updatepersonInformationPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 * @param temp customer (or waiter) object
	 */


	//Update the information Panel

	public void updatePersonInformationPanel(PersonAgent temp) {
		currentPerson = temp;
		PersonAgent person = temp;
		silenceButtons();
		infoCustomerLabel.setText(
				"<html><pre>     Name: " + person.getName() + " </pre></html>");


		personInformationPanel.validate();
	}

	//Update the last customer
	public void updateLastPerson()
	{
		if (currentPerson != null)
		{
			silenceButtons();
			personInformationPanel.validate();
		}
	}

	private void silenceButtons(){
		bankGo.setEnabled(!currentPerson.getGui().getBusy());
		marketGo.setEnabled(!currentPerson.getGui().getBusy());
		housingGo.setEnabled(!currentPerson.getGui().getBusy());
		restaurantGo.setEnabled(!currentPerson.getGui().getBusy());
		workGo.setEnabled(!currentPerson.getGui().getBusy());
	}
	
	private void silenceScenButtons(){
		scenO.setEnabled(false);
		scenA.setEnabled(false);
		scenF.setEnabled(false);
		scenJ.setEnabled(false);
		scenB.setEnabled(false);

		scenP.setEnabled(false);
		scenCrash.setEnabled(false);

		scenR.setEnabled(false);
		rubric.setEnabled(false);

	}
	//Action Listener
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeBank){
			for (Building b : buildings){
				if (b.getType() == buildingType.bank){
					b.ForceClosed();
					Bank r = (Bank) b;
					for (Teller t : r.workingTellers){
						t.msgLeaveWork();
					}
					r.workingHost.msgLeaveWork();
				}
			}
			closeBank.setEnabled(false);
		}
		if (e.getSource() == closeRestN){
			for (Building b : buildings){
				if (b.getType() == buildingType.restaurant && b.owner.equals("Norman"))
					b.ForceClosed();
			}
			closeRestN.setEnabled(false);
		}
		if (e.getSource() == closeRestA){
			for (Building b : buildings){
				if (b.getType() == buildingType.restaurant && b.owner.equals("Aleena")){
					b.ForceClosed();
					RestaurantA r = (RestaurantA) b;
					r.workingHost.msgLeaveWork();
					r.workingCashier.msgLeaveWork();
					r.workingCook.msgLeaveWork();
					for (restaurantA.WaiterAgent t : r.workingWaiters){
						t.msgLeaveWork();
					}
				}
			}
			closeRestA.setEnabled(false);
		}
		if (e.getSource() == closeRestC){
			for (Building b : buildings){
				if (b.getType() == buildingType.restaurant && b.owner.equals("Chris")){
					b.ForceClosed();
					RestaurantC r = (RestaurantC) b;
					r.workingHost.msgLeaveWork();
					r.workingCashier.msgLeaveWork();
					r.workingCook.msgLeaveWork();
					for (restaurantC.WaiterRole t : r.workingWaiters){
						t.msgLeaveWork();
					}
				}
			}
			closeRestC.setEnabled(false);
		}
		if (e.getSource() == fillBank){
			Scenario.getInstance().EmployBank(this.cityPanel);
		}
		if (e.getSource() == fillRestaurant){

			Scenario.getInstance().EmployRest(this.cityPanel);
		}
		if (e.getSource() == closeMarket){
			for (Building b : buildings){
				if (b.getType() == buildingType.market)
					b.ForceClosed();
			}
			closeMarket.setEnabled(false);
		}
		
		if (e.getSource() == busGo){
			this.cityPanel.createBusSystem(); // trans: will remove piece by piece as I integrate bus sustem into city
			busGo.setEnabled(false);
		}
		if (e.getSource() == scenO){
			Scenario.getInstance().CallScenarioO(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == scenF){
			Scenario.getInstance().CallScenarioF(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == scenJ){
			Scenario.getInstance().CallScenarioJ(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == scenA){
			Scenario.getInstance().CallScenarioA(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == scenB){
			Scenario.getInstance().CallScenarioB(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == scenR){
			Scenario.getInstance().CallScenarioR(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == scenCrash){
			Scenario.getInstance().collisionTest(this.cityPanel);
			silenceScenButtons();
		}
		if (e.getSource() == noAIGo){
			this.cityPanel.setNoAI(true);
			noAIGo.setEnabled(false);
		}
		if (e.getSource() == tempGo){
			Scenario.getInstance().CallScenarioTest(this.cityPanel);
			silenceScenButtons();
		}
		
		if (e.getSource() == rubric){
			Scenario.getInstance().setCityPanel(this.cityPanel);
			Scenario.getInstance().CallScenarioRubric(this.cityPanel);
			silenceScenButtons();
		}

		if (currentPerson != null){
			if (e.getSource() == restaurantGo) 
			{
				PersonAgent c = (PersonAgent) currentPerson;
				c.getGui().setHungry();
				silenceButtons();
			}
			if (e.getSource() == workGo){
				PersonAgent c = (PersonAgent) currentPerson;
				c.getGui().setWork();
				silenceButtons();
			}

			if (e.getSource() == bankGo)
			{

				                                PersonAgent c = (PersonAgent) currentPerson;
				
				                                String purpose = transactionList.getSelectedItem().toString();
				
				                                if (purpose.equals("New Account")){
					                                        c.getGui().setNeedsMoney(true, purpose, 0);
					                                }
				                                else if (!amountInput.getText().isEmpty()){
				                                        String amtTemp = amountInput.getText();
				                                        double amt = Double.parseDouble(amtTemp);
				                                        amt = Math.round(amt * 100) / 100.0d;
				                                        c.getGui().setNeedsMoney(true, purpose, amt);
				                                }
				                                silenceButtons();

//				Restaurant temp;
//				for (Building b: buildings)
//				{
//					if(b.getType() == buildingType.restaurant)
//					{
//						temp = (Restaurant)b;
//						System.out.println(temp.panel.host);
//						if (temp.panel.host != null)
//							temp.panel.host.msgLeaveWork();
//						System.out.println(temp.panel.cook);
//						if (temp.panel.cook != null)
//							temp.panel.cook.msgLeaveWork();
//						System.out.println(temp.panel.cashier);
//						if (temp.panel.cashier != null)
//							temp.panel.cashier.msgLeaveWork();
//						System.out.println(temp.panel.waiters.get(0));
//						if (temp.panel.waiters.get(0) != null)
//							temp.panel.waiters.get(0).msgLeaveWork();
//					}
//				}

			}
			if (e.getSource() == housingGo)
			{

				PersonAgent c = (PersonAgent) currentPerson;
				String purpose = housingList.getSelectedItem().toString();
				c.getGui().setNeedsHome(true, purpose);
				//silenceButtons();
			}

			if (e.getSource() == marketGo)
			{

				PersonAgent c = (PersonAgent) currentPerson;

				if (!marketQ.getText().isEmpty())
				{
					String item = marketList.getSelectedItem().toString();
					String quantityStr = marketQ.getText();
					double quantity = Double.parseDouble(quantityStr);
					quantity = Math.round(quantity * 1) / 1d;
					c.getGui().setShop(true, item, quantity);
				}

				silenceButtons();
			}

			if (e.getSource() == marketList)
			{
				if (marketList.getSelectedItem().toString().equals("Cook")){
					foodList.setEnabled(true);
				}
				if (marketList.getSelectedIndex() == 2)
					foodList.setEnabled(true);
			}
			//        if (e.getSource() == personHousingCheckBox)
			//        {
			//                PersonAgent c = (PersonAgent) currentPerson;
			//                c.getGui().setNeedsHome(true);
			//                personHousingCheckBox.setEnabled(false);
			//                }
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
	}

	public void createFrame(PersonAgent p)
	{
		PersonFrame f = new PersonFrame(p);
	}



	//Resturant Creation
	public void createRestaurant(String name, int x, int y)
	{
			RestaurantGui rg = new RestaurantGui();
			rg.setTrackerGui(trackingWindow);
			Restaurant r = new Restaurant(rg, name);
			r.setPaymentFund(GovernmentFunds);
			rg.setRestaurant(r);
			r.setEntrance(x,y);
			buildings.add(r); 
	}
	
	public void createRestaurantA(String name, int x, int y)
	{
			restaurantA.gui.RestaurantGui rg = new restaurantA.gui.RestaurantGui();
			rg.setTrackerGui(trackingWindow);
			restaurantA.RestaurantA r = new restaurantA.RestaurantA(rg, name);
			rg.setRestaurant(r);
			r.setEntrance(x,y);
			buildings.add(r);
	}
	
	public void createRestaurantD(String name, int x, int y)
	{
			//restaurantD.gui.RestaurantGui rg = new restaurantD.gui.RestaurantGui();
			//rg.setTrackerGui(trackingWindow);
			//restaurantD.RestaurantD r = new restaurantD.RestaurantD(rg, name);
			//rg.setRestaurant(r);
			//r.setEntrance(x,y);
			//buildings.add(r);
	}
	
	public void createRestaurantC(String name, int x, int y) {
			restaurantC.gui.RestaurantGui rg = new restaurantC.gui.RestaurantGui();
			rg.setTrackerGui(trackingWindow);
			restaurantC.RestaurantC r = new restaurantC.RestaurantC(rg, name);
			rg.setRestaurant(r);
			r.setEntrance(x, y);
			buildings.add(r);
	}

	public void createBank(String name, int x, int y)
	{
		BankGui bg = new BankGui();
		bg.setTrackerGui(trackingWindow);
		Bank b = new Bank(bg, name);
		b.setPaymentFund(GovernmentFunds);
		b.setEntrance(x,y);
		buildings.add(b);
	}

	public void createMarket(String name, int x, int y)
	{
		MarketGui mg = new MarketGui();
		mg.setTrackerGui(trackingWindow);
		Market b = new Market(name, mg);
		b.setPaymentFund(GovernmentFunds);
		b.setEntrance(x,y);
		buildings.add(b);
	}

	//apartment creation
	public void createApartment(String name, int x, int y) {
		HousingGui hg = new HousingGui();
		hg.setTrackerGui(trackingWindow);
		Apartment a = new Apartment(name, hg);
		a.setPaymentFund(GovernmentFunds);
		a.setEntrance(x,y);
		buildings.add(a);
	}

	//Set Person Enabled
	public void setPersonEnabled(PersonAgent p) {
		PersonAgent per = currentPerson;
		if (p.equals(per)) 
		{
			silenceButtons();
		}
	}

	//Main Function - Sets up the program
	public static void main(String[] args) 
	{    
		trackingWindow = new TrackerGui();
		CityGui gui = new CityGui();
		gui.setVisible(true);
		trackingWindow.trackerFrame.setAlwaysOnTop(true);

		//gui.cityPanel.createBusSystem(); // trans: will remove piece by piece as I integrate bus sustem into city
		//gui.cityPanel.collisionTest(); // trans: will remove piece by piece as I integrate bus sustem into city
	}


}
