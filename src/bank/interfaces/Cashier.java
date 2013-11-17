package bank.interfaces;

public interface Cashier 
{
	public abstract void msgHereIsACheck(Teller newW, Customer newC, String newChoice);
	public abstract void msgHereIsMyPayment(Customer newC, float payment);
}