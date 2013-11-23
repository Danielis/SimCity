package housing.guis;

import housing.HousingCustomerAgent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import restaurant.HostAgent;
import city.PersonAgent;
import city.guis.CityGui;
import city.guis.PersonGui.Coordinate;

public class HousingCustomerGui implements Gui, restaurant.gui.Gui{

	//variables
	private boolean isPresent = true;
	private boolean isHungry = false;
	private boolean goingSomewhere = false;

	int roomIndex;

	//finals
	private final int deltadivider = 100;

	//self agent
	private HousingCustomerAgent agent = null;

	//private HostAgent host;
	HousingGui gui;

	private Image avatar;
	private Semaphore waitingForAnimation = new Semaphore(-1);

	//Coordinates
	Coordinate position;
	Coordinate destination;
	Coordinate outside;
	Coordinate cashier;
	Coordinate waitingroom;

	//images
	//List of tables

	public HousingCustomerGui(HousingCustomerAgent c, HousingGui gui3, int n){

		roomIndex = n;

		agent = c;
		this.gui = gui3;

		try
		{
			avatar = ImageIO.read(getClass().getResource("/resources/trainer2.png"));
		} catch (IOException e ) {}

		outside = new Coordinate(475,750);
		position = new Coordinate(475,750);
		cashier = new Coordinate(255, 75);
		waitingroom = new Coordinate(140,70);
		destination = new Coordinate(460,20 + (100*roomIndex));
		goingSomewhere = true;
	}

	//*************UTILITIES ***********************************************
	public class Coordinate
	{
		int x;
		int y;

		Coordinate()
		{
			x = 0;
			y = 0;
		}
		Coordinate(int a, int b)
		{
			x = a;
			y = b;
		}
	}

	public void WaitForAnimation() {
		try {
			waitingForAnimation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DoneWithAnimation() {
		waitingForAnimation.release();
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
				DoneWithAnimation();
			}
		}
	}

	public void draw(Graphics2D g) 
	{
		Graphics2D newG = (Graphics2D)g;
		Color customerColor = new Color(195, 178, 116);
		newG.setColor(customerColor);
		//newG.fillRect(50, 50, customerSize, customerSize);
		newG.drawImage(avatar, position.x, position.y, agent.copyOfAnimationPanel());			
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setHungry() {
		isHungry = true;
		//agent.msgGotHungry();
		setPresent(true);
	}

	public void setNotHungry() {
		isHungry = false;
		setPresent(false);
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoExitRestaurant()
	{
		goingSomewhere = true;
		//System.out.println(agent.getName() + " is leaving.");
		destination = outside;
		//agent.WaitForAnimation();
	}

	public void DoGoToCashier()
	{
		goingSomewhere = true;
		//System.out.println(agent.getName() + " is going to cashier.");
		destination = cashier;
		//agent.WaitForAnimation();
	}

	public void DoGoToWaitingRoom()
	{
		goingSomewhere = true;
		//System.out.println(agent.getName() + " is going to the waiting room.");
		destination = waitingroom;
		//agent.WaitForAnimation();
	}

	public void DoGoToPhone() {
		goingSomewhere = true;
		destination = new Coordinate(250, 50);
		//agent.WaitForAnimation();
		WaitForAnimation();
	}

	public void DoGoToThreshold() {
		goingSomewhere = true;
		destination = new Coordinate(475,20 + (100*roomIndex));
		WaitForAnimation();
	}
	public void DoGoToBed() {
		goingSomewhere = true;
		destination = new Coordinate(600, 100 + (100*roomIndex));
		WaitForAnimation();
	}

	public void DoGoToLandlord() {
		goingSomewhere = true;
		destination = new Coordinate(325,50);
		WaitForAnimation();
	}
	public void DoGoToKitchen() {
		goingSomewhere = true;
		destination = new Coordinate(225,100);
		WaitForAnimation();
	}
	public void DoGoToFridge() {
		goingSomewhere = true;
		destination = new Coordinate(50, 80);
		WaitForAnimation();
	}
	public void DoGoToTable() {
		goingSomewhere = true;
		destination = new Coordinate(50, 125);
		WaitForAnimation();
	}
}
