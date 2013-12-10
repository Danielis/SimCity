package restaurantA;

import restaurantA.RestaurantA.*;
import restaurantA.ProducerConsumerMonitor.Ticket;
import restaurantA.MarketAgent;
import restaurantA.Table;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.CookGui;
import restaurantA.interfaces.*;
import roles.*;

import java.util.*;
import java.util.concurrent.Semaphore;

public class CookAgent extends Role implements Cook {

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


		public Order(WaiterAgent w, String choice, Table table, orderState s, CustomerAgent c){
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
	private String name;
	public Collection<Order> orders;
	public List<MyMenuItem> menu;
	public Collection<MarketAgent> markets;
	private CashierAgent cashier;
	private int marketIndex = 0;
	private Semaphore cookingFood = new Semaphore(0,true);
	private CookGui cookGui;
	public AnimationPanel copyOfAnimPanel;
	public RestaurantA rest = null;
	private Boolean leave = false;
	private double balance = 0;
	double salary;
	
	ProducerConsumerMonitor theMonitor;
	
	public CookAgent(String name, CashierAgent cashier) {
		super();
		this.name = name;
		this.cashier = cashier;
		orders = Collections.synchronizedList(new ArrayList<Order>());
		//menu = rest.menu;
		markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
		for (int i = 0; i < 3; i++)
			addMarket();
		addMarket(15);
	}
	
	public CookAgent(String name2) {
		this.name = name2;
		orders = Collections.synchronizedList(new ArrayList<Order>());

		markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
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

	public String getName() {
		return name;
	}


	/* MESSAGES*******************************************************************/
	public void msgFoodDone(Order o){
		stateChanged();
	}
	
	public void msgHereIsMonitor(ProducerConsumerMonitor theMonitor2) {
		theMonitor = theMonitor2;
	}
	
	public void msgNotEmpty() {
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


	/* SCHEDULER ***************************************************************/
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		synchronized(menu){
			for (MyMenuItem m : menu)
			{
				if (m.s == itemState.needsReOrder){
					OrderMore(m, m.capacity - m.stock);
					return true;
				}
			}
		}

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
				}
			}
		}

		if (theMonitor != null && theMonitor.getCount() > 0){
			 TakeTicket();
			 return true;
		}
		
		if (leave){
			if (canLeave())
				LeaveWork();
			else
				return true;
		}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private boolean canLeave() {
		if (rest.workingWaiters.isEmpty()  && rest.currentCustomers.isEmpty())
		{
			boolean temp = true;
			for (Order o : orders)
			{
				if (o.s != orderState.finished)
					temp = false;
			}
			return temp;
		}
		else
			return false;
	}

	private void LeaveWork() {
		rest.NoCook();
		cookGui.setDone();
		myPerson.msgLeftWork(this, balance);

	}
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
	}
	
	private void TakeTicket()
	{
		 Ticket data = theMonitor.remove();
		 consume_item(data);
	}
	
	private void consume_item(Ticket data)
	{
		orders.add(new Order((WaiterAgent)data.w, data.choice, data.table, orderState.pending, (CustomerAgent)data.cu));
	}

	public void setGui(CookGui g) {
		cookGui = g;
	}
	@Override
	public void msgLeaveWork() {
		leave = true;
		stateChanged();
	}
	@Override
	public void msgGetPaid() {
		balance += this.rest.takePaymentForWork(salary);

	}
	public void setRestaurant(RestaurantA rest2) {
		rest = rest2;
		menu = rest.menu;
	}

	public void setSalary(double i) {
		salary = i;
		
	}
}




