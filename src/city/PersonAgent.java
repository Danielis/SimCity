package city;

//Package Imports
import java.awt.Point;
import java.lang.Math;

import restaurantA.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import restaurant.Restaurant;
import restaurant.CookAgent.state;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import restaurant.roles.*;
import agent.Agent;
import bank.BankCustomerRole;
import restaurant.gui.CustomerGui;
import city.TimeManager.Day;
import city.Interfaces.Person;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import bank.Bank;
import city.BankDatabase.*;
import bank.BankHostRole;
import bank.interfaces.*;
import roles.Apartment;
import roles.Building;
import roles.Building.buildingType;
import roles.Role;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import market.*;
import market.interfaces.MarketCustomer;
import transportation.BusStopAgent; // needed for BusStop variable
import housing.HousingCustomerRole;
import housing.interfaces.HousingCustomer;
import bank.*;
import transportation.TransportationCompanyAgent;
import roles.Coordinate;



//Utility Imports
import java.util.*;
import java.util.concurrent.Semaphore;

public class PersonAgent extends Agent implements Person
{	
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/
 
	 //Lists: Roles, Restaurants
	public List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	public Vector<Building> buildings = new Vector<Building>(); //City Gui won't let me implement Lists

	//Inventory List
	public List<Item> inventory = Collections.synchronizedList(new ArrayList<Item>());
	
	public TrackerGui trackingWindow;

	//Variable
	PersonGui gui = null;
	public double cash = 500;
	String name;
	public PersonStatus Status = new PersonStatus();
	public Semaphore animSemaphore = new Semaphore(0, true);
	public Semaphore busSemaphore = new Semaphore(0, true);
	List <Account> accounts = new ArrayList<Account>();
	List <Loan> loans = new ArrayList<Loan>();
	List <PersonAgent> people = new ArrayList<PersonAgent>();
	public CityAnimationPanel CityAnimPanel;
	Timer timer = new Timer();
	public int waiterindex = 0;
	public int waiterindexA = 0;
	public long timeSinceLastAte;
	String bankPurpose, marketPurpose, homePurpose;
	double marketQuantity, bankAmount;
	
	public boolean changeBubble = false;
	public double bubbleValue = 0;
	public int bubbleIndex = 0;

	public class Job{
		public JobType type;
		public Coordinate location;
		int timeStart = 8;
		static final int timeEnd = 20;
		public Building workBuilding;
		List <Day> daysWorking = new ArrayList<Day>();
		private Day dayLastRobbed = Day.friday;
		
		public Job(JobType parseJob) {
			type = parseJob;
			//if (type == JobType.marketWorker || type == JobType.marketHost){
			//	timeStart = 8;
			//	timeEnd
			//}
			if (type == JobType.bankHost){
				//timeStart = 8; 
				//timeEnd = 4;
				for (Building b : buildings){
					if (workBuilding == null && b.type == buildingType.bank){
						Bank ba = (Bank) b;
						if (ba.needsHost()){
							ba.sethost();
							workBuilding = b;
						}
					}
				}
			}
			else if (type == JobType.restHost){
				for (Building b : buildings){
					if (workBuilding == null && b.type == buildingType.restaurant){
						RestBase ba = (RestBase) b;
						if (ba.needsHost()){
							print("set h");
							ba.sethost();
							workBuilding = b;
							print("owner: " + b.owner);
						}
					}
				}
				
			}
			else if (type == JobType.cook){
				for (Building b : buildings){
					if (workBuilding == null && b.type == buildingType.restaurant){
						RestBase ba = (RestBase) b;
						if (ba.needsCook()){
							ba.setCook();

							print("set c");
							workBuilding = b;
						}
					}
				}
			}
			else if (type == JobType.cashier){
				for (Building b : buildings){
					if (workBuilding == null && b.type == buildingType.restaurant){
						RestBase ba = (RestBase) b;
						if (ba.needsCashier()){
							ba.setCashier();

							print("set cas");
							workBuilding = b;
						}
					}
				}
			}
			else if (type == JobType.waiter){
				for (Building b : buildings){
					if (workBuilding == null && b.type == buildingType.restaurant){
						RestBase ba = (RestBase) b;
						if (ba.needsWaiter()){
							ba.setWaiter();

							print("set w");
							workBuilding = b;
						}
					}
				}
			}
			else if (type == JobType.teller || type == JobType.waiter){
				for (Building b : buildings){
					if (workBuilding == null && b.type == buildingType.bank){
						Bank ba = (Bank) b;
						if (ba.needsTeller()){
							ba.setTeller();
							workBuilding = b;
						}
					}
				}
			}
			else if (type == JobType.landLord || type == JobType.repairman){
				//timeStart = 8;
				//timeEnd = 4;
			}
			
			else if(type == JobType.student)
			{
				workBuilding = null;
			}
			
			assignWorkDay(parseJob);
		}
		
		
		
		public void assignWorkDay(JobType parseJob) {
			// so if all buildings are full employed 
			// they wont get stuck trying to go to work
			if(workBuilding != null){			
				daysWorking.add(Day.monday);
				daysWorking.add(Day.tuesday);
				daysWorking.add(Day.wednesday);
				daysWorking.add(Day.thursday);
				daysWorking.add(Day.friday);
			
				print("build" + workBuilding.type.toString());
				
				print("owner" + workBuilding.owner);
				if (workBuilding.type.equals(buildingType.restaurant)){
					if (workBuilding.owner.equals("Norman") ||
						workBuilding.owner.equals("Daniel")){
					daysWorking.add(Day.saturday);
					daysWorking.add(Day.sunday);
					}
				}
			
				else if (!workBuilding.type.equals(buildingType.bank)){ //banks closed on weekends
					daysWorking.add(Day.saturday);
					daysWorking.add(Day.sunday);
				}
			
			}
			
		}
		
	}
	
	public enum JobType {noAI, none, marketWorker, marketHost, bankHost, teller, 
		restHost, cook, cashier, waiter, landLord, repairman, crook, student, professor}
	public Job job;
	
	public enum WealthLevel {average, wealthy, poor}
	
	
	public WealthLevel wealthLevel;
	
