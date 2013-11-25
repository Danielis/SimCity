package bank.test.mock;
import java.util.List;

import bank.BankCustomerRole;
import bank.gui.TellerGui;
import bank.interfaces.Teller;


public class MockTeller extends Mock implements Teller {

        public EventLog log;

        public MockTeller(String name) 
        {
                super(name);
                log = new EventLog();
        }

		
		public void IWantAccount(BankCustomerRole c, double amount) {}
		
		public void DepositMoney(BankCustomerRole c, int accountID, double amount) {}
		
		public void WithdrawMoney(BankCustomerRole c, int accountID, double amount) {}
		
		public void IWantLoan(BankCustomerRole c, double amount) {}
		
		public void PayMyLoan(BankCustomerRole c, double amount) {}

		public void IAmLeaving() {}
	
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

}