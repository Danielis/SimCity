package bank.interfaces;

import java.awt.image.ImageObserver;

import bank.gui.*;

import java.util.List;

import logging.TrackerGui;
import bank.Bank;

public interface Teller 
{
	boolean  isOnBreak = false;
	Object host = null;
	ImageObserver copyOfAnimPanel = null;
	public void IWantAccount(BankCustomer c, double amount);
	public void DepositMoney(BankCustomer c, int accountID, double amount);
	public void WithdrawMoney(BankCustomer c, int accountID, double amount);
	public void IWantLoan(BankCustomer c, double amount);
	public void PayMyLoan(BankCustomer c, double amount);
	public void IAmLeaving();
	public void msgSetOffBreak();
	public void msgSetOnBreak();
	public void msgBreakGranted(Boolean permission);
	public int getTableNum();
	public String getName();
	public void WaitForAnimation();
	public void DoneWithAnimation();
	public TellerGui getGui();
	public void setBank(Bank b);
	public void setAnimPanel(BankAnimationPanel animationPanel);
	public void setGui(TellerGui g);
	public void setTableNum(int tellerNunmber);
	public void setHost(BankHost host2);		
		

}