	BusStopAgent destinationStop;//trans: added
	BusStopAgent curStop;//trans: addded
	
	TransportationCompanyAgent metro;
	double timeSinceLastSlept;
	public Boolean noAI;
	
	public PersonAgent(String name, String j, String wealth, Vector<Building> build){
		this.name = name;
		buildings = build;
		job = new Job(parseJob(j));
		wealthLevel = parseWealth(wealth);
		cash = setWealth();
		System.out.println("Added person " + name + " with job type " + job.type + " and wealth level: " + wealthLevel);
		double time = TimeManager.getInstance().getCurrentSimTime();
		int num = (int)(Math.random() * ((30000 - 10000) + 10000));
		timeSinceLastAte = TimeManager.getInstance().getCurrentSimTime() - num; // sets random time for ate, before added
		int num2 = (int)(Math.random() * ((30000 - 10000) + 10000));
		timeSinceLastSlept = TimeManager.getInstance().getCurrentSimTime() - num2; // sets random time for ate, before added
		
	}	

	public PersonAgent(String name, String j, String wealth){
		this.name = name;
		job = new Job(parseJob(j));
		wealthLevel = parseWealth(wealth);
		cash = setWealth();
		System.out.println("Added person " + name + " with job type " + job.type + " and wealth level: " + wealthLevel);
		double time = TimeManager.getInstance().getCurrentSimTime();
		int num = (int)(Math.random() * ((30000 - 10000) + 10000));
		timeSinceLastAte = TimeManager.getInstance().getCurrentSimTime() - num; // sets random time for ate, before added
		int num2 = (int)(Math.random() * ((30000 - 10000) + 10000));
		timeSinceLastSlept = TimeManager.getInstance().getCurrentSimTime() - num2; // sets random time for ate, before added
		
	}	
	
	public double setWealth() {
		if (wealthLevel.equals(WealthLevel.average)){
			addItem("Car", 0, 1, 1);
			addItem("Steak", 5, 3, 5);
			addItem("Pasta", 5, 3, 5);
			addItem("Milk", 1, 1, 1);
			return 15000;
		}
		else if (wealthLevel.equals(WealthLevel.wealthy)){
			addItem("Car", 0, 1, 1);
			addItem("Steak", 8, 5, 8);
			addItem("Chicken", 8, 5, 8);
			addItem("Milk", 2, 1, 2);
			return 50000;
		}
		else if (wealthLevel.equals(WealthLevel.poor)){
			addItem("Car", 0, 1, 1);
			addItem("Eggs", 3, 3, 5);
			addItem("Pasta", 3, 3, 3);
			addItem("Milk", 0, 1, 1);
			return 2000;
		}
		else
			return 35000;
	}

	public double getCashThresholdUp(){
		if (wealthLevel == WealthLevel.average)
			return 50;
		else if (wealthLevel == WealthLevel.wealthy)
			return 100;
		else if (wealthLevel == WealthLevel.poor)
			return 40;
		return 0;
	}
	
	public double getCashThresholdLow(){
		if (wealthLevel == WealthLevel.average)
			return 20;
		else if (wealthLevel == WealthLevel.wealthy)
			return 50;
		else if (wealthLevel == WealthLevel.poor)
			return 5;
		return 0;
	}
	
	public double getMoneyThreshold(){
		if (wealthLevel == WealthLevel.average)
			return 1000;
		else if (wealthLevel == WealthLevel.wealthy)
			return 1000;
		else if (wealthLevel == WealthLevel.poor)
			return 1000;
		return 0;
	}

	public WealthLevel parseWealth(String wealth) {
		if (wealth.equals("Average"))
			return wealthLevel.average;
		else if (wealth.equals("Wealthy"))
			return wealthLevel.wealthy;
		else if (wealth.equals("Poor"))
			return wealthLevel.poor;
		else
			return null;
	}

	//Class Declarations
	public JobType parseJob(String job) {
	
	if (job.equals("No AI"))
		return JobType.noAI;	
	else if (job.equals("None"))
		return JobType.none;
	else if (job.equals("Market Worker"))
		return JobType.marketWorker;
	else if (job.equals("Market Host"))
		return JobType.marketHost;
	else if (job.equals("Bank Host"))
		return JobType.bankHost;
	else if (job.equals("Teller"))
		return JobType.teller;
	else if (job.equals("Restaurant Host"))
		return JobType.restHost;
	else if (job.equals("Cook"))
		return JobType.cook;
	else if (job.equals("Cashier"))
		return JobType.cashier;
	else if (job.equals("Waiter"))
		return JobType.waiter;
	else if (job.equals("Landlord"))
		return JobType.landLord;
	else if (job.equals("Repairman"))
		return JobType.repairman;
	else if (job.equals("Crook")){
		cash = 0;
		return JobType.crook;
	}
	else if (job.equals("Student"))
		return JobType.student;
	else if (job.equals("Professor"))
		return JobType.professor;
	else
		return null;
	}

	/*****************************************************************************
										CLASSES
	 ******************************************************************************/
	public class PersonStatus
	{
		nourishment nour;
		location loc;
		destination des;
		workStatus work;
		bankStatus bank;
		houseStatus house;
		marketStatus market;
		transportStatus trans;
		morality moral;    
		//will require constructor and set and gets for the components

		PersonStatus()
		{
			nour = nourishment.notHungry;
			loc = location.outside;
			des = destination.outside;
			work = workStatus.notWorking;
			bank = bankStatus.nothing;
			house = houseStatus.notHome;
			market = marketStatus.nothing;
			trans = transportStatus.nothing;
			moral = morality.good;
		}
		
		public void setWorkStatus(workStatus state) {
			work = state;
		}

		public void setNourishment(nourishment state)
		{
			nour = state;
		}

		public nourishment getNourishmnet()
		{
			return nour;
		}
		public void setTransportationStatus(transportStatus state){
			trans = state;
		}
		public transportStatus getTransportationStatus(){
			return trans;
		}
		public void setMoneyStatus(bankStatus state)
		{
			bank = state;
		}

		public bankStatus getMoneyStatus()
		{
			return bank;
		}

		public void setHousingStatus(houseStatus state)
		{
			house = state;
		}
		
