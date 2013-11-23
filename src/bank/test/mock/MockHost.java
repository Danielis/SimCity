package bank.test.mock;


import java.util.List;

import bank.BankCustomerRole;
import bank.TellerAgent;
import bank.interfaces.*;

public class MockHost extends Mock implements Host {

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

	



}