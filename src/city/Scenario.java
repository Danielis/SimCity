package city;

import city.guis.CityGui;
import city.guis.CityPanel;


public class Scenario {
	
	enum Day{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
	
	private static Scenario instance = null;

	Scenario(){
	}

	
	public static synchronized Scenario getInstance() {
        if (instance == null) {
                instance = new Scenario();
        }
        return instance;
	}
	
public void CallScenario1(CityPanel c){
		c.addPerson("Bank Host", "Bank Host", "Average");
		c.addPerson("Teller 1", "Teller", "Average");
		c.addPerson("Teller 2", "Teller", "Wealthy");
		c.addPerson("Teller 3", "Teller", "Poor");
		c.addPerson("Chef", "Cook", "Average");
		c.addPerson("Waiter 1", "Waiter", "Average");
		c.addPerson("Waiter 2", "Waiter", "Wealthy");
		c.addPerson("Cashier", "Cashier", "Average");
		c.addPerson("Restaurant Host", "Restaurant Host", "Average");
		c.addPerson("Jobless Joe", "None", "Average");
		c.addPerson("Jobless Smith", "None", "Wealthy");
		c.addPerson("Jobless Smith", "None", "Wealthy");
		c.addPerson("Jobless Betty", "None", "Wealthy");
		c.addPerson("Jobless Alex", "None", "Average");
		c.addPerson("Jobless Alex", "None", "Poor");
		c.addPerson("Jobless Alex", "None", "Average");
		c.addPerson("Jobless Alex", "None", "Poor");
		c.addPerson("Jobless Alex", "None", "Average");
		c.addPerson("Jobless Sally", "None", "Wealthy");
	}
}