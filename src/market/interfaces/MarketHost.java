package market.interfaces;

import market.MarketCustomerRole;
import market.MarketWorkerAgent;

public interface MarketHost {
	public void IWantService(MarketCustomerRole c);
		public void msgNewTeller(MarketWorkerAgent t);
		public void IAmFree(MarketWorkerAgent tell);
		public void msgIdLikeToGoOnBreak(MarketWorkerAgent t);
		public void msgIdLikeToGetOffBreak(MarketWorkerAgent t);
}
