package roles;

import javax.swing.JFrame;

import restaurant.ProducerConsumerMonitor.Ticket;
import agent.RestaurantMenu;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.CashierAgent;
import restaurant.ProducerConsumerMonitor;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;

public class Restaurant extends Building{
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
    public ProducerConsumerMonitor theMonitor;
   
    
    //public CookAgent cook;
    //public CashierAgent cashier;
    //public String customerRole; //value is something like "Restaurant1CustomerRole"
    //public RestaurantMenu menu;
    //public String type;
    
    public Restaurant(RestaurantGui gui, String name)
    {
    	this.gui = gui;
    	this.panel = gui.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
        gui.setTitle(name);
        gui.setVisible(false);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
       theMonitor = new ProducerConsumerMonitor();
        
    }
}