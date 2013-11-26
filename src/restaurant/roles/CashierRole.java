package restaurant.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.CashierAgent.CheckState;
import restaurant.CashierAgent.MyCheck;
import restaurant.CashierAgent.MyPayment;
import restaurant.CashierAgent.PaymentState;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import roles.Role;

public class CashierRole extends Role implements Cashier{
	
	//Statics
	private static float initialBalance = 500.0f;
	
	//Lists
	List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	List<MyPayment> payments = Collections.synchronizedList(new ArrayList<MyPayment>());
	List<Float> owes = Collections.synchronizedList(new ArrayList<Float>());
	
	//Variables
	private String name = "Squidward";
	private float account;
	
	//Other
	private Map<String, Float> prices = new HashMap<String, Float>();
	
	//Constructor
	public CashierRole(String name) {
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
		return false;
	}

//ACTIONS********************************************************
	
	private void ComputeCheck(MyCheck mc)
	{
		mc.state = CheckState.computed;
		print("Computing check.");
		mc.owedAmount += mc.price;
		print(mc.c.getName() + " owes $" + mc.owedAmount);
		mc.w.msgCheckIsComputed(mc.c, mc.choice, mc.owedAmount);
	}
	
	private void CheckIfFullyPaid(MyCheck mc)
	{
		mc.state = CheckState.paid;
		if (mc.owedAmount == 0)
		{
			print("Customer paid for his meal.");
			checks.remove(mc);
		}
		else if (mc.owedAmount > 0)
		{
			print("Customer still owes $" + mc.owedAmount);
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
		
		//If we have enough
		if (account > mp.payment)
		{
			print("Paying " + mp.m.getName() + " $" + mp.payment);
			mp.m.msgHereIsAPayment(mp.payment);
			account -= mp.payment;
			account = this.RoundToTwoDigits(account);
			payments.remove(mp);
		}
		
		//If we don't have enough
		else if (account < mp.payment)
		{
			if (account <= 0)
				print("I'm out of cash.");
			else
				print("Paying " + mp.m.getName() + " $" + account);
			
			mp.payment = mp.payment - account;
			print("I owe " + mp.m.getName() + " $" + mp.payment);
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
	
	private float RoundToTwoDigits(float a)
	{
		a = Math.round(a*100f);
		a = a/100f;
		return a;
	}
}

