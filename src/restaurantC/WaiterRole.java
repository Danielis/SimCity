package restaurantC;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.*;

import restaurantC.gui.WaiterGui;
import restaurantC.interfaces.Cashier;
import restaurantC.interfaces.Customer;
import restaurantC.interfaces.Waiter;
import roles.Role;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

public class WaiterRole extends Role implements Waiter {
	//creates an enum that manages what the waiter is doing
	public enum CustomerState{none, needsSeating, seated, readyToOrder, beingHelped, ordered, waitingForFood, 
		foodReady, outOfFood, eating, doneEating, wantsCheck, WaitingForCheck, leaving};

		//semaphore that will pause the agents while animations are happening
		public Semaphore waitingForAnimation = new Semaphore(0);

		//declared gui.  will be set to new gui
		public WaiterGui waiterGui = null;

		//boolean for being on break
		public boolean wantToGoOnBreak;
		public boolean onBreak;

		//name
		public String name;

		//private class to keep track of customers, and a data structure to hold them
		private class myCustomer {
			CustomerRole c;
			String choice;
			int table;
			CustomerState state;
			double owed = 0;
		}
		public List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());

		//host and cook agents
		private CookRole cook; 
		private HostRole host;
		private Cashier cashier;

		//constructor
		public WaiterRole(String nm) {
			super();
			this.name = nm;
			wantToGoOnBreak = false;
			onBreak = false;
			System.out.println("Waiter agent created.");			
		}

		//---------------setters and getters for relevant agents-----------------------
		public void setCook(CookRole cook) {
			this.cook = cook;
		}

		public CookRole getCook() {
			return this.cook;
		}

		public void setHost(HostRole h) {
			this.host = h;
		}

		public void setCashier(Cashier c) {
			this.cashier = c;
		}
		//-----------------------------------------------------------------------
		//----------------------------Messages-----------------------------------
		//-----------------------------------------------------------------------
		//message from host to seat the customer.
		public void seatCustomer(CustomerRole cust, int table)
		{
			System.out.println(name +" has been told to seat customer");
			//creates new customer and adds it to the list
			myCustomer tempCust = new myCustomer();
			tempCust.c = cust;
			tempCust.table = table;
			tempCust.state = CustomerState.needsSeating;
			customers.add(tempCust);
			stateChanged();
		}
		public void msgAtWaitingCust() {
			waitingForAnimation.release();
		}
		//sent from gui, waiterGui is at table, release semaphore
		public void msgAtTable() {//from animation
			waitingForAnimation.release();
		}

		//sent from customer, ready to order
		public void readyToOrder(Customer cust){
			synchronized(customers) {
				for(myCustomer customer:customers)
					if(customer.c == cust){
						customer.state = CustomerState.readyToOrder;
						stateChanged();
					}
			}
		}
		//sent from customer, contains actual order
		public void order(Customer cust, String choice) {
			synchronized(customers) {
				for(myCustomer customer:customers)
					if(customer.c == cust) {
						customer.choice = choice;
						customer.state = CustomerState.ordered;
						stateChanged();
					}
			}
		}
		//sent from gui, waiter is at cook
		public void msgAtCook() {
			waitingForAnimation.release();
		}
		//sent from cook saying food is ready
		public void foodReady(String choice, int table){
			synchronized(customers) {
				for(myCustomer customer:customers)
					if(customer.table == table){
						customer.choice = choice;
						customer.state = CustomerState.foodReady;
					}
			}
			stateChanged();
		}
		public void outOfFood(String choice, int table){
			synchronized(customers) {
				for(myCustomer customer:customers) {
					if(customer.table == table) {
						customer.state = CustomerState.outOfFood;
						stateChanged();
					}
				}
			}
		}
		//sent from customer, done with food
		public void doneWithFood(Customer cust){
			synchronized(customers) {
				for(myCustomer customer:customers)
					if(customer.c == cust){
						customer.state = CustomerState.doneEating;
						stateChanged();
					}
			}
		}
		//sent from customer, wants the check
		public void checkPlease(Customer cust) {
			synchronized(customers) {
				for(myCustomer customer:customers)
					if(customer.c == cust){
						customer.state = CustomerState.wantsCheck;
						stateChanged();
					}
			}
		}
		//sent from gui, waiter is at the cashier
		public void msgAtCashier() {
			waitingForAnimation.release();
		}
		//sent from cashier, check processed
		public void hereIsCheck(int t, double p) {
			synchronized(customers) {
				for(myCustomer cust:customers) {
					if(cust.table == t) {
						cust.owed = p;
						cust.state = CustomerState.WaitingForCheck;
						stateChanged();
					}
				}
			}
		}
		//sent from customer, leaving table
		public void msgLeavingTable(Customer cust) {
			synchronized(customers){
				for(myCustomer customer:customers)
					if(customer.c == cust)
					{
						customer.state = CustomerState.leaving;
						stateChanged();
					}
			}
		}
		//BREAK STUFF
		//waiter wants to go on break.  sent from gui
		public void wantToGoOnBreak() {
			System.out.println(name + ": Can I go on a break?");
			wantToGoOnBreak = true;
			host.askForBreak(this);
		}
		public void noBreak() {
			wantToGoOnBreak = false;
			onBreak = false;
		}

		public void goOnBreak() {
			onBreak = true;
			wantToGoOnBreak = true;
		}

		//----------------------------------------------------
		//--------------------Scheduler-----------------------
		//----------------------------------------------------
		public boolean pickAndExecuteAnAction() {
			synchronized(customers) {
				for(myCustomer customer:customers) {
					//customer needs seating.  comes from seatCustomer
					if(customer.state == CustomerState.needsSeating) {
						customer.state = CustomerState.none;
						seatCustomerAction(customer);
						return true;
					}
					//invoked after the semaphore is released and state is changed in the actions for seating the customer
					if(customer.state == CustomerState.seated) {
						customer.state = CustomerState.none;
						DoLeaveCustomer();
						return true;
					}
					//customer is ready to order.  invoked after readyToOrder
					if(customer.state == CustomerState.readyToOrder) {
						customer.state = CustomerState.none;
						takeOrder(customer);
						return true;
					}
					//customer has ordered.  invoked after order
					if(customer.state == CustomerState.ordered) {
						relayOrder(customer);
						return true;
					}
					//customer's food is ready.  should be invoked after foodReady
					if(customer.state == CustomerState.foodReady) {
						returnOrder(customer);
						return true;
					}
					//customer's food choice is out of stock.  should be invoked after outOfFood
					if(customer.state == CustomerState.outOfFood) {
						customer.state = CustomerState.none;
						needNewChoice(customer);
					}
					if(customer.state == CustomerState.eating) {
						DoLeaveCustomer();
						return true;
					}
					//done eating.  should be invoked after doneWithFood
					if(customer.state == CustomerState.doneEating) {
						customer.state = CustomerState.none;
						anythingElse(customer);
						return true;
					}
					//customers needs check.  should be invoked after checkPlease
					if(customer.state == CustomerState.wantsCheck) {
						customer.state = CustomerState.none;
						getCheck(customer);
						return true;
					}
					//should be invoked after here is check.  waiter returns check to customer
					if(customer.state == CustomerState.WaitingForCheck) {
						customer.state = CustomerState.none;
						giveCheck(customer);
						return true;
					}
					//should be invoked after msgLeavingTable
					if(customer.state == CustomerState.leaving) {
						customer.state = CustomerState.none;
						emptyTable(customer);
						return true;
					}
				}
			}
			return false;
		}
		//---------------------------------------------------
		//---------------Actions-----------------------------
		//---------------------------------------------------
		//seats customer.  invokes gui action to seat customer
		private void seatCustomerAction(myCustomer customer) {
			waiterGui.GoToWaitingCust(customer.c.getX(), customer.c.getY());
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(name + " Seating " + customer.c.name + " at " + customer.table);
			waiterGui.GoToTable(customer.table); 
			customer.c.msgSitAtTable(customer.table, new Menu());
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			customer.state = CustomerState.seated;
			stateChanged();
		}
		//leaves the customer
		private void DoLeaveCustomer() {
			waiterGui.DoLeaveCustomer();
		}
		//goes to customer and asks what the order is.
		private void takeOrder(myCustomer cust) {
			waiterGui.GoToTable(cust.table);
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(name + ": What do you want?");
			cust.c.whatDoYouWant();
		}
		//brings order to cook
		private void relayOrder(myCustomer cust) {
			System.out.println(name + ": Bringing order to cook.");
			cust.state = CustomerState.waitingForFood;
			waiterGui.showOrder(cust.choice);
			waiterGui.GoToCookDrop();
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}
			cook.hereIsOrder(cust.choice, cust.table, this);
			System.out.println(name + ": Cook this order.");
			waiterGui.removeOrder();
			waiterGui.DoLeaveCustomer();
		}
		//returns order.  right now doubles as out of food
		private void returnOrder(myCustomer cust) {
			waiterGui.GoToCookReceive();
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			waiterGui.GoToTable(cust.table);
			waiterGui.finalizeOrder(cust.choice);
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(cust.choice != "Sorry") {
				System.out.println(name + " here is your " + cust.choice);
				waiterGui.removeOrder();
				cust.c.hereIsFood(cust.choice);
				cust.state = CustomerState.eating;
				stateChanged();
			}
		}
		//get new choice when food is gone
		private void needNewChoice(myCustomer cust) {
			waiterGui.GoToTable(cust.table);
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(name + ": Sorry, we are out of your choice.");
			cust.c.outOfFood();
			DoLeaveCustomer();
			stateChanged();
		}
		//approaches customer when they have finished eating and asks if they need anything else
		private void anythingElse(myCustomer cust) {
			waiterGui.GoToTable(cust.table);
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(name + "anything else?");
			cust.c.anythingElse();
		}
		//gets check from cashier
		private void getCheck(myCustomer cust) {
			System.out.println(name + ": Getting check for customer.");
			waiterGui.GoToCashier();
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cashier.checkRequest(this, cust.table, cust.choice, cust.c);
		}
		//goes to table and gives check to customer
		private void giveCheck(myCustomer cust) {
			waiterGui.GoToTable(cust.table);
			try {
				waitingForAnimation.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(name + ": here is your check. You owe $" + cust.owed);
			DoLeaveCustomer();
			cust.c.hereIsCheck(cust.owed);
		}
		//empty table
		private void emptyTable(myCustomer cust) {
			if(wantToGoOnBreak)
			{
				System.out.println(name + ": GOING ON BREAK");
				onBreak = true;
				host.onBreak(this);
			}
			customers.remove(cust);
			waiterGui.DoLeaveCustomer();
			host.msgLeavingTable(cust.c, this);
		}
		

		//----------------Utilities---------------------------------

		public void setGui(WaiterGui gui) {
			waiterGui = gui;
		}

		public WaiterGui getGui() {
			return waiterGui;
		}

		@Override
		public void msgLeaveWork() {
			// TODO Auto-generated method stub	
		}

		@Override
		public void msgGetPaid() {
			// TODO Auto-generated method stub
			
		}

}

