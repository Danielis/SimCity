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
import java.util.TimerTask;

import javax.imageio.ImageIO;

import city.PersonAgent;
import city.guis.CityGui;
import java.util.Timer;


public class BusGui implements Gui{
	
	//variables
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean goingSomewhere = false;
	
	//Semaphore to Pause animation from moving
		boolean collisionstop = false;
		Timer timer = new Timer();
	
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
	Coordinate checkpointE;
	Coordinate checkpointF;
	
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
        	imgBus = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}
		
		agent = c;
		this.gui = gui2;
		
		checkpointA = new Coordinate(402,125);
		checkpointB = new Coordinate(402,300);
		checkpointC = new Coordinate(402,500);
		checkpointD = new Coordinate(430,500);
		checkpointE = new Coordinate(430,300);
		checkpointF = new Coordinate(430,125);
		
		
		outside = new Coordinate(100, 100);
    	position = new Coordinate(100, 100);
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
		if (goingSomewhere && !collisionstop)
    	{	
        	int deltax = destination.x - position.x;
        	int deltay = destination.y - position.y;        
        	
        	if (deltax < 0){
        		deltax *= -1;
        		 try
        	        {
        			 imgBus = ImageIO.read(getClass().getResource("/resources/lbus.png"));
        	        } catch (IOException e ) {}
        	}
        	else if(deltax > 0){
        		 try
     	        {
     			 imgBus = ImageIO.read(getClass().getResource("/resources/rbus.png"));
     	        } catch (IOException e ) {}
        	}
        	if (deltay < 0){
        		deltay *= -1;
        		 try
        	        {
        			 imgBus = ImageIO.read(getClass().getResource("/resources/ubus.png"));
        	        } catch (IOException e ) {}
        	}
        	else if(deltay > 0){
        		try
    	        {
    			 imgBus = ImageIO.read(getClass().getResource("/resources/dbus.png"));
    	        } catch (IOException e ) {}
        	}
        	
        	if (Math.abs( position.x - destination.x) <=2){
            	position.x = destination.x;
            }
        	else if (position.x < destination.x)
//                position.x += (1 + deltax/deltadivider);
            	position.x += 2;
            else if (position.x > destination.x)
//            	position.x -= (1 + deltax/deltadivider);
            	position.x -= 2;
            if (Math.abs( position.y-destination.y ) <=2){
            	position.y = destination.y;
            }
            else if (position.y < destination.y)
//            	position.y += (1 + deltay/deltadivider);
            	position.y += 2;
            else if (position.y > destination.y)
//                position.y -= (1 + deltay/deltadivider);
            	position.y -= 2;
            

            if (position.x == destination.x && position.y == destination.y)
            {
//            	System.out.println(agent.getName() + "Destination reached");
            	goingSomewhere = false;
            	agent.DoneWithAnimation();
            	agent.msgBusGuiMoved();
            }
            agent.carMoved();
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
	
	public void DoGoToPlace()
	{
        goingSomewhere = true;
        destination = checkpointA;
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
	      else if(a == 'E' || a == 'e')
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
	}
	
	public void NextStop(){
		if(this.position.x == checkpointA.x && this.position.y == checkpointA.y){
			this.DoGoToCheckpoint('B');
		}
		else if(this.position.x == checkpointB.x && this.position.y == checkpointB.y){
			this.DoGoToCheckpoint('C');
		}
		else if(this.position.x == checkpointC.x && this.position.y == checkpointC.y){
			this.DoGoToCheckpoint('D');
		}
		else if(this.position.x == checkpointD.x && this.position.y == checkpointD.y){
			this.DoGoToCheckpoint('E');
		}
		else if(this.position.x == checkpointE.x && this.position.y == checkpointE.y){
			this.DoGoToCheckpoint('F');
		}
		else if(this.position.x == checkpointF.x && this.position.y == checkpointF.y){
			this.DoGoToCheckpoint('A');
		}
		else
			this.DoGoToCheckpoint('A');
			
	}
	public int getXPosition(){
		return this.position.x;
	}
	public int getYPosition(){
		return this.position.y;
	}
	public int getImgX(){
		return imgBus.getTileWidth();
	}
	public int getImgY(){
		return imgBus.getTileHeight();
	}
	public void setPosition(int X, int Y){
		this.position.x = X;
		this.position.y = Y;
	}
	public void pauseGuiAnimation(){
		collisionstop = true;
	}
	public void releaseGuiAnimation(){
		collisionstop = false;
	}
	public void collision(){
		pauseGuiAnimation();
		timer.schedule( new TimerTask()
		{
			public void run()
			{	
				agent.carMoved();
			}
		}, 1000);
	}
	public BusAgent getAgent(){
		return agent;
	}
}