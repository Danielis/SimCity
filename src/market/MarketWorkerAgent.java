package market;

import agent.Agent;
import agent.RestaurantMenu;
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
		if(market.Amount(o.item) >= o.quantity)
			GivePrice(o);
		else if(market.Amount(o.item) > 0){
			print("We only have enough for partial order...");
			o.quantity = market.Amount(o.item);
			GivePrice(o);
		}
		else{
			print("We are out of stock of " + o.item);
			o.s = orderState.done;
			o.c.OutOfStock();
		}
			
	}
	
	
	private void GivePrice(MyOrder o){
		o.price = market.calculatePrice(o.item, o.quantity);
		print("Your order costs $" + o.price);
		o.s = orderState.waiting;
		o.c.YouOwe(o.price);
	}


	private void TellHostFree(){
		print("I am ready for next customer.");
		host.IAmFree(this);
	}
	
	private void FetchItems(MyOrder o){
		print("Heading to stock room...");
		o.s = orderState.fulfilling;
		
		//gui stuff
		
		// when arrives at item:
		if (market.Amount(o.item) < o.quantity){
		print("Stock was lower than expected... grabbed all remaning items");
			o.quantity = market.Amount(o.item);
			o.price = market.calculatePrice(o.item, o.quantity);
		}
		market.RemoveInventory(o.item, o.quantity);
		o.s = orderState.waiting;
		print("We had " + o.quantity + " " + o.item + " in stock. You owe " + o.price);
		o.c.PleasePay(o.price);
	}
	
	private void GiveItems(MyOrder o){
		print(" here is ur order");
		o.s = orderState.done;
		o.c.HereIsOrder(o.item, o.quantity);
	}
	
	private void AskForBreak()
	{
		print("Asking host for a break");
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

