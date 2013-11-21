package housing.guis;

import housing.HousingCustomerAgent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import restaurant.HostAgent;
import city.PersonAgent;
import city.guis.CityGui;
import city.guis.PersonGui.Coordinate;

public class HousingCustomerGui implements Gui, restaurant.gui.Gui{

	//variables
		private boolean isPresent = false;
		private boolean isHungry = false;
		private boolean goingSomewhere = false;
		
		//finals
		private final int tables_y = 450;
		private final int customerSize = 20;
		private final int deltadivider = 100;
		private final int starting_X = 150;
		private final int table_divider = 100;

		//self agent
		private HousingCustomerAgent agent = null;

		//private HostAgent host;
		HousingGui gui;
		
		private Image avatar;
		
		//Coordinates
		Coordinate position;
		Coordinate destination;
		Coordinate outside;
		Coordinate cashier;
		Coordinate waitingroom;
		
		//images
		//List of tables

		public HousingCustomerGui(HousingCustomerAgent c, HousingGui gui3){
			
	        
			agent = c;
			this.gui = gui3;
			
			try
	        {
	            avatar = ImageIO.read(getClass().getResource("/resources/trainer2.png"));
	        } catch (IOException e ) {}
			
			outside = new Coordinate(-50,105);
	    	position = new Coordinate(-50,105);
	    	cashier = new Coordinate(255, 75);
	    	waitingroom = new Coordinate(140,70);
	    	destination = outside;
	    	
		}
		//UTILITIES ***********************************************
	    public class Coordinate
	    {
	    	int x;
	    	int y;
	    	
	    	Coordinate()
	    	{
	    		x = 0;
	    		y = 0;
	    	}
	    	Coordinate(int a, int b)
	    	{
	    		x = a;
	    		y = b;
	    	}
	    }
	    
		public void updatePosition() {
			if (goingSomewhere)
	    	{
	    		//Do you like my Delta Movement System?
	    		//I thought of it myself :D
	    		//EC PLS
				
	        	int deltax = destination.x - position.x;
	        	int deltay = destination.y - position.y;
	        	
	        	if (deltax < 0) deltax *= -1;
	        	if (deltay < 0) deltay *= -1;
	        	
	            if (position.x < destination.x)
	                position.x += (1 + deltax/deltadivider);
	            else if (position.x > destination.x)
	                position.x -= (1 + deltax/deltadivider);

	            if (position.y < destination.y)
	                position.y += (1 + deltay/deltadivider);
	            else if (position.y > destination.y)
	                position.y -= (1 + deltay/deltadivider);
	            

	            if (position.x == destination.x && position.y == destination.y)
	            {
	            	goingSomewhere = false;
	            	//agent.DoneWithAnimation();
	            }
	    	}
		}

		public void draw(Graphics2D g) 
		{
			Graphics2D newG = (Graphics2D)g;
			Color customerColor = new Color(195, 178, 116);
			newG.setColor(customerColor);
			//newG.fillRect(50, 50, customerSize, customerSize);
			newG.drawImage(avatar, position.x, position.y, agent.copyOfAnimationPanel());			
		}

		public boolean isPresent() {
			return isPresent;
		}
		
		public void setHungry() {
			isHungry = true;
			//agent.msgGotHungry();
			setPresent(true);
		}
		
		public void setNotHungry() {
			isHungry = false;
			setPresent(false);
		}
		
		public boolean isHungry() {
			return isHungry;
		}

		public void setPresent(boolean p) {
			isPresent = p;
		}
		
		public void DoExitRestaurant()
		{
			goingSomewhere = true;
			//System.out.println(agent.getName() + " is leaving.");
			destination = outside;
			//agent.WaitForAnimation();
		}
		
		public void DoGoToCashier()
		{
			goingSomewhere = true;
			//System.out.println(agent.getName() + " is going to cashier.");
			destination = cashier;
			//agent.WaitForAnimation();
		}
		
		public void DoGoToWaitingRoom()
		{
			goingSomewhere = true;
			//System.out.println(agent.getName() + " is going to the waiting room.");
			destination = waitingroom;
			//agent.WaitForAnimation();
		}

}
