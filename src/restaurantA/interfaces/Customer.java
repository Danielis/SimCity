package restaurantA.interfaces;

import java.awt.image.ImageObserver;

import javax.swing.Icon;

import restaurantA.HostAgent;
import restaurantA.Table;
import restaurantA.gui.AnimationPanel;
import restaurantA.gui.CustomerGui;

public interface Customer {

	Object check = null;
	String choice = "Steak";
	Table table = null;
	ImageObserver copyOfAnimPanel = null;

	void msgGetOut();

	void addMoneyAmountOwed(double d);

	void msgYouAreGoodToGo();

	void msgHereIsChange(double amountChange);

	String getCustomerName();

	String getName();

	void setHost(HostAgent host);

	void setGui(CustomerGui g);

	void setAnimPanel(AnimationPanel animationPanel);

	void msgAnimationFinishedGoToSeat();

	void gotHungry();

	void msgAnimationFinishedGoToCashier();

	void msgAnimationFinishedLeaveRestaurant();
}