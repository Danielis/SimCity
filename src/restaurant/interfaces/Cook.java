package restaurant.interfaces;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import restaurant.MarketAgent;
import restaurant.CookAgent.myIcon;

public interface Cook {

	public List<myIcon> icons = new ArrayList<myIcon>();
	ImageObserver copyOfAnimPanel = null;

	public abstract void msgHereIsAnOrder(Waiter waiter, String choice, int table);

	public abstract void msgTakingItem(Waiter waiter);

	public abstract void msgHereIsYourOrder(MarketAgent marketAgent, String name,
			int amountwanted, boolean b);

	public abstract void DoneWithAnimation();

	public abstract void WaitForAnimation();

}
