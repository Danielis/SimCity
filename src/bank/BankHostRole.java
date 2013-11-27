package bank;

import agent.Agent;
import roles.Role;
import bank.gui.BankAnimationPanel;
import bank.gui.HostGui;
import bank.interfaces.*;
import bank.BankCustomerRole;

import java.util.*;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;

//Host Agent
public class BankHostRole extends Role implements BankHost {


	//Lists
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	Collection<MyTeller> myTellers = new ArrayList<MyTeller>();

	//Other Variables
	private String name;
	public HostGui hostGui = null;
	double balance = 0;
	Bank bank;
	public Semaphore animSemaphore = new Semaphore(0,true);
	public BankAnimationPanel copyOfAnimPanel;

	//CONSTRUCTOR
	public BankHostRole(String name) {
		super();
		this.name = name;
		tellTellers();
	}
	private void tellTellers() {
		
	}
	Boolean leave = false;

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
	public void setAnimPanel(BankAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}

	//CLASSES****************************************************
	public class MyTeller{

		public MyTeller(Teller t2) {
			this.t = t2;
			this.s = tellerState.free;
		}
		Teller t;
		tellerState s;
	}
	enum tellerState {free, busy, wantsBreak, onBreak};

	public class MyCustomer{

		public MyCustomer(BankCustomerRole c) {
			this.c = c;
			this.s = customerState.waiting;
		}
		BankCustomerRole c;
		customerState s;
	}
	enum customerState {waiting, done};



//MESSAGES****************************************************
	public void msgGetPaid(){
		balance =+50;
	}
	@Override
	public void msgLeaveWork() {
			bank.removeMe(this);
			leave = true;
			stateChanged();
		}
		
	public void IWantService(BankCustomerRole c){
	    customers.add(new MyCustomer(c));
	    updateCustpost();
	    stateChanged();
	}

	public void addMe(Teller t)
	{
		myTellers.add(new MyTeller(t));	
		stateChanged();
	}

	public void IAmFree(Teller tell){
		//print("received msg free");
		for(MyTeller t: myTellers){
			if (t.t == tell){
				t.s = tellerState.free;
				stateChanged();
			}
		}
	}


	public void msgIdLikeToGoOnBreak(Teller t)
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

	public void msgIdLikeToGetOffBreak(Teller t)
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

	public boolean pickAndExecuteAnAction() {
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
			if (leave)
				LeaveWork();
			return false;
		}

		catch (ConcurrentModificationException e)
		{ 
			return false; 
		}
	}

//ACTIONS********************************************************
	
	private void LeaveWork() {
		bank.Leaving();
		hostGui.setDone();
		myPerson.msgLeftWork(this, balance);
		
	}
	private void NoTellers(MyCustomer c){
		print("Sorry the bank is closed");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "BankHostRole","Sorry the bank is closed", new Date()));

		c.c.BankIsClosed();
		customers.remove(c);
	}
	private void RemoveCustomer(BankCustomerRole mc)
	{
		customers.remove(mc);
	}

	private void assignCustomer(MyCustomer c, MyTeller t){
		print("Customer go to teller " + t.t.getTableNum());
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "BankHostRole", "Customer go to teller " + t.t.getTableNum(), new Date()));
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
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "BankHostRole", "Granting break for " + mw.t.getName() + " when customers are done. Will stop sending him customers.", new Date()));
			print("Granting break for " + mw.t.getName() + " when customers are done. Will stop sending him customers.");
			mw.s = tellerState.onBreak;
			mw.t.msgBreakGranted(true);
		}
		else if (!b)
		{
			print("Break rejected for " + mw.t.getName());
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.BANK, "BankHostRole", "Break rejected for " + mw.t.getName(), new Date()));
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


	@Override
	public void setBank(Bank b) {
		bank = b;
	}


	public void setTellers(List<Teller> tellers) {
		for (Teller t : tellers)
			myTellers.add(new MyTeller(t));
	}



}

