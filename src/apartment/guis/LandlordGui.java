package apartment.guis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import apartment.LandlordRole;
import apartment.interfaces.Landlord;

public class LandlordGui implements Gui, restaurant.gui.Gui{

	//variables
		private boolean isPresent = true;
		private boolean isHungry = false;
		private boolean goingSomewhere = false;
		
		//finals
		private final int deltadivider = 100;

		//self agent
		private LandlordRole agent = null;

		//private HostAgent host;
		ApartmentGui gui;
		
		private Image avatar;
		
		//Coordinates
		Coordinate position;
		Coordinate destination;


		public LandlordGui(Landlord l, ApartmentGui gui3){
			
	        
			agent = (LandlordRole) l;
			this.gui = gui3;
			
			try
	        {
	            avatar = ImageIO.read(getClass().getResource("/resources/trainer2.png"));
	        } catch (IOException e ) {}
			
			position = new Coordinate(365,-50);			
			destination = new Coordinate(365, 25);
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

}
