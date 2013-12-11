package city;

import java.util.TimerTask;
import java.util.Timer;
import java.util.TimerTask;

import city.PersonAgent.Job;
import city.PersonAgent.JobType;
import city.guis.CityGui;
import city.*;
import city.guis.CityPanel;

import java.util.Timer;

import roles.Building;
import roles.Building.buildingType;

public class Scenario {

	enum Day{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
	Timer timer = new Timer();
	
	CityPanel cp;
	int numStudentsArrived = 0;

	private static Scenario instance = null;

	Scenario(){
	}


	public static synchronized Scenario getInstance() {
		if (instance == null) {
			instance = new Scenario();
		}
		return instance;
	}

//	public void fillWork(CityPanel c){
//		c.addWorker("Bank Host", "Bank Host", "Average");
//		c.addWorker("Teller 1", "Teller", "Average");
//		c.addWorker("Teller 2", "Teller", "Average");
//		c.addWorker("Teller 3", "Teller", "Average");
//
//		c.addWorker("Bank Host", "Bank Host", "Average");
//		c.addWorker("Teller 1", "Teller", "Average");
//		c.addWorker("Teller 2", "Teller", "Average");
//		c.addWorker("Teller 3", "Teller", "Average");
//
//		//for restaurant: need to repeat for all restaurants
//		c.addWorker("Chef", "Cook", "Average");
//		c.addWorker("Waiter 1", "Waiter", "Average");
//		c.addWorker("Waiter 2", "Waiter", "Average");
//		c.addWorker("Cashier", "Cashier", "Average");
//		c.addWorker("Restaurant Host", "Restaurant Host", "Average");
//	}
//
//
//	public void CallScenarioA(CityPanel c){				// for points 1-4
//
//		fillWork(c);
//		TimeManager.getInstance().setDivider(20);
//		TimeManager.getInstance().setOffset(300000);
//		PersonAgent p = new PersonAgent("Scen A", "None", "Wealthy");
//		c.addPerson(p);
//
//		p.setHungry();
//		p.setBus(false);
//	}
//
//	public void CallScenarioB(CityPanel c){
//
//		fillWork(c);
//		TimeManager.getInstance().setDivider(20);
//		TimeManager.getInstance().setOffset(300000);
//
//		PersonAgent p = new PersonAgent("Driver", "None", "Wealthy");
//		p.GiveCar();
//		p.addItem("Juice", 0, 2, 2);
//		p.setBus(false);
//		c.addPerson(p);
//
//		PersonAgent p2 = new PersonAgent("Busser", "None", "Average");
//		p2.setBus(true);
//		c.addPerson(p2);
//
//		PersonAgent p3 = new PersonAgent("Walker", "None", "Poor");
//		p3.setBus(false);
//		p3.setHungry();
//		c.addPerson(p3);
//
//	}
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
//
//	public void CallScenario1(CityPanel c){ // scen o, robbery
//		TimeManager.getInstance().setDivider(20);
//		TimeManager.getInstance().setOffset(300000);
//		c.addWorker("Bank Host", "Bank Host", "Average");
//		c.addWorker("Teller 1", "Teller", "Average");
//		c.addWorker("Teller 2", "Teller", "Average");
//		c.addWorker("Teller 3", "Teller", "Average");
//
//		PersonAgent p2 = new PersonAgent("Robber", "Crook", "Average");
//		c.addPerson(p2);
//
//	}
//
//
//	public void CallScenarioTest(CityPanel c) {
//
//		TimeManager.getInstance().setDivider(20);
//		TimeManager.getInstance().setOffset(300000);
//		c.addWorker("Host", "Restaurant Host", "Average");
//		c.addWorker("W 1", "Waiter", "Average");
//		c.addWorker("W 2", "Waiter", "Average");
//		c.addWorker("W 3", "Waiter", "Average");
//		c.addWorker("Cashier", "Cashier", "Average");
//		c.addWorker("Chef", "Cook", "Average");
//	}

	public void collisionTest(CityPanel c){
//		c.createBusSystem();
//		
		c.addPerson("Person1", "No AI", "Wealthy");
		PersonAgent p1= c.people.lastElement();
		p1.setPosition(100, 165);
		p1.addItem("Car",1);
		
		
		c.addPerson("Person4", "No AI", "Wealthy");
		PersonAgent p4= c.people.lastElement();
		p4.setPosition(100, 137);
		p4.addItem("Car",1);
		
		c.addPerson("Person2", "No AI", "Wealthy");
		PersonAgent p2= c.people.lastElement();
		p2.setPosition(100,530);
		p2.addItem("Car",1);
		
		c.addPerson("Person3", "No AI", "Wealthy");
		PersonAgent p3= c.people.lastElement();
		p3.setPosition(100,90);

		c.addPerson("Person5", "No AI", "Wealthy");
		PersonAgent p5= c.people.lastElement();
		p5.setPosition(300,100);
		p5.addItem("Car",1);
		
		c.addPerson("Person6", "No AI", "Wealthy");
		PersonAgent p6= c.people.lastElement();
		p6.setPosition(100,450);
		

		p1.msgGoToMarket("Car", 1);
		p4.msgGoToMarket("Car", 1);
//		
		p2.msgGoToHome("Sleep");
		p3.msgGoToMarket("Car", 1);
//			
		p6.msgGoToMarket("Car",1);
	}

