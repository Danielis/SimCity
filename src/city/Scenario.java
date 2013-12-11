package city;

import java.util.TimerTask;

import city.guis.CityGui;
import city.*;
import city.guis.CityPanel;
import java.util.Timer;

public class Scenario {

	enum Day{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
	Timer timer = new Timer();
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
		TimeManager.getInstance().setDivider(20);
		TimeManager.getInstance().setOffset(300000);
		PersonAgent p = new PersonAgent("Scen A", "None", "Wealthy");
		c.addPerson(p);

		p.setHungry();
		p.setBus(false);
	}

	public void CallScenarioB(CityPanel c){

		fillWork(c);
		TimeManager.getInstance().setDivider(20);
		TimeManager.getInstance().setOffset(300000);

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
	public void CallScenarioP(CityPanel c){
		c.addWorker("Bank Host", "Bank Host", "Wealthy");
		c.addWorker("Teller 1", "Teller", "Poor");
		c.addWorker("Teller 2", "Teller", "Poor");
		c.addWorker("Teller 3", "Teller", "Average");

		c.addWorker("Bank Host", "Bank Host", "Average");
		c.addWorker("Teller 1", "Teller", "Average");
		c.addWorker("Teller 2", "Teller", "Wealthy");
		c.addWorker("Teller 3", "Teller", "Poor");

		//for restaurant: need to repeat for all restaurants
		c.addWorker("Chef", "Cook", "Average");
		c.addWorker("Waiter 1", "Waiter", "Average");
		c.addWorker("Waiter 2", "Waiter", "Average");
		c.addWorker("Cashier", "Cashier", "Average");
		c.addWorker("Restaurant Host", "Restaurant Host", "Wealthy");


		TimeManager.getInstance().setDivider(30);
		TimeManager.getInstance().setOffset(70420000);

		PersonAgent p2 = new PersonAgent("No Job 1", "None", "Wealthy");
		PersonAgent p3 = new PersonAgent("No Job 2", "None", "Poor");
		PersonAgent p4 = new PersonAgent("No Job 3", "None", "Average");

		c.addPerson(p2);
		c.addPerson(p3);
		c.addPerson(p4);

	}

	public void CallScenario1(CityPanel c){ // scen o, robbery
		TimeManager.getInstance().setDivider(20);
		TimeManager.getInstance().setOffset(300000);
		c.addWorker("Bank Host", "Bank Host", "Average");
		c.addWorker("Teller 1", "Teller", "Average");
		c.addWorker("Teller 2", "Teller", "Average");
		c.addWorker("Teller 3", "Teller", "Average");

		PersonAgent p2 = new PersonAgent("Robber", "Crook", "Average");
		c.addPerson(p2);

	}


	public void CallScenarioTest(CityPanel c) {

		TimeManager.getInstance().setDivider(20);
		TimeManager.getInstance().setOffset(300000);
		c.addWorker("Host", "Restaurant Host", "Average");
		c.addWorker("W 1", "Waiter", "Average");
		c.addWorker("W 2", "Waiter", "Average");
		c.addWorker("W 3", "Waiter", "Average");
		c.addWorker("Cashier", "Cashier", "Average");
		c.addWorker("Chef", "Cook", "Average");
	}

	public void collisionTest(CityPanel c){
//		c.createBusSystem();
//		
		c.addPerson("Person1", "No AI", "Wealthy");
		PersonAgent p1= c.people.lastElement();
		p1.setPosition(100, 100);
		p1.addItem("Car",1);
		
//		c.addPerson("Person4", "No AI", "Wealthy");
//		PersonAgent p4= c.people.lastElement();
//		p4.setPosition(110, 100);
//		p4.addItem("Car",1);
//		
//		c.addPerson("Person2", "No AI", "Wealthy");
//		PersonAgent p2= c.people.lastElement();
//		p2.setPosition(441,250);
//		p2.addItem("Car",1);
//		
//		c.addPerson("Person2", "No AI", "Wealthy");
//		PersonAgent p3= c.people.lastElement();
//		p3.setPosition(400,425);


		p1.msgGoToMarket("Car", 1);
//		p4.msgGoToMarket("Car", 1);
//		
//		p2.msgGoToMarket("Car", 1);
//		p3.msgGoToMarket("Car", 1);
//			
		//p2.getGui().DoGoToLocation(100, 100);
	}
}