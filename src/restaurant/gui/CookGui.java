package restaurant.gui;

import restaurant.CookAgent;
import restaurant.CookAgent.iconState;
import restaurant.CookAgent.myIcon;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Cook;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class CookGui implements Gui{
	
	//variables
	private boolean goingSomewhere = false;
	private boolean isPresent = true;
	
	private int movementTicker = 0;
	
	//finals
	private final int deltadivider = 100;

	//self agent
	private Cook agent = null;

	RestaurantGui gui;
	
	//Coordinates
	Coordinate position;
	Coordinate destination;
	Coordinate fridge;
	Coordinate grill;
	Coordinate platingarea;
	
	//images
	BufferedImage imgSteak;
	BufferedImage imgChicken;
	BufferedImage imgSalad;
	BufferedImage imgPizza;
	
	BufferedImage imgCook;

	public CookGui(Cook cook, RestaurantGui gui)
	{
		try
        {
        	imgCook = ImageIO.read(getClass().getResource("/resources/globalSprites/cook/down0.png"));
        } catch (IOException e ) {}
		
        try
        {
        	imgSteak = ImageIO.read(getClass().getResource("/resources/steak.png"));
        } catch (IOException e ) {}
        
        try
        {
        	imgChicken= ImageIO.read(getClass().getResource("/resources/chicken.png"));
        } catch (IOException e ) {}
        
        try
        {
        	imgSalad = ImageIO.read(getClass().getResource("/resources/salad.png"));
        } catch (IOException e ) {}
        
        try
        {
        	imgPizza = ImageIO.read(getClass().getResource("/resources/pizza.png"));
        } catch (IOException e ) {}
        
        
		agent = cook;
		this.gui = gui;
		position = new Coordinate(450,90);
    	fridge = new Coordinate(370, 55);
    	grill = new Coordinate(535,75);
    	platingarea = new Coordinate(450,110);  	
    	
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
			 imgCook = ImageIO.read(getClass().getResource("/resources/globalSprites/cook/down1.png"));
	       } catch (IOException e ) {}
	}
	
	private void setAnim2() {
		 try
	       {
			 imgCook = ImageIO.read(getClass().getResource("/resources/globalSprites/cook/down2.png"));
	       } catch (IOException e ) {}
	}
	
	private void setDefault() {
		 try
	       {
			 imgCook = ImageIO.read(getClass().getResource("/resources/globalSprites/cook/down0.png"));
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
		//Color cookColor = new Color(193, 218, 214);
		//newG.setColor(cookColor);
		//newG.fillRect(position.x, position.y, cookSize, cookSize);
		newG.drawImage(imgCook, position.x, position.y, agent.copyOfAnimPanel);
		
		Graphics2D graphic = (Graphics2D)g;
		Coordinate temp_c = new Coordinate(390,135);
		int x = temp_c.x;
		int y = temp_c.y;
		
		int i = 0;
		synchronized (agent.icons)
		{
			for(myIcon icon : agent.icons)
			{
				if (icon.state == iconState.steak)
				{
					graphic.drawImage(imgSteak,  x + i*30,  y, agent.copyOfAnimPanel);
				}
				if (icon.state == iconState.chicken)
				{
					graphic.drawImage(imgChicken, x + i*30, y, agent.copyOfAnimPanel);
				}
				if (icon.state == iconState.salad)
				{
					graphic.drawImage(imgSalad, x + i*30, y, agent.copyOfAnimPanel);
				}
				if (icon.state == iconState.pizza)
				{
					graphic.drawImage(imgPizza, x + i*30, y, agent.copyOfAnimPanel);
				}
				i++;
			}
		}
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

	
	public void takeitem(WaiterAgent w)
	{
		synchronized(agent.icons)
		{
			for (myIcon icon : agent.icons)
			{
				if(icon.w == w)
				{
					agent.icons.remove(icon);
				}
			}
		}
	}
	
	public void Disable()
	{
		setPresent(false);
	}
}