	public void fillWork(CityPanel c){
		EmployBank(c);
		EmployBank(c);
		EmployRest(c);
		EmployRest(c);
		EmployHousing(c);
	}
	
	public void oneBuildEmployed(CityPanel c){
		EmployBank(c);
		EmployRest(c);
		EmployHousing(c);
	}

	public void EmployBank(CityPanel c) {

		c.addWorker("Bank Host", "Bank Host", "Average");
		c.addWorker("Teller 1", "Teller", "Average");
		c.addWorker("Teller 2", "Teller", "Average");
		c.addWorker("Teller 3", "Teller", "Average");
	}

	public void EmployHousing(CityPanel c) {

		c.addWorker("Landlord", "Landlord", "Average");
		c.addWorker("Repairman 1", "Repairman", "Average");
		c.addWorker("Repairman 2", "Repairman", "Average");
		c.addWorker("Repairman 3", "Repairman", "Average");
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
	
	public void setDanielRestClosed(CityPanel c){
		for (Building b : c.buildings){
			if (b.type.equals(buildingType.restaurant) && b.owner.equals("Daniel")){
				b.ForceClosed();
			}
		}
	}

	public void CallScenarioA(CityPanel c){				// for points 1-4
		// 	A:
		//	All workplaces (markets, all restaurants, banks) fully employed.
		//	Day starts and all workers go to work.
		//	One not-working person eats at home, then visits all the workplaces by walking.
		//	Roads should have appropriate complexity [e.g. intersections with stop signs and/or signals]
		
		workShift();
		setDanielRestClosed(c);
		oneBuildEmployed(c);
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
		setDanielRestClosed(c);
		oneBuildEmployed(c);

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
		TimeManager.getInstance().setOffset(50000);

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
		setDanielRestClosed(c);
		EmployBank(c);
		PersonAgent p2 = new PersonAgent("Robber", "Crook", "Average");
		c.addPerson(p2);
	}


	public void CallScenarioTest(CityPanel c) {

		workShift();
		setDanielRestClosed(c);
		EmployRest(c);
		EmployRest(c);
	}




	public void CallScenarioF(CityPanel c) {
		//	Bring each workplace down, one by one.
		//	Show how one not-working person still visits all the workplaces but not the ones that are down. 
		//	Say you only have one bank and it is down, the person should avoid all banking behavior.
		workShift();
		setDanielRestClosed(c);
		oneBuildEmployed(c);
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
		setDanielRestClosed(c);
		fillWork(c);
		
		PersonAgent p2 = new PersonAgent("No Job 1", "None", "Wealthy");
		PersonAgent p3 = new PersonAgent("No Job 2", "None", "Poor");
		PersonAgent p4 = new PersonAgent("No Job 3", "None", "Average");
		PersonAgent p5 = new PersonAgent("Robber", "Crook", "Average");
		PersonAgent p6 = new PersonAgent("Chris", "None", "Wealthy");
		PersonAgent p7 = new PersonAgent("No Job 6", "None", "Poor");
		

		p5.setHungry();
		p4.setHungry();

		c.addPerson(p2);
		c.addPerson(p3);
		c.addPerson(p4);
		c.addPerson(p5);
		c.addPerson(p6);
		c.addPerson(p7);
		
		PersonAgent p = new PersonAgent("Norman", "None", "Wealthy");
		p.GiveCar();
		p.addItem("Juice", 0, 2, 2);
		c.addPerson(p);

		PersonAgent p8 = new PersonAgent("Aleena", "None", "Average");
		c.addPerson(p8);

		PersonAgent p9 = new PersonAgent("Daniel", "None", "Wealthy");
		p9.GiveCar();
		p9.setHungry();
		c.addPerson(p9);
	}

	public void setCityPanel(CityPanel c)
	{
		this.cp = c;
	}

	public void CallScenarioRubric(CityPanel c) {
		System.out.println("Where's the rubric...?");
		
		workShift();
		setDanielRestClosed(c);
		
		cp = c;

		PersonAgent prof = new PersonAgent("Wilczynski", "Professor", "Average");
		c.addPerson(prof, true);
		/*
		timer.schedule( new TimerTask()
		{
			public void run()
			{				
				
			}
		}, 7 * 1000);*/
	}
	
	public void fillStudents(int numstudents)
	{
		
		for (int i = 0; i < numstudents; i++)
		{
			PersonAgent p = new PersonAgent("Student", "Student", "Average");
			cp.addPerson(p, false);
		}
	}
	
	public boolean AllThere()
	{
		return (numStudentsArrived >= 20);
	}
	
	public void Continue1()
	{
		numStudentsArrived++;
		if (AllThere())
		{
			HeartAttack();
		}
	}
	
	public void HeartAttack()
	{
		for (PersonAgent p: cp.people)
		{
			if (p.job != null)
			{
				if(p.job.type == JobType.professor)
				{
					p.msgHaveHeartAttack();
				}
			}
		}
	}
	
	public void finish()
	{
		for (PersonAgent p: cp.people)
		{
			if (p.job != null)
			{
				if(p.job.type == JobType.student)
				{
					p.msgGoBotherTheCPs();
				}
			}
		}
	}
}