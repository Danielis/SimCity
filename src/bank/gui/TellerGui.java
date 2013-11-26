package bank.gui;

import bank.BankHostRole;
import bank.interfaces.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;


public class TellerGui implements Gui {
	
	//finals
	private final int tables_y = 150;
	private final int waiterSize = 20;
	private final int deltadivider = 100;
	private final int starting_X = 150;
	private final int table_divider = 100;
	
	int index; //used for unique locations
	//images
	BufferedImage imgTrainer;
		
    private Teller agent = null;
    BankGui gui;

    Boolean isPresent;
    Boolean onBreak = false;
    
    Coordinate position;
    Coordinate destination;
    
    Coordinate host;
    Coordinate cook;
    Coordinate cashier;
    Coordinate waitingroom;
    Coordinate homeposition;
    
    Boolean showSpeechBubble = false;
	Coordinate speechBubbleLoc;
	BufferedImage speechBubble;
	Timer timer = new Timer();
    
    Boolean goingSomewhere = false;
    
    public List<Coordinate> tables = new ArrayList<Coordinate>();

    public TellerGui(Teller w, BankGui gui, int in) {
    	
    	setPresent(true);
    	isPresent = true;
    	
        this.agent = w;
    	this.gui = gui;
    	this.index = in;
    	
    	host = new Coordinate(-30,125);
    	homeposition = new Coordinate(index * 127 + 33, 89);
    	position = new Coordinate(homeposition.x + 10, homeposition.y - 10);
    	destination = homeposition;
    	
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/teller.png"));
        } catch (IOException e ) {}
    	
  
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
    	//Color waiterColor = new Color(193, 218, 214);
       // g.setColor(waiterColor);
       // g.fillRect(position.x, position.y, waiterSize, waiterSize);
    	Graphics2D newG = (Graphics2D)g;
        newG.drawImage(imgTrainer, position.x, position.y, agent.copyOfAnimPanel);
      
        //System.out.println("X: "+ homeposition.x + " Y: " + homeposition.y);
        
        if (showSpeechBubble){
			newG.drawImage(speechBubble, speechBubbleLoc.x, speechBubbleLoc.y, agent.copyOfAnimPanel);
		
        }
    }
    
    
    public void setSpeechBubble(String temp){
    	//System.out.println("setting");
		speechBubbleLoc = new Coordinate(position.x - 82, position.y - 53);
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
    
    
//    public void AskForBreak()
//    {
//    	agent.host.msgIdLikeToGoOnBreak(agent);
//    }
//    
//    public void AskGoOffBreak()
//    {
//    	agent.host.msgIdLikeToGetOffBreak(agent);
//    }

    public boolean isPresent() {
		return isPresent;
	}
	
//	public void setOnBreak() 
//	{
//		//agent.msgSetOnBreak();
//		agent.isOnBreak = true;
//	}
//	
//	public void setOffBreak()
//	{
//		//agent.msgSetOffBreak();
//		agent.isOnBreak = false;
//	}
	
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
	
	
	public void DoGoToHomePosition()
	{
    	goingSomewhere = true;
		//System.out.println(agent.getName() + " is going to the waiting room");
		destination = homeposition;
		agent.WaitForAnimation();
	}
}
