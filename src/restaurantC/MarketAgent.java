package restaurantC;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Agent;
import restaurantC.interfaces.Market;


// Restaurant Market Agent

public class MarketAgent extends Agent implements Market {

	//utilities, like getters and setters
	public int getAvailability(String s) {
		if(s.equals("Steak")) {
			return steak.remaining;
		}
		else if(s.equals("Chicken")) {
			return chicken.remaining;
		}
		else if(s.equals("Salad")) {
			return salad.remaining;
		}
		else if(s.equals("Pizza")) {
			return pizza.remaining;
		}
		else {
			return 0;
		}
	}
	
	public void setCashier(CashierRole c) {
		cashier = c;
	}
	//-----------------------------------------------------------
	//-----------------------Data--------------------------------
	//-----------------------------------------------------------
	public String name;				//name of market
	private List<myClient> clients = Collections.synchronizedList(new ArrayList<myClient>());
	private CashierRole cashier;
	private double money = 0.00;
	
	//inner class called myClient will keep track of stuff about the client
	private class myClient {
		Food choice;
		CookRole cook;
		int amountNeeded;
		public myClient(String s, CookRole c, int a) {
			cook = c;
			amountNeeded = a;
			if(s == "Steak") {
				choice = steak;
			}
			else if(s == "Salad") {
				choice = salad;
			}
			else if(s == "Chicken") {
				choice = chicken;
			}
			else if(s == "Pizza") {
				choice = pizza;
			}	
		}
	}
	//inner class called Food.  keeps track of availability
	private class Food {
		String type;
		int remaining;
		double price; 
		
		public Food(String t) {
			type = t;
			if(t == "Steak") {
				remaining = 1;
				price = 10.00;
			}
			else if (t == "Salad") {
				remaining = 1;
				price = 3.00;
			}
			else if(t == "Pizza") {
				remaining = 4;
				price = 5.00;
			}
			else if(t == "Chicken") {
				remaining = 4;
				price = 8.00;
			}
		}
	}
	
	//creates foods to track availability
	private Food steak = new Food("Steak");
	private Food pizza = new Food("Pizza");
	private Food salad = new Food("Salad");
	private Food chicken = new Food("Chicken");

	//constructor
	public MarketAgent(String name) {
		super();
		this.name = name;
		System.out.println("Market " + name +" has been created.");
		}
	
	//------------------------------------------------
	//--------------Messages--------------------------
	//------------------------------------------------
	//from cook, ask for food
	public void askForFood(CookRole cook, String food, int amount) {
		clients.add(new myClient(food, cook, amount));
		stateChanged();
	}

	//from cashier, receiving order amount
	public void hereIsMoney(double amount) {
		System.out.println(name + ": received $" + amount);
		money += amount;
		System.out.println(name + ": now has $" + money);
	}

	//--------------Scheduler--------------------------
	protected boolean pickAndExecuteAnAction() {
		if(!clients.isEmpty()) {
			giveFood();
			return true;
		}
		return false;
	}

	//------------------------------------------------------
	//--------------Actions---------------------------------
	//------------------------------------------------------
	private void giveFood() {
		deliver();		//seems counter-intuitive, but will make it easier to add new features I think
	}
	private void deliver() {
		System.out.println(name + ": here is your " + clients.get(0).choice.type);
		clients.get(0).cook.giveFood(clients.get(0).choice.type, clients.get(0).amountNeeded);
		System.out.println(name + ": billing cashier $" + (clients.get(0).amountNeeded * clients.get(0).choice.price));
		cashier.sendBill(this, clients.get(0).amountNeeded * clients.get(0).choice.price);
		clients.get(0).choice.remaining--;
		clients.remove(clients.get(0));
	}
}