		public houseStatus getHousingStatus()
		{
			return house;
		}
		
		public void setLocation(location state)
		{
			loc = state;
		}

		public location getLocation()
		{
			return loc;
		}

		public void setDestination(destination state)
		{
			des = state;
		}

		public destination getDestination()
		{
			return des;
		}

		public workStatus getWork() {
			return work;
		}

		
	}

	public class Item {
		String type;
		int quantity;
		int threshold;
		int capacity;
		
		public Item(String t, int q, int th, int cap) {
			type = t;
			quantity = q;
			threshold = th;
			capacity = cap;
		}
		
	public void setThreshold(int q){
			threshold = q;
		}
	public String getType() {
		return type;
	}
	public int getQuantity() {
		return quantity;
	}
	public void removeItem() {
		quantity--;
	}
	
		
	}
	
	public void addItem(String item, int q){
		for (Item i : inventory){
			if (i.type.equals(item)){
				i.quantity += q;
				print("I now have " + i.quantity + " " + i.type);
				return;
			}
		}
		inventory.add(new Item(item, q, 0, 4));
		print("I now have " + q + " " + item);
	}
	
	public void addItem(String item, int q, int j, int cap) {
		for (Item i : inventory){
			if (i.type.equals(item)){
				i.quantity += q;
				print("I now have " + i.quantity + " " + i.type);
				return;
			}
		}
		inventory.add(new Item(item, q, j, cap));
		print("I now have " + q + " " + item);
		
	}
	
	public void removeItem(String item, int q){
		for (Item i : inventory){
			if (i.type.equals(item)){
				i.quantity -= q;
				print("I now have " + i.quantity + " " + i.type);
			}
		}
	}

	public void setItemQuantity(String item, int q){
		for (Item i : inventory){
			if (i.type.equals(item)){
				i.quantity -= q;
				print("I now have " + i.quantity + " " + i.type);
			}
		}
	}
	//Enum States
	enum nourishment	{notHungry,Hungry,goingToFood} // may not need goingToFood
	enum location		{outside,home,restaurant,bank,market,transportation,work}
	enum destination	{outside,home,restaurant,bank,market,transportation,work,Wilczynski}
	enum workStatus		{notWorking,working,onBreak,goingToWork}
	enum bankStatus		{nothing,withdraw,deposit,owe,goingToBank}
	enum houseStatus	{notHome,home,noHome,goingHome,needsToGo} //no home may be used for deadbeats
	enum marketStatus	{nothing,buying,waiting}
	enum transportStatus{nothing, walking,car,bus}
	enum morality		{good,bad} // may be used for theifs later on for non-norms
	//other potentials: rent, 


