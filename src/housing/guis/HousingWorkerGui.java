package housing.guis;

import housing.HousingWorkerRole;
import housing.interfaces.HousingWorker;

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

public class HousingWorkerGui implements Gui, restaurant.gui.Gui{

	//variables
		private boolean isPresent = true;
		private boolean isHungry = false;
		private boolean goingSomewhere = false;
		private int seatIndex = 0;
		
		//finals
		private final int tables_y = 450;
		private final int customerSize = 20;
		private final int deltadivider = 100;
		private final int starting_X = 150;
		private final int table_divider = 100;

		//self agent
		private HousingWorkerRole agent = null;

		//private HostAgent host;
		HousingGui gui;
		
		private Image avatar;
		
		//Coordinates
		Coordinate position;
		Coordinate destination;
		Coordinate outside;
		Coordinate workarea;
		
		//images
		//List of tables

		public HousingWorkerGui(HousingWorker p, HousingGui gui3, int i){
			
	        seatIndex = i;
			agent = (HousingWorkerRole) p;
			this.gui = gui3;
			
			try
	        {
	            avatar = ImageIO.read(getClass().getResource("/resources/trainer2.png"));
	        } catch (IOException e ) {}
			
			position = new Coordinate(475,750);			
			destination = new Coordinate(10, 295 + 25 * seatIndex);
	    	workarea = new Coordinate(12,80);
	    	goingSomewhere = true;

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
				//System.out.println("Is this being consistently called?");
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
	            

	            if (position.x == destination.x && position.y == destination.y) {
	            	goingSomewhere = false;
	            	System.out.println("Reached destination!.");
	            	agent.DoneWithAnimation();
	            }
	    	}
		}

		public void draw(Graphics2D g) 
		{
			Graphics2D newG = (Graphics2D)g;
			Color customerColor = new Color(195, 178, 116);
			newG.setColor(customerColor);
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
		
		public void DoGoHome()
		{
			goingSomewhere = true;
			destination = new Coordinate(10, 295 + 25 * seatIndex);
			agent.WaitForAnimation();
		}
		public void DoGoToComplex() {
			goingSomewhere = true;
			destination = new Coordinate(12, 80);
			agent.WaitForAnimation();
		}
		public void DoGoToLandLord() {
			goingSomewhere = true;
			destination = new Coordinate(335, 50);
			agent.WaitForAnimation();
		}

}
