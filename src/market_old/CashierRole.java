package market_old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import city.Person;

public class CashierRole extends agent.Agent implements Cashier {

//// DATA /////////////////////////////////////////////////////////
	
	CashierGui gui;
	
	float moneyInRegister = 0;
	
	private static Map<String, Integer> inventory = new HashMap<String, Integer>() {
		{
			put("borsch", 100);
			put("caviar", 100);
			put("kebobs", 100);
			put("dog", 100);
		}
	};
	
	
	static Coord c; //temporary
	private static Map<String, Coord> locations = new HashMap<String, Coord>() {
		{
			put("borsch", c);
			put("caviar", c);
			put("kebobs", c);
			put("dog", c);
		}
	};
	
	private static Map<String, Float> Prices = new HashMap<String, Float>() {
		{
			put("borsch", 1.00f);
			put("caviar", 4.00f);
			put("kebobs", 2.50f);
			put("dog", 6.50f);
		}
	};
	
	List<MarketCustomerRole> customerQueue = new ArrayList<MarketCustomerRole>();
	
	List<Item> itemsToGet = new ArrayList<Item>();
	List<Item> itemsInHand = new ArrayList<Item>();
	MarketCustomerRole currentCustomer;
	
	public enum CashierState{readyForNewCustomer, receivedOrder, paid};
	public CashierState state;
	
//// MESSAGES /////////////////////////////////////////////////////
	
	public void msgEnteringMarket(MarketCustomerRole p) {
		print("Get in line!");
		customerQueue.add(p);
	}
	
	public void msgIWantToBuy(/*List<String> desiredItems, List<Integer> desiredQuantities*/ List<Item> itemsToBuy) {
		/*for (String s : desiredItems) {
			Item i = new Item(s, desiredQuantities.get(0));
			desiredQuantities.remove(0);
			itemsToGet.add(i);
		}*/
		itemsToGet = itemsToBuy;
		state = CashierState.receivedOrder;
		stateChanged();
	}
	
	public void msgHereIsPayment(float price) {
		moneyInRegister += price;
		state = CashierState.paid;
		stateChanged();
	}
	
	public void msgCantAffordLeavingMarket() {
		state = CashierState.readyForNewCustomer;
		stateChanged();
	}
	
	
//// SCHEDULER /////////////////////////////////////////////////////
	
	protected boolean pickAndExecuteAnAction() {
		
		if (state == CashierState.readyForNewCustomer) {
			helpNextCustomer();
			return true;
		}
		
		if (state == CashierState.receivedOrder) {
			calculatePrice();
			return true;
		}
		
		if (state == CashierState.paid) {
			getItems();
			return true;
		}
		
		return false;
	}

	
//// METHODS ///////////////////////////////////////////////////////////////
	
	public void helpNextCustomer() {
		currentCustomer = customerQueue.get(0);
		customerQueue.remove(0);
		print("Next customer!");
		currentCustomer.msgHowMayIHelpYou();
	}
	
	public void calculatePrice() {
		float price = 0.0f;
		for(Item i : itemsToGet) {
			price += ( i.price() * i.quantity() );
		}
		print("That's $" + price + ". Can you even afford that?");
		currentCustomer.msgItCostsThisMuch(price);
	}
	
	public void getItems() {
		float moneyOwedBack = 0; //money owed back to customer for lack of items in market inventory
		print("Alright, I'ma go fetch those for you...");
		for(Item i : itemsToGet) {
			//gui.doGoToItem(i.name());
			if (inventory.get(i.name()) >= i.quantity()) {
				inventory.put( i.name(), inventory.get(i.name()) - i.quantity() );
				itemsInHand.add(i);
			}
			else {
				moneyOwedBack += ( i.price() * ( i.quantity() - inventory.get(i.name()) ) );
				i.setQuantity(inventory.get(i.name()));
				inventory.put(i.name(), 0);
				itemsInHand.add(i);
			}			
		}
		itemsToGet.clear();
		moneyInRegister -= moneyOwedBack;
		print("Here's your stuff. Goodbye.");
		currentCustomer.msgHereAreYourItems(itemsInHand, moneyOwedBack);
		itemsInHand.clear();
		state = CashierState.readyForNewCustomer;
		stateChanged();
	}
	
	
	
//// CLASSES ///////////////////////////////////////////////////////////////
	
	public class Item {
		private String name;
		private int quantity;
		private float price;
		
		public Item(String name, int quantity) {
			this.name = name;
			this.quantity = quantity;
			price = inventory.get(name);
		}
		
		public String name() { return name;	}
		public int quantity() { return quantity; }
		public double price() { return price; }
		
		public void setQuantity(int quantity) { this.quantity = quantity; }
	}
	
	public class Coord {
		//temporary class until I make gui stuff, to represent the location of items in the market
		public Coord() {}
	}
	
}
