package restaurantC.gui;

import java.awt.*;

import restaurantC.CustomerRole;

public class CustomerGui implements Gui{

	//agent backing the gui
	private CustomerRole agent = null;

	//I think ideally these will be phased out
	private boolean isPresent = false;
	private boolean isHungry = false;
	private int homeY;

	//private HostAgent host;
	RestaurantGui gui;

	//coordinate position
	private int xPos, yPos;
	private int tableNumber;
	//intended destination
	private int xDestination, yDestination;
	//boolean for making sure the at-table call is only sometimes done
	boolean callToAgent = true;

	//constants that mark the locations of the tables
    private static final int TABLEX = 150;
    private static final int TABLEY = 175;
    private static final int TABLEX2 = 225;
    private static final int TABLEY2 = 175;
    private static final int TABLEX3 = 300;
    private static final int TABLEY3 = 175;

    //--------------------Constructor---------------------------------------
	public CustomerGui(CustomerRole c, RestaurantGui gui, int count) {
		agent = c;
		homeY = 25 * (count + 1);
		xPos = 10;
		yPos = homeY;
		xDestination = 10;
		yDestination = homeY;
		this.gui = gui;
	}

	//Updates Position.  This will be repeating in order to move the gui
	public void updatePosition() {
		//moves the gui towards destination coordinates
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;
		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		//what to do if the customer is at the destination
		if (xPos == xDestination && yPos == yDestination && callToAgent) {
			if(yDestination == 400) {
				agent.msgAtCashier();
				callToAgent = false;
			}
			else if (xDestination == TABLEX || xDestination == TABLEX2 || xDestination == TABLEX3) {
				agent.msgAtTable();
				callToAgent = false;
			}
			else if (xDestination == 10) {
				agent.msgLeftRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
				callToAgent = false;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoWait() {
		xDestination = 10;
		yDestination = homeY;
	}

	public void DoGoToSeat(int seatnumber) {
		callToAgent = true;
		tableNumber = seatnumber;
		if(seatnumber == 1)
		{
			xDestination = TABLEX;
			yDestination = TABLEY;
		}
		else if(seatnumber == 2)
		{
			xDestination = TABLEX2;
			yDestination = TABLEY2;
		}
		else if(seatnumber == 3)
		{
			xDestination = TABLEX3;
			yDestination = TABLEY3;
		}
	}

	public void DoExitRestaurant() {
		callToAgent = true;
		xDestination = 10;
		yDestination = homeY;
	}
	
	public void DoGoToCashier() {
		callToAgent = true;
    	xDestination = 200;
    	yDestination = 400;
    }
	
    public void finalizeOrder(String s) {
    	gui.animationPanel.finalizeOrder(s, tableNumber);
    }
    
    public void removeOrder() {
    	gui.animationPanel.removeOrder(tableNumber);
    }
    
    public int getX() {
    	return xPos;
    }
    
    public int getY() {
    	return yPos;
    }
	
}
