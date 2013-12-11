package city.guis;

import restaurant.gui.Gui;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Random;

import javax.imageio.ImageIO;

import roles.Coordinate;
import city.PersonAgent;
import city.Scenario;
import city.PersonAgent.*;

import java.util.Timer;
import java.util.TimerTask;

public class PersonGui implements Gui{
	
	//variables
	private boolean isPresent = true;
	
	private boolean isHungry = false;
	//private boolean needsmoney = false;
	private boolean goingHome = false;
	private boolean isWorking = false;
	
	private boolean goingSomewhere = false;
	private boolean moveCarCalled = false;
	private boolean isBusy = false;
	
	//Semaphore to Pause animation from moving
	boolean collisionstop = false;
	Timer timer = new Timer();
	public boolean part1 = true;
	
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
	Coordinate checkpointM; 
	Coordinate checkpointN; 
	Coordinate checkpointO; 
	Coordinate checkpointP;
	Coordinate checkpointQ;
	Coordinate checkpointR;
	Coordinate checkpointS; 
	Coordinate checkpointT; 
	Coordinate checkpointU; 
	Coordinate checkpointV;
	Coordinate checkpointW;
	Coordinate checkpointX;
	Coordinate destCheckpoint;
	
	
	Coordinate checkpointHouse;

	String direct = "down";
	
	Coordinate position;
	Coordinate destination;
	
	Coordinate outside;
	Coordinate cashier;
	Coordinate waitingroom;
	
	public BufferedImage imgTrainer;
	public BufferedImage imgBubble;
	public boolean drawBubble = false;
	public boolean deathBubble = false;
	public boolean showBubble = false;
	public double bubbleValue = 0;
	public int bubbleIndex = 0;
	
	public boolean playdeath = false;
	public double deathValue = 0;
	
	//List of tables
    public List<Coordinate> tables = new ArrayList<Coordinate>();

	public PersonGui(PersonAgent c, CityGui gui2){
        
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}  
        
        try
        {
        	imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble2.png"));
        } catch (IOException e ) {} 
		
		//System.out.println("Got to the persongui constructor");
		
		agent = c;
		this.gui = gui2;

		checkpointA = new Coordinate(380,90);//left top
		checkpointB = new Coordinate(380,165);
		checkpointC = new Coordinate(380,278);
		checkpointD = new Coordinate(380,347);
		checkpointE = new Coordinate(380,473);
		checkpointF = new Coordinate(380,540);//left bot
		checkpointG = new Coordinate(450,540);//right bot
		checkpointH = new Coordinate(450,473);
		checkpointI = new Coordinate(450,347);
		checkpointJ = new Coordinate(450,278);
		checkpointK = new Coordinate(450,165);
		checkpointL = new Coordinate(450,90);//right top
		
		
		checkpointM = new Coordinate(430,120);//mid-right top
		checkpointN = new Coordinate(430,137);
		checkpointO = new Coordinate(430,300);
		checkpointP = new Coordinate(430,320);
		checkpointQ = new Coordinate(430,492);
		checkpointR = new Coordinate(430,512);//mid-right bot
		checkpointS = new Coordinate(402,522);//mid-left bot
		checkpointT = new Coordinate(402,492);
		checkpointU = new Coordinate(402,320);
		checkpointV = new Coordinate(402,300);
		checkpointW = new Coordinate(402,139);
		checkpointX = new Coordinate(402,120);//mid-left top
		destCheckpoint = checkpointX;

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
    	int x1=r.nextInt(600-50) + 50;
		
    	position = new Coordinate(x1, y1); 
    	
    	cashier = new Coordinate(255, 75);
    	waitingroom = new Coordinate(140,70);
    	destination = outside;
	}
	
	public PersonGui(PersonAgent c, CityGui gui2, boolean b){
        
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}  
        
        try
        {
        	imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble2.png"));
        } catch (IOException e ) {} 
		
		//System.out.println("Got to the persongui constructor");
		
		agent = c;
		this.gui = gui2;
