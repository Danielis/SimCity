package roles;

import agent.RestaurantMenu;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.CashierAgent;

public class Restaurant {
	String name; //Name of the restaurant
    Location location;
    HostAgent host;
    CookAgent cook;
    CashierAgent cashier;
    String customerRole; //value is something like "Restaurant1CustomerRole"
    RestaurantMenu menu;
    String type;
}