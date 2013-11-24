package market.interfaces;

import java.util.List;

import market.MarketCustomerRole;

public interface MarketWorker 
{

	public void IAmLeaving();
	public void msgSetOffBreak();
	public void msgSetOnBreak();
	public void msgBreakGranted(Boolean permission);
	public int getTableNum();
	public void GiveOrder(MarketCustomerRole marketCustomerAgent, String item,
			int quantityWanted);
	public void GivePayment(MarketCustomerRole marketCustomerAgent,
			double amountOwed);
	public void PleaseFulfill(MarketCustomerRole marketCustomerAgent);
		
		

}