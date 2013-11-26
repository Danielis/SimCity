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
	private boolean isPresent = true;
	
	private boolean isHungry = false;
	//private boolean needsmoney = false;
	private boolean goingHome = false;
	private boolean isWorking = false;
	
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
	Coordinate checkpointL;
	
	Coordinate checkpointHouse;

	
	Coordinate position;
	Coordinate destination;
	
	Coordinate outside;
	Coordinate cashier;
	Coordinate waitingroom;
	
	public BufferedImage imgTrainer;
	
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
		checkpointD = new Coordinate(385,275);//middle higher street corner
		checkpointE = new Coordinate(280,275);//in front of market
		checkpointF = new Coordinate(280,265);//Market
		checkpointG = new Coordinate(385,106);//Top street corner
		checkpointH = new Coordinate(319,106);//in front of Apartments
		checkpointI = new Coordinate(319,90);//Apartments
		checkpointJ = new Coordinate(73,106);//in front of bank
		checkpointK = new Coordinate(73,74);//Bank

		checkpointL = new Coordinate(485,474);
		//Daniel: These are not correct points may have been incorrectly added with a merge from someone else
//		checkpointA = new Coordinate(395,250);
//		checkpointB = new Coordinate(395,125);
//		checkpointC = new Coordinate(320,125);
//		checkpointD = new Coordinate(320,100);
		checkpointHouse = new Coordinate(536,473);
		

		//checkpointA = new Coordinate(395,250);
		//checkpointB = new Coordinate(395,125);
		//checkpointC = new Coordinate(320,125);
		//checkpointD = new Coordinate(320,100);
		//checkpointHouse = new Coordinate(329,88);
		Coordinate bank = new Coordinate(80,74);

		
		outside = new Coordinate(700, 250);
		
		
		
		Random x = new Random();
    	int y1=x.nextInt(500-50) + 50;
    	
		Random r = new Random();
    	int x1=r.nextInt(500-50) + 50;
		
    	position = new Coordinate(x1, y1);
    	
    	
    	
    	
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
    		if(agent.hasCar()){
    			if (deltax < 0){
    				deltax *= -1;
    				try
    				{
    					imgTrainer = ImageIO.read(getClass().getResource("/resources/lcar.png"));
    				} catch (IOException e ) {}
    			}
    			else if(deltax > 0){
    				try
    				{
    					imgTrainer = ImageIO.read(getClass().getResource("/resources/rcar.png"));
    				} catch (IOException e ) {}
    			}
    			if (deltay < 0){
    				deltay *= -1;
    				try
    				{
    					imgTrainer = ImageIO.read(getClass().getResource("/resources/dcar.png"));
    				} catch (IOException e ) {}
    			}
    			else if(deltay > 0){
    				try
    				{
    					imgTrainer = ImageIO.read(getClass().getResource("/resources/ucar.png"));
    				} catch (IOException e ) {}
    			}
    		}

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
            if(!agent.hasCar()){
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
	public void setWork() {
		isWorking = true;
		agent.msgGoToWork();
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
		//this.needsmoney = b;
		agent.msgGoToBank(purpose, amt);
		setPresent(true);
		setBusy(true);
	}
	

	public void setNeedsHome(boolean b, String purpose) {
		this.goingHome = b;
		agent.msgGoToHome(purpose);
		setPresent(true);
		setBusy(true);
	}
	
	public void setShop(Boolean b, String item, double quantity)
	{
		//this.needsmoney = b;
		agent.msgGoToMarket(item, quantity);
		setPresent(true);
		setBusy(true);
	}
	
	public boolean getNeedsHome()
	{
		return this.goingHome;
	}
	
//	public boolean needsMoney()
//	{
//		return needsmoney;
//	}

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
	      else if(a == 'L' || a == 'l')
          {
              goingSomewhere = true;
              destination = checkpointL;
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