package bank.gui;


import bank.CustomerAgent;
import bank.TellerAgent;
import bank.HostAgent;
import bank.gui.Coordinate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HostGui implements Gui {

    private HostAgent agent = null;
    BufferedImage imgTrainer;
    Boolean goingSomewhere = false;
    Coordinate position;
    Coordinate destination;
    Coordinate homeposition;
    
    private final int deltadivider = 100;
    
    public HostGui(HostAgent agent) {
        this.agent = agent;
        try
        {
        	imgTrainer = ImageIO.read(getClass().getResource("/resources/trainer.png"));
        } catch (IOException e ) {}
    	homeposition = new Coordinate(297, 260);
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
