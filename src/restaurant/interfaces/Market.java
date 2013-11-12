package restaurant.interfaces;

import java.util.List;

import restaurant.MarketAgent;
import restaurant.interfaces.*;

public interface Market{
	public String getName();
	public void msgHereIsAPayment(float amount);
	public void msgIWantToOrder(String choice, int amount);
}