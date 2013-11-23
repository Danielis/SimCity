package bank.interfaces;


import javax.swing.Icon;

import bank.TellerAgent;
import bank.gui.BankAnimationPanel;
import bank.gui.CustomerGui;

public interface BankCustomer 
{
	
	public BankAnimationPanel copyOfAnimPanel = null;

public abstract void 	msgWantsTransaction();

public abstract void	WantsToDo(String visitPurpose, int quantity);

public abstract void	GoToTeller(Teller t);

public abstract void	AccountCreated();

public abstract void	MoneySuccesfullyDeposited();

public abstract void	LoanCreated(double temp);

public abstract void	CannotCreateLoan();

public abstract void	CreditNotGoodEnough();

public abstract void	YourLoanIsPaidOff(double change);

public abstract void	YouStillOwe(double d, int i);

public abstract void	HereIsWithdrawal(double amount);

public abstract void	HereIsPartialWithdrawal(double amount);

public abstract void	NoMoney();

public abstract void 	WantAccount();

public abstract String getName();

public void WaitForAnimation();

public void DoneWithAnimation();

public abstract void setHost(bank.HostAgent host);


public abstract void setGui(CustomerGui g);

public abstract void setAnimPanel(BankAnimationPanel animationPanel);

public abstract CustomerGui getGui();

public abstract void startThread();

public abstract void pauseAgent();

public abstract void resumeAgent();



	

}