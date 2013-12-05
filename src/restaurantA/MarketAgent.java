package restaurantA;

import agent.Agent;
import restaurantA.CashierAgent;
import restaurantA.Table;
import restaurantA.CookAgent.orderState;
import restaurantA.interfaces.Market;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.Semaphore;

public class MarketAgent extends Agent implements Market {
	private String name;
	private int index;
	private CashierAgent cashier;
	public Collection<MyOrder> orders;
	public Collection<MyInventory> inventory;
	public boolean OutOfStock = false;
	Timer timer = new Timer();
	private Semaphore preppingOrder = new Semaphore(0,true);

	public MarketAgent(int index, int inv, CashierAgent cashier){
		super();
		this.name = "Market " + index;
		this.cashier = cashier;
		this.index = index;
		orders = Collections.synchronizedList(new ArrayList<MyOrder>());
		inventory = new ArrayList<MyInventory>();
		
		inventory.add(new MyInventory("Steak", inv, 8));
		inventory.add(new MyInventory("Pizza", inv, 3));
		inventory.add(new MyInventory("Salad", inv, 1));
		inventory.add(new MyInventory("Chicken", inv, 4));
		
	}
	
	
	//* messages *//
	public void msgHereIsPayment(int amount){
		
	}
	public void msgNeedFood(CookAgent c, String f, int quantity) {
		orders.add( new MyOrder(c, f, quantity) );
		stateChanged();
	}

	// * scheduler *//
	protected boolean pickAndExecuteAnAction() {
		synchronized(orders){
		for (MyOrder o : orders){
			if (o.s == orderState.ordered){
				fulfillOrder(o);
				return true;
			}
		}
		}
		synchronized(orders){
		for (MyOrder o : orders){
			if (o.s == orderState.fulfilled){
				sendOrder(o);
				return true;
			}
		}
		}
		return false;
	}
	
	//* actions *//
	private void fulfillOrder(MyOrder o){
		for (MyInventory i : inventory){
			if (i.name == o.name)
			{
				if (i.stock >= o.quantity){
					i.stock -= o.quantity;
					canFulfill(o);
					print(i.name + " new stock: " + i.stock);
				}
				else if (i.stock > 0){
					o.quantity = i.stock;
					partialFulfill(o);
					i.stock -= o.quantity;
					print(i.name + " new stock: " + i.stock);
				}
				else{
					cannotFulfill(o);
				}
			}
		}
		
		
		
	}
	private void partialFulfill(MyOrder o){
		print("Fulfilling partial order of " + o.name + " (" + o.quantity + ")");
		//o.s = orderState.fulfilled;
		
		o.c.msgCanOnlyFullfillPartial(this, o.name, o.quantity);
		o.s = orderState.fulfilling;
		timer.schedule(new TimerTask() {
			@SuppressWarnings("unused")
			Object cookie = 1;
			public void run() {
				
				preppingOrder.release();
				stateChanged();
			}
		},
		5000);
		
		try {
			preppingOrder.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		o.s = orderState.fulfilled;	
	}
	
	private void canFulfill(MyOrder o){
		//o.c.msgHereIsFood(this, o.name, o.quantity);
		print("Fulfilling order of " + o.name + " (" + o.quantity + ")");
		//o.s = orderState.fulfilled;
		
		
		o.s = orderState.fulfilling;
		timer.schedule(new TimerTask() {
			@SuppressWarnings("unused")
			Object cookie = 1;
			public void run() {
				
				preppingOrder.release();
				stateChanged();
			}
		},
		5000);
		
		try {
			preppingOrder.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		o.s = orderState.fulfilled;
	}
	
	private void cannotFulfill(MyOrder o){
		print("Cannot fulfill order of " + o.name + " (" + o.quantity + ")");
		OutOfStock = true;
		o.s = orderState.done;
		o.c.msgOutOfFood(this, o.name);
	}
	
	private void sendOrder(MyOrder o){
		print("Shipping order of " + o.name + " (" + o.quantity + ")");
		o.s = orderState.done;
		cashier.msgHereIsBill(this, CalculateBill(o));
		o.c.msgHereIsFood(this, o.name, o.quantity);
	}
	
	
	//* helper functions *//
	private int CalculateBill(MyOrder o){
		for (MyInventory i : inventory){
			if (i.name == o.name)
				return o.quantity * i.price;
		}
		return 0;
	}
	public String getName() {
		return name;
	}
	public int getIndex() {
		return index;
	}
	
	class MyInventory{
		String name;
		int stock;
		int price;
		
		MyInventory(String name, int stock, int price){
			this.name = name;
			this.stock = stock;
			this.price = price;
		}
	}

	public class MyOrder{
		String name;
		orderState s;
		CookAgent c;
		int quantity;
		
		MyOrder(CookAgent c, String name, int q){
			this.name = name;
			this.s = orderState.ordered;
			this.c = c;
			this.quantity = q;
		}
	}
	
	enum orderState{done, ordered, fulfilled, fulfilling};
	

}
