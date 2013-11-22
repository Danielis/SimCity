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
import roles.Restaurant;

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
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    public Vector<Restaurant> restaurants = new Vector<Restaurant>();
    private Vector<PersonAgent> people = new Vector<PersonAgent>();
    
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

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
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(personPanel);
        add(group);
    }
   
    //Displays the person information
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
    
	//Adds a person to the city
    public void addPerson(String name) 
    {
    	System.out.println("Got here A");
		PersonAgent p = new PersonAgent(name);	
		PersonGui g = new PersonGui(p, gui);
		gui.cityAnimationPanel.addGui(g);
		p.setGui(g);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setRestaurants(restaurants);
		people.add(p);
		p.startThread();
    }
    
    public void setRestaurants(Vector<Restaurant> res)
    {
    	restaurants = res;
    }
    
    public void pause()
    {

    }
    
    public void resume()
    {

    }

    public void refresh()
    {
    	gui.updateLastPerson();
    }
}
