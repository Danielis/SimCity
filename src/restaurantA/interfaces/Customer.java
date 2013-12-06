package restaurantA.interfaces;

import restaurantA.Table;

public interface Customer {

	Object check = null;
	String choice = "Steak";
	Table table = null;

	void msgGetOut();

	void addMoneyAmountOwed(int amountOwed);

	void msgYouAreGoodToGo();

	void msgHereIsChange(int amountChange);

	String getCustomerName();
}