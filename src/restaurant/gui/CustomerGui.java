package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.CustomerAgent.iconState;
import restaurant.HostAgent;
import restaurant.gui.WaiterGui.Coordinate;
import restaurant.interfaces.Customer;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import restaurant.roles.*;
public class CustomerGui implements Gui{
	
	//variables
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean goingSomewhere = false;
	
	//finals
	private final int tables_y = 450;
	private final int customerSize = 20;
	private final int deltadivider = 40;
	private final int starting_X = 150;
	private final int table_divider = 100;
	
	private int movementTicker = 0;

	//self agent
	private CustomerRole agent = null;

	//private HostAgent host;
	RestaurantGui gui;
	
	//Coordinates
	Coordinate position;
	Coordinate destination;
	Coordinate outside;
	Coordinate cashier;
	Coordinate waitingroom;
	
	//images
	BufferedImage imgTrainer;
	BufferedImage imgQuestion;
	BufferedImage imgSteak;
	BufferedImage imgChicken;
	BufferedImage imgSalad;
	BufferedImage imgPizza;
	
	//List of tables
    public List<Coordinate> tables = new ArrayList<Coordinate>();

	public CustomerGui(CustomerRole c, RestaurantGui gui){
		
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}
		
        try
        {
        	imgQuestion = ImageIO.read(getClass().getResource("/resources/question.png"));
        } catch (IOException e ) {}
        
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
		
		outside = new Coordinate(-50,105);
    	position = new Coordinate(-50,105);
    	cashier = new Coordinate(255, 75);
    	waitingroom = new Coordinate(140,70);
    	destination = outside;
    	
    	for (int i = 0; i < 4; i++)
    	{
    		tables.add(new Coordinate(starting_X + table_divider*i, tables_y));
    	}
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
    
	private void setAnim1()
	{
		String temp = "/resources/globalSprites/" + agent.job + "/down1.png";
		 try
	     {
			 imgTrainer = ImageIO.read(getClass().getResource(temp));
	     } catch (IOException e ) {}
	}
	
	private void setAnim2() {
		String temp = "/resources/globalSprites/" + agent.job + "/down2.png";
		 try
	     {
			 imgTrainer = ImageIO.read(getClass().getResource(temp));
	     } catch (IOException e ) {}
	}
	
	private void setDefault() {
		String temp = "/resources/globalSprites/" + agent.job + "/down0.png";
		 try
	     {
			 imgTrainer = ImageIO.read(getClass().getResource(temp));
	     } catch (IOException e ) {}
		
	}

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
		newG.drawImage(imgTrainer, position.x, position.y, agent.copyOfAnimPanel);
		
		// TODO i commented this out - aleena 
		
//		if (agent.icon != iconState.none)
//		{
//			Graphics2D graphic = (Graphics2D)g;
//			Coordinate temp_c = tables.get(agent.mySeat-1);
//			int x = temp_c.x + 15;
//			int y = temp_c.y + 15;
//			if (agent.icon == iconState.question)
//			{
//				graphic.drawImage(imgQuestion,  x,  y, agent.copyOfAnimPanel);
//			}
//			if (agent.icon == iconState.steak)
//			{
//				graphic.drawImage(imgSteak,  x,  y, agent.copyOfAnimPanel);
//			}
//			if (agent.icon == iconState.chicken)
//			{
//				graphic.drawImage(imgChicken, x, y, agent.copyOfAnimPanel);
//			}
//			if (agent.icon == iconState.salad)
//			{
//				graphic.drawImage(imgSalad, x, y, agent.copyOfAnimPanel);
//			}
//			if (agent.icon == iconState.pizza)
//			{
//				graphic.drawImage(imgPizza, x, y, agent.copyOfAnimPanel);
//			}
//		}
		
	}

	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.msgGotHungry();
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
}
