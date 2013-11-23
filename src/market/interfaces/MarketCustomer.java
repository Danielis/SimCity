package market.interfaces;


import market.MarketTellerAgent;

public interface MarketCustomer 
{

public abstract void 	msgWantsTransaction(String type, double temp);

public abstract void	WantsToDo(String visitPurpose, int quantity);

public abstract void	GoToTeller(MarketTeller t);

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
	

}