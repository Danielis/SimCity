package bank;

import agent.Agent;
import bank.gui.HostGui;
import bank.TellerAgent;
import bank.CustomerAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

//Host Agent
public class HostAgent extends Agent {
	
		
	//Lists
	List<CustomerAgent> customers = new ArrayList<CustomerAgent>();
	Collection<MyTeller> myTellers = new ArrayList<MyTeller>();

	//Other Variables
	private String name;
	public HostGui hostGui = null;

//CONSTRUCTOR
	public HostAgent(String name) {
		super();
		this.name = name;
	}

//UTILITIES************************************************************
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	
//CLASSES****************************************************
		public class MyTeller{
			
			public MyTeller(TellerAgent t2) {
				this.t = t2;
				this.s = tellerState.free;
			}
			TellerAgent t;
			tellerState s;
		}
		enum tellerState {free, busy, wantsBreak, onBreak};
		

	

//MESSAGES****************************************************

	public void IWantService(CustomerAgent c){
		    customers.add(c);
	}
		
	public void msgNewTeller(TellerAgent t)
	{
		myTellers.add(new MyTeller(t));	
		stateChanged();
	}
	
	
	public void msgIdLikeToGoOnBreak(TellerAgent t)
	{
		print("Received message that " + t.getName() + " wants to go on break.");
		for (MyTeller mw : myTellers)
		{
			if (mw.t == t)
			{
				mw.s = tellerState.wantsBreak;
				stateChanged();
			}
		}
	}
	
	public void msgIdLikeToGetOffBreak(TellerAgent t)
	{
		print("Received message that " + t.getName() + " wants to go off break.");
		for (MyTeller mw : myTellers)
		{
			if (mw.t == t)
			{
				if (mw.s.equals(tellerState.onBreak))
				{
					print(t.getName() + " is already off break.");
				}
				mw.s = tellerState.free;
				stateChanged();
			}
		}
	}


//SCHEDULER****************************************************
	
	protected boolean pickAndExecuteAnAction() {
		try
		{
			synchronized(customers)
			{
				if (customers.size() > 0){
					for (MyTeller t : myTellers){
						if (t.s.equals(tellerState.free)){
							assignCustomer(customers.get(0), t);
						}
					}
				}
			}
			
			
			synchronized(myTellers)
			{
				//Check if waiter can go on break
				for (MyTeller mw : myTellers)
				{
					//If a waiter Wants a break
					if (mw.s == tellerState.wantsBreak)
					{
						//If there's more than one waiter
						if (myTellers.size() > 1)
						{
							//if there are other waiters who are not on break
							for (MyTeller mw_other : myTellers)
							{
								if (mw_other.s == tellerState.free)
								{
									if (!mw_other.s.equals(tellerState.onBreak))
									{
										//Grant Break
										GrantBreak(mw, true);
										return true;
									}
									else
									{
										print(mw.t.getName() + " is already on break.");
									}
								}
							}
							print("There's only one waiter available right now.");
						}
						else
						{
							//Deny break if only one waiter
							print("There is only one waiter. Break denied.");
							GrantBreak(mw, false);
						}
					}
				}
			}
			return false;
		}
		
		catch (ConcurrentModificationException e)
		{ 
			return false; 
		}
	}

//ACTIONS********************************************************
	
	private void RemoveCustomer(CustomerAgent mc)
	{
		customers.remove(mc);
	}
	
	private void assignCustomer(CustomerAgent c, MyTeller t){
		print("Customer go to teller");
	    customers.remove(0);
	    c.GoToTeller(t.t);
	}

	
	private void GrantBreak(MyTeller mw, Boolean b)
	{
		if (b == true)
		{
			print("Granting break for " + mw.t.getName() + " when customers are done. Will stop sending him customers.");
			mw.s = tellerState.onBreak;
			mw.t.msgBreakGranted(true);
		}
		else if (!b)
		{
			print("Break rejected for " + mw.t.getName());
			mw.s = tellerState.free;
			mw.t.msgBreakGranted(false);
		}
	}
}

