package restaurantD.gui;


import static java.lang.System.out;
import restaurantD.interfaces.*;

import java.awt.*;

public class WaiterGui implements Gui {

    private Waiter agent=null;
    private RestaurantGui g = null;
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;

    public static final int xTable2 = 300;
        public static final int yTable2 = 250;
    

        
    public WaiterGui(Waiter agent) { 
        this.agent = agent;
    }
    public WaiterGui(Waiter agent, RestaurantGui g, int position ){
            this.agent = agent;
            this.g = g;
    }
    
    public void updatePosition() {
            if(!(xPos == xDestination && yPos == yDestination)){
                    if (xPos < xDestination)
                            xPos++;
                    else if (xPos > xDestination)
                            xPos--;

                    if (yPos < yDestination)
                            yPos++;
                    else if (yPos > yDestination)
                            yPos--;

                    if (xPos == xDestination && yPos == yDestination
                                    & (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
                            agent.msgFree();
                    }
                    if (xPos == xDestination && yPos == yDestination
                                    & (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
                            agent.msgFree();
                    }
                    if (xPos == xDestination && yPos == yDestination
                                    & (xDestination == -20) & (yDestination == -20)) {
                            agent.msgFree();
                    }
                    if (xPos == xDestination && yPos == yDestination
                                    & (xDestination == 470) & (yDestination == -20)) {
                            agent.msgFree();
                    }
            }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer,int number) {
        if(number==1){
                xDestination = xTable + 20;
                yDestination = yTable - 20;
        }
        else{//Hack for second table
                xDestination = xTable2 + 20;
                yDestination = yTable2 - 20;
        }
    }

    public void doGoToTable(int number) {
        if(number==1){
                xDestination = xTable + 20;
                yDestination = yTable - 20;
        }
        else{//Hack for second table
                xDestination = xTable2 + 20;
                yDestination = yTable2 - 20;
        }
    }
    public void goHome() {
            if( xDestination == -20 && yDestination ==-20)
                    agent.msgFree();
        xDestination = -20;
        yDestination = -20;
    }
    
    public void goToChef(){
            if( xDestination == 470 && yDestination ==-20)
                    agent.msgFree();
            xDestination = 470;
            yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    
    
}
/*
package restaurant.gui;


import static java.lang.System.out;
import restaurant.interfaces.*;

import java.awt.*;

public class WaiterGui implements Gui {

    private Waiter agent=null;
    private RestaurantGui g = null;
    
    private int xPos, yPos;//default waiter position
    private int xDestination, yDestination ;//default start position
    private int position;

    public static final int xTable = 200;
    public static final int yTable = 250;

    public static final int xTable2 = 300;
	public static final int yTable2 = 250;
    

	
    public WaiterGui(Waiter agent) { 
        this.agent = agent;
    }
    public WaiterGui(Waiter agent, RestaurantGui g , int position){
    	this.agent = agent;
    	this.g = g;
    	this.position=position;
    	xPos = 0;
    	yPos = 50*(1+position);
    	xDestination = 10;
    	yDestination = 50*(1+position);
    	
    }
    
    public void updatePosition() {
    	if(!(xPos == xDestination && yPos == yDestination)){
    		if (xPos < xDestination)
    			xPos++;
    		else if (xPos > xDestination)
    			xPos--;

    		if (yPos < yDestination)
    			yPos++;
    		else if (yPos > yDestination)
    			yPos--;

    		if (xPos == xDestination && yPos == yDestination
    				& (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
    			agent.msgFree();
    		}
    		if (xPos == xDestination && yPos == yDestination
    				& (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
    			agent.msgFree();
    		}
    		if (xPos == xDestination && yPos == yDestination
    				& (xDestination == -20) & (yDestination == -20)) {
    			agent.msgFree();
    		}
    		if (xPos == xDestination && yPos == yDestination
    				& (xDestination == 470) & (yDestination == -20)) {
    			agent.msgFree();
    		}
    	}
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer,int number) {
        if(number==1){
        	xDestination = xTable + 20;
        	yDestination = yTable - 20;
        }
        else{//Hack for second table
        	xDestination = xTable2 + 20;
        	yDestination = yTable2 - 20;
        }
    }

    public void doGoToTable(int number) {
        if(number==1){
        	xDestination = xTable + 20;
        	yDestination = yTable - 20;
        }
        else{//Hack for second table
        	xDestination = xTable2 + 20;
        	yDestination = yTable2 - 20;
        }
    }
    public void goHome() {
    	if( xDestination == 10 && yDestination == 50*(1+position)){
    		agent.msgFree();
    	}
    	xDestination = 10;
    	yDestination = 50*(1+position);
    }
    
    public void goToChef(){
    	if( xDestination == 470 && yDestination ==10)
    		agent.msgFree();
    	xDestination = 470;
    	yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    
    
}
*/