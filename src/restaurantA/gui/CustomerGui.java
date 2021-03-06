package restaurantA.gui;

import restaurantA.CustomerAgent;
import restaurantA.HostAgent;
import restaurantA.Table;
import restaurantA.interfaces.Customer;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean isEating = false;
	private boolean isOrdering = false;
	private String choice = null;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, GoToCashier};
	private Command command=Command.noCommand;
	Image img, food;
	String direct = "up";
	int move = 0;
	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 0;
		yDestination = 0;
		//maitreD = m;
		this.gui = gui;
		
		 try
	        {
			 img = ImageIO.read(getClass().getResource("/resources/trainer.png"));
	        } catch (IOException e ) {}
		 
		 try
	        {
			 food = ImageIO.read(getClass().getResource("/resources/restSprites/A/food2.png"));
	        } catch (IOException e ) {}
	}

	public void updatePosition() {
		if (xPos < xDestination){
			direct = "right";
            setImage(false);
			xPos++;
			move ++;
		}
		else if (xPos > xDestination){
			direct = "left";
		    setImage(false);
			xPos--;
			move ++;
		}

		else if (yPos < yDestination){
			direct = "down";
            setImage(false);
			yPos++;
			move ++;
		}
		else if (yPos > yDestination){
			direct = "up";
            setImage(false);
			yPos--;
			move ++;
		}

		if (xPos == xDestination && yPos == yDestination) {

            setImage(true);
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			else if (command == Command.GoToCashier ){
				agent.msgAnimationFinishedGoToCashier();
			}
				
			command=Command.noCommand;
		}
	}
	private void setImage(Boolean noMove){
		String start = "/resources/globalSprites/";
		String mid = direct;
		String num = "0";
		String end = ".png";
		if (move >= 50 || noMove){
			num = "0";
			move = 0;
		}
        else if (move < 25)
        	num = "2";
        else if (move < 50)
        	num = "1";
    
       // resource/globalSprites/None/left0.png
		String collapse = start + agent.job + "/" + mid + num + end;
		//System.out.println(collapse);
		 try
	        {
	        	img = ImageIO.read(getClass().getResource(collapse));
	        } catch (IOException e ) {}
    }
	
	public void draw(Graphics2D g) {
		g.drawImage(img, xPos, yPos, agent.copyOfAnimPanel);

		
		
		 if (isEating){
	    		g.drawImage(food, xPos + 1, yPos + 26, agent.copyOfAnimPanel);
	      }
		 
		 if (isOrdering){
			 g.setColor(Color.DARK_GRAY);
			 if (!choice.equals(" :("))
				 g.drawString(choice + "?", xPos, yPos-10);
			 else
				 g.drawString(choice, xPos, yPos-10);
		 }
	}
	
	public void isEating(String choice){
		isEating = true;
		this.choice = choice;
	}
	
	public void doneEating(){
		isEating = false;
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
	
	public void shuffle(int x, int y){
		xDestination = x;
		yDestination = y;
	}

	public void DoGoToSeat(Table table) {//later you will map seatnumber to table coordinates.
		xDestination = table.getxPos() + 15;
		yDestination = table.getyPos() - 23;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoToCashier() {
		xDestination = 80;
		yDestination = 30;
		command = Command.GoToCashier;
	}

	public void DoGiveOrder(String choice) {
		isOrdering = true;
		this.choice = choice;
	}
	
	public void CantOrder(){
		isOrdering = true;
		this.choice = " :(";
	}
	
	public void DoneGiveOrder() {
		isOrdering = false;
	}
}
