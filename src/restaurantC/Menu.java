package restaurantC;

import java.util.Vector;

public class Menu {
	public Vector<Food> foods = new Vector<Food>();
	public class Food{
		double price;
		String name;
		boolean available;
		public Food(String n, double p) {
			name = n;
			price = p;
			available = true;
		}
	}
	public Food chicken = new Food("Chicken", 10.99);
	public Food steak = new Food("Steak", 16.99);
	public Food pizza = new Food("Pizza", 8.99);
	public Food salad = new Food("Salad", 5.99);
	public Menu() {
		foods.add(chicken);
		foods.add(steak);
		foods.add(pizza);
		foods.add(salad);
	}
} 