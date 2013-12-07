package bank.test.mock;
import bank.Bank;
import city.BankDatabase.*;
import bank.Bank.*;
import bank.BankCustomerRole;
import bank.gui.BankAnimationPanel;
import bank.gui.TellerGui;
import bank.interfaces.BankHost;
import bank.interfaces.*;
import bank.gui.*;

public class MockCustomer extends Mock implements BankCustomer {

    public EventLog log = new EventLog();
    
	public MockCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public  void 	msgWantsTransaction(){}

	public  void	WantsToDo(String visitPurpose, int quantity){}

	public  void	GoToTeller(Teller t){
		log.add(new LoggedEvent("Received message GoToTeller from host"));
	}


	public  void	MoneySuccesfullyDeposited(){}


	public  void	CannotCreateLoan(){}

	public  void	CreditNotGoodEnough(){
		log.add(new LoggedEvent("Received message CreditNotGoodEnough from teller"));}

	public  void	YourLoanIsPaidOff(double change){
		log.add(new LoggedEvent("Received message YourLoanIsPaidOff from teller"));
		}

	public  void	YouStillOwe(double d, int i){
		log.add(new LoggedEvent("Received message YouStillOwe from teller"));}

	public  void	HereIsWithdrawal(double amount){
		log.add(new LoggedEvent("Received message HereIsWithdrawal from teller"));
		}

	public  void	HereIsPartialWithdrawal(double amount){
		log.add(new LoggedEvent("Received message HereIsPartialWithdrawal from teller"));}

	public  void	NoMoney(){
		log.add(new LoggedEvent("Received message NoMoney from teller"));}

	public  void 	WantAccount(){
		log.add(new LoggedEvent("Received message WantAccount from teller"));
	}

	public  String getName(){
		return null;}

	public void WaitForAnimation(){}

	public void DoneWithAnimation(){}

	public  void setHost(BankHost host){}


	public  void setGui(CustomerGui g){}

	public  void setAnimPanel(BankAnimationPanel animationPanel){}

	public  CustomerGui getGui(){
		return null;}

	public  void startThread(){}

	public  void pauseAgent(){}

	public  void resumeAgent(){}

	public  void NoLoan(){}

	public  void LoanCreated(double amount, Loan loan){
		log.add(new LoggedEvent("Received message LoanCreated from teller"));}

	public  void AccountCreated(Account account){
		log.add(new LoggedEvent("Received message AccountCreated from teller"));
	}
	
	public void BankIsClosed() {
		log.add(new LoggedEvent("Received message BankIsClosed from host"));
		
	}
	@Override
	public void GetOut() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OkHereIsMoney(double amount) {
		// TODO Auto-generated method stub
		
	}

}
