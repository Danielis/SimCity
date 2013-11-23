package city.guis;

import restaurant.CustomerAgent;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.MarketAgent;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
//adding transportation
import transportation.*;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class CityPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Oprah");
    private HostGui hostGui = new HostGui(host);
    public CashierAgent cashier = new CashierAgent("Squidward");
    private MarketAgent market1 = new MarketAgent("Market 1");
    private MarketAgent market2 = new MarketAgent("Market 2");
    private MarketAgent market3 = new MarketAgent("Market 3");
    public CookAgent cook = new CookAgent("Gordon Ramsay");
    private CookGui cookGui;
    public RestaurantPanel restPanel;
    public TransportationCompanyAgent metro = new TransportationCompanyAgent("Metro");
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    private Vector<PersonAgent> people = new Vector<PersonAgent>();
    
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    private CityListPanel personPanel = new CityListPanel(this, "People");
    private JPanel group = new JPanel();
        
    //Image Related
    ImageIcon iconOwner;
    JLabel picOwner;
    ImageIcon iconMenu;
    JLabel picMenu;
    
    private CityGui gui; //reference to main gui
        

    public CityPanel(CityGui gui) {
        this.gui = gui;
        //cookGui = new CookGui(cook, gui);
        host.setGui(hostGui);
        cook.setGui(cookGui);
        
        iconOwner = new ImageIcon(getClass().getResource("/resources/menu_header.png"));
        picOwner = new JLabel(iconOwner);
        iconMenu = new ImageIcon(getClass().getResource("/resources/menu.png"));
        picMenu = new JLabel(iconMenu);
        
        market1.startThread();
        market2.startThread();
        market3.startThread();
        
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        market3.setCashier(cashier);
        
        market1.setCook(cook);
        market2.setCook(cook);
        market3.setCook(cook);
        
        cook.setMarkets(market1);
        cook.setMarkets(market2);
        cook.setMarkets(market3);
        
        host.startThread();
        cashier.startThread();
        cook.startThread();
        metro.startThread();
        
        /*
        //Setting Metro and adding buses and busStops MODIFY FOR CITY CHANGES
        metro = new TransportationCompanyAgent("Metro");
        metro.startThread();
        
        BusAgent B = new BusAgent("Bus1");
        BusGui Bg = new BusGui(B,gui);
        gui.cityAnimationPanel.addGui(Bg);
        B.setGui(Bg);
        Bg.setPosition(295,250);
        Bg.isPresent();
        B.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBus(B);
        B.startThread();

        
        BusStopAgent S = new BusStopAgent("BusStop1");
        BusStopGui Sg = new BusStopGui(S,gui);
        gui.cityAnimationPanel.addGui(Sg);
        S.setGui(Sg);
        Sg.setPosition(395,250);
        Sg.isPresent();
        S.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S);
        Sg.isPresent();
        S.startThread();*/
        
        //Need to set bus destination to A so that it begins it's route then it should act alone, BusStop should react to person reaching
        // A by having person first move to Bus Stop A. This can be done by passing a command to go to Bus Stop A from cityGui.tranportationCompany 
        // Maybe people have a cityGui to know where the bus stops are
        //placing error to mark place
        
        //B.getGui().DoGoToCheckpoint('a'); // This makes 2 frames not show?
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(personPanel);

        //initRestLabel();
        //add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        /*old
         * 
         * label.setText(
                "<html>"
	                + "<h3><u>Tonight's Staff</u></h3>"
	                + "<table>"
	                	+ "<tr><td>Host:</td><td>" + host.getName() + "</td></tr>"
        			+ "</table>"
	                + "<h3><u> Menu</u></h3>"
	                + "<table>"
		                + "<tr><td>Steak</td><td>$15.99</td></tr>"
		                + "<tr><td>Chicken</td><td>$10.99</td></tr>"
		                + "<tr><td>Salad</td><td>$5.99</td></tr>"
		                + "<tr><td>Pizza</td><td>$8.99</td></tr>"
	                + "</table><br>"
                + "</html>");
         */

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(picMenu, BorderLayout.SOUTH);
        restLabel.add(picOwner, BorderLayout.NORTH);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showPersonInfo(String name)
    {
        for (int i = 0; i < people.size(); i++) {
        	
            PersonAgent temp = people.get(i);
            if (temp.getName() == name)
            {
                personPanel.updatePerson(temp);
                gui.updatePersonInformationPanel(temp);
                personPanel.updatePerson(temp);
            }
        }
    }
    
   

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
//    public void addCustomer(String name) 
//    {
//		CustomerAgent c = new CustomerAgent(name);	
//		CustomerGui g = new CustomerGui(c, gui);
//		gui.animationPanel.addGui(g);
//		c.setHost(host);
//		c.setCashier(cashier);
//		c.setGui(g);
//		c.setAnimPanel(gui.animationPanel);
//		customers.add(c);
//		c.startThread();
//    }
    
    public void addPerson(String name) 
    {
    	System.out.println("Got here A");
		PersonAgent p = new PersonAgent(name);
		PersonGui g = new PersonGui(p, gui);
		gui.cityAnimationPanel.addGui(g);
		p.setGui(g);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setRestaurantPanel(restPanel);
		people.add(p);
		p.startThread();
    }
    
    public void createBusSystem() // Once AI and adding implementations are done, we can make this just an addBus or addStop function
    {
        System.out.println("Got to create bus system");
        BusAgent B = new BusAgent("Bus1");
        BusGui Bg = new BusGui(B,gui);
        gui.cityAnimationPanel.addGui(Bg);
        B.setCompany(metro);
        B.setGui(Bg);
        Bg.setPosition(395,250);
        Bg.setPresent(true);
        B.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBus(B);
        B.startThread();
        
        BusStopAgent S = new BusStopAgent("BusStop1");
        BusStopGui Sg = new BusStopGui(S,gui);
        gui.cityAnimationPanel.addGui(Sg);
        S.setCompany(metro);
        S.setGui(Sg);
        Sg.setPosition(395,250);
        S.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S);
        Sg.setPresent(true);
        S.startThread();
        
        BusStopAgent S2 = new BusStopAgent("BusStop2");
        BusStopGui S2g = new BusStopGui(S2,gui);
        gui.cityAnimationPanel.addGui(S2g);
        S2.setGui(S2g);
        S2g.setPosition(395,125);
        S2.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S2);
        S2g.setPresent(true);
        S2.startThread();
        
        /*
        this.addPerson("Mark");
        people.lastElement().getGui().DoGoToCheckpoint('a');
        people.lastElement().msgGoToStop(metro.stops.get(0), metro.stops.get(1));
        */
    }
    public void beginBusMovement(){
    	//metro.buses.get(0).getGui().DoGoToCheckpoint('A');
    	//metro.buses.get(0).msgNextStop();
    }
    
    public void sendPersonToStop(){
    	this.personPanel.addPerson("TestPerson");
    	PersonAgent testPerson = people.lastElement();
    	testPerson.msgGoToStop(metro.stops.get(0), metro.stops.get(1));
    }
    public void setRestPanel(RestaurantPanel panel)
    {
    	restPanel = panel;
    }
    
    public void pause()
    {

    }
    
    public void resume()
    {

    }

    public void refresh()
    {
    	gui.updateLastCustomer();
    }
}
