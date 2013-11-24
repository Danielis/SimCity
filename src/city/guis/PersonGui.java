package city.guis;

import restaurant.gui.Gui;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import city.PersonAgent;

public class PersonGui implements Gui{
	
	//variables
	private boolean isPresent = false;
	
	private boolean isHungry = false;
	private boolean needsmoney = false;
	private boolean goingSomewhere = false;
	
	//finals
	private final int customerSize = 20;
	private final int deltadivider = 100;

	//self agent
	private PersonAgent agent = null;

	//private HostAgent host;
	CityGui gui;
	
	//Coordinates
	Coordinate checkpointA;
	Coordinate checkpointB;
	Coordinate checkpointC;
	Coordinate checkpointD;
	Coordinate checkpointHouse;
	
	Coordinate position;
	Coordinate destination;
	Coordinate outside;
	Coordinate cashier;
	Coordinate waitingroom;
	
	BufferedImage imgTrainer;
	
	//List of tables
    public List<Coordinate> tables = new ArrayList<Coordinate>();

	public PersonGui(PersonAgent c, CityGui gui2){
        
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}
		
		System.out.println("Got to the persongui constructor");
		
		agent = c;
		this.gui = gui2;
		
		checkpointA = new Coordinate(395,250);
		checkpointB = new Coordinate(395,125);
		checkpointC = new Coordinate(320,125);
		checkpointD = new Coordinate(320,100);
		checkpointHouse = new Coordinate(536,473);
		
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
		newG.drawImage(imgTrainer, position.x, position.y, agent.copyOfCityAnimPanel);
	}

	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.msgGoToRestaurant();
		setPresent(true);
	}
	
	public void setNotHungry()
	{
		isHungry = false;
		setPresent(false);
	}
	
	public boolean isHungry() {
		return isHungry;
	}
	
	public void setNeedsMoney(Boolean b)
	{
		this.needsmoney = b;
		agent.msgGoToBank();
		setPresent(true);
	}
	
	public boolean needsMoney()
	{
		return needsmoney;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToHouse()
	{
		System.out.println("Going home.");
		goingSomewhere = true;
		destination = checkpointHouse;
		agent.WaitForAnimation();
		
	}
	
	public void DoGoToCheckpoint(char a)
	{
	      if(a == 'A' || a == 'a')
          {
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
}