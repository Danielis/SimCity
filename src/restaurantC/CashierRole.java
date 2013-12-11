package restaurantC;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurantC.interfaces.Cashier;
import restaurantC.interfaces.Customer;
import restaurantC.interfaces.Market;
import restaurantC.interfaces.Waiter;
import roles.Role;

//Restaurant Cashier Agent
public class CashierRole extends Role implements Cashier {

	//getters and setters
	public double getMoney() {
		return money;
	}
	public List<Meal> getMeals() {
		return meals;
	}

	//-------------Data---------------------------------
	public String name;
	private double money;

	//inner class to track a meal with waiter, price, table, choice
	public class Meal {
		public Waiter waiter;
		public double price;
		public double change = 0;
		public int table;
		public String choice;
		public Customer customer;
		public boolean paid = false;
		public boolean processed = false;
	}

	//inner class to keep track of what the cashier owes to the markets
	private class MyMarket {
		public double amountOwed;
		public Market market;
	}
	//vector of markets
	public List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());
	//vector of meals
	private List<Meal> meals = Collections.synchronizedList(new ArrayList <Meal>());
	//constructor
	public CashierRole(String name) {
		super();
		money = 100;
		this.name = name;
	}

	//--------------------------------------------------
	//--------------Messages----------------------------
	//---------------------------------------------------

	//sent from waiter requesting check
	public void checkRequest(Waiter w, int t, String c, Customer cust) {
		synchronized(meals) {
			for(Meal m: meals) {
				if(m.customer == cust) {
					m.waiter = w;
					m.table = t;
					m.choice = c;
					m.paid = false;
					m.processed = false;
					stateChanged();
					return;
				}
			}
		}
		Meal tempMeal = new Meal();
		tempMeal.waiter = w;
		tempMeal.table = t;
		tempMeal.choice = c;
		tempMeal.customer = cust;
		tempMeal.price = 0;
		meals.add(tempMeal);
		stateChanged();
	}
	//sent from customer paying check
	public void hereIsMoney(double amount, Customer c) {
		synchronized(meals) {
			for(Meal m:meals) {
				if(m.customer == c) {
					money += amount;
					if(amount > m.price){
						m.change = amount - m.price;
						m.price = 0;
					}
					else {
						m.change = 0;
						m.price -= amount;
					}
					m.paid = true;
				}
			}
		}
		System.out.println(name + ": I was given $" + amount);
		stateChanged();
	}
	//sent from market, billing cashier
	public void sendBill(Market m, double amount) {
		MyMarket tempMarket = new MyMarket();
		tempMarket.market = m;
		tempMarket.amountOwed = amount;
		markets.add(tempMarket);
		stateChanged();
	}
	//--------------Scheduler--------------------------
	public boolean pickAndExecuteAnAction() {
		synchronized(meals) {
			for(Meal m:meals) {
				if(!m.processed) {
					processMeal(m);
					return true;
				}
				else if(m.paid) {
					giveChange(m);
					return true;
				}
			}
		}
		synchronized(markets) {
			for(MyMarket mm:markets) {
				if(mm.amountOwed > 0) {
					payMarket(mm);
					return true;
				}
			}
		}
		return false;
	}

	//------------------Actions-------------------------------

	public void processMeal(Meal m) {
		System.out.println("Cashier: processing meal.");
		if(m.choice == "Chicken") {
			m.price += 10.99;
			m.processed = true;
			m.waiter.hereIsCheck(m.table, m.price);
		}
		else if(m.choice == "Steak") {
			m.price += 15.99;
			m.processed = true;
			m.waiter.hereIsCheck(m.table, m.price);
		}
		else if(m.choice == "Salad") {
			m.price += 5.99;
			m.processed = true;
			m.waiter.hereIsCheck(m.table, m.price);
		}
		else if(m.choice == "Pizza") {
			m.price += 8.99;
			m.processed = true;
			m.waiter.hereIsCheck(m.table, m.price);
		}
	}
	//meal paid
	public void giveChange(Meal m) {
		if(m.price == 0) {
		System.out.println(name + ": Change to be given: $" + m.change);
		money -= m.change;
		System.out.println(name + ": Restaurant has now made $" + money);
		System.out.println(name + ": Meal paid.  Thank you.");
		m.customer.hereIsChange(m.change);
		meals.remove(m);
		}
		else {
			System.out.println(name + ": Not enough money given.  Pay next time. Owed: $" + m.price);
			System.out.println(name + ": Restaurant has now made $" + money);
			System.out.println(name + ": Meal partially paid.  Thanks I guess");
			m.change = 0;
			m.choice = "";
			m.paid = false;
			m.processed = true;
			m.customer.hereIsChange(m.change);
		}
	}
	
	//pay market the amount owed
	public void payMarket(MyMarket mm) {
		System.out.println(name + ": paying market $" + mm.amountOwed);
		mm.market.hereIsMoney(mm.amountOwed);
		money -= mm.amountOwed;
		System.out.println(name + ": Restaurant now has $" + money);
		markets.remove(mm);
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

