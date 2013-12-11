package restaurant.gui;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import restaurant.interfaces.Host;

public class HostGui implements Gui{
	
	//variables
	private boolean goingSomewhere = false;
	private boolean isPresent = true;
	
	private int movementTicker = 0;
	
	//finals
	private final int cookSize = 20;
	private final int deltadivider = 100;

	//self agent
	private Host agent = null;

	//private HostAgent host;
	RestaurantGui gui;
	
	//Coordinates
	Coordinate position;
	Coordinate destination;
	Coordinate fridge;
	Coordinate grill;
	Coordinate platingarea;
	
	BufferedImage imgHost;

	public HostGui(Host host, RestaurantGui gui)
	{
		try
        {
        	imgHost = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost/down0.png"));
        } catch (IOException e ) {}
        
		agent = host;
		this.gui = gui;
		position = new Coordinate(93,-10);
    	fridge = new Coordinate(370, 55);
    	grill = new Coordinate(535,75);
    	platingarea = new Coordinate(93,10);  	
    	
    	destination = platingarea;
    	
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
    
    private void setAnim1() {
		 try
	       {
			 imgHost = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost/down1.png"));
	       } catch (IOException e ) {}
	}
	
	private void setAnim2() {
		 try
	       {
			 imgHost = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost/down2.png"));
	       } catch (IOException e ) {}
	}
	
	private void setDefault() {
		 try
	       {
			 imgHost = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost/down0.png"));
	       } catch (IOException e ) {}
	}
    
	public void updatePosition() {
		if (goingSomewhere)
    	{
			//The magical delta movement system
			
        	int deltax = destination.x - position.x;
        	int deltay = destination.y - position.y;
        	
        	if (deltax < 0) deltax *= -1;
        	if (deltay < 0) deltay *= -1;
        	
            if (position.x < destination.x)
            {
                position.x += (1 + deltax/deltadivider);
                movementTicker++;
            }
            else if (position.x > destination.x)
            {
                position.x -= (1 + deltax/deltadivider);
                movementTicker++;
            }

            if (position.y < destination.y)
            {
                position.y += (1 + deltay/deltadivider);
                movementTicker++;
            }
            else if (position.y > destination.y)
            {
                position.y -= (1 + deltay/deltadivider);
                movementTicker++;
            }
            
            if (movementTicker < 30)
            {
            	setAnim1();
            }
            else if (movementTicker < 60)
            {
            	setAnim2();
            }
            else if (movementTicker >= 60)
            {
            	movementTicker = 0;
            }
            

            if (position.x == destination.x && position.y == destination.y)
            {
            	goingSomewhere = false;
            	agent.DoneWithAnimation();
            }
    	}
		else
			setDefault();
	}

	public void draw(Graphics2D g) 
	{
		Graphics2D newG = (Graphics2D)g;
		newG.drawImage(imgHost, position.x, position.y, agent.copyOfAnimPanel);
	}

	
	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToPlatingArea()
	{
		goingSomewhere = true;
		destination = platingarea;
		agent.WaitForAnimation();
	}
	
	public void DoGoToGrill()
	{
		goingSomewhere = true;
		destination = grill;
		agent.WaitForAnimation();
	}
	
	public void DoGoToFridge()
	{
		goingSomewhere = true;
		destination = fridge;
		agent.WaitForAnimation();
	}
	public void Disable()
	{
		setPresent(false);
	}
}
