package bank.test.mock;
import bank.Bank;
import bank.BankCustomerRole;
import bank.gui.BankAnimationPanel;
import bank.gui.TellerGui;
import bank.interfaces.BankHost;
import bank.interfaces.*;
import bank.gui.*;

public class MockCustomer extends Mock implements BankCustomer {

	public MockCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public void 	msgWantsTransaction(){
	}

	public  void	WantsToDo(String visitPurpose, int quantity){
	}
	public  void	GoToTeller(Teller t){
	}

	public  void	MoneySuccesfullyDeposited(){
	}

	public  void	CannotCreateLoan(){
	}
	public  void	CreditNotGoodEnough(){
	}
	public  void	YourLoanIsPaidOff(double change){
	}
	public  void	YouStillOwe(double d, int i){
	}

	public  void	HereIsWithdrawal(double amount){
	}
	public  void	HereIsPartialWithdrawal(double amount){
	}
	public  void	NoMoney(){
	}
	public  void 	WantAccount(){
	}
	public  String getName(){
		return null;
	}
	public void WaitForAnimation(){
	}
	public void DoneWithAnimation(){
	}
	public  void setHost(BankHost host){
	}

	public  void setGui(CustomerGui g){
	}
	public  void setAnimPanel(BankAnimationPanel animationPanel){
	}
	public  CustomerGui getGui(){
		return null;
	}
	public  void startThread(){
	}
	public  void pauseAgent(){
	}
	public  void resumeAgent(){
	}
}
