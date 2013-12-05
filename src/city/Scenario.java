package city;

import city.guis.CityGui;
import city.*;
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

public void fillWork(CityPanel c){
	c.addWorker("Bank Host", "Bank Host", "Average");
	c.addWorker("Teller 1", "Teller", "Average");
	c.addWorker("Teller 2", "Teller", "Average");
	c.addWorker("Teller 3", "Teller", "Average");
	
	c.addWorker("Bank Host", "Bank Host", "Average");
	c.addWorker("Teller 1", "Teller", "Average");
	c.addWorker("Teller 2", "Teller", "Average");
	c.addWorker("Teller 3", "Teller", "Average");
	
	//for restaurant: need to repeat for all restaurants
	c.addWorker("Chef", "Cook", "Average");
	c.addWorker("Waiter 1", "Waiter", "Average");
	c.addWorker("Waiter 2", "Waiter", "Average");
	c.addWorker("Cashier", "Cashier", "Average");
	c.addWorker("Restaurant Host", "Restaurant Host", "Average");
}


public void CallScenarioA(CityPanel c){				// for points 1-4
	fillWork(c);
	//TimeManager.getInstance().setDivider(20);
	//TimeManager.getInstance().setOffset(300000);
	PersonAgent p = new PersonAgent("Scen A", "None", "Wealthy");
	c.addPerson(p);
	
	p.setHungry();
	p.setBus(false);
}

public void CallScenarioB(CityPanel c){
	fillWork(c);
	//TimeManager.getInstance().setDivider(20);
	//TimeManager.getInstance().setOffset(300000);
	
	PersonAgent p = new PersonAgent("Driver", "None", "Wealthy");
	p.GiveCar();
	p.addItem("Juice", 0, 2, 2);
	p.setBus(false);
	c.addPerson(p);
	
	PersonAgent p2 = new PersonAgent("Busser", "None", "Average");
	p2.setBus(true);
	c.addPerson(p2);
	
	PersonAgent p3 = new PersonAgent("Walker", "None", "Poor");
	p3.setBus(false);
	p3.setHungry();
	c.addPerson(p3);
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