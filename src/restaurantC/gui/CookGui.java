package restaurantC.gui;

import java.awt.*;

import restaurantC.CookRole;

public class CookGui implements Gui{

	//agent backing the gui
	private CookRole agent = null;
	private boolean isPresent = true;
	
	RestaurantGui gui;

	//coordinate position
	private int xPos, yPos;
	//intended destination
	private int xDestination, yDestination;
	//boolean for making sure the at-table call is only sometimes done
	boolean callToAgent = true;


	public boolean isPresent() {
		return isPresent;
	}
	
    //--------------------Constructor---------------------------------------
	public CookGui(CookRole c, RestaurantGui gui) {
		agent = c;
		xPos = 400;
		yPos = 50;
		xDestination = 400;
		yDestination = 50;
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

	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, 20, 20);
	}

    public void showOrder(String s) {
    	gui.animationPanel.showOrder(s);
    }
    
    public void finalizeOrder(String s) {
    	gui.animationPanel.finalizeOrder(s);
    }
    
    public void removeOrder(String s) {
    	gui.animationPanel.removeOrder(s);
    }
}
