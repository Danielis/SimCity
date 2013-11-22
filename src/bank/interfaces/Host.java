package bank.interfaces;

import bank.CustomerAgent;
import bank.TellerAgent;

public interface Host {
	public void IWantService(CustomerAgent c);
		public void msgNewTeller(TellerAgent t);
		public void IAmFree(TellerAgent tell);
		public void msgIdLikeToGoOnBreak(TellerAgent t);
		public void msgIdLikeToGetOffBreak(TellerAgent t);
}
