package roles;

public class Building {

	public Coordinate entrance;
	Coordinate topLeft;
	Coordinate topRight;
	Coordinate botLeft;
	Coordinate botright;
	public buildingType type;
	public String name;
	public double PaymentFund;
	
	public enum buildingType{bank, restaurant, market, housingComplex};
	
	protected Building(){
		
	}

	public buildingType getType() {
		return type;
	}

	public void setType(buildingType type) {
		this.type = type;
	}
	
	public void setPaymentFund(double fund)
	{
		this.PaymentFund = fund;
	}
	
	public void setEntrance(int x, int y) {
		Coordinate c = new Coordinate(x,y);
		entrance = c;
	}
	
	public boolean lowOnFunds()
	{
		if (PaymentFund < 1000)
		{
			return true;
		}
		else return false;
	}
	
	public void addFunds(double amount)
	{
		PaymentFund += amount;
	}
	
}
