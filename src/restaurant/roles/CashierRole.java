package restaurant.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import restaurant.CashierAgent.CheckState;
import restaurant.CashierAgent.MyCheck;
import restaurant.CashierAgent.MyPayment;
import restaurant.CashierAgent.PaymentState;
import restaurant.Restaurant;
import restaurant.gui.CashierGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.*;
import roles.Role;

public class CashierRole extends Role implements Cashier{
	
	Restaurant r;
	
	public WorkState myState = WorkState.none;
	//Statics
	private static float initialBalance = 500.0f;
	
	public RestaurantAnimationPanel copyOfAnimPanel;
	public CashierGui cashierGui = null;
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	//Lists
	List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	List<MyPayment> payments = Collections.synchronizedList(new ArrayList<MyPayment>());
	List<Float> owes = Collections.synchronizedList(new ArrayList<Float>());
	
	//Variables
	private String name = "Squidward";
	private double mymoney;
	private float account;
	private double  balance;
	//Other 
	private Map<String, Float> prices = new HashMap<String, Float>();
	
	//Constructor
	public CashierRole(String name, double cash) {
		super();
		this.name = name;
		
		
		//Map name to foods
		prices.put("Steak",  15.99f);
		prices.put("Chicken", 10.99f);
		prices.put("Salad", 5.99f);
		prices.put("Pizza", 8.99f);
		
		for (int i = 0; i < 3; i++)
			owes.add(0f);
		
		account = initialBalance; //Starting account balance for cashier
		mymoney = cash;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setBalance(float balance)
	{
		account = balance;
	}
	
	public float getAccountBalance(){
		return account;
	}
	
//UTILITIES**************************************************
	
	public void setGui(CashierGui g) {
		cashierGui = g;
	}
	
	public CashierGui getGui(){
		return cashierGui;
	}
	
	//GETTERS
	public List<MyCheck> getChecks() {
		return checks;
	}
	public List<MyPayment> getPayments() {
		return payments;
	}
	
	public List<Float> getOwes()
	{
		return owes;
	}
	public class MyPayment
	{
		public Market m;
		public String order;
		public int amount;
		public float payment;
		public PaymentState state;
		
		MyPayment(Market newMarket, String newOrder, int newAmount, PaymentState newState)
		{
			m = newMarket;
			order = newOrder;
			amount = newAmount;
			payment = 0;
			state = newState;
		}
	}
	
	public class MyCheck
	{
		public Waiter w;
		public Customer c;
		public String choice;
		public float price;
		public float owedAmount;
		public CheckState state;
		
		MyCheck(Waiter newWaiter, Customer newCustomer, String newChoice, CheckState newState)
		{
			w = newWaiter;
			c = newCustomer;
			choice = newChoice;
			price = prices.get(newChoice);
			owedAmount = 0.0f;
			state = newState;
		}
	}
	
	
	//enums
	public enum PaymentState
	{
		none, needsPayment, paid
	}
	
	public enum CheckState
	{
		none, needsComputing, computed,
		justPaid, paid,
	};


//MESSAGES****************************************************

	public void msgGetPaid(){
		balance =+50;
	}

	public void msgLeaveWork()
	{
		myState = WorkState.needToLeave;
		stateChanged();
	}
	
	public void msgHereIsACheck(Waiter newW, Customer newC, String newChoice)
	{
		print("Received message to compute check.");
		
		//Check if the customer already exists and if so, update it.
		Boolean exists = false;
		synchronized(checks)
		{
			for (MyCheck mc : checks)
			{
				if (mc.c == newC)
				{
					print("Customer already exists. Keeping this in mind.");
					mc.w = newW;
					mc.choice = newChoice;
					mc.price = prices.get(newChoice);
					mc.state = CheckState.needsComputing;
					exists = true;
				}
			}
		}
		
		//If the customer doesn't already exist
		if (exists == false)
		{
			checks.add(new MyCheck(newW, newC, newChoice, CheckState.needsComputing));
		}
		
		//Run Scheduler
		stateChanged();
	}
	
	public void msgHereAMarketOrder(Market m, String type, int amount)
	{
		print("Received message to pay " + m.getName());
		payments.add(new MyPayment(m, type, amount, PaymentState.needsPayment));
		
		//Run Scheduler
		stateChanged();
	}
	
	public void msgHereIsMyPayment(Customer newC, float payment)
	{
		synchronized(checks)
		{
			for (MyCheck mc : checks)
			{
				if (mc.c == newC)
				{
					mc.owedAmount -= payment;
					account += payment;
					account = this.RoundToTwoDigits(account);
					print("Received customer payment. My balance is now $" + account);
					mc.state = CheckState.justPaid;
					stateChanged();
				}
			}
		}
	}


//SCHEDULER****************************************************
	
	public boolean pickAndExecuteAnAction() 
	{
		synchronized(checks)
		{
			for (MyCheck mc : checks)
			{
				if (mc.state == CheckState.needsComputing)
				{
					ComputeCheck(mc);
					return true;
				}
			}
		}
		
		synchronized(checks)
		{
			for (MyCheck mc : checks)
			{
				if (mc.state == CheckState.justPaid)
				{
					CheckIfFullyPaid(mc);
					return true;
				}
			}
		}
		
		synchronized(payments)
		{
			for (MyPayment mp : payments)
			{
				if (mp.state == PaymentState.needsPayment)
				{
					PayMarket(mp);
					return true;
				}
			}
		}
		
		if (myState == WorkState.needToLeave)
		{
			//Check if there's no host
			System.out.println("Checks: " + checks.size() + "/Payments: " + payments.size());
			if
				(
					checks.size() == 0 &&
					payments.size() == 0
				)
			{
				LeaveWork();
			}
		}
		
		return false;
	}

//ACTIONS********************************************************
	
	private void ComputeCheck(MyCheck mc)
	{
		mc.state = CheckState.computed;
		print("Computing check.");
		mc.owedAmount += mc.price;
		print(mc.c.getName() + " owes $" + mc.owedAmount);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", mc.c.getName() + " owes $" + mc.owedAmount, new Date()));
		mc.w.msgCheckIsComputed(mc.c, mc.choice, mc.owedAmount);
	}
	
	private void CheckIfFullyPaid(MyCheck mc)
	{
		mc.state = CheckState.paid;
		if (mc.owedAmount == 0)
		{
			print("Customer paid for his meal.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", mc.c.getName() + " owes $" + mc.owedAmount, new Date()));
			checks.remove(mc);
		}
		else if (mc.owedAmount > 0)
		{
			print("Customer still owes $" + mc.owedAmount);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "Customer still owes $" + mc.owedAmount, new Date()));

		}
	}
	
	private void PayMarket(MyPayment mp)
	{
		mp.state = PaymentState.paid;
		
		//Calculate how much cashier owes
		mp.payment += (float)mp.amount * (float)prices.get(mp.order);
		if (mp.m.getName() == "Market 1")
		{
			mp.payment += owes.get(0);
		}
		else if (mp.m.getName() == "Market 2")
		{
			mp.payment += owes.get(1);
		}
		else if (mp.m.getName() == "Market 3")
		{
			mp.payment += owes.get(2);
		}
		mp.payment = this.RoundToTwoDigits(mp.payment);
		print("I must pay " + mp.m.getName() + " $" + mp.payment + " and I have $" + account);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "I must pay " + mp.m.getName() + " $" + mp.payment + " and I have $" + account, new Date()));

		
		//If we have enough
		if (account > mp.payment)
		{
			print("Paying " + mp.m.getName() + " $" + mp.payment);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "Paying " + mp.m.getName() + " $" + mp.payment, new Date()));
			mp.m.msgHereIsAPayment(mp.payment);
			account -= mp.payment;
			account = this.RoundToTwoDigits(account);
			payments.remove(mp);
		}
		
		//If we don't have enough
		else if (account < mp.payment)
		{
			if (account <= 0){
				print("I'm out of cash.");
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "I'm out of cash", new Date()));
			}

			else {
				trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "Paying " + mp.m.getName() + " $" + account, new Date()));
				print("Paying " + mp.m.getName() + " $" + account);
			}
				
			mp.payment = mp.payment - account;
			print("I owe " + mp.m.getName() + " $" + mp.payment);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "I owe " + mp.m.getName() + " $" + mp.payment, new Date()));
			mp.m.msgHereIsAPayment(account);
			account = 0;
			if (mp.m.getName() == "Market 1")
			{
				owes.set(0, mp.payment);
			}
			else if (mp.m.getName() == "Market 2")
			{
				owes.set(1, mp.payment);
			}
			else if (mp.m.getName() == "Market 3")
			{
				owes.set(2, mp.payment);
			}
			payments.remove(mp);
		}
	}
	
	public void LeaveWork()
	{
		cashierGui.Disable();
		myState = WorkState.leaving;
		print("CashierRole: Called to leave work.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.RESTAURANT, "CashierRole", "Called to leave work.", new Date()));
		//STUB
		myPerson.msgLeftWork(this, this.mymoney);
		r.nullifyCashier();
	}
	
	private float RoundToTwoDigits(float a)
	{
		a = Math.round(a*100f);
		a = a/100f;
		return a;
	}


	
	public void WaitForAnimation()
	{
		try
		{
			this.animSemaphore.acquire();	
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:", e);
        }
	}
	
	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}

	@Override
	public void setAnimPanel(RestaurantAnimationPanel animationPanel) {
		this.copyOfAnimPanel = animationPanel;
		
	}
	
	public void setRestaurant(Restaurant r)
	{
		this.r = r;
	}
	
	
}

