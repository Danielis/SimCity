package restaurantA;

import restaurantA.interfaces.Customer;


public class Table {
	Customer occupiedBy;
	int tableNumber;
	private int xPos;
	private int yPos;

	public Table(int tableNumber, int xPos, int yPos) {
		this.tableNumber = tableNumber;
		this.setxPos(xPos);
		this.setyPos(yPos);
	}

	void setOccupant(Customer cust) {
		occupiedBy = cust;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	Customer getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
}


