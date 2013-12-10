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
		EmployBank(c);
		EmployBank(c);
		EmployRest(c);
		EmployRest(c);
	}

	public void EmployBank(CityPanel c) {

		c.addWorker("Bank Host", "Bank Host", "Average");
		c.addWorker("Teller 1", "Teller", "Average");
		c.addWorker("Teller 2", "Teller", "Average");
		c.addWorker("Teller 3", "Teller", "Average");

	}


	public void EmployRest(CityPanel c) {
		c.addWorker("Host 1", "Restaurant Host", "Average");
		c.addWorker("W 1", "Waiter", "Average");
		c.addWorker("W 2", "Waiter", "Average");
		c.addWorker("W 3", "Waiter", "Average");
		c.addWorker("Cashier 1", "Cashier", "Average");
		c.addWorker("Chef 1", "Cook", "Average");
	}

	public void workShift(){
		TimeManager.getInstance().setDivider(20);
		TimeManager.getInstance().setOffset(300000);
	}

	public void CallScenarioA(CityPanel c){				// for points 1-4
		// 	A:
		//	All workplaces (markets, all restaurants, banks) fully employed.
		//	Day starts and all workers go to work.
		//	One not-working person eats at home, then visits all the workplaces by walking.
		//	Roads should have appropriate complexity [e.g. intersections with stop signs and/or signals]

		workShift();
		fillWork(c);

		PersonAgent p = new PersonAgent("Scen A", "None", "Wealthy");
		p.addItem("Juice", 0, 2, 2);
		c.addPerson(p);
		p.setHungry();
		p.setBus(false);
	}

	public void CallScenarioB(CityPanel c){
		//	All workplaces fully employed.
		//	Day starts and all workers go to work.
		//	Three not-working persons eat at home, then visit all the workplaces in different orders. 
		//	[one should walk; one should take a car; one should take a bus.]

		workShift();
		fillWork(c);

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
	public void CallScenarioR(CityPanel c){
		//	Weekend behavior is different
		//	Some workplaces are closed on the weekend.
		//	Show that people naturally avoid them and seek alternatives.

		//starts on sat:
		TimeManager.getInstance().setDivider(2);
		TimeManager.getInstance().setDayOffset(5);

		//starts on fri:
		//	TimeManager.getInstance().setDivider(1);
		//	TimeManager.getInstance().setOffset(50000);
		//	TimeManager.getInstance().setDayOffset(4);


		fillWork(c);

		PersonAgent p2 = new PersonAgent("No Job 1", "None", "Wealthy");
		PersonAgent p3 = new PersonAgent("No Job 2", "None", "Poor");
		PersonAgent p4 = new PersonAgent("No Job 3", "None", "Average");

		c.addPerson(p2);
		c.addPerson(p3);
		c.addPerson(p4);

	}

	public void CallScenarioO(CityPanel c){ // scen o, robbery
		//	An evil person decides to rob a bank.
		//	On entrance he ... (you design it)
		workShift();
		EmployBank(c);
		PersonAgent p2 = new PersonAgent("Robber", "Crook", "Average");
		c.addPerson(p2);
	}


	public void CallScenarioTest(CityPanel c) {

		workShift();
		EmployRest(c);
		EmployRest(c);
	}




	public void CallScenarioF(CityPanel c) {
		//	Bring each workplace down, one by one.
		//	Show how one not-working person still visits all the workplaces but not the ones that are down. 
		//	Say you only have one bank and it is down, the person should avoid all banking behavior.
		workShift();
		fillWork(c);
	}

	public void CallScenarioJ(CityPanel c) {
		//	Fully employed workplaces [enough people to fill all the workplace roles].
		//	Fully populated city [enough people to be wandering around doing interesting things]. At least 50 people.
		//	All restaurants integrated.
		//	At least 2 instances of other workspaces (your decision as to how many more you want).
		//	People make decisions to eat at home.
		//	People make decisions to eat at particular restaurants.
		//	Enough vehicle traffic to show stopping/starting at intersections.
		//	Vehicles stop for pedestrians with right of way.

		workShift();
		fillWork(c);
	}


	public void CallScenarioRubric(CityPanel cityPanel) {
		//STUB 
		System.out.println("Testing button");
	}


}