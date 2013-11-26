package housing.guis;

import housing.HousingCustomerAgent;
import housing.interfaces.HousingCustomer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

public class HousingCustomerGui implements Gui, restaurant.gui.Gui{

	//variables
	private boolean isPresent = true;
	private boolean isHungry = false;
	public boolean goingSomewhere = false;
	public boolean initial = true;
	public boolean isBusy = false;
	int roomIndex;

	//finals
	private final int deltadivider = 100;

	//self agent
	private HousingCustomer agent = null;

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

	public HousingCustomerGui(HousingCustomer c, HousingGui gui3, int n){

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
		destination = new Coordinate(475,20 + (100*roomIndex));
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
			e.printStackTrace();
		}
	}

//	public void DoneWithAnimation() {
//		waitingForAnimation.release();
//	}


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
			//	System.out.println("reached dest");
					goingSomewhere = false;
					agent.DoneWithAnimation();
			}
		}
	}

	public void draw(Graphics2D g) 
	{
		Graphics2D newG = (Graphics2D)g;
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

	public void DoGoToPhone() {
		goingSomewhere = true;
		destination = new Coordinate(250, 50);
		//agent.WaitForAnimation();
		agent.WaitForAnimation();
	}

	public void DoGoToThreshold() {
		goingSomewhere = true;
		destination = new Coordinate(475, 20 + (100*(roomIndex/5)));
		agent.WaitForAnimation();
	}
	public void DoGoToBed() {
		goingSomewhere = true;
		destination = new Coordinate(525 + (roomIndex % 5), 100 + (100*(roomIndex/5)));
		agent.WaitForAnimation();
	}

	public void DoGoToLandlord() {
		goingSomewhere = true;
		destination = new Coordinate(325,50);
		agent.WaitForAnimation();
	}
	public void DoGoToKitchen() {
		goingSomewhere = true;
		destination = new Coordinate(225,100);
		agent.WaitForAnimation();
	}
	public void DoGoToFridge() {
		goingSomewhere = true;
		destination = new Coordinate(50, 80);
		agent.WaitForAnimation();
	}
	public void DoGoToTable() {
		goingSomewhere = true;
		destination = new Coordinate(50, 125);
		agent.WaitForAnimation();
	}

	public void setAction() {
		isBusy = true;
		//int temp = Integer.parseInt(amount);
		agent.msgDoSomething();
		setPresent(true);
	}
	public void DoWalkOut(){
		goingSomewhere = true;
		destination = new Coordinate(475, 750);
		agent.WaitForAnimation();
	}
	public void setDone()
	{
		isBusy = false;
		setPresent(false);
	}
}
