package bank.interfaces;

import bank.BankCustomerRole;
import bank.TellerAgent;

public interface Host {
	public void IWantService(BankCustomerRole c);
		public void msgNewTeller(TellerAgent t);
		public void IAmFree(TellerAgent tell);
		public void msgIdLikeToGoOnBreak(TellerAgent t);
		public void msgIdLikeToGetOffBreak(TellerAgent t);
}
