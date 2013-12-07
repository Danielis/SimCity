package restaurantA;

import java.awt.Frame;

import javax.swing.JFrame;


//import restaurantA.ProducerConsumerMonitor;
import restaurantA.gui.RestaurantGui;
import restaurantA.gui.RestaurantPanel;
import roles.*;
import roles.Building.*;
import city.*;

public class RestaurantA extends RestBase{
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name; //Name of the restaurant
    public Coordinate location;
   // public ProducerConsumerMonitor theMonitor;
    public int numWaitersWorking = 0;
	public String owner = "Aleena";
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

	@Override
	public Boolean isOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean needsHost() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sethost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needsCook() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needsCashier() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needsWaiter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWaiter() {
		// TODO Auto-generated method stub
		
	}
}
