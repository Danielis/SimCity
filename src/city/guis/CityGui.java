package city.guis;


//Import other packages
import restaurant.CustomerAgent;
//import bank.gui.BankGui;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customer;
import roles.Apartment;
import roles.Building;
import roles.Restaurant;
import roles.Building.buildingType;
import housing.guis.HousingGui;
import bank.Bank;
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
import city.Clock;
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


public class CityGui extends JFrame implements ActionListener {
	
	
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
    
    //Copy of the current person
    private PersonAgent currentPerson;

    //Functionality buttons
    private JButton pauseButton;
    private JButton refreshButton;
       
    Boolean isPaused = false;
        
    public static TrackerGui trackingWindow;
    
    private Clock clock = new Clock();
    
    
    
    // ************ START FUNCTION PANEL *********************
    

    
    private JPanel bankPanel = new JPanel();
    private String[] transactions = { "New Account", "Deposit", "Withdraw", "New Loan", "Pay Loan" };
    private JComboBox transactionList = new JComboBox(transactions);
    private JButton bankGo = new JButton("Go");
    private JTextField amountInput = new JTextField("");
    
    private JPanel marketPanel = new JPanel();
    String[] marketTransactions = { "Steak", "Pizza", "Salad", "Chicken", "Car" };
    JComboBox marketList = new JComboBox(marketTransactions);
    private JButton marketGo = new JButton("Go");
    private JTextField marketQ = new JTextField("");
   
 
   
    private JPanel housingPanel = new JPanel();
    String[] housingOptions = { "Pay Rent", "Call for Repair", "Cook", "Sleep" };
    JComboBox housingList = new JComboBox(housingOptions);
    String[] foodOptions = { "Pasta", "Chicken", "Eggs" };
    JComboBox foodList = new JComboBox(foodOptions);
    private JButton housingGo = new JButton("Go");
    
    
    private JPanel restaurantPanel = new JPanel(); 
    private JButton restaurantGo = new JButton("Go");
    
    private JPanel otherFunction = new JPanel(); 
    private JButton busGo = new JButton("Bus");
    private JButton scen1 = new JButton("Scenario 1");
    private JButton workGo = new JButton("Work");
    
    // ************ END FUNCTION PANEL *********************
    
    //CONSTRUCTOR
    public CityGui() {  
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
        //functionPanelL.setText("<html>function panel</html>");
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
		bankPanel.add(amountInput);
		bankPanel.add(transactionList);
		bankPanel.add(bankGo);
		
		TitledBorder marketTitle = BorderFactory.createTitledBorder(loweredetched, "Market");
		functionPanel.add(marketPanel);
		marketPanel.setLayout(new FlowLayout());
		marketPanel.setBorder(marketTitle);
		marketQ.setColumns(10);
		marketPanel.add(marketQ);
		marketPanel.add(marketList);
		marketPanel.add(marketGo);
		
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
		
		TitledBorder funct = BorderFactory.createTitledBorder(loweredetched, "City");
		functionPanel.add(otherFunction);
		otherFunction.setLayout(new FlowLayout());
		otherFunction.setBorder(funct);
		otherFunction.add(workGo);
		otherFunction.add(busGo);
		otherFunction.add(scen1);
		
		busGo.addActionListener(this);
		scen1.addActionListener(this);
		bankGo.addActionListener(this);
		marketGo.addActionListener(this);
		housingGo.addActionListener(this);
		restaurantGo.addActionListener(this);
		workGo.addActionListener(this);
		housingList.addActionListener(this);
        
        
        
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
        RestaurantPortion.add(personInformationPanel, BorderLayout.SOUTH);
        add(cityAnimationPanel);
        add(RestaurantPortion);
 
    	trackingWindow = new TrackerGui();

        //City Element Creation
        createRestaurant("Norman's Restaurant", "Norman");
        createBank("Aleena's Bank");
        createMarket("Aleena's Market");
        createApartment("The Chris Apartment Complex");
        
        //Mouse Listener for the coordinates
		cityAnimationPanel.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
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
			    
			    if ((x<159) && (y<85) && (x>0) && (y>0)){
			    	
			    	for (Building b: buildings){
						if (b.getType() == buildingType.bank){
							Bank r = (Bank) b;
							r.gui.setVisible(true);
						}
					}   
			    }
			    
			    if ((x<314) && (y<468) && (x>210) && (y>370)){
			    	for (Building b: buildings){
						if (b.getType() == buildingType.restaurant){
							Restaurant r = (Restaurant) b;
							r.gui.setVisible(true);
						}
					}
			    }
			    
			    if ((x<398) && (y<85) && (x>257) && (y>0)){
			    	for (Building b: buildings){
						if (b.getType() == buildingType.housingComplex){
							Apartment r = (Apartment) b;
							r.gui.setVisible(true);
						}
					}
			    }
			    
			    if ((x<315) && (y<258) && (x>258) && (y>212)){
		         	for (Building b: buildings){
		 				if (b.getType() == buildingType.market){
		 					Market r = (Market) b;
		 					r.gui.setVisible(true);
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
    //Action Listener
    public void actionPerformed(ActionEvent e) {
    	 if (e.getSource() == busGo){
         	this.cityPanel.createBusSystem(); // trans: will remove piece by piece as I integrate bus sustem into city
         	this.cityPanel.sendPersonToStop(); // trans: will remove piece by piece as I integrate bus sustem into city
         	busGo.setEnabled(false);
        }
    	 if (e.getSource() == scen1){
    		Scenario.getInstance().CallScenario1(this.cityPanel);
          	scen1.setEnabled(false);
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
        
        	if (!marketQ.getText().isEmpty()){
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
//        	PersonAgent c = (PersonAgent) currentPerson;
//        	c.getGui().setNeedsHome(true);
//        	personHousingCheckBox.setEnabled(false);
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
    
    public void createFrame(PersonAgent p)
    {
    	PersonFrame f = new PersonFrame(p);
    }
    
    //Resturant Creation
    public void createRestaurant(String name, String owner)
    {
    	if (owner == "Norman")
    	{
    		RestaurantGui rg = new RestaurantGui();
    		Restaurant r = new Restaurant(rg, name);
    		rg.setRestaurant(r);
    		buildings.add(r);
    	}
    }
    
    public void createBank(String name)
    {
    	BankGui bg = new BankGui();
    	Bank b = new Bank(bg, name);
    	bg.setTrackerGui(trackingWindow);
    	buildings.add(b);
    }
    
    public void createMarket(String name)
    {
    	Market b = new Market(name, new MarketGui());
    	buildings.add(b);
    }

    //apartment creation
    public void createApartment(String name) {
    	Apartment a = new Apartment(name, new HousingGui());
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
    	
    	CityGui gui = new CityGui();
    	gui.setVisible(true);
    	



    	//gui.cityPanel.createBusSystem(); // trans: will remove piece by piece as I integrate bus sustem into city
     //   gui.cityPanel.sendPersonToStop(); // trans: will remove piece by piece as I integrate bus sustem into city

    }
}
