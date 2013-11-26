package bank.test.mock;


import java.util.List;

import logging.TrackerGui;
import bank.BankCustomerRole;
import bank.gui.BankAnimationPanel;
import bank.gui.HostGui;
import bank.interfaces.*;

public class MockHost extends Mock implements BankHost {

    public EventLog log;

    public MockHost(String name) 
    {
            super(name);
            log = new EventLog();
    }

	public void IWantService(BankCustomerRole c) {
	}

	public void msgNewTeller(Teller t) {
	}

	public void IAmFree(Teller tell) {		
	}

	public void msgIdLikeToGoOnBreak(Teller t) {
	}


	public void msgIdLikeToGetOffBreak(Teller t) {
	}


	public void WaitForAnimation() {
		// TODO Auto-generated method stub
		
	}

	
	public void DoneWithAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimPanel(BankAnimationPanel animationPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(HostGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrackerGui(TrackerGui t) {
		
	}

	



}