	/*****************************************************************************
									 UTILITIES
	 ******************************************************************************/
	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}
	
	public List<Item> getInvetory() {
		return inventory;
	}
	
	public int getBound_leftx()
	{
		return gui.getXPosition() - gui.imgTrainer.getWidth()/2;
	}
	
	public int getBound_rightx()
	{
		return gui.getXPosition() + gui.imgTrainer.getWidth()/2;
	}
	
	public int getBound_topy()
	{
		return gui.getYPosition() - gui.imgTrainer.getMinTileY()/2;
	}
	
	public int getBound_boty()
	{
		return gui.getYPosition() + gui.imgTrainer.getMinTileY()/2;
	}

	public void addRole(Role r)
	{
		roles.add(r);
		r.setPerson(this);
	}

	public void removeRole(Role r)
	{
		roles.remove(r);
	}

	public String getName(){
		return name;
	}

	public void setGui(PersonGui g)
	{
		this.gui = g;
	}

	public void setAnimationPanel(CityAnimationPanel panel) {
		CityAnimPanel = panel;
	}

	public PersonGui getGui() {
		return gui;
	}

	public boolean hasCar() {
		synchronized(inventory){
			for(Item i:inventory) {
				if(i.type.equals("Car") && i.quantity>0) {
					return true;
				}
			}
			return false;
		}
	}

	public void WaitForAnimation()
	{
		try
		{
			this.animSemaphore.acquire();	
		} catch (InterruptedException e) {
			// no action - expected when stopping or when deadline changed
		} catch (Exception e) {
			print("Unexpected exception caught in Agent thread:", e);
		}
	}

	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}
	
	public void WaitForBus() // trans: leaving in just in case will delete if bus integration doesn't use this, which it shouldn't
	{
		try
		{
			this.busSemaphore.acquire();	
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:", e);
        }
	}
	
	public void DoneWithBus()// trans: leaving in just in case will delete if bus integration doesn't use this, which it shouldn't
	{
		this.busSemaphore.release();
	}
	public void setMetro(TransportationCompanyAgent m){
		this.metro = m;
	}
	/** closestBusStop()
	 *  Will return the closest BusStopAgent to the Person so that they can travel to the destination stop determined by the action they are trying to do
	 */
	public BusStopAgent closestBusStop(){
		BusStopAgent B = metro.stops.get(0);
		double C = checkBusStopDistance(metro.stops.get(0));
		for(int i=1;i<metro.stops.size();i++){
			if(C > checkBusStopDistance(metro.stops.get(i))){
				C = checkBusStopDistance(metro.stops.get(i)); 
				B = metro.stops.get(i);
			}
		}
		return B;
	}
	public double checkBusStopDistance(BusStopAgent x){
		int A = Math.abs( this.getGui().getXPosition() - x.getGui().getXPosition());
		int B = Math.abs( this.getGui().getYPosition() - x.getGui().getYPosition());
		double C = Math.sqrt(A*A + B*B);
		return C;
	}
	/** ClosestCheckpoint() will move gui to closest checkpoint G,D,C,B
	 * 
	 */
	public void closestCheckpoint(){
		double C;
		char P = 'G';
		C = checkPointDistance(385,106); // Assign G
		//print("C is " + C + " My current check C is: " + checkPointDistance(385,278) + "  P is " + P);
		if ( C > checkPointDistance(385,278)){
			C = checkPointDistance(385,278);//assign D
			P = 'D';
		}
		//print("C is " + C + " My current check C is: " + checkPointDistance(385,362) + "  P is " + P);
		if ( C > checkPointDistance(385,362)){
			C = checkPointDistance(385,362);//assign C
		P = 'C';
		}
		//print("C is " + C + " My current check C is: " + checkPointDistance(385,474) + "  P is " + P);
		if ( C > checkPointDistance(385,474)){
			C = checkPointDistance(385,474);//assign B
			P = 'B';
		}
		//print("C is " + C + "  P is " + P);
		//print("Going To Point " + P);
		gui.DoGoToCheckpoint(P);
	}
	/** checkPointDistance(int x, int y) is mainly used in closestCheckPoint() to determine where a person should go to begin their journey somewhere
	 * 
	 */
	public double checkPointDistance(int x, int y){
		int A = Math.abs( this.getGui().getXPosition() - x);
		int B = Math.abs( this.getGui().getYPosition() - y);
		double C = Math.sqrt(A*A + B*B);
		return C;
	}
	// This next function is used in my TestPerson but will later be used to change the person's transportStatus from the Gui so that they
	// are as Aleena put it Lazy or Athletic, could be random at the start of creation or a click button like Hungry
	public void transportationStatusBus(){
		this.Status.setTransportationStatus(transportStatus.bus);
	}
	public void setBus(Boolean temp){
		if (temp)
			Status.setTransportationStatus(transportStatus.bus);
	}
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/

	public void msgWakeUp() {
		stateChanged();
	}
	
	public void msgGetPaid() {
		 for (Role r : roles){
			 if (r.active){
				 r.msgGetPaid();
			 }
		 }
	}

	public void msgLeaveWork() {
		for (Role r : roles){
			if (r.active){
				r.msgLeaveWork();
				
			}
		}
	}

	public void msgLeftWork(Role r, double balance) {
		print("Left work");
		cash += balance; //pay
	    r.setActivity(false);
	    gui.setBusy(false);
		roles.remove(r);
	    Status.setLocation(location.outside);
	    Status.setDestination(destination.outside);
	    Status.setHousingStatus(houseStatus.notHome);
	    Status.setWorkStatus(workStatus.notWorking);
	    gui.setPresent(true);
	    stateChanged() ;
	}
	
	public void msgLeavingHome(Role r, double balance){
		print("Left home");
		cash = balance;
	    r.setActivity(false);
	    gui.setBusy(false);
		roles.remove(r);
	    Status.setLocation(location.outside);
	    Status.setDestination(destination.outside);
	    Status.setHousingStatus(houseStatus.notHome);
	    gui.setPresent(true);
	    
	    if (homePurpose.equals("Cook")){
	    	timeSinceLastAte = TimeManager.getInstance().getCurrentSimTime();
	    }
	    if (homePurpose.equals("Sleep")){
	    	timeSinceLastSlept = TimeManager.getInstance().getCurrentSimTime();
	    }
	    for (Item i : inventory){
			print(i.getType() + " " + i.quantity);
		}
	    stateChanged();
	}
	
	public void msgGoToWork() {
		print("Called msgGoToWork");
		if (job != null && job.type != JobType.none && job.type != JobType.crook)
			Status.setDestination(destination.work);
		gui.setPresent(false);
		stateChanged();
	}
	
	//Special
	public void msgGoToWilczynski()
	{
		print("Asking for a rubric");
		Status.setLocation(location.outside);
		Status.setDestination(destination.Wilczynski);
		gui.setPresent(true);
		stateChanged();
	}

	//Housing
	public void msgGoToHome(String purpose){
		homePurpose = purpose;
	    Status.setHousingStatus(houseStatus.needsToGo);
	    Status.setDestination(destination.home);
	    gui.setPresent(true);
	    stateChanged();
	}

	//Restaurant
	public void msgGoToRestaurant(){ // sent from gui
		print("Called msgGoToRestaurant");
		Status.setNourishment(nourishment.Hungry);
		Status.setDestination(destination.restaurant);
		gui.setPresent(true);
		stateChanged();
	}

	public void msgLeavingRestaurant(Role r, float myMoney){
		cash = myMoney;
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		Status.setNourishment(nourishment.notHungry);
		gui.setPresent(true);
		this.Status.setLocation(location.outside);
		//roles.remove(r);
		timeSinceLastAte = TimeManager.getInstance().getCurrentSimTime();
		stateChanged();

	}
	public void msgLeavingRestaurant(Role r, double myMoney){
		cash = myMoney;
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		Status.setNourishment(nourishment.notHungry);
		gui.setPresent(true);
		this.Status.setLocation(location.outside);
		//roles.remove(r);
		timeSinceLastAte = TimeManager.getInstance().getCurrentSimTime();
		stateChanged();

	}

	public void msgGoToBank(String purpose, double amt)
	{
		bankPurpose = purpose;
		bankAmount = amt;
		print("Going to bank");
		Status.setDestination(destination.bank);
		Status.setMoneyStatus(bankStatus.withdraw);
		gui.setPresent(true);
		stateChanged();
	}

	public void msgLeavingBank(BankCustomerRole r, double balance) {
		print("Left bank.");
		cash = balance;
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside); 
		gui.setPresent(true);
		roles.remove(r);
		stateChanged();
	}
	
	public void msgLeaveRestA(CustomerAgent r, double money) {
		print("Left Aleena's Restaurant.");
		cash = money;
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside); 
		gui.setPresent(true);
		roles.remove(r);
		stateChanged();
	}
	
	public void msgNewAccount(BankCustomerRole bankCustomerRole, Account acct) {
		accounts.add(acct);
	}
	
	public void msgNewLoan(BankCustomerRole bankCustomerRole, Loan loan) {
		loans.add(loan);
	}
	//Transportation
	
	
	public void msgAtBusStop(){
		this.DoneWithBus(); // msgBusStopReached() should release agent to do other actions
	}
	/* removed since Person does not need to be told to go to stop just go to Restaurant or Market or so forth
	public void msgGoToStop(BusStopAgent curStop,BusStopAgent dest){
	 
		print("Person going to STOP");
		this.curStop = curStop;
		this.destinationStop = dest;
		this.Status.setTransportationStatus(transportStatus.goingToBusStop);
		this.goingToStop = true;
		stateChanged();
	}
	*/
	public void setDestinationStop(BusStopAgent P){
		this.destinationStop = P;
	}
	public BusStopAgent getDestinationBusStop(){
		return this.destinationStop;
	}
	
	public void setPosition(int X, int Y){
		gui.setPosition(X,Y);
	}
	
	public void msgGoToMarket(String purpose, double quantity)
	{
		marketPurpose = purpose;
		marketQuantity = quantity;
		print("Going to market");
		Status.setDestination(destination.market);
		Status.market = marketStatus.buying;
	    gui.setPresent(true);
	    stateChanged();
	}
	
	public void msgLeavingMarket(MarketCustomerRole r, double balance, String item, int quantRec) {
		print("Left market.");
		cash = balance;
		addItem(item, quantRec);
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		gui.setPresent(true);
		gui.setBusy(false);
		roles.remove(r);
		stateChanged();
	}

	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/

	@Override

	public boolean pickAndExecuteAnAction() {
		
		if (changeBubble)
		{
			bubbleValue++;
			if (bubbleValue > 40)
			{
				Random rand = new Random();
				bubbleValue = 0;
				bubbleIndex = Math.abs(rand.nextInt() % 10);
			}
		}

		if (job.type == JobType.noAI){		

			if (Status.getWork() == workStatus.notWorking &&
					Status.getDestination() == destination.work) {
				GoToWork();
				return true;
			}
			if (Status.getNourishmnet() == nourishment.Hungry &&
					Status.getLocation() == location.outside && CheckRestaurantOpen()) {
				Building r = findOpenBuilding(buildingType.restaurant);
				GoToRestaurant(r);
				return true;
			}
			if (Status.getMoneyStatus() == bankStatus.withdraw &&
					Status.getDestination() == destination.bank && CheckBankOpen()) {
				GoToBank();
				return true;
			}
			if (Status.getDestination() == destination.market &&
					Status.market == marketStatus.buying && CheckMarketOpen()) {
				GoToMarket();
				return true;
			}
			if (Status.getHousingStatus() == houseStatus.needsToGo &&
					Status.getDestination() == destination.home){
				GoHomeToDoX();
				return true;
			}
		}
		
		
		if (AIandNotWorking()){	

			//print(" role no active, should be true" + noRoleActive());
			//print("is gui busy, should be false" + gui.getBusy());
			//print("ai type, should be anything but NOAI" + job.type);
			//print("at work, should be anything be work" + Status.getWork());
			//if (!gui.getBusy() && ! noAI && job.type != JobType.noAI && Status.getWork() != workStatus.working && noRoleActive()){	

			//	if (job.type != JobType.none && TimeManager.getInstance().getHour() > (Job.timeStart - 2) && TimeManager.getInstance().getHour() < Job.timeEnd){
			if (needToWork()){
				for (Day d : job.daysWorking){
					if (d == TimeManager.getInstance().getDay()){
						GoToWork();
						return true;
					}
				}
			}

			if (isHungry()){
				GoEat();
				return true;
			}

			if(needsBankTransaction() && CheckBankOpen()){
				GoToBank();
				return true;
			}

			if(needsToBuy() && CheckMarketOpen()){
				GoToMarket();
				return true;
			}	

			if(isTired()){
				GoToSleep();
				return true;
			}
		}

		Boolean anytrue = false;
		synchronized(roles)
		{
			try{
				for(Role r : roles)
				{
					if(r.active)
					{
						anytrue = anytrue || r.pickAndExecuteAnAction();
					}
				}
			}catch (ConcurrentModificationException e)
			{
				for(Role r : roles)
				{
					if(r.active)
					{
						anytrue = anytrue || r.pickAndExecuteAnAction();
					}
				}
			}
		}


		if(anytrue)
			return true;


		if (!gui.getBusy() && job.type != JobType.noAI){
			WalkAimlessly();
		}

		return false;
	}
	
	

	//////////////////////////////////////////////Scheduler ends here ////////////////////////////////////
	private Boolean needToWork(){
		if (job.type != JobType.none && job.type != JobType.crook && TimeManager.getInstance().getHour() > (3) && 
				TimeManager.getInstance().getHour() < Job.timeEnd && !job.workBuilding.forceClosed){
		{
			for (Day d : job.daysWorking){
					return true;
				}
			}
			return false;
		}
		else
			return false;
	}
	private boolean AIandNotWorking() {
		return (!gui.getBusy() && job.type != JobType.noAI && Status.getWork() != workStatus.working && noRoleActive());
	}
	
	private void GoToSleep() {
		homePurpose = "Sleep";
		GoHomeToDoX();
	}

	private boolean CheckRestOpen() {
		print("I need to go to the restaurant!");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "I need to go to the restaurant!", new Date()));
		RestBase r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.restaurant){
					//print("found b");
					r = (RestBase) b;
				}
			}
		}
		if(r.isOpen())
			return true;
		else{
			print("Aww.. restaurant is closed :(");	
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "Aww... restaurant is closed :(", new Date()));

			return false;
		}
	}
	
	private Building findOpenBuilding(buildingType type){
		//Building r = null;
		Building r = null;
		double smallestDistance = 999999999;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == type){
					//print(" added " + b);
					double distance = Math.sqrt((Math.pow(b.entrance.x - gui.getXPosition(), 2)) + (Math.pow(b.entrance.x - gui.getXPosition(), 2)));
			
					if (b.isOpen() && distance < smallestDistance){
						r = b;
						smallestDistance = distance;
					}
				}
			}
		}
		if (r == null){
			gui.setBusy(false);
			return null;
		}
		else
			return r;
	}

	private void GoEat() {
		Boolean restaurant = false;
		Building r = null;
		r = findOpenBuilding(buildingType.restaurant);
		
		if (r != null){
			int num = (int)(Math.random() * ((10 - 0) + 0));
			if (wealthLevel == WealthLevel.wealthy && num <= 9){
					restaurant = true;
			}
			else if (wealthLevel == WealthLevel.average && num <=7){
					restaurant = true;
			}
			else if (wealthLevel == WealthLevel.poor && num <=3){
					restaurant = true;
			}
		}
		else
			restaurant = false;
		
		if (restaurant){
			GoToRestaurant(r);
		}
		else{
			homePurpose = "Cook";
			GoHomeToDoX();
		} 
		
	}
	
	private void GoToRestaurant(Building r){
		if (r != null){
		if (r.owner.equals("Aleena"))
			GoToRestaurantA(r);
		if (r.owner.equals("Norman"))
			GoToRestaurantN(r);
		}
		else{
			gui.setBusy(false);
			stateChanged();
		}
	}

	private boolean isHungry() {
		if (TimeManager.getInstance().getCurrentSimTime() - timeSinceLastAte > 60000){
			print("Hmm... I'm hungry. I better eat soon");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "Hmm... I'm hungry. I better eat soon", new Date()));
			
			return true;
		}
		else
			return false;
	}
	
	private boolean isTired() {
		if (TimeManager.getInstance().getCurrentSimTime() - timeSinceLastSlept > 90000){
			print("Hmm... I'm tired. I better sleep soon");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "Hmm... I'm tired. I better sleep soon", new Date()));
			
			return true;
		}
		else
			return false;
	}

	private void WalkAimlessly() {
		if (hasCar() && wealthLevel.equals(WealthLevel.average)){
			gui.DoGoToLocation(18, 529);
			gui.DoGoToLocation(617, 529);
		}
		else if (hasCar()){
			gui.DoGoToLocation(24, 138);
			gui.DoGoToLocation(375, 138);
		}
		else if (wealthLevel.equals(WealthLevel.average)){
			gui.DoGoToLocation(24, 332);
			gui.DoGoToLocation(375, 332);
		}
		else{
			gui.DoGoToLocation(24, 267);
			gui.DoGoToLocation(375, 267);
		}
		gui.setBusy(false);
		stateChanged();
	}

	private boolean noRoleActive() {
		synchronized(roles)
		{
			for(Role r : roles)
			{
				if(r.active)
				{
					return false;
				}
			}
		}
		return true;
	}

	private Boolean CheckBankOpen() {
		print("I need to go to the bank!");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "I need to go to the bank!", new Date()));
		Bank r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.bank && b.isOpen()){
					return true;
				}
			}
		}
		
			print("Aww.. bank is closed :(");	
			gui.setBusy(false);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "Aww... bank is closed :(", new Date()));
			return false;
		
	}
	
	private Boolean CheckRestaurantOpen(){
		print("I need to go to a restaurant!");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "I need to go to a restaurant!", new Date()));
		Bank r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.restaurant && b.isOpen()){
					return true;
				}
			}
		}
		
			print("Aww.. all restaurants are closed :(");	
			gui.setBusy(false);
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "Aww... all restaurants are closed :(", new Date()));
			return false;
		
	}

	private Boolean CheckMarketOpen() {
		Market r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.market){
					//print("found b");
					r = (Market) b;
				}
			}
		}
		if(r.isOpen())
			return true;
		else{
			gui.setBusy(false);
			print("Aww.. market is closed :(");	
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "Aww... market is closed :(", new Date()));
			return false;
		}
	}

	private void DelayGoToWork() {
		int num = (int)(Math.random() * ((5 - 0) + 1));
		num *= 1000;
		timer.schedule( new TimerTask()
		{
			public void run()
			{				
				GoToWork();
			}
		}, num);

	}

	private boolean needsToBuy() {
		if (inventory.size() > 0){
			for (Item i : inventory){
				//print("type " + i.type + " quantHas " + i.quantity + " quantwnats" + i.threshold);
				if(i.quantity < i.threshold && canAfford(i)){
					marketPurpose = i.type;
					marketQuantity = i.capacity - i.quantity;
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean canAfford(Item i){
		if (i.type == "Car")
			return (cash > 20000);
		else
			return true;
	}

	private boolean needsBankTransaction() {
		//print(job.dayLastRobbed.toString());
		//dayLastRobbed = Day.wednesday;
		//print(job.dayLastRobbed.toString());
		//print(TimeManager.getInstance().getDay().toString());
		if (job.type.equals(JobType.crook)){
			if (TimeManager.getInstance().getDay() != job.dayLastRobbed){ //TODO modify this so they dont cont. rob lol
				bankPurpose = "Rob";
				bankAmount = 200;
				return true;
			}
			else 
				return false;
		}
		else {
			if (hasLoan() && cash > getCashThresholdUp()){ //if has loan, and has enough cash to live
				bankPurpose = "Pay Loan";
				bankAmount = cash - getCashThresholdUp();
				return true;
			}
			else if (cash > getCashThresholdUp() && hasCar()){ //if has too much cash, and hasn't bought a car
				bankPurpose = "Deposit";
				bankAmount = cash - getCashThresholdUp();
				return true;
			}
			else if (cash < getCashThresholdLow()){			
				bankPurpose = "Withdraw";
				bankAmount = getCashThresholdLow() - cash;
				return true;
			}
			else if (getTotalMoney() < getMoneyThreshold()){
				bankPurpose = "Take Loan";
				bankAmount = getMoneyThreshold() - getTotalMoney();
				return true;
			}
			else if (accounts.size() == 0){
				bankPurpose = "New Account";
				bankAmount = cash - getCashThresholdUp();
				return true;
			}
			else return false;
		}
	}

	private double getTotalMoney() {
		double temp = cash;
		for (Account a : accounts){
			temp += a.getBalance();
		}
		return temp;
	}

	private boolean hasLoan() {
		for(Loan l :loans){
			if (l.s != loanState.paid){
				return true;
			}
		}
		return false;
	}

	/*****************************************************************************
										ACTIONS
	 ******************************************************************************/

	private void GoHomeToDoX()
	{
		Apartment a = null;
		for (Building b: buildings){
			if (b.getType() == buildingType.housingComplex){
				 a = (Apartment) b;
			}
		}
		
		for (Item i : inventory){
			print(i.getType() + " " + i.quantity);
		}
		
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going home to " + homePurpose);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going home to " + homePurpose, new Date()));
		Status.setHousingStatus(houseStatus.goingHome);

		takeBusIfApplicable(0);
		
		gui.DoGoToLocation(a.entrance);
		this.Status.setLocation(location.home);
		gui.setPresent(false);
		
		//Role terminologies
		HousingCustomerRole c = new HousingCustomerRole(this.getName(), cash, inventory);
		c.setTrackerGui(trackingWindow);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		a.panel.tenantPanel.addTenant((HousingCustomer) c, homePurpose);
	}

	private void GoToBank()
	{
		Building r2 = findOpenBuilding(buildingType.bank);
		
		if (r2 != null){
			
			if(bankPurpose.equals("Rob"))
			{
				job.dayLastRobbed = TimeManager.getInstance().getDay();
			}
			
		Bank r = (Bank) r2;
	
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to bank to " + bankPurpose);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to bank to " + bankPurpose, new Date()));
		Status.setMoneyStatus(bankStatus.goingToBank);


		takeBusIfApplicable(0);
		
		gui.DoGoToLocation(r.entrance);
		this.Status.setLocation(location.bank);
		
		//if (CheckBankOpen()){
		gui.setPresent(false);
		BankCustomerRole c = new BankCustomerRole(this.getName(), bankPurpose, bankAmount, cash, job.type.toString());
		if (!accounts.isEmpty())
			c.setAccount(accounts.get(0));
		if (!accounts.isEmpty())
			c.setLoan(loans);
		
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		c.setTrackerGui(trackingWindow);
		r.panel.customerPanel.addCustomer((BankCustomerRole) c);
		}
		else{
			gui.setBusy(false);
			stateChanged();
		}
	}
	

	public void GoToWork(){
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to work as " + job.type);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to work as " + job.type, new Date()));
		Status.setWorkStatus(workStatus.goingToWork);
		
		
		if (job.type == JobType.bankHost || job.type == JobType.teller){
			WorkAtBank();
		}
		if (job.type == JobType.cashier || job.type == JobType.cook || job.type == JobType.waiter  || job.type == JobType.restHost){

			if (job.workBuilding.owner.equals("Norman"))
				WorkAtRest();
			else if (job.workBuilding.owner.equals("Aleena"))
				WorkAtRestA();
		}
		if (job.type == JobType.landLord || job.type == JobType.repairman){
			WorkAtApartment();
		}
		
		if (job.type == JobType.student)
		{
			AskForRubric();
		}

		Status.loc = location.work;

	}
	
	
	private void WorkAtApartment() {

		takeBusIfApplicable(0);
		
		gui.DoGoToCheckpoint('G');
		gui.DoGoToCheckpoint('H');
		gui.DoGoToCheckpoint('I');
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.home);
		gui.setPresent(false);
		if (job.type == JobType.landLord)
		{
//			c = new RestaurantHostRole(this.getName());
//			r.panel.customerPanel.addHost((BankHost) c);
		}
		if (job.type == JobType.repairman)
		{
//			c = new RestaurantHostRole(this.getName());
//			r.panel.customerPanel.addHost((BankHost) c);
		}
	}

	public void WorkAtRest() {

		/*=====================================================================
		  						RESTAURANT SALARIES
		  =====================================================================
			Cashier (Economist)  		|   100 per shift
			Host (Manager)		 		|   80  per shift
			Cook (Culinary Grad) 		|   80  per shift
			Waiter (Low-wage student)   |   50  per shift
		  =====================================================================*/

		//takeBusIfApplicable(2);

	
		Restaurant r = (Restaurant) job.workBuilding;
				
		gui.DoGoToLocation(r.entrance);
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);

		if (job.type == JobType.restHost && r.panel.host == null)
		{
			HostRole c = new HostRole(this.getName(), this.cash);
			c.setBuilding(r);
			c.setSalary(80);
			c.setTrackerGui(trackingWindow);
			c.setPerson(this);
			r.panel.addHost((HostRole) c);
			roles.add(c);
			c.setActivity(true);
		}
		
		if (job.type == JobType.cook  && r.panel.cook == null){
			CookRole c = new CookRole(this.getName(), this.cash);
			c.setBuilding(r);
			c.setSalary(80);
			c.setTrackerGui(trackingWindow);
			r.panel.addCook((CookRole) c);
			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
		}
		if (job.type == JobType.cashier && r.panel.cashier == null){
			CashierRole c = new CashierRole(this.getName(), this.cash);
			c.setBuilding(r);
			c.setSalary(100);
			c.setTrackerGui(trackingWindow);
			r.panel.addCashier((CashierRole) c);
			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
		}
		if (job.type == JobType.waiter){
			Random rand = new Random();
			double randval = rand.nextInt();
			if (randval % 2 == 0)
			{
				ModernWaiterRole c = new ModernWaiterRole(this.getName(), r, this.cash);
				r.panel.addWaiter((ModernWaiterRole) c, waiterindex);
				c.setPerson(this);
				c.setSalary(50);
				c.setTrackerGui(trackingWindow);
				roles.add(c);
				c.setActivity(true);
			}
			else
			{
				TraditionalWaiterRole c = new TraditionalWaiterRole(this.getName(), r, this.cash);
				r.panel.addWaiter((TraditionalWaiterRole) c, waiterindex);
				c.setPerson(this);
				c.setSalary(50);
				c.setTrackerGui(trackingWindow);
				roles.add(c);
				c.setActivity(true);
			}
			if (waiterindex>5) waiterindex = 0;
		}
	}
	
	public void WorkAtRestA() {
		
		/*	=====================================================================
						RESTAURANT A SALARIES
			=====================================================================
				Cashier (Economist)  		|   100 per shift
				Host (Manager)		 		|   80  per shift
				Cook (Culinary Grad) 		|   80  per shift
				Waiter (Low-wage student)   |   50  per shift
			=====================================================================*/

		//takeBusIfApplicable(2);
		
		RestaurantA r = (RestaurantA) job.workBuilding;
				
		gui.DoGoToLocation(r.entrance);
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);
		
		if (job.type == JobType.restHost)
		{
			restaurantA.HostAgent c = new restaurantA.HostAgent(this.getName());
			c.setTrackerGui(trackingWindow);
			r.workingHost = (restaurantA.HostAgent) c;
			r.panel.customerPanel.addHost((restaurantA.HostAgent) c);	
			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
			c.setSalary(80);
		}
		
		if (job.type == JobType.waiter)
		{
			Random rand = new Random();
			double randval = rand.nextInt();
			if (randval % 2 == 0)
			{
				restaurantA.WaiterAgent c = new restaurantA.ModernWaiterAgent(this.getName(), r, this.cash);
				c.setSalary((double)50);
				//r.workingWaiters.add((restaurantA.WaiterAgent) c);
				c = new restaurantA.WaiterAgent(this.getName());
				c.setTrackerGui(trackingWindow);
				r.panel.customerPanel.addWaiter((restaurantA.WaiterAgent) c);
				c.setPerson(this);
				roles.add(c);
				c.setActivity(true);
			}
			else
			{
				restaurantA.WaiterAgent c = new TraditionalWaiterAgent(this.getName(), r, this.cash);
				//r.workingWaiters.add((restaurantA.WaiterAgent) c);
				c.setSalary((double)50);
				c.setTrackerGui(trackingWindow);
				r.panel.customerPanel.addWaiter((restaurantA.WaiterAgent) c);
				c.setPerson(this);
				roles.add(c);
				c.setActivity(true);
		}
			//if (waiterindexA>5) waiterindex = 0;
		}
		
		if (job.type == JobType.cook)
		{
			restaurantA.CookAgent c = new restaurantA.CookAgent(this.getName());
			c.setTrackerGui(trackingWindow);
			r.workingCook = (restaurantA.CookAgent) c;
			r.panel.customerPanel.addCook((restaurantA.CookAgent) c);
			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
			c.setSalary(80);
		}
		
		if (job.type == JobType.cashier)
		{
			restaurantA.CashierAgent c = new restaurantA.CashierAgent(this.getName());
			c.setTrackerGui(trackingWindow);
			r.workingCashier = (restaurantA.CashierAgent) c;
			r.panel.customerPanel.addCashier((restaurantA.CashierAgent) c);
			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
			c.setSalary(100);
		}	

	}

	public void WorkAtBank() {
		
		/*=====================================================================
									BANK SALARIES
		=====================================================================
		Host (Manager)  				|   80 per shift
		Teller (Accountant)		 		|   110  per shift
		=====================================================================*/

		//takeBusIfApplicable(0);

		//Role c = null;
		Bank r = (Bank) job.workBuilding;
	
		gui.DoGoToLocation(r.entrance);
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
		
		if (job.type == JobType.bankHost && r.workingHost == null)
		{
			BankHostRole c = new BankHostRole(this.getName());
			c.setTrackerGui(trackingWindow);
			c.setSalary(80);
			r.workingHost = (BankHostRole) c;
			r.panel.customerPanel.addHost((BankHost) c);

			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
		}
		if (job.type == JobType.teller){
			TellerRole c = new TellerRole(this.getName());
			c.setTrackerGui(trackingWindow);
			c.setSalary(110);
			r.panel.customerPanel.addTeller((Teller) c);

			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
		}

	}

	public void GoToRestaurantN(Building b)
	{
		Restaurant r = (Restaurant) b;
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to Norman's Restaurant");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to Norman's restaurant.", new Date()));
		Status.setNourishment(nourishment.goingToFood);
	
		takeBusIfApplicable(2);
		
		gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('A');


		gui.DoGoToLocation(r.entrance);
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);

		//Role terminologies
		CustomerRole c = new CustomerRole(this.getName(), cash);
		c.setTrackerGui(trackingWindow);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		//r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
		r.panel.customerPanel.addCustomer((Customer) c, r);
	
	}
	
	public void GoToRestaurantA(Building b)
	{
		RestaurantA r = (RestaurantA) b;
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to Aleena's Restaurant");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to Aleena's Restaurant.", new Date()));
		Status.setNourishment(nourishment.goingToFood);

		//takeBusIfApplicable(2);
		
		gui.DoGoToLocation(r.entrance);
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);

		//Role terminologies
		restaurantA.CustomerAgent c = new restaurantA.CustomerAgent(this.getName(), cash, job.type.toString());
		c.setTrackerGui(trackingWindow);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
	//	r.panel.customerPanel.hungry.setSelected(true);
		r.panel.customerPanel.addCustomer((restaurantA.CustomerAgent) c, r);
	
	}

	public void GoToMarket(){
		Market r = null;
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to market to buy " + marketQuantity + " of " + marketPurpose);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to market to buy " + marketQuantity + " of " + marketPurpose, new Date()));

		Status.market = marketStatus.waiting;
		takeBusIfApplicable(1);
	
		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.market){
					//print("found market");
					r = (Market) b;
				}
			}
		}
		gui.DoGoToLocation(r.entrance);
		
		
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
		this.Status.setLocation(location.market);
		print("At market entrance");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "At market entrance", new Date()));
		gui.setPresent(false);
		MarketCustomerRole c = new MarketCustomerRole(this.getName(), marketPurpose, marketQuantity, cash, job.type.toString());
		c.setTrackerGui(trackingWindow);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
		r.panel.customerPanel.addCustomer((MarketCustomerRole) c);
		r.gui.customerStateCheckBox.setSelected(true);
		
	}
	
	public void AskForRubric()
	{
		Coordinate studentLoc = new Coordinate(400,400);
		gui.DoGoToLocation(studentLoc);
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.outside);
		//RunTimer
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				changeBubble = true;
				gui.setBubble(bubbleIndex);
				StopAsking();
			}
		}, 7 * 1000);
	}
	
	public void StopAsking()
	{
		changeBubble = false;
	}
	
	public void takeBusIfApplicable(int destin){
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar() && !metro.buses.isEmpty()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(destin); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
	}

	public void setBuildings(Vector<Building> buildings) {
		this.buildings = buildings;
	}

	public void setPersonList(Vector<PersonAgent> people) {
		this.people = people;
	}

	public void setAI(Boolean noAI) {
		this.noAI = noAI;
	}


	public void setThreshold(int q) {
		// TODO Auto-generated method stub

	}


	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}


	public int getQuantity() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void removeItem() {
		// TODO Auto-generated method stub

	}

	public void setHungry() {
		timeSinceLastAte = -190000;
	}

	public void GiveCar() {
		addItem("Car", 1);
	}
	
}
