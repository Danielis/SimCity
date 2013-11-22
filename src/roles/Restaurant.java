package roles;

import agent.RestaurantMenu;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.CashierAgent;

public class Restaurant {
	public String name; //Name of the restaurant
    public Location location;
    public HostAgent host;
    public CookAgent cook;
    CashierAgent cashier;
    public String customerRole; //value is something like "Restaurant1CustomerRole"
    public RestaurantMenu menu;
    public String type;
    
    //add panels
}