package roles;

public abstract class Building {

	public Coordinate entrance;
	Coordinate topLeft;
	Coordinate topRight;
	Coordinate botLeft;
	Coordinate botright;
	public buildingType type;
	public String name;
	public double PaymentFund;

	public Object panel;
	public String owner = "None";
	
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

	public abstract Boolean isOpen();
	
}
