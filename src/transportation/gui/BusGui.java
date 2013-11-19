package transportation.gui;

import restaurant.gui.Gui;
import transportation.BusAgent;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import city.PersonAgent;
import city.guis.CityGui;

public class BusGui implements Gui{
	
	//variables
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean goingSomewhere = false;
	
	//finals
	private final int customerSize = 20;
	private final int deltadivider = 100;

	//self agent
	private BusAgent agent = null;

	//private HostAgent host;
	CityGui gui;
	
	//Coordinates
	Coordinate checkpointA;
	Coordinate checkpointB;
	Coordinate checkpointC;
	Coordinate checkpointD;
	
	Coordinate position;
	Coordinate destination;
	Coordinate outside;
	Coordinate cashier;
	Coordinate waitingroom;
	
	BufferedImage imgBus;
	
	//List of tables
    public List<Coordinate> tables = new ArrayList<Coordinate>();

	public BusGui(BusAgent c, CityGui gui2){
        
        try
        {
        	imgBus = ImageIO.read(getClass().getResource("/resources/rbus.png"));
        } catch (IOException e ) {}
		
		System.out.println("Got to the Busgui constructor");
		
		agent = c;
		this.gui = gui2;
		
		checkpointA = new Coordinate(395,250);
		checkpointB = new Coordinate(395,125);
		checkpointC = new Coordinate(320,125);
		checkpointD = new Coordinate(320,100);
		
		outside = new Coordinate(700, 250);
    	position = new Coordinate(700, 250);
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
		newG.drawImage(imgBus, position.x, position.y, agent.CityAnimPanel);
	}

	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToCheckpoint(char a)
	{
	      if(a == 'A' || a == 'a')
          {
	    	  System.out.println("Got here E");
              goingSomewhere = true;
              destination = checkpointA;
              agent.WaitForAnimation();
          }
	      else if(a == 'B' || a == 'b')
          {
              goingSomewhere = true;
                  destination = checkpointB;
                  agent.WaitForAnimation();
          }
	      else if(a == 'C' || a == 'c')
          {
              goingSomewhere = true;
                  destination = checkpointC;
                  agent.WaitForAnimation();
          }
	      else if(a == 'D' || a == 'd')
          {
              goingSomewhere = true;
                  destination = checkpointD;
                  agent.WaitForAnimation();
          }
	}
	
	public void NextStop(){
		if( this.position.equals(destination)){
			if(this.position.equals(checkpointA)){
				this.DoGoToCheckpoint('B');
			}
			if(this.position.equals(checkpointB)){
				this.DoGoToCheckpoint('A');
			}
			if(this.position.equals(checkpointC)){

			}
			if(this.position.equals(checkpointD)){

			}
		}
	}
}