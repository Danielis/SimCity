package restaurantA;

import java.awt.Frame;

import javax.swing.JFrame;

//import restaurantA.ProducerConsumerMonitor;
import restaurantA.gui.RestaurantGui;
import restaurantA.gui.RestaurantPanel;
import roles.*;
import roles.Building.*;

public class RestaurantA extends Building{
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
   // public ProducerConsumerMonitor theMonitor;
    public int numWaitersWorking = 0;
	
	public RestaurantA(restaurantA.gui.RestaurantGui rg, String name)
    {
    	this.gui = rg;
    	this.panel = rg.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
        rg.setTitle(name);
        rg.setVisible(true);
        rg.setState(Frame.NORMAL);
        rg.setResizable(false);
        rg.setAlwaysOnTop(true);
        rg.setAlwaysOnTop(false);
        rg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      // theMonitor = new ProducerConsumerMonitor();
    }

	public boolean hostIsHere() {
		// TODO Auto-generated method stub
		return false;
	}
}