//
//		checkpointA = new Coordinate(257,472);//restaurant
//		checkpointB = new Coordinate(385,472);//bottom street corner  
//		checkpointC = new Coordinate(385,362);//middle lower street corner
//		checkpointD = new Coordinate(385,275);//middle higher street corner
//		checkpointE = new Coordinate(280,275);//in front of market
//		checkpointF = new Coordinate(280,265);//Market
//		checkpointG = new Coordinate(385,106);//Top street corner
//		checkpointH = new Coordinate(319,106);//in front of Apartments
//		checkpointI = new Coordinate(319,90);//Apartments
//		checkpointJ = new Coordinate(73,106);//in front of bank
//		checkpointK = new Coordinate(73,74);//Bank
//
//		checkpointL = new Coordinate(485,474);
//		//Daniel: These are not correct points may have been incorrectly added with a merge from someone else
////		checkpointA = new Coordinate(395,250);
////		checkpointB = new Coordinate(395,125);
////		checkpointC = new Coordinate(320,125);
////		checkpointD = new Coordinate(320,100);
		checkpointHouse = new Coordinate(536,473);
		
		//checkpointA = new Coordinate(395,250);
		//checkpointB = new Coordinate(395,125);
		//checkpointC = new Coordinate(320,125);
		//checkpointD = new Coordinate(320,100);
		//checkpointHouse = new Coordinate(329,88);
		Coordinate bank = new Coordinate(80,74);

		outside = new Coordinate(700, 250);
		
		Random x = new Random();
		int y1;
		if (b)
			y1 = 320;
		else 
			y1 = 320 + x.nextInt()%100;
    	
		Random r = new Random();
    	int x1 = 1000 + r.nextInt()%300;
		
    	position = new Coordinate(x1, y1);

    	cashier = new Coordinate(255, 75);
    	waitingroom = new Coordinate(140,70);
    	destination = outside;
	}
	//UTILITIES ***********************************************
   
    
    private void setAnim1() {
    	
    	if (agent.job.type.equals(JobType.bankHost)){
   		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/bankHost1.png"));
   	       } catch (IOException e ) {}
    	}
    	else if (agent.job.type.equals(JobType.teller)){
    		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/teller1.png"));
   	       } catch (IOException e ) {}
    	}
    	else if (agent.job.type.equals(JobType.restHost)){
      		 try
     	       {
     			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost1.png"));
     	       } catch (IOException e ) {}
       	}
    	else if (agent.job.type.equals(JobType.waiter)){
     		 try
    	       {
    			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/waiter1.png"));
    	       } catch (IOException e ) {}
      	}
    	else if (agent.job.type.equals(JobType.cashier)){
      		 try
     	       {
     			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/cashier1.png"));
     	       } catch (IOException e ) {}
       	}
    	else if (agent.job.type.equals(JobType.cook)){
     		 try
    	       {
    			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/cook1.png"));
    	       } catch (IOException e ) {}
      	}
    	else if (agent.job.type.equals(JobType.crook)){
      		 try
     	       {
     			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/crook1.png"));
     	       } catch (IOException e ) {}
       	}
    	else 
    		try
	       {
			 imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer_1.png"));
	       } catch (IOException e ) {}
	}
	
    private void setAnim2() {
    	if (agent.job.type.equals(JobType.bankHost)){
      		 try
      	       {
      			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/bankHost2.png"));
      	       } catch (IOException e ) {}
       	}
    	else if (agent.job.type.equals(JobType.teller)){
     		 try
    	       {
    			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/teller2.png"));
    	       } catch (IOException e ) {}
     	}
    	else if (agent.job.type.equals(JobType.restHost)){
      		 try
    	       {
    			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost2.png"));
    	       } catch (IOException e ) {}
      	}
    	else if (agent.job.type.equals(JobType.waiter)){
    		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/waiter2.png"));
   	       } catch (IOException e ) {}
     	}
    	else if (agent.job.type.equals(JobType.cashier)){
      		 try
     	       {
     			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/cashier2.png"));
     	       } catch (IOException e ) {}
       	}
    	else if (agent.job.type.equals(JobType.cook)){
    		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/cook2.png"));
   	       } catch (IOException e ) {}
     	}
    	else if (agent.job.type.equals(JobType.crook)){
      		 try
     	       {
     			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/crook2.png"));
     	       } catch (IOException e ) {}
       	}
       	else 
       		try
    	{
    		imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer_2.png"));
    	} catch (IOException e ) {}
    }

    private void setDefault() {
    	if (agent.job.type.equals(JobType.bankHost)){
      		System.out.println("BankHost reached inside"); 
    		try
      	       {
      			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/bankHost3.png"));
      	       } catch (IOException e ) {}
       	}
    	else if (agent.job.type.equals(JobType.teller)){
    		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/teller3.png"));
   	       } catch (IOException e ) {}
    	}
    	else if (agent.job.type.equals(JobType.restHost)){
   		 try
  	       {
  			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost3.png"));
  	       } catch (IOException e ) {}
    	}
    	else if (agent.job.type.equals(JobType.waiter)){
    		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/waiter3.png"));
   	       } catch (IOException e ) {}
     	}
    	else if (agent.job.type.equals(JobType.cashier)){
   		 try
  	       {
  			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/cashier3.png"));
  	       } catch (IOException e ) {}
    	}
    	else if (agent.job.type.equals(JobType.cook)){
    		 try
   	       {
   			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/cook3.png"));
   	       } catch (IOException e ) {}
     	}
    	else if (agent.job.type.equals(JobType.student)){
   		 try
  	       {
  			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/student3.png"));
  	       } catch (IOException e ) {}
    	}
    	else if (agent.job.type.equals(JobType.professor)){
    		if (playdeath)
    		{
		   		 try
		  	       {
		  			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/coma.png"));
		  	       } catch (IOException e ) {}
    		}
    		else
    		{
    			try
		  	       {
		  			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/professor3.png"));
		  	       } catch (IOException e ) {}
    		}
    	}
    	else if (agent.job.type.equals(JobType.crook)){
   		 try
  	       {
  			 imgTrainer = ImageIO.read(getClass().getResource("/resources/globalSprites/crook3.png"));
  	       } catch (IOException e ) {}
    	}
       	else if(agent.hasCar()){
       			setImage();
       	}	
       	else
       		try
    	{
    		imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
    	} catch (IOException e ) {}
    }
    
    public void setBubbleProfessor(double value)
    {
    	double step = 40;
    	if (value < step)	
    	{
    		try
   	       {
   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble10.png"));
   	       } catch (IOException e ) {}
    	}
    	else if (value < step * 2)
    	{
    		try
   	       {
   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble11.png"));
   	       } catch (IOException e ) {}
    	}
    	else if (value < step * 3)
    	{
    		try
   	       {
   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble12.png"));
   	       } catch (IOException e ) {}
    	}
    	
    	else if (value < step * 4)
    	{
    		try
    	       {
    			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble13.png"));
    	       } catch (IOException e ) {}
     		
     		Scenario.getInstance().finish();
    	}
    	else if (value < step * 5)
    	{
     		//this.drawBubble = false;
    	}
    }
    
    public void setDeath()
    {
    	playdeath = true;
    }
    
    public void setBubble(double value)
    {
    	if (value > 0) drawBubble = true;
    	else drawBubble = false;
    	
    	if (part1)
	    {
	    	if (value == 0){
	      		 try
	     	       {
	     			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble0.png"));
	     	       } catch (IOException e ) {}
	       	}
	       	else if (value == 1){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble1.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 2){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble2.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 3){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble3.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 4){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble4.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 5){
	     		 try
	    	       {
	    			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble5.png"));
	    	       } catch (IOException e ) {}
	      	}
	       	else if (value == 6){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble6.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value == 7){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble7.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value == 8){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble8.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value == 9){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble9.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value >= 10)
	      	{
	      		try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble2.png"));
	   	       } catch (IOException e ) {}
	      	}
	    }
    	else
    	{
    		value = value % 11;
    		if (value == 0){
	      		 try
	     	       {
	     			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble2.png"));
	     	       } catch (IOException e ) {}
	       	}
	       	else if (value == 1){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble5.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 2){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/bubble7.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 3){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_1.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 4){
	     		 try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_2.png"));
	   	       } catch (IOException e ) {}
	     	}
	       	else if (value == 5){
	     		 try
	    	       {
	    			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_3.png"));
	    	       } catch (IOException e ) {}
	      	}
	       	else if (value == 6){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_4.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value == 7){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_5.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value == 8){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_6.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value == 9){
	    		 try
	  	       {
	  			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_7.png"));
	  	       } catch (IOException e ) {}
	    	}
	      	else if (value >= 10)
	      	{
	      		try
	   	       {
	   			 imgBubble = ImageIO.read(getClass().getResource("/resources/profscen/b_4.png"));
	   	       } catch (IOException e ) {}
	      	}
    	}
    }

    public void updatePosition() {

    	if (goingSomewhere && !collisionstop)
    	{		
//    		System.out.println("Updating Position");	
    		if (showBubble)
    		{
				bubbleValue++;
				if (bubbleValue > 40)
				{
					bubbleValue = 0;
					Random rand = new Random();
					bubbleIndex = Math.abs(rand.nextInt() % 15);
				}
				
            	setBubble(bubbleIndex);
    		}

    		int deltax = destination.x - position.x;
    		int deltay = destination.y - position.y;

    		if (deltax < 0) deltax *= -1;
    		if (deltay < 0) deltay *= -1;
    		
    		//Move the person, speed based on car or not
    		if(agent.hasCar()){
    			if (Math.abs( position.x - destination.x) <=3 && Math.abs( position.x - destination.x)!=0 ){
    				setImage(); 
    				position.x = destination.x;
    			}
    			else if (position.x < destination.x){
    				direct = "right";
    				setImage();
    				position.x += 3;
    			}
    			else if (position.x > destination.x){
    				direct = "left";
    				setImage();
    				position.x -= 3;
    			}
    			else if (Math.abs( position.y-destination.y ) <=3 && Math.abs( position.y-destination.y )!=0){
    				setImage(); 
    				position.y = destination.y;
    			}
    			else if (position.y < destination.y){
    				direct = "down";
    				setImage();
    				position.y += 3;
    			}
    			else if (position.y > destination.y){
    				direct = "up";
    				setImage(); 
    				position.y -= 3;
    			}
    		}
    		else{
    			if (position.x < destination.x)
    			{
    				direct = "right";
    				setImage();
//    				position.x += (1 + deltax/deltadivider);
    				position.x += 1;
    				movementTicker++;
    			}
    			else if (position.x > destination.x)
    			{
    				direct = "left";
    				setImage();
//    				position.x -= (1 + deltax/deltadivider);
    				position.x -= 1;
    				movementTicker++;
    			}

    			else if (position.y < destination.y)
    			{
    				direct = "down";
    				setImage();
//    				position.y += (1 + deltay/deltadivider);
    				position.y += 1;
    				movementTicker++;
    			}

    			else if (position.y > destination.y)
    			{
    				direct = "up";
    				setImage();   				
//    				position.y -= (1 + deltay/deltadivider);
    				position.y -= 1;
    				movementTicker++;
    			}
    		}
    		//            if(!agent.hasCar()){
    		//            	if (movementTicker < 30)
    		//            	{
    		//            		//setAnim1();
    		//            	}
    		//            	else if (movementTicker < 60)
    		//            	{
    		//            		//setAnim2();
    		//            	}
    		//            	else if (movementTicker >= 60)
    		//            	{
    		//            		movementTicker = 0;
    		//            	}
    		//            }
//    		System.out.println("Looked at the destination equals position scheduler");
//    		if (position.x == destination.x && position.y == destination.y)
//            else if (position.y < destination.y)
//            {
//            	direct = "down";
//            	setImage();
//                position.y += (1 + deltay/deltadivider);
//                movementTicker++;
//            }
//            
//            else if (position.y > destination.y)
//            {
//            	direct = "up";
//            	setImage();
//                position.y -= (1 + deltay/deltadivider);
//                movementTicker++;
//            }
    		//Implementing a pause at lights
    		if(agent.hasCar() && position.x > 375 && position.x < 380 && deltax>0){
    			System.out.println("Should have stopped at corner");
    			goingSomewhere = false;
    			timer.schedule( new TimerTask()
    			{
    				public void run()
    				{	
    					goingSomewhere = true;
//    					releaseGuiAnimation();
    				}
    			}, 2000);
    		}
    		if(agent.hasCar() && position.x > 450 && position.x < 455 && deltax<0){
    			System.out.println("Should have stopped at corner");
    			goingSomewhere = false;
    			timer.schedule( new TimerTask()
    			{
    				public void run()
    				{	
    					goingSomewhere = true;
//    					releaseGuiAnimation();
    				}
    			}, 2000);
    		}
    		if(agent.hasCar() && position.y > 255 && position.y < 260 && deltay<0){
    			System.out.println("Should have stopped at corner");
    			goingSomewhere = false;
    			timer.schedule( new TimerTask()
    			{
    				public void run()
    				{	
    					goingSomewhere = true;
//    					releaseGuiAnimation();
    				}
    			}, 2000);
    		}
    		if(agent.hasCar() && position.y > 470 && position.y < 475 && deltay>0){
    			System.out.println("Should have stopped at corner");
    			goingSomewhere = false;
    			timer.schedule( new TimerTask()
    			{
    				public void run()
    				{	
    					goingSomewhere = true;
//    					releaseGuiAnimation();
    				}
    			}, 2000);
    		}
    		if(agent.hasCar() && position.y > 367 && position.y < 372 && deltay <0){
    			System.out.println("Should have stopped at corner");
    			goingSomewhere = false;
    			timer.schedule( new TimerTask()
    			{
    				public void run()
    				{	
    					goingSomewhere = true;
//    					releaseGuiAnimation();
    				}
    			}, 2000);
    		}
    		if(agent.hasCar() && position.y > 170 && position.y < 175 && deltay<0){
    			System.out.println("Should have stopped at corner");
    			goingSomewhere = false;
    			timer.schedule( new TimerTask()
    			{
    				public void run()
    				{	
    					goingSomewhere = true;
//    					releaseGuiAnimation();
    				}
    			}, 2000);
    		}
    		
    		
            if (position.x == destination.x && position.y == destination.y)
            {
    			
    			if(moveCarCalled){
    				moveCarCalled = false;
    				System.out.println("Entered the destination equals position scheduler");
    				setImage();
    				agent.DoneWithAnimation();
//    				doMoveCar();
    			}
    			else{
    				System.out.println("BLAH BLAH NOT FROM CAR");
    				setImage();
    				goingSomewhere = false;
    				agent.DoneWithAnimation();
    			}
            }
         // Tells transportation company to check for a car on _____ collision
        	if(agent.hasCar()){
        		agent.carMoved();
        	}

    	}
    	else
    	{
    		setDefault();
    	}
    }
    
    private void setImage(){
    	if (agent.hasCar()){
			if (direct.equals("down")){
				try
				{
				imgTrainer = ImageIO.read(getClass().getResource("/resources/ucar.png"));
				} catch (IOException e ) {}
			}

			if (direct.equals("up")){
				try
				{
				imgTrainer = ImageIO.read(getClass().getResource("/resources/dcar.png"));
				} catch (IOException e ) {}
			}
			if (direct.equals("right")){
				try
				{
				imgTrainer = ImageIO.read(getClass().getResource("/resources/rcar.png"));
				} catch (IOException e ) {}
			}
			if (direct.equals("left")){
				try
				{
				imgTrainer = ImageIO.read(getClass().getResource("/resources/lcar.png"));
				} catch (IOException e ) {}
			}
		}
    	else{
		String start = "/resources/globalSprites/";
		String mid = direct;
		String num = "0";
		String end = ".png";
		if (movementTicker >= 50 || !goingSomewhere){
			num = "0";
			movementTicker = 0;
		}
        else if (movementTicker < 25)
        	num = "2";
        else if (movementTicker < 50)
        	num = "1";
    
       // resource/globalSprites/None/left0.png
		String collapse = start + agent.job.type.toString() + "/" + mid + num + end;
		//System.out.println(collapse);
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource(collapse));
	        } catch (IOException e ) {}
    	}
	}

	public void draw(Graphics2D g) 
	{
		
		if (playdeath)
		{
			setBubbleProfessor(deathValue);
			deathValue++;
		}
		
		Graphics2D newG = (Graphics2D)g;
		Graphics2D newG2 = (Graphics2D)g;
		newG.drawImage(imgTrainer, position.x, position.y, agent.CityAnimPanel);
		
		if (drawBubble)
		{
			if(playdeath)
				newG2.drawImage(imgBubble, position.x - 150, position.y - 110, agent.CityAnimPanel);
			else
				newG2.drawImage(imgBubble, position.x - 150, position.y - 50, agent.CityAnimPanel);
		}
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
	      else if(a == 'M' || a == 'm')
          {
              goingSomewhere = true;
              destination = checkpointM;
              agent.WaitForAnimation();
          }
	      else if(a == 'N' || a == 'n')
          {
              goingSomewhere = true;
              destination = checkpointN;
              agent.WaitForAnimation();
          }
	      else if(a == 'O' || a == 'o')
          {
              goingSomewhere = true;
              destination = checkpointO;
              agent.WaitForAnimation();
          }
	      else if(a == 'P' || a == 'p')
          {
              goingSomewhere = true;
              destination = checkpointP;
              agent.WaitForAnimation();
          }
	      else if(a == 'Q' || a == 'Q')
          {
              goingSomewhere = true;
              destination = checkpointQ;
              agent.WaitForAnimation();
          }
	      else if(a == 'R' || a == 'r')
          {
              goingSomewhere = true;
              destination = checkpointR;
              agent.WaitForAnimation();
          }
	      else if(a == 'S' || a == 's')
          {
              goingSomewhere = true;
              destination = checkpointS;
              agent.WaitForAnimation();
          }
	      else if(a == 'T' || a == 't')
          {
              goingSomewhere = true;
              destination = checkpointT;
              agent.WaitForAnimation();
          }
	      else if(a == 'U' || a == 'u')
          {
              goingSomewhere = true;
              destination = checkpointU;
              agent.WaitForAnimation();
          }
	      else if(a == 'V' || a == 'v')
          {
              goingSomewhere = true;
              destination = checkpointV;
              agent.WaitForAnimation();
          }
	      else if(a == 'W' || a == 'w')
          {
              goingSomewhere = true;
              destination = checkpointW;
              agent.WaitForAnimation();
          }
	      else if(a == 'X' || a == 'x')
          {
              goingSomewhere = true;
              destination = checkpointX;
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
	public int getImgX(){
		return imgTrainer.getTileWidth();
	}
	public int getImgY(){
		return imgTrainer.getTileHeight();
	}
	public boolean hasCar(){
		return agent.hasCar();
	}
	public void setPosition(int X, int Y){
		position.x = X;
		position.y = Y;
	}

	public void DoGoToLocation(Coordinate entrance) {
		DoGoToLocation(entrance.x, entrance.y);
	}

	public void pauseGuiAnimation(){
		collisionstop = true;
		//		try{
		//			this.pauseSemaphore.acquire();
		//		} catch (InterruptedException e) {
		//			// no action - expected when stopping or when deadline changed
		//		} catch (Exception e) {
		//			System.out.println("Unexpected exception caught in Agent thread:" + e);
		//		}
	}
	public void releaseGuiAnimation(){
		collisionstop = false;
		//		this.pauseSemaphore.release();
	}
	public void collision(){
		pauseGuiAnimation();
		timer.schedule( new TimerTask()
		{
			public void run()
			{	
				agent.carMoved();
//				releaseGuiAnimation();
			}
		}, 1000);
	}
	public Coordinate doMoveAsCar(){
//		destCheckpoint = getCheckPoint(D);
		goingSomewhere = true;
//		if(position.x == getCheckPoint(D).x && position.y == getCheckPoint(D).y){
//			System.out.println("doMoveAsCar(char) end reached");
//			this.goingSomewhere = false;
//			return destination;
//		}
//		else{
			System.out.println("doMoveAsCar(char) reached");
			doGoToNextCheckPoint(destination);
			return destination;
//		}
	}
	
//	public void doMoveCar(){
//		goingSomewhere = true;
//		if(position.x == destCheckpoint.x && position.y == destCheckpoint.y){
//			moveCarCalled = false;
//			System.out.println("doMoveCar end reached");
//			this.goingSomewhere = false;
//			agent.DoneWithAnimation();
//		}
//		else{
//			System.out.println("doMoveCar else reached");
//			agent.DoneWithAnimation();
//			doGoToNextCheckPoint(destination);
//		}
//	}
	
	public Coordinate getCheckPoint(char a){
		if(a == 'A' || a == 'a')
		{
			return checkpointA;
		}
		else if(a == 'B' || a == 'b')
		{
			return checkpointB;
		}
		else if(a == 'C' || a == 'c')
		{
			return checkpointC;
		}
		else if(a == 'D' || a == 'd')
		{
			return checkpointD;
		}
		else if(a == 'E' || a == 'E')
		{
			return checkpointE;
		}
		else if(a == 'F' || a == 'f')
		{
			return checkpointF;
		}
		else if(a == 'G' || a == 'g')
		{
			return checkpointG;
		}
		else if(a == 'H' || a == 'h')
		{
			return checkpointH;
		}
		else if(a == 'I' || a == 'i')
		{
			return checkpointI;
		}
		else if(a == 'J' || a == 'j')
		{
			return checkpointJ;
		}
		else if(a == 'K' || a == 'k')
		{
			return checkpointK;
		}
		else if(a == 'L' || a == 'l')
		{
			return checkpointL;
		}
		else if(a == 'M' || a == 'm')
		{
			return checkpointM;
		}
		else if(a == 'N' || a == 'n')
		{
			return checkpointN;
		}
		else if(a == 'O' || a == 'o')
		{
			return checkpointO;
		}
		else if(a == 'P' || a == 'p')
		{
			return checkpointP;
		}
		else if(a == 'Q' || a == 'Q')
		{
			return checkpointQ;
		}
		else if(a == 'R' || a == 'r')
		{
			return checkpointR;
		}
		else if(a == 'S' || a == 's')
		{
			return checkpointS;
		}
		else if(a == 'T' || a == 't')
		{
			return checkpointT;
		}
		else if(a == 'U' || a == 'u')
		{
			return checkpointU;
		}
		else if(a == 'V' || a == 'v')
		{
			return checkpointV;
		}
		else if(a == 'W' || a == 'w')
		{
			return checkpointW;
		}
		else if(a == 'X' || a == 'x')
		{
			return checkpointX;
		}
		else{
			return null;
		}
	}

	public void doGoToNextCheckPoint( Coordinate a){
		//Car will only deal with M and higher
		System.out.println("doGoToNextCheckPoint reached");
		moveCarCalled = true;
		if(a == checkpointM)
		{
			goingSomewhere = true;
			destination = checkpointX;
			agent.WaitForAnimation();
		}
		else if(a == checkpointN)
		{
			goingSomewhere = true;
			destination = checkpointM;
			agent.WaitForAnimation();
		}
		else if(a == checkpointO)
		{
			goingSomewhere = true;
			destination = checkpointN;
			agent.WaitForAnimation();
		}
		else if(a == checkpointP)
		{
			goingSomewhere = true;
			destination = checkpointO;	
			agent.WaitForAnimation();
		}
		else if(a == checkpointQ)
		{
			goingSomewhere = true;
			destination = checkpointP;
			agent.WaitForAnimation();
		}
		else if(a == checkpointR)
		{
			goingSomewhere = true;
			destination = checkpointQ;
			agent.WaitForAnimation();
		}
		else if(a == checkpointS)
		{
			goingSomewhere = true;
			destination = checkpointR;
			agent.WaitForAnimation();
		}
		else if(a == checkpointT)
		{
			goingSomewhere = true;
			destination = checkpointS;
			agent.WaitForAnimation();
		}
		else if(a == checkpointU)
		{
			goingSomewhere = true;
			destination = checkpointT;
			agent.WaitForAnimation();
		}
		else if(a == checkpointV)
		{
			System.out.println("V was registered");
			goingSomewhere = true;
			destination = checkpointU;
			agent.WaitForAnimation();
		}
		else if(a == checkpointW)
		{
			System.out.println("W was registered");
			goingSomewhere = true;
			destination = checkpointV;
			agent.WaitForAnimation();
		}
		else if(a == checkpointX)
		{
			goingSomewhere = true;
			destination = checkpointW;
			agent.WaitForAnimation();
		}
	}
	public void removeCar(){
		agent.removeCar();
	}
}