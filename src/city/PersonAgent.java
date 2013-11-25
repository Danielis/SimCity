package city;

//Package Imports
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import restaurant.CustomerAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Customer;
import agent.Agent;
import bank.BankCustomerRole;
import restaurant.gui.CustomerGui;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import bank.Bank;
import bank.interfaces.BankCustomer;
import roles.Apartment;
import roles.Building;
import roles.Building.buildingType;
import roles.CustomerRole;
import roles.Restaurant;
import roles.Role;
import market.*;
import market.interfaces.MarketCustomer;
import city.guis.PersonGui.Coordinate; //trans: added for trans
import transportation.BusStopAgent; // needed for BusStop variable
import housing.HousingCustomerRole;
import housing.interfaces.HousingCustomer;


import transportation.TransportationCompanyAgent;

//Utility Imports
import java.util.*;
import java.util.concurrent.Semaphore;

public class PersonAgent extends Agent implements Person
{
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/

	//Lists: Roles, Restaurants
	List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	Vector<Building> buildings = new Vector<Building>(); //City Gui won't let me implement Lists

	//Inventory List
	List<Item> inventory = Collections.synchronizedList(new ArrayList<Item>());

	//For housing: List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());


	//Variable
	PersonGui gui = null;
	public double money = 500;
	String name;
	public PersonStatus Status = new PersonStatus();
	public Semaphore animSemaphore = new Semaphore(0, true);
	public Semaphore busSemaphore = new Semaphore(0, true);

	public CityAnimationPanel CityAnimPanel;	
	public RestaurantPanel restPanel;
	
	
	String bankPurpose, marketPurpose, homePurpose;
	double marketQuantity;
	double bankAmount;
	
	
	public class Job{
		
		public Job(JobType parseJob) {
			type = parseJob;
//			if (type == JobType.marketWorker || type == JobType.marketHost)
//				location =;
//			else if (type == JobType.bankHost || type == JobType.teller)
//				location =;
//			else if (type == JobType.restHost || type == JobType.cook || type == JobType.cashier || type == JobType.waiter)
//				location =;
//			else if (type == JobType.landLord || type == JobType.repairman)
//				location =;
			
		}
		JobType type;
		Coordinate location;
		
	}
	enum JobType {none, marketWorker, marketHost, bankHost, teller, restHost, cook, cashier, waiter, landLord, repairman, crook}
	Job job;
	
	enum WealthLevel {average, wealthy, poor}
	
	
	WealthLevel wealthLevel;
	
	BusStopAgent destinationStop;//trans: added
	BusStopAgent curStop;//trans: addded
	
	TransportationCompanyAgent metro;
	
	public PersonAgent(String name, String j, String wealth){
		this.name = name;
		job = new Job(parseJob(j));
		
		wealthLevel = parseWealth(wealth);
		money = setWealth();
		System.out.println("Added person " + name + " with job type " + job.type + " and wealth level: " + wealthLevel);
	}	
	
	private double setWealth() {
		if (wealthLevel.equals("Average"))
			return 35000;
		else if (wealthLevel.equals("Wealthy")){
			inventory.add(new Item("Car", 1));
			return 50000;
		}
		else if (wealthLevel.equals("Poor"))
			return 2000;
		else
			return 35000;
	}

