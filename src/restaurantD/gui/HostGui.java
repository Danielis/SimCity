package restaurantD.gui;


import static java.lang.System.out;
import restaurantD.CustomerAgent;
import restaurantD.WaiterAgent;

import java.awt.*;

public class HostGui implements Gui {

    private WaiterAgent agent=null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;

    public static final int xTable2 = 300;
	public static final int yTable2 = 250;
    

	
    public HostGui(WaiterAgent agent) { 
        this.agent = agent;
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
    				& (xDestination == -30) & (yDestination == -30)) {
    			agent.msgFree();
    		}
    		if (xPos == xDestination && yPos == yDestination
    				& (xDestination == 480) & (yDestination == -30)) {
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

    public void DoBringToTable(CustomerAgent customer,int number) {
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
    	if( xDestination == -30 && yDestination ==-30)
    		agent.msgFree();
        xDestination = -30;
        yDestination = -30;
    }
    
    public void goToChef(){
    	xDestination = 480;
    	yDestination = -30;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    
    
}
