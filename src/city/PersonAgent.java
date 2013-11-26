package city;

//Package Imports
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import restaurant.CustomerAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Customer;
import restaurant.roles.CustomerRole;
import agent.Agent;
import bank.BankCustomerRole;
import restaurant.gui.CustomerGui;
import city.TimeManager.Day;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import bank.Bank;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;
import bank.BankHostRole;
import bank.interfaces.*;
import roles.Apartment;
import roles.Building;
import roles.Building.buildingType;
import roles.Role;
import market.*;
import market.interfaces.MarketCustomer;
import city.guis.PersonGui.Coordinate; //trans: added for trans
import transportation.BusStopAgent; // needed for BusStop variable
import housing.HousingCustomerRole;
import housing.interfaces.HousingCustomer;
import bank.*;
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
	
	
	String bankPurpose, marketPurpose, homePurpose;
	double marketQuantity, bankAmount;
	
	
	public class Job{
		JobType type;
		Coordinate location;
		static final int timeStart = 8;
		static final int timeEnd = 20;
		List <Day> daysWorking = new ArrayList<Day>();
		
		public Job(JobType parseJob) {
			type = parseJob;
			//if (type == JobType.marketWorker || type == JobType.marketHost){
			//	timeStart = 8;
			//	timeEnd
			//}
			if (type == JobType.bankHost || type == JobType.restHost){
				//timeStart = 8; 
				//timeEnd = 4;
			}
			else if (type == JobType.teller || type == JobType.cook || type == JobType.cashier || type == JobType.waiter){
				//timeStart = 9;
				//timeEnd = 5;
			}
			else if (type == JobType.landLord || type == JobType.repairman){
				//timeStart = 8;
				//timeEnd = 4;
			}
			
			assignWorkDay(parseJob);
		}
		
		
		
		private void assignWorkDay(JobType parseJob) {
			
				daysWorking.add(Day.monday);
				daysWorking.add(Day.tuesday);
				daysWorking.add(Day.wednesday);
				daysWorking.add(Day.thursday);
				daysWorking.add(Day.friday);
			if (type != JobType.bankHost && type != JobType.teller){
				daysWorking.add(Day.saturday);
				daysWorking.add(Day.sunday);
			}
			
//			int sameJob = 0;
//			for (PersonAgent p : people){
//				if (p.job.type == parseJob){
//					sameJob++;
//				}
//			}
//			if (sameJob % 2 == 0)
//				assignWorkSet(1);
//			else
//				assignWorkSet(2);
//			
		}

//		private void assignWorkSet(int i) {
//			if (i == 1){
//				if (type == JobType.bankHost || type == JobType.teller){
//					daysWorking.add(Day.monday);
//					daysWorking.add(Day.wednesday);
//					daysWorking.add(Day.friday);
//				}
//			}
//			else{
//				if (type == JobType.bankHost || type == JobType.teller){
//					daysWorking.add(Day.tuesday);
//					daysWorking.add(Day.wednesday);
//					daysWorking.add(Day.friday);
//				}
//			}
//		}
		
		
	}
	enum JobType {noAI, none, marketWorker, marketHost, bankHost, teller, restHost, cook, cashier, waiter, landLord, repairman, crook}
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
		cash = setWealth();
		System.out.println("Added person " + name + " with job type " + job.type + " and wealth level: " + wealthLevel);
	}	
	
	private double setWealth() {
		if (wealthLevel.equals(WealthLevel.average)){
			addItem(inventory, "Car", 0, 1);
			return 25000;
		}
		else if (wealthLevel.equals(WealthLevel.wealthy)){
			addItem(inventory, "Car", 1, 0);
			return 50000;
		}
		else if (wealthLevel.equals(WealthLevel.poor)){
			addItem(inventory, "Car", 0, 1);
			return 2000;
		}
		else
			return 35000;
	}
	


	private double getCashThresholdUp(){
		if (wealthLevel == WealthLevel.average)
			return 150;
		else if (wealthLevel == WealthLevel.wealthy)
			return 250;
		else if (wealthLevel == WealthLevel.poor)
			return 75;
		return 0;
	}
	
	private double getCashThresholdLow(){
		if (wealthLevel == WealthLevel.average)
			return 20;
		else if (wealthLevel == WealthLevel.wealthy)
			return 50;
		else if (wealthLevel == WealthLevel.poor)
			return 5;
		return 0;
	}
	
	private double getMoneyThreshold(){
		if (wealthLevel == WealthLevel.average)
			return 1000;
		else if (wealthLevel == WealthLevel.wealthy)
			return 1000;
		else if (wealthLevel == WealthLevel.poor)
			return 1000;
		return 0;
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
		int threshold;
		public Item(String t, int q, int th) {
			type = t;
			quantity = q;
			threshold = th;
		}
		
	public void setThreshold(int q){
			threshold = q;
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
		inv.add(new Item(item, q, 0));
		print("I now have " + q + " " + item);
	}
	
	private void addItem(List<Item> inv, String item, int q, int j) {
		for (Item i : inv){
			if (i.type.equals(item)){
				i.quantity += q;
				print("I now have " + i.quantity + " " + i.type);
				return;
			}
		}
		inv.add(new Item(item, q, j));
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
		print("C is " + C + " My current check C is: " + checkPointDistance(385,278) + "  P is " + P);
		if ( C > checkPointDistance(385,278)){
			C = checkPointDistance(385,278);//assign D
			P = 'D';
		}
		print("C is " + C + " My current check C is: " + checkPointDistance(385,362) + "  P is " + P);
		if ( C > checkPointDistance(385,362)){
			C = checkPointDistance(385,362);//assign C
		P = 'C';
		}
		print("C is " + C + " My current check C is: " + checkPointDistance(385,474) + "  P is " + P);
		if ( C > checkPointDistance(385,474)){
			C = checkPointDistance(385,474);//assign B
			P = 'B';
		}
		print("C is " + C + "  P is " + P);
		print("Going To Point " + P);
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

	public void msgWakeUp() {
		stateChanged();
	}
	
	//TODO ADD THIS MSG TO ALL WORKER ROLES
	public void msgLeaveWork() {
		for (Role r : roles){
			if (r.active){
			r.msgLeaveWork();
			// TODO
			}
		}
		
//		for (Role r : roles){
//				if (r.active){
//				r.setActivity(false);
//				roles.remove(r);
//			    Status.setLocation(location.outside);
//			    Status.setDestination(destination.outside);
//			    Status.setHousingStatus(houseStatus.notHome);
//			    gui.setPresent(true);
//			}
//		}
		stateChanged();	
	}

	public void msgLeaveHome() {
		
		for (Role r : roles){
			if (r.active){
			HousingCustomerRole x = (HousingCustomerRole) r;
			x.msgLeaveHouse();
			// TODO
			}
		}
		
//		for (Role r : roles){
//				if (r.active){
//				r.setActivity(false);
//				roles.remove(r);
//			    Status.setLocation(location.outside);
//			    Status.setDestination(destination.outside);
//			    Status.setHousingStatus(houseStatus.notHome);
//			    gui.setPresent(true);
//			}
//		}
		stateChanged();	
	}
	
	public void msgGoToWork() {
		print("Called msgGoToWork");
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
	    gui.setPresent(true);
	    stateChanged();
	}
	
	public void msgLeavingHome(Role r, double balance){
		cash = balance;
	    r.setActivity(false);
		roles.remove(r);
	    Status.setLocation(location.outside);
	    Status.setDestination(destination.outside);
	    Status.setHousingStatus(houseStatus.notHome);
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

//		}
		
		//Commenting out since AI should handle movement after the person gets out of restaurant
		//gui.DoGoToCheckpoint('D');
		//gui.DoGoToCheckpoint('C');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('A');
		// however will make person just go home or where housing will be /////////////////////////////////////
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
		//Commenting out since AI should handle movement after leaving bank
		//gui.DoGoToCheckpoint('D');
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
		addItem(inventory, item, quantRec);
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
	protected boolean pickAndExecuteAnAction() {
		
		//Not exactly sure where this next bit of code has to go or when it will be called
		// it is called when a person needs to go to work and the SimCity has determined that it has to
		// take a Bus to get somehere so perhaps before other actions are performed. Will need to work this out in PersonAgent later on
		//If you're hungry and outside, go to the restaurant. Preliminary.
		
if (job.type == JobType.noAI){		
		if (Status.getWork() == workStatus.notWorking &&
				Status.getDestination() == destination.work) {
			print("Scheduler realized the person wants to go to work");
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
				Status.getDestination() == destination.bank && CheckBankOpen()) {
				GoToBank();
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
}

if (!gui.getBusy() && job.type != JobType.noAI && Status.getWork() != workStatus.working && noRoleActive()){	
	
	if (job.type != JobType.none && TimeManager.getInstance().getHour() > (Job.timeStart - 2) && TimeManager.getInstance().getHour() < Job.timeEnd){
		for (Day d : job.daysWorking){
			if (d == TimeManager.getInstance().getDay()){
				GoToWork();
				return true;
			}
		}
	}
	
	if(needsBankTransaction() && CheckBankOpen()){
		GoToBank();
		return true;
	}
	
	if(needsToBuy()){
		GoToMarket();
		return true;
	}	
		
}

//TODO: THIS SECTION RELIES ON TIMERS / OUTSIDE MESSAGES	
//	if (Hungry()){
//		Boolean restaurant;
//		if (wealthLevel == WealthLevel.wealthy)
//			restaurant = 70% chance
//		else
//			restaurant = 10% chance
//
//		if (restaurant)
//			GoToRestaurant();
//			return true;
//		else{
//			homePurpose = "Cook";
//			GoHomeToDoX();
//			return true;
//		} 
//	}
//	if (OwesRent()){
//		homePurpose = "Pay Rent";
//		GoHomeToDoX();
//		return true;
//	}
//	if (AptBroken()){
//		homePurpose = "Call for Repair";
//		GoHomeToDoX();
//		return true;
//	}

		
	
	
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

		if (!gui.getBusy() && job.type != JobType.noAI){	
		homePurpose = "Sleep";
		GoHomeToDoX();
		}
		return false;	
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
		Bank r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.bank){
					//print("found b");
					r = (Bank) b;
				}
			}
		}
		if(r.isOpen())
			return true;
		else{
			print("Aww.. bank is closed :(");	
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
				if(i.quantity < i.threshold){
					marketPurpose = i.type;
					marketQuantity = i.threshold - i.quantity;
					return true;
				}
			}
		}
		return false;
	}

	private boolean needsBankTransaction() {
		if (hasLoan() && cash > getCashThresholdUp()){
			bankPurpose = "Pay Loan";
			bankAmount = cash - getCashThresholdUp();
			return true;
		}
		if (cash > getCashThresholdUp() && hasCar()){
			bankPurpose = "Deposit";
			bankAmount = cash - getCashThresholdUp();
			return true;
		}
		if (cash < getCashThresholdLow()){
			bankPurpose = "Withdraw";
			bankAmount = getCashThresholdLow() - cash;
			return true;
		}
		if (getTotalMoney() < getMoneyThreshold()){
			bankPurpose = "Take Loan";
			bankAmount = getMoneyThreshold() - getTotalMoney();
			return true;
		}
		
		if (accounts.size() == 0){
			bankPurpose = "New Account";
			return true;
		}
		return false;
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
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going home to " + homePurpose);
		Status.setHousingStatus(houseStatus.goingHome);

	
		gui.DoGoToLocation(328, 87);
		this.Status.setLocation(location.home);
		gui.setPresent(false);
		
		//Role terminologies
		HousingCustomerRole c = new HousingCustomerRole(this.getName(), cash);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);

		for (Building b: buildings){
			//print(" type: " + b.getType() + " n: ");
			if (b.getType() == buildingType.housingComplex){
				Apartment a = (Apartment) b;
				a.panel.tenantPanel.addTenant((HousingCustomer) c, homePurpose);
			}
		}
	}

	public void GoToBank()
	{
		Bank r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.bank){
					//print("found b");
					r = (Bank) b;
				}
			}
		}
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to bank to " + bankPurpose);
		Status.setMoneyStatus(bankStatus.goingToBank);

		gui.DoGoToLocation(80,76);
		
		
		this.Status.setLocation(location.bank);
		
		gui.setPresent(false);
		BankCustomerRole c = new BankCustomerRole(this.getName(), bankPurpose, bankAmount, cash);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		r.panel.customerPanel.addCustomer((BankCustomer) c);
	}
	

	private void GoToWork(){
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to work as " + job.type);
		Status.setWorkStatus(workStatus.goingToWork);
		
		
		if (job.type == JobType.bankHost || job.type == JobType.teller){
			WorkAtBank();
		}
		if (job.type == JobType.cashier || job.type == JobType.cook || job.type == JobType.waiter  || job.type == JobType.restHost){
			WorkAtRest();
		}
		if (job.type == JobType.landLord || job.type == JobType.repairman){
			WorkAtApartment();
		}
		
		
	}
	
	
	private void WorkAtApartment() {
		gui.DoGoToLocation(328, 87);
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

	private void WorkAtRest() {

		gui.DoGoToLocation(264, 472);
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);
		Role c = null;
//		Restaurant r = null;
//		synchronized(buildings)
//		{
//			for (Building b: buildings){
//				if (b.getType() == buildingType.bank)
//				{
//					r = (Restaurant) b;
//				}
//			}
//		}

		//TODO: PLEASE ADD ADD HOST, COOK, CASHIER, WAITER FUNCTIONS HERE:
		if (job.type == JobType.restHost)
		{
//			c = new RestaurantHostRole(this.getName());
//			r.panel.customerPanel.addHost((BankHost) c);
		}
		if (job.type == JobType.cook){
//			c = new CookRole(this.getName());
//			r.panel.customerPanel.addTeller((Teller) c);
		}
		if (job.type == JobType.cashier){
//			c = new CashierRole(this.getName());
//			r.panel.customerPanel.addTeller((Teller) c);
		}
		if (job.type == JobType.waiter){
//			c = new WaiterRole(this.getName());
//			r.panel.customerPanel.addTeller((Teller) c);
		}
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
	}

	private void WorkAtBank() {
		gui.DoGoToLocation(80,76);
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
		
		
		Role c = null;
		Bank r = null;
		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.bank)
				{
					r = (Bank) b;
				}
			}
		}
	
		
		if (job.type == JobType.bankHost && r.host == null)
		{
			c = new BankHostRole(this.getName());
			r.host = (BankHostRole) c;
			r.panel.customerPanel.addHost((BankHost) c);
		}
		if (job.type == JobType.teller){
			c = new TellerRole(this.getName());
			r.panel.customerPanel.addTeller((Teller) c);
		}

		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
	}

	private void GoToRestaurant()
	{
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to restaurant");
		Status.setNourishment(nourishment.goingToFood);


//		TODO
//		if(Status.getTransportationStatus() == transportStatus.bus){
//			curStop = this.closestBusStop();
//			destinationStop = metro.stops.get(2); // two is the busStop closest to restaurant top left is 0, top right is 6
//			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
//			gui.setPresent(false);
//			curStop.msgImAtStop(this);
//			this.WaitForBus();
//		}
//		else
//			closestCheckpoint();
		
		gui.DoGoToLocation(264, 472);
		
			
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);

		//Role terminologies
		CustomerRole c = new CustomerRole(this.getName(), cash);
		c.setPerson(this);
		roles.add(c);
		//this.roles.get(0).setActivity(true);
		c.setActivity(true);

		//restaurants.get(0).panel.host.msgCheckForASpot((Customer)roles.get(0));


