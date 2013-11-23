package market.gui;


import market.MarketCustomerAgent;
import market.MarketTellerAgent;
import market.MarketHostAgent;
import market.gui.Coordinate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class MarketHostGui implements Gui {

    private MarketHostAgent agent = null;
    BufferedImage imgTrainer;
    Boolean goingSomewhere = false;
    Coordinate position;
    Coordinate destination;
    Coordinate homeposition;
    
    Timer timer = new Timer();
	BufferedImage speechBubble;
	
	Boolean showSpeechBubble = false;
	Coordinate speechBubbleLoc;
    
    private final int deltadivider = 100;
    
    public MarketHostGui(MarketHostAgent agent) {
        this.agent = agent;
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/bankSprites/host.png"));
        } catch (IOException e ) {}
    	homeposition = new Coordinate(297, 249);
        position = new Coordinate(homeposition.x, homeposition.y - 20);
    	destination = homeposition;
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
     	Graphics2D newG = (Graphics2D)g;
        newG.drawImage(imgTrainer, position.x, position.y, agent.copyOfAnimPanel);
   
        if (showSpeechBubble){
			newG.drawImage(speechBubble, speechBubbleLoc.x, speechBubbleLoc.y, agent.copyOfAnimPanel);
		}
    }
    
    public void setSpeechBubble(String temp){
		System.out.println("setting");
		speechBubbleLoc= new Coordinate(position.x - 10, position.y - 53);
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
			}, 1000);
	}

    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToHomePosition()
	{
    	goingSomewhere = true;
		//System.out.println(agent.getName() + " is going to the waiting room");
		destination = homeposition;
		agent.WaitForAnimation();
	}
}
