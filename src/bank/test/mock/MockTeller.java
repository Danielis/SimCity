package bank.test.mock;
import java.util.List;

import logging.TrackerGui;
import bank.Bank;
import bank.Bank.Loan;
import bank.gui.BankAnimationPanel;
import bank.gui.TellerGui;
import bank.interfaces.*;


public class MockTeller extends Mock implements Teller {

        public EventLog log;
		public Bank bank;

        public MockTeller(String name) 
        {
                super(name);
                log = new EventLog();
        }

		
		public void IWantAccount(BankCustomer c, double amount) {
			log.add(new LoggedEvent("Received message IWantAccount from customer"));
			}
		
	
		public void DepositMoney(BankCustomer c, int accountID, double amount) {
			System.out.println("tea");
			log.add(new LoggedEvent("Received message DepositMoney from customer"));
			}
		
		public void WithdrawMoney(BankCustomer c, int accountID, double amount) {
			log.add(new LoggedEvent("Received message WithdrawMoney from customer"));}
		
		public void IWantLoan(BankCustomer c, double amount) {
			log.add(new LoggedEvent("Received message IWantLoan from customer"));}
		
		public void PayMyLoan(BankCustomer c, double amount) {
			log.add(new LoggedEvent("Received message PayMyLoan from customer"));}

		public void IAmLeaving() {
			System.out.println("tea");
			log.add(new LoggedEvent("Received message IAmLeaving from customer"));}
	
		public void msgSetOffBreak() {}
		
		public void msgSetOnBreak() {}

		public void msgBreakGranted(Boolean permission) {}


		public int getTableNum() {
			return 0;}


		@Override
		public void WaitForAnimation() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void DoneWithAnimation() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public TellerGui getGui() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public void setBank(Bank b) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void setAnimPanel(BankAnimationPanel animationPanel) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void setGui(TellerGui g) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void setTableNum(int tellerNunmber) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void setHost(BankHost host2) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void IAmRobbing(BankCustomer bankCustomerRole, double amount) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void PayMyLoan(BankCustomer c, double amount, Loan loan) {
			// TODO Auto-generated method stub
			
		}


}