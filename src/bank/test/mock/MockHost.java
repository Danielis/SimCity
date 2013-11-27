package bank.test.mock;


import java.util.List;



import logging.TrackerGui;
import bank.Bank;
import bank.BankCustomerRole;
import bank.gui.BankAnimationPanel;
import bank.gui.HostGui;
import bank.interfaces.*;

public class MockHost extends Mock implements BankHost {

    public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public EventLog log;
	public void IWantService(BankCustomer c){}
	public  void addMe(Teller t){}
	public  void IAmFree(Teller tell){}
	public  void msgIdLikeToGoOnBreak(Teller t){}
	public  void msgIdLikeToGetOffBreak(Teller t){}
	public  void WaitForAnimation(){}

	public  void DoneWithAnimation(){}
	
	public  void setAnimPanel(BankAnimationPanel animationPanel){}
	public  String getName(){
		return null;}
	public  void setGui(HostGui g){}

	public void setBank(Bank b){}
	public void setTellers(List<Teller> workingTellers){}


}