	private WealthLevel parseWealth(String wealth) {
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

	private JobType parseJob(String job) {
	
	if (job.equals("None"))
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
	else if (job.equals("Crook"))
		return JobType.crook;
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

	class Item {
		String type;
		int quantity;
		public Item(String t, int q) {
			type = t;
			quantity = q;
		}
	}
	
	public void addItem(List<Item> inv, String item, int q){
		for (Item i : inv){
			if (i.type.equals(item)){
				i.quantity += q;
				print("I now have " + i.quantity + " " + i.type);
				return;
			}
		}
		inv.add(new Item(item, q));
		print("I now have " + q + " " + item);
	}
	
	public void removeItem(List<Item> inv, String item, int q){
		for (Item i : inv){
			if (i.type.equals(item)){
				i.quantity -= q;
				print("I now have " + i.quantity + " " + i.type);
			}
		}
	}

	//Enum States
	enum nourishment	{notHungry,Hungry,goingToFood} // may not need goingToFood
	enum location		{outside,home,restaurant,bank,market,transportation,work}
	enum destination	{outside,home,restaurant,bank,market,transportation,work}
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
				if(i.equals("Car")) {
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
		C = checkPointDistance(385,474); // Assign G
		if ( C > checkPointDistance(385,282))
			C = checkPointDistance(385,282);//assign D
		if ( C > checkPointDistance(385,362))
			C = checkPointDistance(385,362);//assign C
		if ( C > checkPointDistance(385,474))
			C = checkPointDistance(385,474);//assign B
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
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/

	public void msgGoToWork() {
		print("Called msgGoToRestaurant");
		if (job != null && job.type != JobType.none)
			Status.setDestination(destination.work);
		gui.setPresent(false);
		stateChanged();
	}
	
	

	//Housing
	public void msgGoToHome(String purpose){
		homePurpose = purpose;
	    Status.setHousingStatus(houseStatus.needsToGo);
	    Status.setDestination(destination.home);
	    gui.setPresent(false);
	    stateChanged();
	}
	
	public void msgLeavingHome(Role r){
	    r.setActivity(false);
		roles.remove(r);
	    Status.setLocation(location.outside);
	    Status.setDestination(destination.outside);
	    Status.setHousingStatus(houseStatus.notHome);
	    gui.setPresent(true);
		gui.DoGoToCheckpoint('A');
//		gui.DoGoToCheckpoint('C');
//		gui.DoGoToCheckpoint('B');
//		gui.DoGoToCheckpoint('A');
	    stateChanged();
	}

	//Restaurant
	public void msgGoToRestaurant(){ // sent from gui
		print("Called msgGoToRestaurant");
		Status.setNourishment(nourishment.Hungry);
		Status.setDestination(destination.restaurant);
		gui.setPresent(false);
		stateChanged();
	}

	public void msgLeavingRestaurant(Role r, float myMoney){
		r.setActivity(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		Status.setNourishment(nourishment.notHungry);
		gui.setPresent(true);
		
//		for (Building b: buildings){
//			print(" type: " + b.getType() + " n: ");
//			if (b.getType() == buildingType.restaurant){
//				Restaurant a = (Restaurant) b;
//				a.panel.removeCustomer((Customer)r);
//			}
//		}
		
		//Commenting out since AI should handle movement after the person gets out of restaurant
		//gui.DoGoToCheckpoint('D');
		//gui.DoGoToCheckpoint('C');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('A');
		// however will make person just go home or where housing will be /////////////////////////////////////
		if(Status.getTransportationStatus() == transportStatus.bus){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(0); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			this.closestCheckpoint();
		gui.DoGoToCheckpoint('G');
		gui.DoGoToCheckpoint('H');
		gui.DoGoToCheckpoint('I');
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);		
		roles.remove(r);
		stateChanged();

	}

	public void msgGoToBank(String purpose, double amt)
	{
		bankPurpose = purpose;
		bankAmount = amt;
		print("Going to bank");
		Status.setDestination(destination.bank);
		Status.setMoneyStatus(bankStatus.withdraw);
		gui.setPresent(false);
		stateChanged();
	}

	public void msgLeavingBank(BankCustomerRole r, double balance) {
		print("Left bank.");
		money = balance;
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside); 
		gui.setPresent(true);
		//Commenting out since AI should handle movement after leaving bank
		//gui.DoGoToCheckpoint('D');
		roles.remove(r);
		stateChanged();
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
	    gui.setPresent(false);
	    stateChanged();
	}
	
	public void msgLeavingMarket(MarketCustomerRole r, double balance, String item, int quantRec) {
		print("Left market.");
		money = balance;
		addItem(inventory, item, quantRec);
		r.setActivity(false);
		gui.setBusy(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		gui.setPresent(true);
		gui.DoGoToCheckpoint('D');
		gui.setBusy(false);
		roles.remove(r);
		stateChanged();
	}
	
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/

	@Override
	protected boolean pickAndExecuteAnAction() {
		//Not exactly sure where this next bit of code has to go or when it will be called
		// it is called when a person needs to go to work and the SimCity has determined that it has to
		// take a Bus to get somehere so perhaps before other actions are performed. Will need to work this out in PersonAgent later on
		//If you're hungry and outside, go to the restaurant. Preliminary.
		if (Status.getWork() == workStatus.notWorking &&
				Status.getLocation() == location.outside) {
			print("Scheduler realized the person wants to go to Restaurant");
			GoToWork();
			return true;
		}
		if (Status.getNourishmnet() == nourishment.Hungry &&
				Status.getLocation() == location.outside) {
			print("Scheduler realized the person wants to go to Restaurant");
			GoToRestaurant();
			return true;
		}
		//If you need to withdraw, and your destination is the bank, withdraw
		if (Status.getMoneyStatus() == bankStatus.withdraw &&
				Status.getDestination() == destination.bank) {
				GoToWithdrawFromBank();
				return true;
			}
		if (Status.getDestination() == destination.market &&
				Status.market == marketStatus.buying) {
				GoToMarket();
				return true;
			}

		//If for any reason you need to go home, go home.
		if (Status.getHousingStatus() == houseStatus.needsToGo &&
				Status.getDestination() == destination.home)
		{
			GoHomeToDoX();
			return true;
		}
		
		Boolean anytrue = false;
		synchronized(roles)
		{
			for(Role r : roles)
			{
				if(r.active)
				{
					anytrue = anytrue || r.pickAndExecuteAnAction();
				}
			}
		}


		if(anytrue)
			return true;

		return false;
	}

	/*****************************************************************************
										ACTIONS
	 ******************************************************************************/

	private void GoHomeToDoX()
	{
		Status.setHousingStatus(houseStatus.goingHome);
		//Transportation t = ChooseTransportation();
		gui.DoGoToHouse();
		this.Status.setLocation(location.home);
		gui.setPresent(false);
		
		//Role terminologies
		HousingCustomerRole c = new HousingCustomerRole(this.getName());
		c.setPerson(this);
		roles.add(c);
		this.roles.get(0).setActivity(true);

		for (Building b: buildings){
			print(" type: " + b.getType() + " n: ");
			if (b.getType() == buildingType.housingComplex){
				Apartment a = (Apartment) b;
				a.panel.tenantPanel.addTenant((HousingCustomer)roles.get(0), homePurpose);
			}
		}
	}
	
	private void GoToWork(){
		Status.setWorkStatus(workStatus.goingToWork);
		gui.DoGoToCheckpoint('D');
		
		//if (jobType == jobType.)
		
		
		
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
		
		Role c = null;
	
		
		//if (job.type == JobType.bankHost)
			//c = new BankHostRole(this.getName());
		
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		
		

		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.bank){
					print("found b");
					Bank r = (Bank) b;
					r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
					r.panel.customerPanel.addCustomer((BankCustomer) c);
				}
			}
		}
	}
	
	
	private void GoToRestaurant()
	{
		print("Going to restaurant");
		Status.setNourishment(nourishment.goingToFood);
		//Transportation t = ChooseTransportation();
		//gui.DoGoToCheckpoint('A');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('C');
		if(Status.getTransportationStatus() == transportStatus.bus){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(2); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			this.closestCheckpoint();
		gui.DoGoToCheckpoint('B');
		gui.DoGoToCheckpoint('A');
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);

		//Role terminologies
		CustomerRole c = new CustomerRole(this.getName(), money);
		c.setPerson(this);
		roles.add(c);
		this.roles.get(0).setActivity(true);


		//restaurants.get(0).panel.host.msgCheckForASpot((Customer)roles.get(0));


		synchronized(buildings)
		{
			for (Building b: buildings){
				//print(" type: " + b.getType() + " n: ");
				if (b.getType() == buildingType.restaurant){
					Restaurant r = (Restaurant) b;
					r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
					r.panel.customerPanel.addCustomer((Customer)roles.get(0));
				}
			}
		}
	}

	public void GoToWithdrawFromBank()
	{
		Status.setMoneyStatus(bankStatus.goingToBank);
		gui.DoGoToCheckpoint('D');
		this.Status.setLocation(location.bank);
		gui.setPresent(false);

		BankCustomerRole c = new BankCustomerRole(this.getName(), bankPurpose, bankAmount, money);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		c.test("New Account", 20);

		
		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.bank){
					print("found b");
					Bank r = (Bank) b;
					r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
					r.panel.customerPanel.addCustomer((BankCustomer) c);
				}
			}
		}
		//((BankCustomerRole) this.roles.get(0)).msgWantsTransaction("New Account", 20);
	}
	
	void GoToMarket(){
		Status.market = marketStatus.waiting;
		gui.DoGoToCheckpoint('A');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('C');
		//gui.DoGoToCheckpoint('D');
		this.Status.setLocation(location.market);
		print("At market entrance");
		gui.setPresent(false);
		
		//MarketCustomerRole c = new MarketCustomerRole(this.getName(), "New Account", 20, money);
		MarketCustomerRole c = new MarketCustomerRole(this.getName(), marketPurpose, marketQuantity, money);

		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.market){
					print("found market");
					Market r = (Market) b;
					r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
					r.panel.customerPanel.addCustomer((MarketCustomer) c);
					r.gui.customerStateCheckBox.setSelected(true);
				}
			}
		}
	}

	public void setBuildings(Vector<Building> buildings) {
		this.buildings = buildings;
	}


}
