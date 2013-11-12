SimCity 201 Design – Restaurant – Team 5
 
Normative Scenario Interaction Diagram
 
Value	Name					Parameters
1		msg	CheckForASpot		(customer);
2		msg	RestaurantIsFull	(Boolean);
3		msg	IWantFood			(customer);
4		msg	SitAtTable		(customer, table);
5		msg	FollowMe			(waiter, table, menu);
6		msg	ReadyToOrder		(customer)
7		msg	WhatWouldYouLike	();
8		msg	HereIsMyOrder		(customer, choice);
9		msg	HereIsAnOrder		(waiter, choice, table);
10		msg	OrderIsReady		(choice, table)
11A		msg	HereIsYourFood		(choice);
11B		msg	HereIsACheck		(waiter, customer, choice);
12A		msg	CheckIsComputed	(customer, choice, bill);
12B		msg	DoneEating		(customer);
13		msg	HereIsYourCheck	(bill);
14A		msg	PayingAndLeaving	(customer);
14B		msg	HereIsMyPayment	(customer, money);
15		msg	TableIsFree		(waiter, table);
Cook/Market Interaction Diagram
 
Value	Name					Parameters
1		msg	IWantToOrder		(choice, count);
2		msg	HereIsYourOrder	(market, count, outOfFood);
 
 
Waiter Out Of Food Interaction Diagram
 
 
 
Value	Name					Parameters
1		msg	OutOfFood			(List<Boolean> foods);
2		msg	SorryOutOfFood		(List<Boolean> foods);
3		msg	msgHereIsMyOrder	(customer, choice);
4		msg	HereIsAnOrder		(waiter, choice, table);
 
Going On Break Interaction Diagram
 
Value	Name					Parameters
1		msg	IdLikeToGoOnBreak	(waiter);
2		msg	BreakGranted		(Boolean);
 
Customer Data
static final float initialMoney = 20;
String name;				//Name
int hungerLevel = 7;        		//Length of Meal
int choosingTime = 6;			//Time it takes to choose
Timer timer;				//Timer Class
int mySeat;				//Seat Number, for mapping
CustomerGui customerGui;			//GUI Class
HostAgent host;				//Host
CashierAgent cashier;			/Cashier
WaiterAgent waiter;			//Waiter
myState state = none;			//State
float myMoney = initialMoney;		//Money
float bill;				//Price to pay
Random random;				//Random Utility
String choice;				//Choice
RestaurantMenu menu;			/Copy of the menu
Boolean imusthavethisitem = false;	//To bypass re-selection
Boolean reordering = false;		//To check if he's reordering
 
private List<Boolean> foodOptions = new ArrayList<Boolean>();
 
public CustomerAgent(String name)
{
	for (int i = 0; i<4; i++)
	{
		foodOptions.add(true);
	}
}
 
public void setHost(HostAgent host) {
	this.host = host;
}
public void setWaiter(WaiterAgent waiter){
	this.waiter = waiter;
}
public void setCashier(CashierAgent cashier){
	this.cashier = cashier;
}
 
public enum myState
{
	none, hungry, waiting, beingSeated, 
	seated, readyToOrder, ordering, finishedOrdering,
	readyToEat, eating, readyToPay, paid, finished,
	waitingForBill, waitingForASpot, gotASpot, IllJustLeave,
} 
Customer Messages
public void msgGotHungry() {
imusthavethisitem = false;
	reordering = false;
	state = myState.hungry;
}
public void msgRestaurantIsFull(Boolean b){
int a = random.nextInt()%2;
if (a<0) a=a*(-1);
if (b == true)
{
	if (a==0){
		state = myState.IllJustLeave;
	}
	else if (a==1){
		state = myState.gotASpot;
	}
}
else if (b == false){
	state = myState.gotASpot;
}
}
public void msgFollowMe(WaiterAgent w, int tableNum, RestaurantMenu m) {
	waiter = w;
	mySeat = tableNum;
	menu = m;
	state = myState.beingSeated;
}
public void msgWhatWouldYouLike(){
	state = myState.ordering;
}
public void msgSorryOutOfFood(List<Boolean> temp){
	reordering = true;
	state = myState.seated;
	foodOptions = temp;
}
public void msgHereIsYourFood(String choice){
	state = myState.readyToEat;
}
public void msgHereIsYourCheck(float check){
	state = myState.readyToPay;
	bill = check;
} 
Customer Scheduler
 
 
if (state == myState.readyToPay){
	PayForMeal();
}
if (state == myState.readyToEat){
	EatFood();
}
if (state == myState.ordering){
	TellWaiterOrder();
}
if (state == myState.seated){
	PickAnOrder();
}
if (state == myState.beingSeated){
	FollowWaiter();
}
if (state == myState.gotASpot){
	DecideToGo();
}
	if (state == myState.hungry){
	GoToRestaurant();
}
if (state == myState.IllJustLeave){
	LeaveBecauseFull();
}
 
