package restaurantA;

import java.awt.Frame;
import java.util.*;

import javax.swing.JFrame;








//import restaurantA.ProducerConsumerMonitor;
import restaurantA.gui.RestaurantGui;
import restaurantA.gui.RestaurantPanel;
import roles.*;
import roles.Building.*;
import city.*;

public class RestaurantA extends RestBase{
	public RestaurantGui gui;
	public RestaurantPanel panel;
	public String name;
    public Coordinate location;
    public ProducerConsumerMonitor theMonitor;
    public int numWaitersWorking = 0;
	public Boolean hasHost = false;
    public Boolean hasCook = false;
    public Boolean hasCashier = false;
    public int numWaiters = 0;

	public List<MyMenuItem> menu = Collections.synchronizedList(new ArrayList<MyMenuItem>());
	public HostAgent workingHost = null;
	public CashierAgent workingCashier = null;
	public CookAgent workingCook = null;
	public List<WaiterAgent> workingWaiters = new ArrayList<WaiterAgent>();
	
	
	public RestaurantA(restaurantA.gui.RestaurantGui rg, String name)
    {
    	this.gui = rg;
    	this.panel = rg.restPanel;
    	this.name = name;
    	type = buildingType.restaurant;
        rg.setTitle(name);
        rg.setVisible(true);
        rg.setState(Frame.NORMAL);
        rg.setResizable(false);
        rg.setAlwaysOnTop(true);
        rg.setAlwaysOnTop(false);
        rg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        owner = "Aleena";
        theMonitor = new ProducerConsumerMonitor();
        
    	int Inv = 40;
		int Cap = 5;
		int Thr = 3;
        menu.add( new MyMenuItem("Steak", 5, Inv, Thr, Cap, 16) );
		menu.add( new MyMenuItem("Chicken", 5, Inv, Thr, Cap, 11) );
		menu.add( new MyMenuItem("Pizza", 5, Inv, Thr, Cap, 9) );
		menu.add( new MyMenuItem("Salad", 5, Inv, Thr, Cap, 6) );
    }

	public boolean hostIsHere() {
		return hasHost;
	}

	@Override
	public Boolean isOpen() {
		return (!forceClosed && workingHost != null && workingCashier != null && workingCook != null && !workingWaiters.isEmpty());
	}

	public boolean needsHost() {
		return !hasHost;
	}

	public void sethost() {
		hasHost = true;
	}

	public boolean needsCook() {
		return !hasCook;
	}

	public void setCook() {
		hasCook = true;
	}

	public boolean needsCashier() {
		return !hasCashier;
	}

	public void setCashier() {
		hasCashier = true;
	}

	public boolean needsWaiter() {
		return (numWaiters < 3);
	}

	public void setWaiter() {
		numWaiters++;
	}

	public void setWorkingHost(HostAgent c) {
		workingHost = c;
	}

	public void setWorkingWaiter(WaiterAgent c) {
		System.out.println("added w " + c.getName());
		workingWaiters.add(c);
	}
	
	public void setWorkingCook(CookAgent c) {
		workingCook = c;
	}
	public void setWorkingCashier(CashierAgent c) {
		workingCashier = c;
	}
	
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

	enum itemState {normal, orderedMore, needsReOrder, checkReOrder}

	public void noHost() {
		workingHost = null;
	}

	public void noCashier() {
		workingCashier = null;
	}

	public void removeMe(WaiterAgent waiterAgent) {
		workingWaiters.remove(waiterAgent);
	}

	public void NoCook() {
		workingCook = null;
	}

	

}
