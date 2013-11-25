package bank.test.mock;


import java.util.List;

import bank.BankCustomerRole;
import bank.TellerAgent;
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

	public void msgNewTeller(TellerAgent t) {
	}

	public void IAmFree(TellerAgent tell) {		
	}

	public void msgIdLikeToGoOnBreak(TellerAgent t) {
	}


	public void msgIdLikeToGetOffBreak(TellerAgent t) {
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

	



}