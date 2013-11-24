package city.guis;


//Import other packages
import restaurant.CustomerAgent;
//import bank.gui.BankGui;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;
import roles.Apartment;
import roles.Building;
import roles.Restaurant;
import housing.guis.HousingGui;
import bank.Bank;
import bank.gui.BankGui;
import city.PersonAgent;

//Import Java utilities
import javax.imageio.ImageIO;
import javax.swing.*;

import market.gui.MarketGui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;


public class CityGui extends JFrame implements ActionListener {
	
	
	//Lists
	//public Vector<Restaurant> restaurants = new Vector<Restaurant>();
	//public Vector<Bank> banks = new Vector<Bank>();
	//public Vector<Apartment> apartments = new Vector<Apartment>();
	
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

    //Useful Checkboxes
    private JCheckBox personHungryCheckBox;
    private JCheckBox personNeedsMoneyCheckBox;
    private JCheckBox personHousingCheckBox;

    //Copy of the current person
    private PersonAgent currentPerson;

    //Functionality buttons
    private JButton pauseButton;
    private JButton refreshButton;
    Boolean isPaused = false;
    
    //CONSTRUCTOR
    public CityGui() {
        int WINDOWX = 500;
        int WINDOWY = 500;
        
        //Set the City Gui's specifications
       	this.setVisible(true);
        setTitle("Team 05's City");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBounds(25, 25, WINDOWX+700, WINDOWY+150);
 
        setLayout(new BorderLayout());
       // cityPanel.setRestaurants(restaurants);
        //cityPanel.setBanks(banks);
        cityPanel.setBuildings(buildings);
        
        //Set the layouts of the panels
        RestaurantPortion.setLayout(new BorderLayout());
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        
        //Set Dimension for CityPanel
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .86));
        cityPanel.setPreferredSize(restDim);
        cityPanel.setMinimumSize(restDim);
        cityPanel.setMaximumSize(restDim);
        
        //Initiate buttons
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(this);
        refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(this);
        
        //Set the information Panel Structures (Java layout for the panels)
        Dimension infoDimCustomer = new Dimension(WINDOWX, (int) (WINDOWY * .30));
        personInformationPanel = new JPanel();
        personInformationPanel.setPreferredSize(infoDimCustomer);
        personInformationPanel.setMinimumSize(infoDimCustomer);
        personInformationPanel.setMaximumSize(infoDimCustomer);
        personInformationPanel.setBorder(BorderFactory.createTitledBorder("Customers"));
        
        personHungryCheckBox = new JCheckBox();
        personHungryCheckBox.setVisible(false);
        personHungryCheckBox.addActionListener(this);
        
        personNeedsMoneyCheckBox = new JCheckBox();
        personNeedsMoneyCheckBox.setVisible(false);
        personNeedsMoneyCheckBox.addActionListener(this);
        
        personHousingCheckBox = new JCheckBox();
        personHousingCheckBox.setVisible(false);
        personHousingCheckBox.addActionListener(this);
        
        personInformationPanel.setLayout(new GridLayout(0,1,1,1));
        infoCustomerLabel = new JLabel(); 
        infoCustomerLabel.setText("<html><p><p>Click Add to make people</p></p></html>");
        
        personInformationPanel.add(infoCustomerLabel);
        personInformationPanel.add(personHungryCheckBox);
        personInformationPanel.add(personNeedsMoneyCheckBox);
        personInformationPanel.add(personHousingCheckBox);
        
        RestaurantPortion.add(cityPanel, BorderLayout.NORTH);
        InformationPanel.add(personInformationPanel, BorderLayout.NORTH);
        RestaurantPortion.add(InformationPanel, BorderLayout.CENTER);
        buttonPanel.add(pauseButton, BorderLayout.CENTER);
        buttonPanel.add(refreshButton, BorderLayout.EAST);
        RestaurantPortion.add(buttonPanel, BorderLayout.SOUTH);
        add(cityAnimationPanel, BorderLayout.CENTER);
        add(RestaurantPortion, BorderLayout.EAST);
        
        //City Element Creation
        createRestaurant("Norman's Restaurant", "Norman");
        createBank("Aleena's Bank");
        createApartment("The Chris Apartment Complex");
        
        //Mouse Listener for the coordinates
        cityAnimationPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            int x=e.getX();
            int y=e.getY();
            System.out.println(x+","+y);//these co-ords are relative to the component
          
            
            if ((x<159) && (y<85) && (x>0) && (y>0)){
                  BankGui gui3 = new BankGui();
                  gui3.setTitle("Aleena's Bank");
                  gui3.setVisible(true);
                  gui3.setResizable(false);
                  gui3.setDefaultCloseOperation(HIDE_ON_CLOSE);   
            }
            
            if ((x<314) && (y<468) && (x>210) && (y>370)){
             RestaurantGui gui2 = new RestaurantGui();
              gui2.setTitle("Norman's Restaurant");
              gui2.setVisible(true);
              gui2.setResizable(false);
              gui2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
            }
            
            if ((x<603) && (y<261) && (x>530) && (y>202)){
            	HousingGui gui4 = new HousingGui();
        		gui4.setTitle("Housing View");
        		gui4.setVisible(true);
        		gui4.setResizable(false);
        		gui4.setDefaultCloseOperation(HIDE_ON_CLOSE);  
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
        personHungryCheckBox.setVisible(true);
        personNeedsMoneyCheckBox.setVisible(true);
        personHousingCheckBox.setVisible(true);
        currentPerson = temp;
        PersonAgent person = temp;
        personHungryCheckBox.setText("Hungry?");
        personNeedsMoneyCheckBox.setText("Needs Money?");
        personHousingCheckBox.setText("Needs to go home?");
        personHungryCheckBox.setSelected(person.getGui().isHungry());
        personHungryCheckBox.setEnabled(!person.getGui().isHungry());
        personNeedsMoneyCheckBox.setSelected(person.getGui().needsMoney());
        personNeedsMoneyCheckBox.setEnabled(!person.getGui().needsMoney());
        personHousingCheckBox.setSelected(person.getGui().getNeedsHome());
        personHousingCheckBox.setEnabled(!person.getGui().getNeedsHome());
        infoCustomerLabel.setText(
           "<html><pre>     Name: " + person.getName() + " </pre></html>");
        personInformationPanel.validate();
    }
    
    //Update the last customer
    public void updateLastPerson()
    {
    	if (currentPerson != null)
    	{
	        personHungryCheckBox.setSelected(currentPerson.getGui().isHungry());
	        personHungryCheckBox.setEnabled(!currentPerson.getGui().isHungry());
	        personNeedsMoneyCheckBox.setSelected(currentPerson.getGui().needsMoney());
	        personNeedsMoneyCheckBox.setEnabled(!currentPerson.getGui().needsMoney());
	        personHousingCheckBox.setSelected(currentPerson.getGui().getNeedsHome());
	        personHousingCheckBox.setEnabled(!currentPerson.getGui().getNeedsHome());
	        personInformationPanel.validate();
    	}
    }
    
    //Action Listener
    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == personHungryCheckBox) 
        {
            PersonAgent c = (PersonAgent) currentPerson;
            c.getGui().setHungry();
            personHungryCheckBox.setEnabled(false);
        }
        if (e.getSource() == personNeedsMoneyCheckBox)
        {
        	PersonAgent c = (PersonAgent) currentPerson;
        	c.getGui().setNeedsMoney(true);
        	personNeedsMoneyCheckBox.setEnabled(false);
        }
        if (e.getSource() == personHousingCheckBox)
        {
        	PersonAgent c = (PersonAgent) currentPerson;
        	c.getGui().setNeedsHome(true);
        	personHousingCheckBox.setEnabled(false);
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
    	Bank b = new Bank(new BankGui(), name);
    	buildings.add(b);
    }
    
    //apartment creation
    public void createApartment(String name) {
    	Apartment a = new Apartment(new HousingGui(), name);
    	buildings.add(a);
    }
    
    //Set Person Enabled
    public void setPersonEnabled(PersonAgent p) {
        PersonAgent per = currentPerson;
        if (p.equals(per)) 
        {
            personHungryCheckBox.setEnabled(true);
            personHungryCheckBox.setSelected(false);
        }
}
    
   //Main Function - Sets up the program
    public static void main(String[] args) 
    {    	  	
	      CityGui gui = new CityGui();
	      gui.setVisible(true);
	      
	      /*
	      MarketGui gui4 = new MarketGui();
          gui4.setTitle("Market");
          gui4.setVisible(true);
          gui4.setResizable(false);
          gui4.setDefaultCloseOperation(HIDE_ON_CLOSE);   
          */
    }
    

}
