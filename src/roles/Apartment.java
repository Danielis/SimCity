package roles;

import java.util.ArrayList;
import java.util.List;

import housing.HousingCustomerAgent;
import housing.HousingWorkerAgent;
import housing.LandlordAgent;
import housing.guis.HousingGui;
import housing.guis.HousingPanel;

import javax.swing.JFrame;

import agent.RestaurantMenu;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.CashierAgent;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;

public class Apartment {
	public HousingGui gui;
	public HousingPanel panel;
	public String name; //Name of the restaurant
    //public Location location;
    public LandlordAgent landlord;
    public List<HousingWorkerAgent> workers = new ArrayList<HousingWorkerAgent>();
    public List<HousingCustomerAgent> tenants = new ArrayList<HousingCustomerAgent>();
    
    //public String customerRole; //value is something like "Restaurant1CustomerRole"
    //public String type;
    
    public Apartment(HousingGui gui, String name)
    {
    	this.gui = gui;
    	this.panel = gui.housingPanel;
    	this.name = name;
        gui.setTitle(name);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}