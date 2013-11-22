package roles;

import javax.swing.JFrame;

import agent.RestaurantMenu;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.CashierAgent;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;

public class Restaurant {
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name; //Name of the restaurant
    public Location location;
    public HostAgent host;
    public CookAgent cook;
    public CashierAgent cashier;
    public String customerRole; //value is something like "Restaurant1CustomerRole"
    public RestaurantMenu menu;
    public String type;
    
    public Restaurant(RestaurantGui gui, String name)
    {
    	this.gui = gui;
    	this.panel = gui.restPanel;
    	this.name = name;
        gui.setTitle(name);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}