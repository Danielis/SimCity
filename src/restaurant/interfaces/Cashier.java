package restaurant.interfaces;

public interface Cashier 
{
	public abstract void msgHereIsACheck(Waiter newW, Customer newC, String newChoice);
	public abstract void msgHereIsMyPayment(Customer newC, float payment);
}