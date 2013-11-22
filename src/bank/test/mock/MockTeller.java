package bank.test.mock;
import java.util.List;

import bank.CustomerAgent;
import bank.interfaces.Teller;


public class MockTeller extends Mock implements Teller {

        public EventLog log;

        public MockTeller(String name) 
        {
                super(name);
                log = new EventLog();
        }

		
		public void IWantAccount(CustomerAgent c, double amount) {}
		
		public void DepositMoney(CustomerAgent c, int accountID, double amount) {}
		
		public void WithdrawMoney(CustomerAgent c, int accountID, double amount) {}
		
		public void IWantLoan(CustomerAgent c, double amount) {}
		
		public void PayMyLoan(CustomerAgent c, double amount) {}

		public void IAmLeaving() {}
	
		public void msgSetOffBreak() {}
		
		public void msgSetOnBreak() {}

		public void msgBreakGranted(Boolean permission) {}


		public int getTableNum() {
			return 0;}

}