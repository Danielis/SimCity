package market.interfaces;

import market.MarketCustomerAgent;
import market.MarketWorkerAgent;

public interface MarketHost {
	public void IWantService(MarketCustomerAgent c);
		public void msgNewTeller(MarketWorkerAgent t);
		public void IAmFree(MarketWorkerAgent tell);
		public void msgIdLikeToGoOnBreak(MarketWorkerAgent t);
		public void msgIdLikeToGetOffBreak(MarketWorkerAgent t);
}
