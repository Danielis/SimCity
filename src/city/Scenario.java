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
		c.addPerson("Bank Host", "Bank Host", "Wealthy");
		c.addPerson("Teller 1", "Teller", "Average");
		c.addPerson("Teller 2", "Teller", "Poor");
		c.addPerson("Teller 3", "Teller", "Poor");
		c.addPerson("Chef", "Cook", "Average");
		c.addPerson("Waiter 1", "Waiter", "Average");
		c.addPerson("Waiter 2", "Waiter", "Average");
		c.addPerson("Cashier", "Cashier", "Poor");
		c.addPerson("Restaurant Host", "Restaurant Host", "Wealthy");
		c.addPerson("Jobless Joe", "None", "Average");
	}
}