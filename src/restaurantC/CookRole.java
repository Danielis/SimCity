package restaurantC;

import java.awt.image.ImageObserver;
import java.util.*;
import java.util.concurrent.Semaphore;

import restaurantC.gui.AnimationPanel;
import restaurantC.gui.CookGui;
import roles.Role;


 //Restaurant Cook Agent


public class CookRole extends Role {

	//--------------------------------------------------------
	//------------------Data----------------------------------
	//--------------------------------------------------------
	//Later we will see how it is implemented
	public enum AgentState{cooking, doneCooking, buying};
	AgentState state = AgentState.doneCooking;
	public String name;
	Timer timer = new Timer();
	private Semaphore cookSemaphore = new Semaphore(0);
	private List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	private CookGui cookGui;
	public RestaurantC restaurant;

	//inner class to handle foods
	private class Food {
		String type;
		int cookTime;
		int remaining;
		boolean moreAvailable = true;
		private CookGui cookGui;
		
		public Food(String t) {
			type = t;
			if(t == "Steak") {
				cookTime = 5;
				remaining = 50;
			}
			else if (t == "Salad") {
				cookTime = 2; 
				remaining = 50;
			}
			else if(t == "Pizza") {
				cookTime = 3;
				remaining = 50;
			}
			else if(t == "Chicken") {
				cookTime = 4;
				remaining = 50;
			}
		}
	}
	
	//inner class to handle a meal
	private class Meal{
		int table;
		Food choice;
		boolean done;
		WaiterRole waiter;
		int remaining; 
		public Meal(int t, String C, WaiterRole w) {
			table = t;
			if(C == "Steak")
				choice = steak;
			else if(C == "Chicken")
				choice = chicken;
			else if(C == "Salad")
				choice = salad;
			else if(C == "Pizza")
				choice = pizza;
			waiter = w;
		}
	}

	//list of meals
	private List<Meal> meals = new LinkedList<Meal>();
	
	//list of foods that are going to be referenced by the meal class.  this allows me to track remaining amounts. 
	private Food steak = new Food("Steak");
	private Food pizza = new Food("Pizza");
	private Food salad = new Food("Salad");
	private Food chicken = new Food("Chicken");
	private int salary;
	private AnimationPanel animationPanel;

	//constructor
	public CookRole(String name) {
		super();
		this.name = name;
		
	}
	
	//Getters/Setters
	public void setMarket(MarketAgent mrkt) {
		markets.add(mrkt);
	}
	
	public void setGui(CookGui gui) {
		cookGui = gui;
	}
	
	//------------------------------------------------
	//--------------Messages--------------------------
	//------------------------------------------------
	//from waiter.  giving a meal to be cooked
	public void hereIsOrder(String Choice, int table, WaiterRole w) {
		cookGui.showOrder(Choice);
		meals.add(new Meal(table, Choice, w));
		stateChanged();
	}
	//from market.  adds food
	public void giveFood(String choice, int amount) {
		System.out.println(name + ": Receiving: "  + amount + " of item " + choice);
		if(choice == "Steak") {
			steak.remaining += amount;
			System.out.println(name + ": Amount remaining: "  + steak.remaining);
		}
		else if (choice == "Salad") {
			salad.remaining += amount;
			System.out.println(name + ": Amount remaining: "  + salad.remaining);
		}
		else if(choice == "Pizza") {
			pizza.remaining += amount;
			System.out.println(name + ": Amount remaining: "  + pizza.remaining);
		}
		else if(choice  == "Chicken") {
			chicken.remaining += amount;
			System.out.println(name + ": Amount remaining: "  + chicken.remaining);
		}
		state = AgentState.doneCooking;
		stateChanged();
	}
	//from market.  out of food
	public void outOfChoice(MarketAgent market, String food) {
		state = AgentState.doneCooking;
		stateChanged();
	}

	//-------------------------------------------------
	//--------------Scheduler--------------------------
	//-------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		synchronized(meals) {
		for(Meal meal:meals) {
			if(meal.done == false && state == AgentState.doneCooking) {
				if(meal.choice.remaining > 0) {
					state = AgentState.cooking;
					cookMeal(meal);	
					return true;
				}
				else {
					state = AgentState.cooking;
					outOfFood(meal);
					return true;
				}
			}

		}
		}
		if(chicken.remaining<=2 && state == AgentState.doneCooking && chicken.moreAvailable){
			state = AgentState.buying;
			getMoreFood(chicken);
			return true;
		}
		else if(steak.remaining<=2 && state == AgentState.doneCooking && steak.moreAvailable) {
			state = AgentState.buying;
			getMoreFood(steak);
			return true;
		}
		else if(salad.remaining<=2 && state == AgentState.doneCooking && salad.moreAvailable) {
			state = AgentState.buying;
			getMoreFood(salad);
			return true;
		}
		else if(pizza.remaining<=2 && state == AgentState.doneCooking && pizza.moreAvailable) {
			state = AgentState.buying;
			getMoreFood(pizza);
			return true;
		}
		return false;
	}

	//--------------------------------------------------------
	// ----------------------Actions--------------------------
	//--------------------------------------------------------
	//cooks meal when there is food
	private void cookMeal(Meal meal) {	
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				cookSemaphore.release();
			}
		},
		5000);
		try {
			cookSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.finalizeOrder(meal.choice.type);
		meal.choice.remaining--;
		System.out.println(name + ": the food is ready.");
		System.out.println(name + ": Remaining inventory: " + meal.choice.remaining);
		meals.remove(meal);
		state = AgentState.doneCooking;
		meal.waiter.foodReady(meal.choice.type, meal.table);
	}
	//when there isn't food, this out of food action is called
	private void outOfFood(Meal meal) {
		meals.remove(meal);
		System.out.println(name + ": Out of a choice!");
		state = AgentState.doneCooking;
		meal.waiter.outOfFood(meal.choice.type, meal.table);
	}
	//get more food food
	private void getMoreFood(Food food) {
		int needed = 2;
		System.out.println("Cook: Running low on " + food.type + ".");
		if(markets.get(0).getAvailability(food.type) >= 2) {
			markets.get(0).askForFood(this, food.type, needed);
			return;
		}
		else if(markets.get(0).getAvailability(food.type) == 1) {
			markets.get(0).askForFood(this, food.type, 1);
			needed--;
		}
		if(markets.get(1).getAvailability(food.type) >= 2) {
			markets.get(1).askForFood(this, food.type, needed);
			return;
		}
		else if(markets.get(1).getAvailability(food.type) == 1) {
			markets.get(1).askForFood(this, food.type, 1);
			needed--;
			if(needed == 0) return;
		}
		if(markets.get(2).getAvailability(food.type) >= 2) {
			markets.get(2).askForFood(this, food.type, needed);
		}
		else if(markets.get(2).getAvailability(food.type) == 1) {
			markets.get(2).askForFood(this, food.type, 1);
			needed--;
		}
		if(needed == 2){
			food.moreAvailable = false;
		}
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub
		
	}

	public void setSalary(int i) {
		salary = i;
	}

	public AnimationPanel copyOfAnimationPanel() {
		return this.animationPanel;
	}

	public void setAnimationPanel(AnimationPanel animationPanel2) {
		this.animationPanel = animationPanel2;
	}	
}

