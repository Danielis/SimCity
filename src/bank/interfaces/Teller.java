package bank.interfaces;

import java.util.List;

import bank.CustomerAgent;

public interface Teller 
{
	public void IWantAccount(CustomerAgent c, double amount);
	public void DepositMoney(CustomerAgent c, int accountID, double amount);
	public void WithdrawMoney(CustomerAgent c, int accountID, double amount);
	public void IWantLoan(CustomerAgent c, double amount);
	public void PayMyLoan(CustomerAgent c, double amount);
	public void IAmLeaving();
	public void msgSetOffBreak();
	public void msgSetOnBreak();
	public void msgBreakGranted(Boolean permission);
	public int getTableNum();
		
		

}