package market.interfaces;

import market.MarketCustomerAgent;
import market.MarketTellerAgent;

public interface MarketHost {
	public void IWantService(MarketCustomerAgent c);
		public void msgNewTeller(MarketTellerAgent t);
		public void IAmFree(MarketTellerAgent tell);
		public void msgIdLikeToGoOnBreak(MarketTellerAgent t);
		public void msgIdLikeToGetOffBreak(MarketTellerAgent t);
}
