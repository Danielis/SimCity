package restaurantA.gui;

import restaurantA.CookAgent;
import restaurantA.HostAgent;
import restaurantA.Table;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CookGui implements Gui{

	private CookAgent agent;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	Boolean grill = false;
	Boolean plate = false;
	String grillFood;
	String plateFood;
	Boolean isPresent = true;
	Image img, table, food1, food2;
	public CookGui(CookAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 400;
		yPos = 50;
		this.gui = gui;
		
		 try
	        {
	        	img = ImageIO.read(getClass().getResource("/resources/bankSprites/host.png"));
	        } catch (IOException e ) {}
		 try
	        {
	        	table = ImageIO.read(getClass().getResource("/resources/restSprites/A/cooktable.png"));
	        } catch (IOException e ) {}
		 try
	        {
	        	food1 = ImageIO.read(getClass().getResource("/resources/restSprites/A/foodLOL.png"));
	        } catch (IOException e ) {}
		 try
	        {
	        	food2 = ImageIO.read(getClass().getResource("/resources/restSprites/A/food2.png"));
	        } catch (IOException e ) {}
	}

	public void updatePosition() {

	}

	public void draw(Graphics2D g) {
	
		
		if (isPresent){
	        g.drawImage(img, xPos, yPos, agent.copyOfAnimPanel);
	    	}
		
		g.drawImage(table, xPos - 20, yPos - 10, agent.copyOfAnimPanel);
		
		if(grill){
		
			g.drawImage(food1, xPos - 20, yPos - 7, agent.copyOfAnimPanel);
			//if (!grillFood.isEmpty())
           // g.drawString(grillFood, xPos + 10, yPos - 17);
		}
		if(plate){
			g.drawImage(food2, xPos - 20, yPos  + 20, agent.copyOfAnimPanel);
			//if (!plateFood.isEmpty())
           // g.drawString(plateFood, xPos + 5, yPos + 45);
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
