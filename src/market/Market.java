package market;

import java.util.*;

import javax.swing.JFrame;

import bank.gui.BankPanel;
import market.gui.*;
import roles.Building;

public class Market extends Building {
	double balance;
	List <Item> inventory = new ArrayList<Item>();
	int idIncr = 0;
	String name;
	public MarketGui gui;
	public MarketPanel panel;

	public Market(String n, MarketGui gui){
		balance = 5000;
		name = n;
		this.gui = gui;
		this.panel = gui.restPanel;
    	gui.restPanel.setMarket(this);
        gui.setTitle(name);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	type = buildingType.market;
		inventory.add(new Item("Steak", 0, 13));
		inventory.add(new Item("Salad", 100, 3));
		inventory.add(new Item("Chicken", 100, 9));
		inventory.add(new Item("Chicken", 100, 9));
		inventory.add(new Item("Eggs", 100, .5));
		inventory.add(new Item("Milk", 100, 4));
		inventory.add(new Item("Flour", 100, 3));
		inventory.add(new Item("Juice", 100, 6));
		inventory.add(new Item("Pasta", 100, 3));
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


	public boolean DoesStock(String item) {
		for (Item i : inventory){
			if (i.name.equals(item))
				return true;
		}
		return false;
	}
	

}


