package restaurant.gui;

import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.WaiterAgent.MyCustomer;
import restaurant.WaiterAgent.myState;
import restaurant.gui.CustomerGui.Coordinate;

import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import agent.RestaurantMenu;

public class WaiterGui implements Gui {
	
	//finals
	private final int tables_y = 450;
	private final int waiterSize = 20;
	private final int deltadivider = 100;
	private final int starting_X = 150;
	private final int table_divider = 100;
	
	int index; //used for unique locations

    private WaiterAgent agent = null;
    RestaurantGui gui;

    Boolean isPresent;
    Boolean onBreak = false;
    
    Coordinate position;
    Coordinate destination;
    
    Coordinate host;
    Coordinate cook;
    Coordinate cashier;
    Coordinate waitingroom;
    Coordinate homeposition;
    
    Boolean goingSomewhere = false;
    
    public List<Coordinate> tables = new ArrayList<Coordinate>();

    public WaiterGui(WaiterAgent w, RestaurantGui gui, int in) {
    	
    	setPresent(true);
    	isPresent = true;
    	
        this.agent = w;
    	this.gui = gui;
    	this.index = in;
    	
    	host = new Coordinate(-30,125);
    	cook = new Coordinate(450,170);
    	cashier = new Coordinate(255, 75);
    	position = new Coordinate(-30,125);
    	waitingroom = new Coordinate(170,90);
    	homeposition = new Coordinate(index * 40 + 200, 190);
    	destination = homeposition;
    	
    	for (int i = 0; i < HostAgent.NTABLES; i++)
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

    public void draw(Graphics2D g) {
    	Color waiterColor = new Color(193, 218, 214);
        g.setColor(waiterColor);
        g.fillRect(position.x, position.y, waiterSize, waiterSize);
    }
    
    public void AskForBreak()
    {
    	agent.host.msgIdLikeToGoOnBreak(agent);
    }
    
    public void AskGoOffBreak()
    {
    	agent.host.msgIdLikeToGetOffBreak(agent);
    }

    public boolean isPresent() {
		return isPresent;
	}
	
	public void setOnBreak() 
	{
		//agent.msgSetOnBreak();
		agent.isOnBreak = true;
	}
	
	public void setOffBreak()
	{
		//agent.msgSetOffBreak();
		agent.isOnBreak = false;
	}
	
	public boolean isOnBreak() {
		return agent.isOnBreak;
	}
	
	public void setBreak(Boolean b)
	{
		onBreak = b;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
//ANIMATION FUNCTIONS ****************************************
	
	public void DoGoToHost()
	{
		goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the host");
		destination = host;
		agent.WaitForAnimation();
	}
	public void DoShowTable(int tableNum, MyCustomer mc)
	{
		mc.c.msgFollowMe(this.agent, tableNum, new RestaurantMenu());
    	goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the table");
		int i_temp = tableNum -1;
		Coordinate c_temp = tables.get(i_temp);
		destination = new Coordinate(c_temp.x+45, c_temp.y-15);
		agent.WaitForAnimation();
	}
	public void DoGoToTable(int tableNum)
	{
    	goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the table");
		int i_temp = tableNum -1;
		Coordinate c_temp = tables.get(i_temp);
		destination = new Coordinate(c_temp.x+45, c_temp.y-15);
		agent.WaitForAnimation();
	}
	public void DoGoToCook()
	{
    	goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the cook");
		destination = cook;
		agent.WaitForAnimation();
	}
	
	public void DoGoToCashier()
	{
    	goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the cashier");
		destination = cashier;
		agent.WaitForAnimation();
	}
	
	public void DoGoToWaitingRoom()
	{
    	goingSomewhere = true;
		System.out.println(agent.getName() + " is going to the waiting room");
		destination = waitingroom;
		agent.WaitForAnimation();
	}
	
	public void DoGoToHomePosition()
	{
    	goingSomewhere = true;
		//System.out.println(agent.getName() + " is going to the waiting room");
		destination = homeposition;
		agent.WaitForAnimation();
	}
}
