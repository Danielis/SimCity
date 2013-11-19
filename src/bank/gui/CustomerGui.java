package bank.gui;

import bank.CustomerAgent;
import bank.CustomerAgent.iconState;
import bank.HostAgent;
import bank.gui.Coordinate;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class CustomerGui implements Gui{
	
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
	private CustomerAgent agent = null;

	//private HostAgent host;
	BankGui gui;
	
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
	
	//images
	BufferedImage imgTrainer;
	
	//List of tables
    public List<Coordinate> tables = new ArrayList<Coordinate>();
 
    
	public CustomerGui(CustomerAgent c, BankGui gui){
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
                setLeftImage();
            }
            else if (position.y < destination.y){
                position.y += (1 + deltay/deltadivider);
                setDownImage();
            }
            else if (position.y > destination.y){
                position.y -= (1 + deltay/deltadivider);
                setUpImage();
            }
            else if (position.x < destination.x){
                position.x += (1 + deltax/deltadivider);
                setRightImage();
            }

            if (position.x == destination.x && position.y == destination.y)
            {
            	goingSomewhere = false;
            	setUpImage();
            	agent.DoneWithAnimation();
            }
    	}
	}

	private void setLeftImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/left.png"));
	        } catch (IOException e ) {}
	}

	private void setRightImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/right.png"));
	        } catch (IOException e ) {}
	}

	private void setDownImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/down.png"));
	        } catch (IOException e ) {}
		
	}

	private void setUpImage() {
		 try
	        {
	        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/up.png"));
	        } catch (IOException e ) {}
		
	}

	public void draw(Graphics2D g) 
	{
		Graphics2D newG = (Graphics2D)g;
		if (isPresent)
		newG.drawImage(imgTrainer, position.x, position.y, agent.copyOfAnimPanel);

		
		
	}

	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setAction(String type, String amount) {
		isBusy = true;
		//int temp = Integer.parseInt(amount);
		double temp = Double.parseDouble(amount);
		temp =  Math.round(temp * 100) / 100.0d;
		agent.msgWantsTransaction(type, temp);
		setPresent(true);
	}
	
	public void finishedTransaction()
	{
		isBusy = false;
		//setPresent(false);
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
		System.out.println(agent.getName() + " is going to table " + tableNum);
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
