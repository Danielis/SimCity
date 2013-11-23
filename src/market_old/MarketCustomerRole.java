package market_old;

import java.util.ArrayList;
import java.util.List;

import market_old.CashierRole.Item;

public class MarketCustomerRole extends agent.Agent implements MarketCustomer {

//// DATA ////////////////////////////////////////////////////////////////
	
	CashierRole c;
	
	float money = 100; //temporary money value until integrated with Person
	
	List<Item> itemsToBuy = new ArrayList<Item>();
	List<Item> itemsOnMe;
	//List<String> itemsToBuy = new ArrayList<String>();
	//List<Integer> quantityOfItemsToBuy = new ArrayList<Integer>();
	
	public enum MarketCustomerState{waiting, readyToBeHelped, orderGiven, paid, leaving};
	public MarketCustomerState state = MarketCustomerState.waiting;
	
//// MESSAGES ////////////////////////////////////////////////////////////
	
	public void msgHowMayIHelpYou() {
		state = MarketCustomerState.readyToBeHelped;
		stateChanged();
	}
	
	public void msgItCostsThisMuch(float price) {
		if (money > price){
			money -= price;
			c.msgHereIsPayment(price);
			state = MarketCustomerState.paid;
		}
		else {
			c.msgCantAffordLeavingMarket();
			state = MarketCustomerState.leaving;
		}
	}
	
	public void msgHereAreYourItems(List<Item> items, float moneyBack) {
		itemsOnMe.addAll(items);
		itemsToBuy.clear();
		print("Thanks");
		state = MarketCustomerState.leaving;
	}
	
	
//// SCHEDULER ///////////////////////////////////////////////////////////
	
	protected boolean pickAndExecuteAnAction() {
		
		if (state == MarketCustomerState.readyToBeHelped) {
			giveOrderToCashier();
		}
		
		
		return false;
	}
	
	
	
//// METHODS /////////////////////////////////////////////////////////////
	
	public void giveOrderToCashier() {
		/*System.out.print("Yeah, get me ");
		for (int i = 0; i < itemsToBuy.size(); i++) {
			//System.out.print(quantityOfItemsToBuy.get(i) + " " + itemsToBuy(i) + ", ");
		}
		System.out.println("and uhhh that's it...");*/
		
		//c.msgIWantToBuy(itemsToBuy, quantityOfItemsToBuy);
		c.msgIWantToBuy(itemsToBuy);
		state = MarketCustomerState.orderGiven;
	}
	
	public void payCashier() {
		
	}
	
	
	
//// CLASSES /////////////////////////////////////////////////////////////
	
	/*public class Item {
		private String name;
		private int quantity;
		private float price;
		
		public Item(String name, int quantity) {
			this.name = name;
			this.quantity = quantity;
		}
		
		public String name() { return name;	}
		public int quantity() { return quantity; }
		public double price() { return price; }
		
		public void setQuantity(int quantity) { this.quantity = quantity; }
	}*/
	
	
	

}
