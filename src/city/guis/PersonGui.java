package city.guis;

import restaurant.gui.Gui;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import roles.Coordinate;
import city.PersonAgent;
import city.Scenario;
import city.PersonAgent.*;
public class PersonGui implements Gui{
	
	//variables
	private boolean isPresent = true;
	
	private boolean isHungry = false;
	//private boolean needsmoney = false;
	private boolean goingHome = false;
	private boolean isWorking = false;
	
	private boolean goingSomewhere = false;
	private boolean isBusy = false;
	
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

		checkpointA = new Coordinate(257,472);//restaurant
		checkpointB = new Coordinate(385,472);//bottom street corner  
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

		checkpointA = new Coordinate(257,472);//restaurant
		checkpointB = new Coordinate(385,472);//bottom street corner  
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
    	if (goingSomewhere)
    	{		
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
    		
            if (position.x < destination.x)
            {
            	direct = "right";
            	setImage();
                position.x += (1 + deltax/deltadivider);
                movementTicker++;
            }
            else if (position.x > destination.x)
            {
            	direct = "left";
            	setImage();
                position.x -= (1 + deltax/deltadivider);
                movementTicker++;
            }

            else if (position.y < destination.y)
            {
            	direct = "down";
            	setImage();
                position.y += (1 + deltay/deltadivider);
                movementTicker++;
            }
            
            else if (position.y > destination.y)
            {
            	direct = "up";
            	setImage();
                position.y -= (1 + deltay/deltadivider);
                movementTicker++;
            }
            if (position.x == destination.x && position.y == destination.y)
            {
            	setImage();
            	goingSomewhere = false;
            	agent.DoneWithAnimation();
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
		position.x = X;
		position.y = Y;
	}

	public void DoGoToLocation(Coordinate entrance) {
		DoGoToLocation(entrance.x, entrance.y);
	}



	

}