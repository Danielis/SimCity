package market;

import agent.Agent;
import agent.RestaurantMenu;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import market.gui.MarketAnimationPanel;
import market.gui.MarketTellerGui;
import market.interfaces.*;
import market.Market;

import java.util.*;
import java.util.concurrent.Semaphore;

//Waiter Agent
public class MarketWorkerAgent extends Agent implements MarketWorker {

	//Lists and Other Agents
	Market market;

	List <MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	List <MyOrder> myOrders = new ArrayList<MyOrder>();

	public MarketAnimationPanel copyOfAnimPanel;

	public MarketHostAgent host;
	public MarketTellerGui waiterGui;

	//Variables
	private int tableNum;
	private String name;
	public Boolean isOnBreak = false;
	public myState state = myState.none;

	//Semaphore
	public Semaphore animSemaphore = new Semaphore(0,true);

	public TrackerGui trackingWindow;

	//Constructors
	public MarketWorkerAgent()
	{
		super();
		this.name = "Default Daniel";
		print("initialized teller");

	}
	public MarketWorkerAgent(String name, int index) {
		super();
		this.name = name;
		this.tableNum = index;
	}


	//UTILITIES***************************************************

	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}
	public void setHost(MarketHostAgent host) {
		this.host = host;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}


	public void setGui(MarketTellerGui gui) {
		waiterGui = gui;
	}

	public MarketTellerGui getGui() {
		return waiterGui;
	}

	public void setAnimPanel(MarketAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
	//CLASSES/ENUMS**********************************************

	private class MyOrder{
		String item;
		orderState s;
		MarketCustomerRole c;
		int quantity;
		double price;
		Boolean delivery;
		// Building b; //only if delivery == true

		MyOrder(MarketCustomerRole c, String i, int q){
			item = i;
			this.c = c;
			quantity = q;
			s = orderState.created;
		}


	}

	enum orderState{done, created, ordered, readyFulfill, fulfilled, fulfilling, waiting, paid, shipping};


	private class MyCustomer{
		MarketCustomerRole c;
	}


	public enum myState
	{
		none, free, wantBreak, askedForBreak, onBreak
	}

	//MESSAGES****************************************************

	public void GiveOrder(MarketCustomerRole c, String item, int q){
		myOrders.add(new MyOrder(c, item, q));
		stateChanged();
	}

	public void GivePayment(MarketCustomerRole c, double amount){
		for (MyOrder o : myOrders){
			if (o.c == c && o.s != orderState.done){
				market.balance += amount;
				o.s = orderState.paid;
				stateChanged();
			}
		}
	}

	public void PleaseFulfill(MarketCustomerRole c){
		for (MyOrder o : myOrders){
			if (o.c == c && o.s != orderState.done){
				o.s = orderState.readyFulfill;
				stateChanged();
			}
		}
	}

	public void IAmLeaving(){
		state = myState.free;
		stateChanged();
	}

	public void msgSetOffBreak()
	{/*
		print(this.name + " is now available.");
		state = myState.none;
		waiterGui.setBreak(false);*/
		isOnBreak = false;
		stateChanged();
	}

	public void msgSetOnBreak()
	{
		/*
		print(this.name + " wants a break.");
		state = myState.wantBreak;
		stateChanged();*/
		isOnBreak = true;
	}

	public void msgBreakGranted(Boolean permission)
	{
		if (permission == false)
		{
			//state = myState.none;
			isOnBreak = false;
			//waiterGui.setBreak(false);
		}
		else if (permission == true)
		{
			//state = myState.onBreak;
			isOnBreak = true;
			//waiterGui.setBreak(true);
		}
	}




	//SCHEDULER****************************************************

	protected boolean pickAndExecuteAnAction() 
	{		
		try
		{
			for (MyOrder o : myOrders){
				if (o.s == orderState.created){
					CheckInventory(o);
					return true;
				}
				if (o.s == orderState.readyFulfill){
					FetchItems(o);
					return true;
				}
				if (o.s == orderState.paid){
					GiveItems(o);
					return true;
				}
			}

			if (state == myState.free){
				TellHostFree();
			}

			if (state == myState.wantBreak)
			{
				AskForBreak();
				return true;
			}
			waiterGui.DoGoToHomePosition();
			return false;
		}
		catch(ConcurrentModificationException e)
		{
			waiterGui.DoGoToHomePosition();
			return false;
		}
	}

	//ACTIONS********************************************************

	private void CheckInventory(MyOrder o){
		if (market.DoesStock(o.item)){
			if(market.Amount(o.item) >= o.quantity)
				GivePrice(o);
			else if(market.Amount(o.item) > 0){
				print("We only have enough for partial order...");
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "We only have enough for a partial order...", new Date()));
				o.quantity = market.Amount(o.item);
				GivePrice(o);
			}
			else{
				print("We are out of stock of " + o.item);
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "We are out of stock of " + o.item, new Date()));
				o.s = orderState.done;
				o.c.OutOfStock();
			}
		}
		else{
			print("We do not stock " + o.item);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "We do not stock " + o.item, new Date()));
			o.s = orderState.done;
			o.c.OutOfStock();
		}
	}



	private void GivePrice(MyOrder o){
		o.price = market.calculatePrice(o.item, o.quantity);
		print("Your order costs $" + o.price);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "Your order costs " + o.price, new Date()));
		o.s = orderState.waiting;
		o.c.YouOwe(o.price);
	}


	private void TellHostFree(){
		print("I am ready for next customer.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "I am ready for the next customer.", new Date()));
		host.IAmFree(this);
	}

	private void FetchItems(MyOrder o){
		print("Heading to stock room...");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "Heading to the stockroom...", new Date()));
		o.s = orderState.fulfilling;

		//gui stuff

		// when arrives at item:
		if (market.Amount(o.item) < o.quantity){
			print("Stock was lower than expected... grabbed all remaning items");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "Stock is lower than expected...grabbed all remaining items", new Date()));
			o.quantity = market.Amount(o.item);
			o.price = market.calculatePrice(o.item, o.quantity);
		}
		market.RemoveInventory(o.item, o.quantity);
		o.s = orderState.waiting;
		print("We had " + o.quantity + " " + o.item + " in stock. You owe " + o.price);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "We had " + o.quantity + " " + o.item + " in stock. You owe " + o.price, new Date()));
		o.c.PleasePay(o.price);
	}

	private void GiveItems(MyOrder o){
		print("Here is your order: " + o.quantity + " of " + o.item);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "Here is your order: " + o.quantity + " of " + o.item, new Date()));
		o.s = orderState.done;
		if (o.item == "Steak")
			waiterGui.setSpeechBubble("heressteak");
		if (o.item == "Chicken")
			waiterGui.setSpeechBubble("hereschicken");
		if (o.item == "Pizza")
			waiterGui.setSpeechBubble("herespizza");
		if (o.item == "Salad")
			waiterGui.setSpeechBubble("heressalad");
		if (o.item == "Car")
			waiterGui.setSpeechBubble("herescar");
		if (o.item == "Juice")
			waiterGui.setSpeechBubble("heresjuice");
		if (o.item == "Eggs")
			waiterGui.setSpeechBubble("hereseggs");
		if (o.item == "Milk")
			waiterGui.setSpeechBubble("heresmilk");
		
		o.c.HereIsOrder(o.item, o.quantity);
		
	}

	private void AskForBreak()
	{
		print("Asking host for a break");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketWorkerAgent.", "Asking host for a break.", new Date()));
		state = myState.askedForBreak;
		host.msgIdLikeToGoOnBreak(this);
	}

	public void WaitForAnimation()
	{
		try
		{
			animSemaphore.acquire();		
		} catch (InterruptedException e) {
			// no action - expected when stopping or when deadline changed
		} catch (Exception e) {
			print("Unexpected exception caught in Agent thread:", e);
		}
	}

	public void DoneWithAnimation()
	{
		animSemaphore.release();
	}
	public int getTableNum() {
		return tableNum;
	}
	public void setMarket(Market b) {
		this.market = b;
	}

}

