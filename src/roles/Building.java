package roles;

public class Building {

	Coordinate entrance;
	Coordinate topLeft;
	Coordinate topRight;
	Coordinate botLeft;
	Coordinate botright;
	public buildingType type;
	
	public enum buildingType{bank, restaurant, market, housingComplex};
	
	protected Building(){
		
	}

	public buildingType getType() {
		return type;
	}

	public void setType(buildingType type) {
		this.type = type;
	}
	
}
