package agent;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class RestaurantMenu {
	public Map<Integer, String> menu = new HashMap<Integer, String>();
	
	/*commented out for now
	class Food
	{
		String type; 		//cooking style?
		String name;		//name of item
		float cost;			//cost of item
	}*/
	
	public RestaurantMenu()
	{  
		menu.put(0, "Steak");
		menu.put(1, "Chicken");
		menu.put(2, "Salad");
		menu.put(3, "Pizza");
	}
}
