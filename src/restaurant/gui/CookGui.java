package restaurant.gui;

import restaurant.CookAgent;
import restaurant.CookAgent.iconState;
import restaurant.CookAgent.myIcon;
import restaurant.HostAgent;
import restaurant.WaiterAgent;

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
	
	//finals
	private final int cookSize = 20;
	private final int deltadivider = 100;

	//self agent
	private CookAgent agent = null;

	//private HostAgent host;
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

	public CookGui(CookAgent c, RestaurantGui gui)
	{
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
        
        
		agent = c;
		this.gui = gui;
		position = new Coordinate(450,125);
    	fridge = new Coordinate(370, 65);
    	grill = new Coordinate(540,85);
    	platingarea = new Coordinate(450,125);  	
    	
    	destination = platingarea;

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
			//The magical delta movement system
			
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
            	agent.DoneWithAnimation();
            }
    	}
	}

	public void draw(Graphics2D g) 
	{
		Graphics2D newG = (Graphics2D)g;
		Color cookColor = new Color(193, 218, 214);
		newG.setColor(cookColor);
		newG.fillRect(position.x, position.y, cookSize, cookSize);
		
		Graphics2D graphic = (Graphics2D)g;
		Coordinate temp_c = new Coordinate(390,145);
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
}