Customer Actions
private void GoToRestaurant() {
	host.msgCheckForASpot(this);
	state = myState.waitingForASpot;	
}	
private void DecideToGo(){
	host.msgIWantFood(this);
	state = myState.waiting;
}
private void FollowWaiter() {
	customerGui.DoGoToSeat(mySeat);
	state = myState.seated;
}	
private void TellWaiterOrder(){
	print("Telling waiter I'm ready to order");
	waiter.msgHereIsMyOrder(this,  choice);
	state = myState.finishedOrdering;
}
private void PickAnOrder(){
	if (outOfFood)
			Leave();
	else{
		int temp = random.nextInt() %4;
		int aram = random.nextInt()%2
		if (myMoney < 5.99 && reordering == false){
			if (aram == 0){
				Leave();
				return;
			}
		}
if (aram == 1 && !imusthavethisitem && myMoney < 15.99 && !reordering)
	{}
	else if (reordering == false){
		if (myMoney < 8.99 && myMoney > 5.99){
			imusthavethisitem = true;
			choice = "Salad";
		}
		else if (myMoney < 10.99 && myMoney > 8.99){
			imusthavethisitem = true;
			int a = random.nextInt()%2;
			if (a==0) choice = "Salad";
			else if (a==1) choice = "Pizza";
		}
		else if (myMoney < 15.99 && myMoney > 10.99){
			imusthavethisitem = true;
			int a = random.nextInt()%3;
			if (a==0) choice = "Salad";
			else if (a==1) choice = "Pizza";
			else if (a==2) choice = "Chicken";
}
	}
	if(imusthavethisitem == true){
		int index;
		//Map Index To Item
		if(foodOptions.get(index) == false){
			Leave();
		}
		else{
			choice = menu.menu.get(index);
			state = myState.readyToOrder;
			timer.schedule(new TimerTask(){
				public void run(){				
					CallWaiter();
				}
			}, choosingTime * 1000);
		}
	}
	else
	{
		Boolean flag;	
		do{
			flag = false;
			if (this.foodOptions.get(temp) == false){
				temp = random.nextInt() %4; //Reorder
				flag = true;		   //Kick out
			}
	} while(flag == true);
	choice = menu.menu.get(temp);
	state = myState.readyToOrder;
timer.schedule( new TimerTask(){
			public void run(){				
				CallWaiter();
			}
		}, choosingTime * 1000);
	}
}
 
private void CallWaiter(){
	waiter.msgReadyToOrder(this);
}
private void EatFood() {
	state = myState.eating;
	timer.schedule(new TimerTask() {
		public void run() {
			DoneEating();
		}
	},getHungerLevel() * 1000);
}
private void DoneEating() {
	state = myState.waitingForBill;
	waiter.msgDoneEating(this);
}
private void PayForMeal(){
	state = myState.paid;
	if (bill > myMoney){
		cashier.msgHereIsMyPayment(this, myMoney);
	}
	else{
		myMoney -= bill;
		cashier.msgHereIsMyPayment(this, bill);
	}
	Leave();
}
private void Leave(){
	waiter.msgPayingAndLeaving(this);
	state = myState.finished;
}
	
private void LeaveBecauseFull(){
	tate = myState.finished;
}
 
Host Data
 
//Global
public static final int NTABLES;
	
//Lists
public List<MyCustomer> customers
public List<MyWaiter> waiters
public Collection<Table> tables
 
public List<CustomerAgent> waitingCustomers
 
private String name;
public HostGui hostGui
public HostAgent(String name){
	this.name = name;
	//Make the tables
	tables = new ArrayList<Table>(NTABLES);
	for (int ix = 1; ix <= NTABLES; ix++) {
		tables.add(new Table(ix, false));
	}
}
public enum waiterState{
	none, wantsBreak, onbreak
};
private class Table {
	Boolean isOccupied;
	int tableNumber;
}
private class MyCustomer{
CustomerAgent c;
	Boolean assigned;
}
private class MyWaiter{
	WaiterAgent w;
	int numCustomersServing;
	Boolean isOnBreak = false;
	waiterState state;
}
 
 
Host Messages
public void msgNewWaiter(WaiterAgent w){
	waiters.add(new MyWaiter(w, 0, false));	
}
public void msgCheckForASpot(CustomerAgent cust){
	waitingCustomers.add(cust);
}
public void msgIWantFood(CustomerAgent cust){
	customers.add(new MyCustomer(cust, false));
}
 
public void msgTableIsFree(WaiterAgent w, int tableNum) {		
	for (Table table : tables) {
		if (table.getTableNumber() == tableNum) {
			table.setUnoccupied();
			w.numCustomersServing--;
		}
	}
}
public void msgIdLikeToGoOnBreak(WaiterAgent w){
	for (MyWaiter mw : waiters){
		if (mw.w == w){
			mw.state = waiterState.wantsBreak;
		}
	}
}
public void msgIdLikeToGetOffBreak(WaiterAgent w){
	for (MyWaiter mw : waiters){
		if (mw.w == w){
			mw.isOnBreak = false;
			mw.state = waiterState.none;
		}
	}
} 
Host Scheduler
 
if ∃ CustomerAgent c in waitingCustomers && tables != full
	TellCustomerWhetherFull(c, true)
	return true;
Else
	TellCustomerWhetherFull(c,false)
	return true;
 
if ∃ MyCustomer mc in customers such that mc.assigned == false 
	if ∃ t in tables such that t.occupied == false
		if waiters.size() > 0
			if ∃ MyWaiter mw in waiters such that mw.w.OnBreak = false
				AssignCustomer(mc,t);
 
if ∃ MyWaiter mw in waiters such that mw.state = wantsBreak
	if waiters.size > 1
		if E MyWaiter other in waiters such that mw.state = none
			if mw.isOnBreak = false
				GreantBreak(mw,true);
 
 
Host Actions
private void TellCustomerWhetherFull(CustomerAgent c, Boolean b){
	for (CustomerAgent ca : waitingCustomers){
		if(ca == c){
			waitingCustomers.remove(ca);
			c.msgRestaurantIsFull(b);
		}
	}
}
private void DeLoad(WaiterAgent w){
	for (MyWaiter mw : waiters){
		if (mw.w == w){
			mw.numCustomersServing--;
		}
	}
}
private void AssignCustomer(MyCustomer mc, Table t) {
	MyWaiter w = PickWaiter();
	t.setOccupied();
	mc.assigned = true;
	w.numCustomersServing++;
	w.w.msgSitAtTable(mc.c, t.tableNumber);
	customers.remove(mc);
}
private void GrantBreak(MyWaiter mw, Boolean b){
	if (b == true){
		mw.state = waiterState.onbreak;
		mw.isOnBreak = true;
		mw.w.msgBreakGranted(true);
	}
	else if (b == false){
		mw.state = waiterState.none;
		mw.isOnBreak = false;
		mw.w.msgBreakGranted(false);
	}
}
public MyWaiter PickWaiter()
{
int least = 0;
	int index = 0;
	for (int i = 0; i< waiters.size(); i++){
		if (waiters.get(i).isOnBreak == false){
			least = waiters.get(i).getNumCustomersServing();
		}
	}
	for (int i = 0; i< waiters.size(); i++){
	if (waiters.get(i).isOnBreak == false)
	{
		if (waiters.get(i).getNumCustomersServing() <= least){
			index = i;
			least = waiters.get(i).getNumCustomersServing();	
		}
	}
	return waiters.get(index);
}
}
 
Waiter Base Class Data
public HostAgent host;
public CookAgent cook;
public CashierAgent cashier;
public WaiterGui waiterGui;
 
public List<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
public List<Boolean> foodsAvailable = new ArrayList<Boolean>();
 
private String name;
public Boolean isOnBreak = false;
public myState state = myState.none;
RestaurantMenu Menu;
 
public WaiterAgent(){
	this.name = "Default Daniel";
	for (int i = 0; i<4; i++){
		foodsAvailable.add(true);
	}
}
public void recalculateInventory(List<Boolean> temp){
	foodsAvailable.clear();
	for (int i = 0; i<temp.size(); i++){
		foodsAvailable.add(temp.get(i));
	}
}
public void setHost(HostAgent host) {
	this.host = host;
}
public void setCook(CookAgent cook) {
	this.cook = cook;
}
public void setCashier(CashierAgent cashier){
	this.cashier = cashier;
}
public class MyCustomer{
	public CustomerAgent c;
	int table;
	public CustomerState s;
	public String choice;
	float price;
}
public enum CustomerState{
	waiting, seated, readyToOrder, hasOrdered,
	ordering, reordering, reordered, finishedOrdering, 
	orderReady, eating, leaving, hasLeft, needsCheck,
	checkComputed
}
public enum myState{
	none, wantBreak, askedForBreak, onBreak
}
 
Waiter Base Class Messages
public void msgBreakGranted(Boolean permission)
{
	if (permission == false){
		isOnBreak = false;
	}
	else if (permission == true){
		isOnBreak = true;
	}
}
public void msgSitAtTable(CustomerAgent c, int table){
	myCustomers.add(new MyCustomer(c, table, CustomerState.waiting, null));
}
public void msgReadyToOrder(CustomerAgent c) {
	for (MyCustomer mc: myCustomers){
		if (mc.c == c) {
			mc.s = CustomerState.readyToOrder;
		}
	}
}
public void msgHereIsMyOrder(CustomerAgent c, String choice){
	for (MyCustomer mc: myCustomers) {
		if (mc.c == c) {
			mc.s = CustomerState.finishedOrdering;
			mc.choice = choice;
		}
	}
}
public void msgOutOfFood(List<Boolean> foods, String choice, int table){
	for (MyCustomer mc: myCustomers) {
		if (mc.table == table) {
			recalculateInventory(foods);
			mc.s = CustomerState.reordering;
		}
	}
}
public void msgOrderIsReady(String choice, int table){
	for (MyCustomer mc: myCustomers) {
		if (mc.table == table) {
			mc.s = CustomerState.orderReady;
		}
	}
}
public void msgCheckIsComputed(CustomerAgent c, String choice, float owed){
	for (MyCustomer mc: myCustomers) {
		if (mc.c == c) {
			mc.s = CustomerState.checkComputed;
			mc.price = owed;
		}
	}
}
public void msgDoneEating(CustomerAgent c){
	for (MyCustomer mc: myCustomers) {
		if (mc.c == c) {
			mc.s = CustomerState.needsCheck;
		}
	}
}
 
public void msgPayingAndLeaving(CustomerAgent c)
{
	for (MyCustomer mc: myCustomers) {
		if (mc.c == c) {
			mc.s = CustomerState.leaving;
		}
	}
}
 
Waiter Base Class Scheduler
if ∃ MyCustomer mc in myCustomers such that state = leaving
	CleanAndNotifyHost(mc);
 
if ∃ MyCustomer mc in myCustomers such that state = needsCheck
	PickUpAndGiveCheck(mc);
 
if ∃ MyCustomer mc in myCustomers such that state = orderReady
	GiveOrderToCustomerAndCheckToCashier(mc);
 
if ∃ MyCustomer mc in myCustomers such that state = reOrdering
	AskAgain(mc);
 
if ∃ MyCustomer mc in myCustomers such that state = finishedOrdering
	PlaceOrder(mc);
 
if ∃ MyCustomer mc in myCustomers such that state = readyToOrder
	AskOrder(mc);
 
if ∃ MyCustomer mc in myCustomers such that state = waiting
	SeatCustomer(mc);
 
If state = wantsBreak
	AskForBreak();
 
Waiter Base Class Actions
private void AskForBreak(){
	state = myState.askedForBreak;
	host.msgIdLikeToGoOnBreak(this);
}
private void SeatCustomer(MyCustomer mc) {
	waiterGui.DoShowTable(mc.table, mc);
	mc.s = CustomerState.seated;
}
private void AskOrder(MyCustomer mc){
	mc.s = CustomerState.ordering;
	mc.c.msgWhatWouldYouLike();
}
private void AskAgain(MyCustomer mc){
	mc.s = CustomerState.reordered;
	mc.c.msgSorryOutOfFood(foodsAvailable);
}
private abstract void PlaceOrder(MyCustomer mc);
 
private void GiveOrderToCustomerAndCheckToCashier(MyCustomer mc)
{	mc.s = CustomerState.eating;
	mc.c.msgHereIsYourFood(mc.choice);
	cashier.msgHereIsACheck(this, mc.c, mc.choice);
}
private void PickUpAndGiveCheck(MyCustomer mc){
	mc.s = CustomerState.leaving;
	mc.c.msgHereIsYourCheck(mc.price);
	CleanAndNotifyHost(mc);
}
private void CleanAndNotifyHost(MyCustomer mc){
	mc.s = CustomerState.hasLeft;
	host.msgTableIsFree(this, mc.table);
	myCustomers.remove(mc);
 
}
 
Waiter: Direct Interaction with Cook.  Extends Waiter Base Class
 
//Actions:
 
//Implemented action for PlaceOrder
private void PlaceOrder(MyCustomer mc) {
	mc.s = CustomerState.hasOrdered;		
	cook.msgHereIsAnOrder(this, mc.choice, mc.table);
}
 
ProducerConsumerMonitor:
    
//should we have a class definition?
Class ProducerConsumerMonitor {
 
    private final int N = 5;
    private int count = 0;
    private Vector theData;
 
//order class
public class Order{
	WaiterAgent w;
	String choice;
	int table;
	state s;
}
    
    synchronized public void insert(Order data) {
        while (count == N) {
            try{ 
                System.out.println("\tFull, waiting");
                wait(5000);                         // Full, wait to add
            } catch (InterruptedException ex) {};
        }
            
        insert_item(data);
        count++;
        if(count == 1) {
            System.out.println("\tNot Empty, notify");
            notify();                               // Not empty, notify a 
                                                    // waiting consumer
        }
    }
    
    synchronized public Order remove() {
        Order data;
        while(count == 0)
            try{ 
                System.out.println("\tEmpty, waiting");
                wait(5000);                         // Empty, wait to consume
            } catch (InterruptedException ex) {};
 
        data = remove_item();
        count--;
        if(count == N-1){ 
            System.out.println("\tNot full, notify");
            notify();                               // Not full, notify a 
                                                    // waiting producer
        }
        return data;
    }
    
    private void insert_item(Item data){
        theData.addElement(data);
    }
    
    private Order remove_item(){
        Order data = (Item) theData.firstElement();
        theData.removeElementAt(0);
        return data;
    }
    
    public ProducerConsumerMonitor(){
        theData = new Vector();
    }
 
}
 
 
Waiter: Shared Data with Cook. Extends Waiter Base Class
 
//Data:
 
Declaration of ProducerConsumerMonitor given to us, using Order class as Item;
 
private ProducerConsumerMonitor theMonitor;  //there will be one of these, which we will set manually to exist in both cook and waiter
 
 
//Actions:
 
//Implemented action for PlaceOrder
private void PlaceOrder(MyCustomer mc) {
	mc.s = CustomerState.hasOrdered;	
	theMonitor.insert(new theMonitor.Order(this, mc.choice, mc.table, state.needsProcessing));
}
 
 
Cook Data
static final int numItemsInInventory = 5;
static final int numItemsToOrder = 3;
static final int numIntemsForThreshhold = 2;
 
List<theMonitor.Order> orders = new ArrayList<theMonitor.Order>();
List<Foods> inventory = new ArrayList<Foods>();
List<MyMarket> markets = new ArrayList<MyMarket>();
private Map<String, Foods> foods = new HashMap<String, Foods>();
 
private ProducerConsumerMonitor theMonitor;  //this exists in both waiter and cook
 
private String name;
Timer timer;
private int time;
 
public CookAgent(String name) {
	foods.put("Steak",  new Foods(5,0));
	foods.put("Chicken", new Foods(6,1));
	foods.put("Salad", new Foods(3,2));
	foods.put("Pizza", new Foods(8,3));
	
	inventory.add(new Foods("Steak", numItemsInInventory, 0));
	inventory.add(new Foods("Chicken", numItemsInInventory, 1));
	inventory.add(new Foods("Salad", numItemsInInventory, 2));
	inventory.add(new Foods("Pizza", numItemsInInventory, 3));
	
	for (int i = 0; i < inventory.size(); i++){
		if ((inventory.get(i).amount <= numIntemsForThreshhold && 
				inventory.get(i).state == OrderState.none) ||
			(inventory.get(i).amount <= numIntemsForThreshhold && 
				inventory.get(i).state == OrderState.ordered)){
			inventory.get(i).state = OrderState.needsToOrder;
		}
	}
}
public void setMarkets(MarketAgent m){
	markets.add(new MyMarket(m));
}
private class Foods{
	public String name;
	public int amount;
	public int cookingTime;
	int index;
	public OrderState state;
}
private class MyMarket{
	public MarketAgent m;
	public List<Boolean> outOfItems = new ArrayList<Boolean>();
}
enum state{
	needsProcessing, pending, cooking, done, complete
};
 
enum OrderState{
	none, needsToOrder, ordering, ordered
};
 
Cook Messages
public void msgHereIsAnOrder(WaiterAgent w, String choice, int table){
	orders.add(new theMonitor.Order(w, choice, table, state.needsProcessing));
}
 
public void msgHereIsYourOrder(MarketAgent m, String name, int count, Boolean out){
	for (MyMarket mm : markets){
		if (mm.m == m){
			if (out == true){
				mm.setOutOfItem(foods.get(name).index);
			}
			inventory.get(foods.get(name).index).amount += count;
			inventory.get(foods.get(name).index).state = OrderState.ordered;
		}
	}
	for (int i = 0; i < inventory.size(); i++){
		if ((inventory.get(i).amount <= numIntemsForThreshhold && 
				inventory.get(i).state == OrderState.none) ||
			(inventory.get(i).amount <= numIntemsForThreshhold && 
				inventory.get(i).state == OrderState.ordered)){
			inventory.get(i).state = OrderState.needsToOrder;
		}
	}
}
 
Cook Scheduler
 
if ∃ MyMarket mm in markets such that mm.outOfItems == false
	Find index;
	OrderFromMarket(mm, inventory.get(index).name, quantity);
 
if ∃ theMonitor.Order o in orders such that o.state = needsProcessing
	ProcessOrder(o);
 
if ∃ theMonitor.Order o in orders such that o.state = pending
	CookOrder(o);
 
if ∃ theMonitor.Order o in orders such that o.state = done
	PlateOrder(o);
 
if ∃ Order in theMonitor
	orders.add(theMonitor.remove());
 
Cook Actions
private void ProcessOrder(theMonitor.Order o){
	List<Boolean> foods = new ArrayList<Boolean>();
	for (Foods f : inventory){
		if (f.getAmount() == 0){
			foods.add(false);
		}
		else{
			foods.add(true);
		}
	}
	for (Foods f : inventory){
		if (f.getName() == o.choice){
			if (f.amount == 0){
				o.w.msgOutOfFood(foods, o.choice, o.table);
				orders.remove(o);
			}
			else{
				o.s = state.pending;
				CookOrder(o);
			}
		}
	}
	
	for (int i = 0; i < inventory.size(); i++)
	{
		if ((inventory.get(i).amount <= numIntemsForThreshhold && 
				inventory.get(i).state == OrderState.none) ||
			(inventory.get(i).amount <= numIntemsForThreshhold && 
				inventory.get(i).state == OrderState.ordered)){
			inventory.get(i).state = OrderState.needsToOrder;
		}
	}
}
private void CookOrder(theMonitor.Order o){
	o.s = state.cooking;
	for (Foods f : inventory){
		if (f.getName() == o.choice){
			f.decrement();
		}
	}
	time = foods.get(o.choice).getCookingTime();
	final Order temp = o;
	timer.schedule(new TimerTask(){
		public void run(){
			temp.s = state.done;
		}
	}, time * 1000);
}
private void PlateOrder(theMonitor.Order o){
	o.w.msgOrderIsReady(o.choice,  o.table);
	orders.remove(o);
}
private void OrderFromMarket(int index, MyMarket mm, String name, int numToOrder){
	inventory.get(index).state = OrderState.ordering;
	mm.m.msgIWantToOrder(inventory.get(index).name, numItemsToOrder);
}
 
Cashier Data
List<MyCheck> checks = new ArrayList<MyCheck>();
 
private String name = "Squidward";
private float balance;
private static float threshholdToDeposit = 200.0f;
private static float amounttoDeposit = 100.0f;
private MyBank bank;
 
private Map<String, Float> prices = new HashMap<String, Float>();
private List<MyCheck> checks = new ArrayList<MyCheck>();
private List<MyPayment> payments = new ArrayList<MyPayment>();
private List<Float> owes = new ArrayList<Float>();
 
//need setBankteller Utility for different banktellers
 
public CashierAgent(String name) {
	prices.put("Steak",  15.99f);
	prices.put("Chicken", 10.99f);
	prices.put("Salad", 5.99f);
	prices.put("Pizza", 8.99f);
}
 
public class MyBank //need initializer, or some way to get the acc number
{
	public int bankAccountNumber;
	public Bankteller bankteller;
	public float myBankBalance;
}
 
 
public class MyPayment
{
	public Market m;
	public String order;
	public int amount;
	public float payment;
	public PaymentState state;
}
 
public class MyCheck{
	public WaiterAgent w;
	public CustomerAgent c;
	public String choice;
	public float price;
	public float owedAmount;
	public CheckState state;
}
enum CheckState{ none, needsComputing, computing, computed, justPaid, paid };
enum PaymentState{ none, needsPayment, paid }; 
Cashier Messages
 
public void msgHereIsACheck(WaiterAgent newW, CustomerAgent newC, String newChoice){
	Boolean exists = false;
	for (MyCheck mc : checks){
		if (mc.c == newC){
			mc.w = newW;
			mc.choice = newChoice;
			mc.price = prices.get(newChoice);
			mc.state = CheckState.needsComputing;
			exists = true;
		}
	}
	if (exists == false){
		checks.add(new MyCheck(newW, newC, newChoice, CheckState.needsComputing));
	}
}
 
 
public void msgHereAMarketOrder(Market m, String type, int amount)
{
	payments.add(new MyPayment(m, type, amount, PaymentState.needsPayment));
}
 
public void msgHereIsMyPayment(CustomerAgent newC, float payment){
	for (MyCheck mc : checks){
		if (mc.c == newC){
			mc.owedAmount -= payment;
			balance += payment;
			mc.state = CheckState.justPaid;
		}
	}
}
 
public void msgDeposted() {
	//stub
} 
Cashier Scheduler
 
if ∃ MyCheck mc in checks such that state = needsComputing
	ComputeCheck(mc);
 
if ∃ MyCheck mc in checks such that state = justPaid
	CheckIfFullyPaid(mc);
 
if ∃ MyPayment mp in payments such that state = needsPayment
	PayMarket(mp);
 
If (balance > threshholdToDeposit)
	DepositToBank();
 
Cashier Actions
private void ComputeCheck(MyCheck mc){
	mc.state = CheckState.computing;
	mc.owedAmount += mc.price;
	mc.w.msgCheckIsComputed(mc.c, mc.choice, mc.owedAmount);
}
private void CheckIfFullyPaid(MyCheck mc){
	mc.state = CheckState.paid;
	if (mc.owedAmount == 0){
		checks.remove(mc);
	}
	else if (mc.owedAmount > 0){
		print("Customer still owes $" + mc.owedAmount);
	}
}
	
private void PayMarket(MyPayment mp)
{
	mp.state = PaymentState.paid;
	//Calculate how much cashier owes
	mp.payment += (float)mp.amount * (float)prices.get(mp.order);
	//add on previous amounts
	if (mp.m.getName() == "Market 1")
		mp.payment += owes.get(0);
	else if (mp.m.getName() == "Market 2")
		mp.payment += owes.get(1);
	else if (mp.m.getName() == "Market 3")
		mp.payment += owes.get(2);	
	//If we have enough
	if (account > mp.payment){
		mp.m.msgHereIsAPayment(mp.payment);
		account -= mp.payment;
		account = this.RoundToTwoDigits(account);	
		if (mp.m.getName() == "Market 1")
			owes.set(0, 0);
		else if (mp.m.getName() == "Market 2")
			owes.set(1, 0);
		else if (mp.m.getName() == "Market 3")
			owes.set(2, 0);
 
		payments.remove(mp);
	}
	
	//If we don't have enough
	else if (account < mp.payment){		
		mp.payment = mp.payment - account;
		print("I owe " + mp.m.getName() + " $" + mp.payment);
		mp.m.msgHereIsAPayment(account);
		account = 0;
		if (mp.m.getName() == "Market 1")
			owes.set(0, mp.payment);
		else if (mp.m.getName() == "Market 2")
			owes.set(1, mp.payment);
		else if (mp.m.getName() == "Market 3")
			owes.set(2, mp.payment);
		payments.remove(mp);
	}
}
 
private void DepositToBank(){
	bank.bankteller.msgIWantToDeposit(bank.bankAccountNumber, this, amountToDeposit);
}