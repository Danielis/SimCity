package market.gui;

import market.MarketCustomerRole;
//import market.MarketCustomerAgent.iconState;
import market.MarketHostAgent;
import market.gui.Coordinate;
import market.interfaces.MarketCustomer;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class MarketCustomerGui implements Gui{
	
	//variables
	private boolean isPresent = false;
	private boolean isBusy = false;
	private boolean goingSomewhere = false;
	
	//finals
	private final int tables_y = 150;
	private final int deltadivider = 100;
	private final int starting_X = 146;
	private final int table_divider = 127;

	//self agent
	private MarketCustomerRole agent = null;
	
	

	//private HostAgent host;
	MarketGui gui;
	
	//Coordinates
	Coordinate destination;
	
	Coordinate outside = new Coordinate(296,435);
	Coordinate position = new Coordinate(296,435);
	Coordinate cashier = new Coordinate(255, 75);
	Coordinate waitingroom = new Coordinate(296, 300);
	Coordinate botLeft = new Coordinate(170, 300);
	Coordinate botRight = new Coordinate(430, 300);
	Coordinate topLeft = new Coordinate(170, 160);
	Coordinate topRight = new Coordinate(430, 180);
	Coordinate topMiddle = new Coordinate(290, 160);
	Timer timer = new Timer();
	
	//images
	BufferedImage imgTrainer;
	BufferedImage speechBubble;
	
	Boolean showSpeechBubble = false;
	Coordinate speechBubbleLoc;
	
	String direct = "up";
	int move = 0;
	
	//List of tables
    public List<Coordinate> tables = new ArrayList<Coordinate>();
 
    
	public MarketCustomerGui(MarketCustomerRole c, MarketGui gui){
		for (int i = 0; i < 5; i++)
		{
			tables.add(new Coordinate(starting_X + table_divider*i, tables_y));
		}
		
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}
        
        
		agent = c;
		this.gui = gui;
		
		
    	destination = outside;
	}
	//UTILITIES ***********************************************
    
	public void updatePosition() {
		if (goingSomewhere)
    	{
    		//Do you like my Delta Movement System?
    		//I thought of it myself :D
    		//EC PLS
			
        	int deltax = destination.x - position.x;
        	int deltay = destination.y - position.y;
        	
        	if (deltax < 0) deltax *= -1;
        	if (deltay < 0) deltay *= -1;
        	
        	if (position.x > destination.x){
                position.x -= (1 + deltax/deltadivider);
                direct = "left";
                setImage(false);
                move++;
            }
            else if (position.y < destination.y){
                position.y += (1 + deltay/deltadivider);
                direct = "down";
                setImage(false);
                move++;
            }
            else if (position.y > destination.y){
                position.y -= (1 + deltay/deltadivider);
                direct = "up";
                setImage(false);
                move++;
            }
            else if (position.x < destination.x){
                position.x += (1 + deltax/deltadivider);
                direct = "right";
                setImage(false);
                move++;

            }

            if (position.x == destination.x && position.y == destination.y)
            {
            	goingSomewhere = false;
            	setImage(true);
            	agent.DoneWithAnimation();
            }
            
    	}
	}
	
	private void setImage(Boolean noMove){
		String start = "/resources/globalSprites/";
		String mid = direct;
		String num = "0";
		String end = ".png";
		if (move >= 50 || !goingSomewhere){
			num = "0";
			move = 0;
		}
        else if (move < 25)
        	num = "2";
        else if (move < 50)
        	num = "1";
    
       // resource/globalSprites/None/left0.png
		String collapse = start + agent.job + "/" + mid + num + end;
		//System.out.println(collapse);
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource(collapse));
	        } catch (IOException e ) {}
	}

	private void setLeftImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/left1.png"));
	        } catch (IOException e ) {}
	}

	private void setRightImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/right1.png"));
	        } catch (IOException e ) {}
	}

	private void setDownImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/down1.png"));
	        } catch (IOException e ) {}
		
	}

	private void setUpImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/up1.png"));
	        } catch (IOException e ) {}
		
	}
	
	public void setSpeechBubble(String temp){
	//	System.out.println("setting");
		speechBubbleLoc= new Coordinate(position.x - 10, position.y + 7);
		showSpeechBubble = true;
		temp = "/resources/bankSprites/speech/" + temp +".png";
		 try
	        {
	        	speechBubble = ImageIO.read(getClass().getResource(temp));
	        } catch (IOException e ) {} 
		 
		 timer.schedule( new TimerTask()
			{
				public void run()
				{				
					
					showSpeechBubble = false;
				}
			}, 2000);
	}

	public void draw(Graphics2D g) 
	{
		//System.out.println("draw called");
		Graphics2D newG = (Graphics2D)g;
		if (isPresent)
			newG.drawImage(imgTrainer, position.x, position.y, agent.copyOfAnimPanel);


		if (showSpeechBubble){
			newG.drawImage(speechBubble, speechBubbleLoc.x, speechBubbleLoc.y, agent.copyOfAnimPanel);
		}
	}

	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setAction() {
		isBusy = true;
		setPresent(true);
//		double temp = Double.parseDouble(amount);
//		temp =  Math.round(temp * 100) / 100.0d;
//		int temp2 = (int) temp;
		//System.out.println("set act");
		agent.msgWantsToBuy();
	}
	
	public void finishedTransaction()
	{
		isBusy = false;
		setPresent(false);
	}
	public boolean isHungry() {
		return isBusy;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToSeat(int tableNum)
	{
    	goingSomewhere = true;
		System.out.println(agent.getName() + " is going to worker " + tableNum);
		int i_temp = tableNum -1;
		Coordinate c_temp = tables.get(i_temp);
		destination = new Coordinate(c_temp.x+15, c_temp.y-15);
		agent.WaitForAnimation();
	}
	
	
	
	public void DoExitRestaurant()
	{
		goingSomewhere = true;
		System.out.println(agent.getName() + " is leaving.");

		destination = outside;
		agent.WaitForAnimation();
		
		setPresent(false);
	}
	
	public void DoGoToCashier()
	{
		goingSomewhere = true;
		System.out.println(agent.getName() + " is going to cashier.");
		destination = cashier;
		agent.WaitForAnimation();
		
	}
	
	public void DoGoToWaitingRoom()
	{
		goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the waiting room.");
		destination = waitingroom;
		agent.WaitForAnimation();
	}
	
	public void DoGoToBotLeft(){
		goingSomewhere = true;
		destination = botLeft;
		agent.WaitForAnimation();
	}
	
	public void DoGoToBotRight(){
		goingSomewhere = true;
		destination = botRight;
		agent.WaitForAnimation();
	}
	
	public void DoGoToTopLeft(){
		goingSomewhere = true;
		destination = topLeft;
		agent.WaitForAnimation();
	}
	
	public void DoGoToTopRight(){
		goingSomewhere = true;
		destination = topRight;
		agent.WaitForAnimation();
	}
	
	public void DoGoToTopMiddle(){
		goingSomewhere = true;
		destination = topMiddle;
		agent.WaitForAnimation();
	}
	
	public void shuffle(int x, int y){
		goingSomewhere = true;
        destination.x = waitingroom.x;
        destination.y = 300 + y;
	}

	
}
