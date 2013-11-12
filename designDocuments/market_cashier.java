// MARKET WORKER DATA
String name;
int index;
CashierAgent cashier;
Collection<MyOrder> orders;
Collection<MyInventory> inventory;
Collection<MarketCustomer> waitingCustomers;
double balance;
Timer timer = new Timer();

Market(){
	inventory.add(new MyInventory("Steak", 8.75));
	inventory.add(new MyInventory("Pizza", 5.40));
	inventory.add(new MyInventory("Salad", 3.20));
	inventory.add(new MyInventory("Chicken", 6.10));
	inventory.add(new MyInventory("Car", 16000.00));
}

class MyInventory{
	String name;
	int stock;
	int price;
}

public class MyOrder{
	Inventory item;
	orderState s;
	MarketCustomer p;
	int quantity;
	double cost;
	Boolean delivery;
	Building b; //only if delivery == true
}
enum orderState{done, ordered, fulfilled, fulfilling, waitingPayment, paid, shipping};
	
	
//  MARKET WORKER MESSAGES
public void IWantService(MarketCustomer c){
	waitingCustomers.add(c);
}

public void HereIsPayment(MarketCustomer p, int amount){
	order = orders.find(p, waitingPayment);

	if (cost == amount){
	order.s = paid;
	balance += amount;
	}
}

public void IWantToBuy(MarketCustomer p, String item, int quantity) {
	orders.add( new MyOrder(p, item, quantity) );
}

public void IWantDelivery(MarketCustomer p, String item, int quantity, Building b){
	orders.add( new MyOrder(p, item, quantity, true, b) );
}

public void DontWantAnymore(MarketCustomer p){
	order = orders.find(p, paying);
	orders.remove(order);
}

// MARKET WORKER SCHEDULER
if there exists an o in orders such that o.s == ordered{ 
	fulfillOrder(o);
	return true;
}
if there exists an o in orders such that o.s == fulfilled{
	askForPayment(o);
	return true;
}
if there exists an o in orders such that o.s == paid{
	sendOrder(o);
	return true;
}
if there exists an o in orders such that o.s == shipping{
	shipOrder(o);
	return true;
}
if there
return false;

	
// MARKET WORKER ACTIONS
private void fulfillOrder(MyOrder o){
	for i in inventory{
		if (i.name == o.name)
		{
			if (i.stock >= o.quantity){
				i.stock -= o.quantity;
				canFulfill(o);
			}
			else if (i.stock > 0){
				o.quantity = i.stock;
				partialFulfill(o);
				i.stock -= o.quantity;
			}
			else{
				cannotFulfill(o);
			}
		}
	}	
}

private void partialFulfill(MyOrder o){
	o.p.CanOnlyFullfillPartial(o.item.getName(), o.quantity);
	o.s = fulfilling;
	DoFulfillOrder(o.item, o.quantity);
	timer.schedule(new TimerTask() {
		public void run() {
			o.s = fulfilled;	
		}
	},
	5000);

}

private void canFulfill(MyOrder o){
	o.s = fulfilling;
	DoFulfillOrder(o.item, o.quantity);
	timer.schedule(new TimerTask() {
		public void run() {
			o.s = fulfilled;
		}
	},
	5000);
}

private void cannotFulfill(MyOrder o){
	o.s = done;
	o.p.OutOfItem(o.item.getName());
}

private void sendOrder(MyOrder o){
	if (o.delivery)
		o.s = shipping;
	else {
		DoGiveOrder(o);
		o.p.HereIsItem(this, o.item.getName(), o.quantity)
		o.s = done;
	}

}

private void askForPayment(MyOrder o){
	o.p.HereIsBill(this, CalculateBill(o));
	o.s = waitingPayment;
}

private void shipOrder(MyOrder o){
	DoShipOrder(o); //gui
	o.s = done;
	o.p.HereIsItem(this, o.item.getName(), o.quantity);
}
	
	
private int CalculateBill(MyOrder o){
	for i in inventory{
		if (i.name == o.name)
			return o.quantity * i.price;
	}
	return 0;
}
	

