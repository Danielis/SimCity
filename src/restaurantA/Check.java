package restaurantA;

import restaurantA.interfaces.*;


public class Check{
	String food;
	private double amountOwed = 0;
	private double amountPaid = 0;
	private double amountChange = 0;
	Table table;
	Customer c;
	Cashier cashier;
	Waiter w;
	private checkState s;
	
	public Check(String f, Table table, Customer c){
		this.food = f;
		this.table = table;
		this.c = c;
		this.setS(checkState.notPaid);
		
	}
	
	public Check(Customer c, int table, int amount){
		//this.table = table;
		this.c = c;
		this.setAmountOwed(amount);
		this.setS(checkState.notPaid);
	
	}
	
	public void setAmount(String inp){
		if (inp.equals("Steak"))
			this.setAmountOwed(this.getAmountOwed() + 16);
		if (inp.equals("Chicken"))
			this.setAmountOwed(this.getAmountOwed() + 11);	
		if (inp.equals("Pizza"))
			this.setAmountOwed(this.getAmountOwed() + 9);
		if (inp.equals("Salad"))
			this.setAmountOwed(this.getAmountOwed() + 6);
		//else 
			//this.amountOwed = 150;
	}
	
	public checkState getS() {
		return s;
	}

	public void setS(checkState s) {
		this.s = s;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double money) {
		this.amountPaid = money;
	}

	public double getAmountOwed() {
		return amountOwed;
	}

	public void setAmountOwed(double d) {
		this.amountOwed = d;
	}

	public double getAmountChange() {
		return amountChange;
	}

	public void setAmountChange(double amountChange) {
		this.amountChange = amountChange;
	}

	public enum checkState {waiting, notPaid, cantbePaid, Paid, paying, needsChange, calculated}
}