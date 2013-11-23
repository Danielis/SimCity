package bank.interfaces;

import java.util.List;

import bank.BankCustomerRole;

public interface Teller 
{
	public void IWantAccount(BankCustomerRole c, double amount);
	public void DepositMoney(BankCustomerRole c, int accountID, double amount);
	public void WithdrawMoney(BankCustomerRole c, int accountID, double amount);
	public void IWantLoan(BankCustomerRole c, double amount);
	public void PayMyLoan(BankCustomerRole c, double amount);
	public void IAmLeaving();
	public void msgSetOffBreak();
	public void msgSetOnBreak();
	public void msgBreakGranted(Boolean permission);
	public int getTableNum();
		
		

}