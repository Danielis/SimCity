package market;

import agent.Agent;
import bank.HostAgent.MyCustomer;
import market.gui.MarketAnimationPanel;
import market.gui.MarketHostGui;
import market.interfaces.MarketHost;
import market.MarketWorkerAgent;
import market.MarketCustomerRole;

import java.util.*;
import java.util.concurrent.Semaphore;

//Host Agent
public class MarketHostAgent extends Agent implements MarketHost {
	
		
	//Lists
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	Collection<MyTeller> myTellers = new ArrayList<MyTeller>();

	//Other Variables
	private String name;
	public MarketHostGui hostGui = null;

	public Semaphore animSemaphore = new Semaphore(0,true);
	public MarketAnimationPanel copyOfAnimPanel;
//CONSTRUCTOR
	public MarketHostAgent(String name) {
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
	
	public void setGui(MarketHostGui gui) {
		hostGui = gui;
	}

	public MarketHostGui getGui() {
		return hostGui;
	}
	public void DoneWithAnimation()
	{
		animSemaphore.release();
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
	public void setAnimPanel(MarketAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}
	
//CLASSES****************************************************
		public class MyTeller{
			
			public MyTeller(MarketWorkerAgent t2) {
				this.t = t2;
				this.s = tellerState.free;
			}
			MarketWorkerAgent t;
			tellerState s;
		}
		enum tellerState {free, busy, wantsBreak, onBreak};
		
		public class MyCustomer{
			
			public MyCustomer(MarketCustomerRole c) {
				this.c = c;
				this.s = customerState.waiting;
			}
			MarketCustomerRole c;
			customerState s;
		}
		enum customerState {waiting, done};
	

//MESSAGES****************************************************

	public void IWantService(MarketCustomerRole c){
    customers.add(new MyCustomer(c));
    updateCustpost();
    stateChanged();
	}
		
	public void msgNewTeller(MarketWorkerAgent t)
	{
		myTellers.add(new MyTeller(t));	
		stateChanged();
	}
	
	public void IAmFree(MarketWorkerAgent tell){
		//print("received msg free");
		for(MyTeller t: myTellers){
			if (t.t == tell){
				t.s = tellerState.free;
				stateChanged();
			}
		}
	}
	
	
	public void msgIdLikeToGoOnBreak(MarketWorkerAgent t)
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
	
	public void msgIdLikeToGetOffBreak(MarketWorkerAgent t)
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
				for (MyCustomer c : customers){
					if (c.s == customerState.waiting){
						for (MyTeller t : myTellers){
							if (t.s.equals(tellerState.free)){
								assignCustomer(c, t);
								return true;
							}
						}
						if (myTellers.size() == 0){
							NoTellers(c);
							return true;
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
			//print("reached gui call");
			hostGui.DoGoToHomePosition();
			return false;
		}
		
		catch (ConcurrentModificationException e)
		{ 
			return false; 
		}
	}

//ACTIONS********************************************************
	private void NoTellers(MyCustomer c){
		c.c.MarketIsClosed();
		customers.remove(c);
	}
	
	private void RemoveCustomer(MarketCustomerRole mc)
	{
		customers.remove(mc);
	}
	
	private void assignCustomer(MyCustomer c, MyTeller t){
		print("Customer go to teller " + t.t.getTableNum());
		if (t.t.getTableNum() == 1)
			hostGui.setSpeechBubble("host_1");
		if (t.t.getTableNum() == 2)
			hostGui.setSpeechBubble("host_2");
		if (t.t.getTableNum() == 3)
			hostGui.setSpeechBubble("host_3");
		
		// c.s = customerState.done;
		customers.remove(0);
	    t.s = tellerState.busy;
	    c.c.GoToTeller(t.t);
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
	
	public void updateCustpost(){
		//print("called update custp os");
       // for (int i =0; i <customers.size(); i++){
       // 	customers.get(i).c.getCustomerGui().shuffle(0, i*25);
       // }
}

	
}

