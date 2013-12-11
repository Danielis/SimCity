package restaurantC.gui;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurantC.WaiterRole;

public class WaiterGui implements Gui {

	private WaiterRole agent = null;

	private int homeX;
	private int xPos = 0, yPos = 0;//default waiter position
	private int xDestination, yDestination; //destination positions
	
	private Image person; 
	
	RestaurantGui gui;
	
	//boolean to manage calls to agent
	boolean callToAgent = true;

	private static final int TABLEX = 150;
	private static final int TABLEY = 175;
	private static final int TABLEX2 = 225;
	private static final int TABLEY2 = 175;
	private static final int TABLEX3 = 300;
	private static final int TABLEY3 = 175;

	public WaiterGui(WaiterRole agent, RestaurantGui g, int count) {
		this.agent = agent;
		homeX = 50 + (count*25);
		xDestination = homeX; 
		yDestination = 20;
		this.gui = g;
		
		
		try
        {
            person = ImageIO.read(getClass().getResource("/resources/trainer2.png"));
        } catch (IOException e ) {}
		
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination && callToAgent) {
			callToAgent = false;
			if(xDestination == TABLEX + 20) {
				agent.msgAtTable();
			}
			else if(xDestination == TABLEX2 + 20) {
				agent.msgAtTable();
			}
			else if(xDestination == TABLEX3 + 20) {
				agent.msgAtTable();
			}
			else if(xDestination == 330 || xDestination == 375) {
				agent.msgAtCook();
			}
			else if(yDestination == 400) {
				agent.msgAtCashier();	
			}
			else if(xDestination != homeX && xDestination != 0) {
				agent.msgAtWaitingCust();
			}

		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(person, xPos, yPos, agent.copyOfAnimationPanel());
	}

	public boolean isPresent() {
		return true;
	}

	public void GoToTable(int seatNumber) {
		callToAgent = true;
		if(seatNumber == 1)
		{
			xDestination = TABLEX + 20;
			yDestination = TABLEY - 20;
		}
		else if(seatNumber == 2)
		{
			xDestination = TABLEX2 + 20;
			yDestination = TABLEY2 - 20;
		}
		else if(seatNumber == 3)
		{
			xDestination = TABLEX3 + 20;
			yDestination = TABLEY3 - 20;
		}    	
	}

	public void DoLeaveCustomer() {
		callToAgent = true;
		xDestination = homeX;
		yDestination = 20;
	}

	public void GoToCookDrop() {
		callToAgent = true;
		xDestination = 375;
		yDestination = 120;
	}
	
	public void GoToCookReceive() {
		callToAgent = true;
		xDestination = 330;
		yDestination = 60;
	}

	public void GoToCashier() {
		callToAgent = true;
		xDestination = 200;
		yDestination = 400;
	}
	
	public void GoToWaitingCust(int x, int y) {
		callToAgent = true;
		xDestination = x + 20;
		yDestination = y + 20;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
    public void showOrder(String s) {
    	gui.animationPanel.showOrder(s,this);
    }
    
    public void finalizeOrder(String s) {
    	gui.animationPanel.finalizeOrder(s, this);
    }
    
    public void removeOrder() {
    	gui.animationPanel.removeOrder(this);
    }
}
