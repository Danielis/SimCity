package market;

import java.util.*;


public class Market {
	double balance;
	List <Item> inventory = new ArrayList<Item>();
	int idIncr = 0;
	
	public Market(){
		balance = 5000;
		
		inventory.add(new Item("Steak", 0, 13));
		inventory.add(new Item("Salad", 100, 3));
		inventory.add(new Item("Chicken", 100, 9));
		inventory.add(new Item("Pizza", 100, 4));
		inventory.add(new Item("Car", 20, 20000)); // lol
	}
	
	
	public class Item {
		String name;
		int quantity;
		double price;
		
		Item(String n, int q, double p){
			name = n;
			quantity = q;
			price = p;
		}
	}


	public double calculatePrice(String item, int quantity) {
		for (Item i : inventory){
			if (i.name.equals(item))
				return i.price * quantity;
		}
		return 0;
	}


	public void RemoveInventory(String item, int quantity) {
		for (Item i : inventory){
			if (i.name.equals(item)){
				i.quantity -= quantity;
			}
				
		}
	}
	
	public int Amount(String item){
		for (Item i : inventory){
			if (i.name.equals(item)){
				return i.quantity;
			}
				
		}
		return 0;
	}
	

}


