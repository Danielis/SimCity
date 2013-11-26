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
		c.addPerson("Teller1", "Teller", "Average");
		c.addPerson("Teller2", "Teller", "Average");
		c.addPerson("Teller3", "Teller", "Average");
		c.addPerson("Jobless Joe", "None", "Average");
	}
}