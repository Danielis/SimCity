package market.interfaces;


import java.awt.image.ImageObserver;

import javax.swing.Icon;

import market.MarketHostAgent;
import market.MarketWorkerAgent;
import market.gui.MarketAnimationPanel;
import market.gui.MarketCustomerGui;

public interface MarketCustomer 
{

	ImageObserver copyOfAnimPanel = null;

	String getName();

	void setHost(MarketHostAgent host);

	void DoneWithAnimation();

	void msgWantsToBuy(String type, int temp2);

	void WaitForAnimation();

	void setGui(MarketCustomerGui g);

	void setAnimPanel(MarketAnimationPanel animationPanel);

	void startThread();

	MarketCustomerGui getGui();

	void pauseAgent();

	void resumeAgent();


	

}