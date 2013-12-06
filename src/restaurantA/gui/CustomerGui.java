package restaurantA.gui;

import restaurantA.CustomerAgent;
import restaurantA.HostAgent;
import restaurantA.Table;

import java.awt.*;

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



	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 0;
		yDestination = 0;
		//maitreD = m;
		this.gui = gui;
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

		if (xPos == xDestination && yPos == yDestination) {
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

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		
		
		
		 if (isEating){
	            g.setColor(Color.DARK_GRAY);
	            g.fillRect(xPos+20, yPos, 10, 10);
	            g.drawString(choice.substring(0,3), xPos+20, yPos+20);
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
		xDestination = table.getxPos();
		yDestination = table.getyPos();
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoToCashier() {
		xDestination = 60;
		yDestination = 20;
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
