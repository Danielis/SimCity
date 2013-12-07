package restaurantA;

import agent.Agent;
import restaurantA.MarketAgent;
import restaurantA.Table;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.CookGui;
import restaurantA.gui.CustomerGui;
import restaurantA.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CookAgent extends Agent implements Cook {
	
 private class CookTimerTask extends TimerTask {
		  Order o;
		  CookAgent cook;
		  public CookTimerTask(Order o, CookAgent cook) {
		   this.o = o;
		   this.cook = cook;
		  }
		  @Override
		  public void run() {
		 
		  }
		 }
	
	
	public class Order {
		WaiterAgent w;
		String choice;
		Table table;
		orderState s;
		CustomerAgent c;
		

		Order(WaiterAgent w, String choice, Table table, orderState s, CustomerAgent c){
			this.w = w;
			this.choice = choice;
			this.table = table;
			this.s = s;
			this.c = c;
		}
		}

	enum orderState {
		pending, cooking, done, finished, reorder, pickedUp
	}
	
	


	Timer timer = new Timer();
	//map(string, Food) foods;
	private String name;
	public Collection<Order> orders;
	public List<MyMenuItem> menu;
	public Collection<MarketAgent> markets;
	private CashierAgent cashier;
	private int marketIndex = 0;
	private Semaphore cookingFood = new Semaphore(0,true);
	private CookGui cookGui;
	public AnimationPanel copyOfAnimPanel;
	public CookAgent(String name, CashierAgent cashier) {
		super();
		this.name = name;
		this.cashier = cashier;
		orders = Collections.synchronizedList(new ArrayList<Order>());
		menu = Collections.synchronizedList(new ArrayList<MyMenuItem>());
		markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
		createInventory();
		for (int i = 0; i < 3; i++)
		addMarket();
		addMarket(15);
	}
	public void setAnimPanel(AnimationPanel animationPanel)
	{
		copyOfAnimPanel = animationPanel;
	}

	public void addMarket() {
		int temp = 2;
		MarketAgent mark = new MarketAgent(marketIndex, temp, cashier); // adds a market with default stock of 5 per item
		mark.startThread();
		markets.add( mark ); 
		print("Added Market " + marketIndex + " with default stock of " + temp + " per item");
		marketIndex++;
	}

	public void addMarket(int temp){
		MarketAgent mark = new MarketAgent(marketIndex, temp, cashier); // adds a market with default stock of 5 per item
		mark.startThread();
		markets.add( mark ); 
		print("Added Market " + marketIndex + " with default stock of " + temp + " per item");
		marketIndex++;
	}

	private void createInventory() {
		int Inv = 2;
		int Cap = 5;
		int Thr = 3;
		menu.add( new MyMenuItem("Steak", 5, Inv, Thr, Cap, 16) );
		menu.add( new MyMenuItem("Chicken", 5, Inv, Thr, Cap, 11) );
		menu.add( new MyMenuItem("Pizza", 5, Inv, Thr, Cap, 9) );
		menu.add( new MyMenuItem("Salad", 5, Inv, Thr, Cap, 6) );
	}


	public String getName() {
		return name;
	}


	// Messages
	
	public void msgFoodDone(Order o){
		//print("test");
		stateChanged();
	}

	public void msgHereIsOrder(WaiterAgent w, String choice, Table table, CustomerAgent c) { 
		//print("new order");
		orders.add(new Order(w, choice, table, orderState.pending, c));
		stateChanged();
	}

	public void msgPickedUpFood(CustomerAgent c){
		int temp = 0;
		for (Order o :orders){
			if (o.c == c)
				o.s = orderState.pickedUp;
			if (o.s == orderState.finished)
					temp++;
		}
		if (temp == 0)
			cookGui.DonePlateFood();
	}
	
	public void msgHereIsFood(Market market, String food, int quantity) { 
		//synchronized(menu){
		for (MyMenuItem m : menu){
			if (m.name == food){
				m.stock += quantity;
				m.s = itemState.normal;
			}
		}
		//}
		stateChanged();
	}
	
	public void msgCanOnlyFullfillPartial(Market mark, String food, int quantity){
		for (MyMenuItem m : menu){
			if (m.name == food){
				//print("tst");
				m.ordered = quantity;
				m.s = itemState.needsReOrder;
			}
		}
		print("Received message that " + mark.getName() + " cannot fulfill full order for " + food + " so will order more from another market");
		stateChanged();
	}
	
	public void msgOutOfFood(Market market, String food) { 
		synchronized(menu){
		for (MyMenuItem m : menu){
			if (m.name == food){
				m.s = itemState.needsReOrder;
			}
		}
		}
		
//		synchronized(markets){
//		for (Market m : markets){
//			if (market == m)
//				markets.remove(m);
//		}
//		}
		stateChanged();
	}
	
	public void msgGuiCallToOrderMore(){
		synchronized(menu){
		for (MyMenuItem m : menu){
				if (m.s != itemState.orderedMore)
				m.s = itemState.checkReOrder;
		}
		}
		stateChanged();
	}
		

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
	//	print("scheduler called");
		synchronized(menu){
		for (MyMenuItem m : menu){
		//	if (m.s == itemState.normal)
			//	print("normal");
			//if (m.s == itemState.orderedMore)
			//	print("orderedMore");
//			if (m.s == itemState.checkReOrder){
//				print("reached check reorder check");
//				CheckStock();
//				return true;
//			}
			if (m.s == itemState.needsReOrder){
				OrderMore(m, m.capacity - m.stock);
				return true;
			}
		}}

		if (orders.size() > 0){
			synchronized(orders){
			for (Order order : orders) {
			if (order.s == orderState.done){
				plateIt(order);
				return true;
				}
			}
		}
			synchronized(orders){
		for (Order order : orders){
			if (order.s == orderState.pending){
				CheckAndCook(order);
				return true;
			}
		}}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void CheckAndCook(Order o){
		synchronized(menu){
		for (MyMenuItem f : menu){
			
			if (f.name == o.choice){
				print(f.name + " stock: " + f.stock);
				
				if (f.stock <= 0 ){
					
					if(f.s != itemState.orderedMore){ // if you haven't already ordered more
						f.s = itemState.needsReOrder;
					}
						OutOfFood(o);
						print("Out of stock of " + f.name);
						return;
				}
				else{
					f.stock = f.stock - 1;
					print(f.name + " stock reduced to: " + f.stock);
					if (f.stock <= f.threshold){
						print("Stock is below threshold for " + f.name);
						if(f.s != itemState.orderedMore) // if you haven't already ordered more
							f.s = itemState.needsReOrder;
						}
					CookIt(o);
				}
				print(f.name + " stock: " + f.stock);
				return;
			}
		}
		}
	}
	
	public void CheckStock(){
		synchronized(menu){
for (MyMenuItem f : menu){

		if (f.stock <= f.threshold){
			print("Stock is below threshold for " + f.name);
			if(f.s != itemState.orderedMore) // if you haven't already ordered more
				//f.s = itemState.needsReOrder;
				OrderMore(f, f.capacity - f.stock);
			}
		else{
			f.s = itemState.normal;
			print("We have enough of " + f.name);
		}
		
	}	
		}
		stateChanged();
}

	
	private void OutOfFood(Order o){
		o.w.msgOutOfFood(this, o.choice, o.table, o.c);
		o.s = orderState.reorder;
	}
	
	private void OrderMore(MyMenuItem f, int quantity){
		// this is dumb
		f.s = itemState.orderedMore;
		int rand = (int )(Math.random() * markets.size());
		//print("~~~~~~~going to order from market " + rand);
		
		if (f.ordered > 0){
			quantity = quantity - f.ordered;
			f.ordered = 0;
		}
		
		if (markets.size() == 0)
			print("There are no more markets with food");
		else{
			synchronized(markets){
			for (MarketAgent m : markets){
				//print("market index: " + m.getIndex() + ", rand: " + rand);
				if (m.getIndex() == rand)
				{
					if (!m.OutOfStock){
					m.msgNeedFood(this, f.name, quantity);
					print("Ordering " + f.name + " (" + quantity + ")");
					}
				}
			}
			}
		}
		
	}
	
	private void plateIt(Order o) {
		print("Plating " + o.choice + " for customer " + o.c.getName() + " at table " + o.table.tableNumber);
		o.s = orderState.finished;
		cookGui.DoPlateFood(o.choice);
		o.w.msgOrderDone(this, o.choice, o.table, o.c);
	}
	
	private void CookIt(Order o){
		print("Cooking " + o.choice + " for customer " + o.c.getName() + " at table " + o.table.tableNumber);
		//o.s = orderState.done;
		
		//TODO
//		timer.schedule(new TimerTask() {
//			@SuppressWarnings("unused")
//			Object cookie = 1;
//			public void run() {
//				o.s = orderState.done;
		
//			}
//		},
//		5000);
		
			o.s = orderState.cooking;
			cookGui.DoGrillFood(o.choice);
			timer.schedule(new CookTimerTask(o, this) {
				Object cookie = 1;
				public void run() {
					cookGui.DoneGrillFood();
				//	cookingFood.release();
					o.s = orderState.done;
					cook.msgFoodDone(o);
					print("Cooked " + o.choice + " for customer " + o.c.getName() + " at table " + o.table.tableNumber);
					
					stateChanged();
				}
			},
			5000);
			
		//	try {
				//cookingFood.acquire();
		//	} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
		//	}
			
			//o.s = orderState.done;
			//print("Cooked " + o.choice + " for customer " + o.c.getName() + " at table " + o.table.tableNumber);
			
			
	}
	
	public void setGui(CookGui g) {
		cookGui = g;
		//print("gui set");
	}




	//utilities


	
	
	public class MyMenuItem{
		String name;
		int stock;
		itemState s;
		int cookingTime;
		int threshold;
		int capacity;
		int price;
		int ordered = 0;
		
		MyMenuItem(String name, int c, int stock, int threshold, int capacity, int price){
			this.name = name;
			this.stock = stock;
			this.cookingTime = c;
			this.threshold = threshold;
			this.capacity = capacity;
			this.s = itemState.normal;
			this.price = price;
		}
	}

	enum itemState {normal, orderedMore, needsReOrder, checkReOrder};

	
	

}