//		synchronized(buildings)
//		{
//			for (Building b: buildings){
//				//print(" type: " + b.getType() + " n: ");
//				if (b.getType() == buildingType.restaurant){
//					Restaurant r = (Restaurant) b;
//					r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
//					r.panel.customerPanel.addCustomer((Customer) c);
//				}
//			}
//		}
	}


	public void GoToWithdrawFromBank()
	{
		Status.setMoneyStatus(bankStatus.goingToBank);
	
		gui.DoGoToLocation(80,76);
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
		// Making this bank amount instead from msgBank
		//BankCustomerRole c = new BankCustomerRole(this.getName(), bankPurpose, bankAmount, money);
		BankCustomerRole c = new BankCustomerRole(this.getName(), bankPurpose, bankAmount, 50);
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
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to market to buy " + marketQuantity + " of " + marketPurpose);
		Status.market = marketStatus.waiting;

		gui.DoGoToLocation(280, 263);
		this.Status.setLocation(location.market);
		print("At market entrance");
		gui.setPresent(false);
		
		MarketCustomerRole c = new MarketCustomerRole(this.getName(), marketPurpose, marketQuantity, cash);

		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.market){
					//print("found market");
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

	public void setPersonList(Vector<PersonAgent> people) {
		this.people = people;
	}

	




}
