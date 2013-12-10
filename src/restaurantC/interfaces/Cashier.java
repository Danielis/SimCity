package restaurantC.interfaces;

public interface Cashier {
	public abstract void checkRequest(Waiter w, int t, String c, Customer cust);
	public abstract void hereIsMoney(double amount, Customer c);
}