package market.interfaces;

import java.util.List;

import market.MarketCustomerAgent;

public interface MarketWorker 
{

	public void IAmLeaving();
	public void msgSetOffBreak();
	public void msgSetOnBreak();
	public void msgBreakGranted(Boolean permission);
	public int getTableNum();
	public void GiveOrder(MarketCustomerAgent marketCustomerAgent, String item,
			int quantityWanted);
	public void GivePayment(MarketCustomerAgent marketCustomerAgent,
			double amountOwed);
	public void PleaseFulfill(MarketCustomerAgent marketCustomerAgent);
		
		

}