package city.guis;

import restaurant.gui.Gui;
import transportation.gui.BusStopGui.Coordinate;

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
	private boolean isBusy = false;
	
	//finals
	//private final int customerSize = 20;
	private final int deltadivider = 100;
	
	int movementTicker = 0;

	//self agent
	private PersonAgent agent = null;

	//private HostAgent host;
	CityGui gui;
	
	//Coordinates
	Coordinate checkpointA;
	Coordinate checkpointB;
	Coordinate checkpointC;
	Coordinate checkpointD;
	Coordinate checkpointE; 
	Coordinate checkpointF; 
	Coordinate checkpointG; 
	Coordinate checkpointH; 
	Coordinate checkpointI; 
	Coordinate checkpointJ; 
	Coordinate checkpointK;
	
	
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
       
		
		//System.out.println("Got to the persongui constructor");
		
		agent = c;
		this.gui = gui2;
		
		checkpointA = new Coordinate(257,474);//restaurant
		checkpointB = new Coordinate(385,474);//bottom street corner  
		checkpointC = new Coordinate(385,362);//middle lower street corner
		checkpointD = new Coordinate(385,282);//middle higher street corner
		checkpointE = new Coordinate(283,282);//in front of market
		checkpointF = new Coordinate(283,265);//Market
		checkpointG = new Coordinate(185,106);//Top street corner
		checkpointH = new Coordinate(319,106);//in front of Apartments
		checkpointI = new Coordinate(319,90);//Apartments
		checkpointJ = new Coordinate(73,106);//in front of bank
		checkpointK = new Coordinate(73,74);//Bank
		
		
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
    
    private void setAnim1() {
		 try
	       {
			 imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer_1.png"));
	       } catch (IOException e ) {}
	}
	
	private void setAnim2() {
		 try
	       {
			 imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer_2.png"));
	       } catch (IOException e ) {}
	}
	
	private void setDefault() {
		 try
	       {
			 imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
	       } catch (IOException e ) {}
	}
	
	public void updatePosition() {
		if (goingSomewhere)
    	{			
			
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
		{
			setDefault();
		}
	}

	public void draw(Graphics2D g) 
	{
		Graphics2D newG = (Graphics2D)g;
		newG.drawImage(imgTrainer, position.x, position.y, agent.CityAnimPanel);
	}

	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.msgGoToRestaurant();
		setPresent(true);
		setBusy(true);
	}
	
	public void setBusy(Boolean x){
		isBusy = x;
	}
	
	public Boolean getBusy(){
		return isBusy;
	}
	
	public void setNotHungry()
	{
		isHungry = false;
		setPresent(false);
		setBusy(false);
	}
	
	public boolean isHungry() {
		return isHungry;
	}
	
	public void setNeedsMoney(Boolean b, String purpose, double amt)
	{
		this.needsmoney = b;
		agent.msgGoToBank(purpose, amt);
		setPresent(true);
		setBusy(true);
	}
	
	public void setShop(Boolean b, String item, double quantity)
	{
		this.needsmoney = b;
		agent.msgGoToMarket(item, quantity);
		setPresent(true);
		setBusy(true);
	}
	
	public boolean needsMoney()
	{
		return needsmoney;
	}

	public void setPresent(boolean p) {
		isPresent = p;
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
	      else if(a == 'E' || a == 'E')
          {
              goingSomewhere = true;
              destination = checkpointE;
              agent.WaitForAnimation();
          }
	      else if(a == 'F' || a == 'f')
          {
              goingSomewhere = true;
              destination = checkpointF;
              agent.WaitForAnimation();
          }
	      else if(a == 'G' || a == 'g')
          {
              goingSomewhere = true;
              destination = checkpointG;
              agent.WaitForAnimation();
          }
	      else if(a == 'H' || a == 'h')
          {
              goingSomewhere = true;
              destination = checkpointH;
              agent.WaitForAnimation();
          }
	      else if(a == 'I' || a == 'i')
          {
              goingSomewhere = true;
              destination = checkpointI;
              agent.WaitForAnimation();
          }
	      else if(a == 'J' || a == 'j')
          {
              goingSomewhere = true;
              destination = checkpointJ;
              agent.WaitForAnimation();
          }
	      else if(a == 'K' || a == 'k')
          {
              goingSomewhere = true;
              destination = checkpointK;
              agent.WaitForAnimation();
          }
	}
	public void DoGoToLocation(int X,int Y){
		goingSomewhere = true;
		setPresent(true);
		this.destination.x = X;
		this.destination.y = Y;
		agent.WaitForAnimation();
	}
	public int getXPosition(){
		return this.position.x;
	}
	public int getYPosition(){
		return this.position.y;
	}
	public void setPosition(int X, int Y){
		this.position.x = X;
		this.position.y = Y;
	}
}