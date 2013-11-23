package market.interfaces;

import java.util.List;

import market.MarketCustomerAgent;

public interface MarketTeller 
{
	public void IWantAccount(MarketCustomerAgent c, double amount);
	public void DepositMoney(MarketCustomerAgent c, int accountID, double amount);
	public void WithdrawMoney(MarketCustomerAgent c, int accountID, double amount);
	public void IWantLoan(MarketCustomerAgent c, double amount);
	public void PayMyLoan(MarketCustomerAgent c, double amount);
	public void IAmLeaving();
	public void msgSetOffBreak();
	public void msgSetOnBreak();
	public void msgBreakGranted(Boolean permission);
	public int getTableNum();
		
		

}