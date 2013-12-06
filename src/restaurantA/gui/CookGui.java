package restaurantA.gui;

import restaurantA.CookAgent;
import restaurantA.HostAgent;
import restaurantA.Table;

import java.awt.*;

public class CookGui implements Gui{

	private CookAgent agent;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	Boolean grill = false;
	Boolean plate = false;
	String grillFood;
	String plateFood;

	public CookGui(CookAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 400;
		yPos = 50;
		this.gui = gui;
	}

	public void updatePosition() {

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.fillRect(xPos, yPos, 20, 20);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(xPos + 5, yPos + 25, 15, 15);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(xPos + 5, yPos - 20, 15, 15);
		
		if(grill){
			g.setColor(Color.DARK_GRAY);
			g.fillRect(xPos + 5, yPos - 20, 10, 10);
			if (!grillFood.isEmpty())
            g.drawString(grillFood, xPos + 5, yPos - 20);
		}
		if(plate){
			g.setColor(Color.DARK_GRAY);
			g.fillRect(xPos + 5, yPos + 25, 10, 10);
			if (!plateFood.isEmpty())
            g.drawString(plateFood, xPos + 5, yPos + 50);
		}
		
	}

	public void DoGrillFood(String name){
		grill = true;
		if (name.length() > 3)
			grillFood = name.substring(0,3);
		else
		    grillFood = name;
	}
	
	public void DoneGrillFood(){
		grill = false;
	}
	
	public void DoPlateFood(String name){
		plate = true;
		if (name.length() > 3)
			plateFood = name.substring(0,3);
		else
			plateFood = name;
	}
	
	public void DonePlateFood(){
		plate = false;
	}
	
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
}
