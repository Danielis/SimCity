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
import restaurant.Restaurant;
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
import city.PersonAgent.Item;
import city.PersonAgent.JobType;
import city.PersonAgent.WealthLevel;
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
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
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

public interface Person
{
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/

	 //Lists: Roles, Restaurants
	public List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	public Vector<Building> buildings = new Vector<Building>(); //City Gui won't let me implement Lists
	public List<Item> inventory = Collections.synchronizedList(new ArrayList<Item>());
	public double setWealth();
	public double getCashThresholdUp();	
	public double getCashThresholdLow();
	public double getMoneyThreshold();
	public WealthLevel parseWealth(String wealth);
	public JobType parseJob(String job);	
	public void setThreshold(int q);
	public String getType();
	public int getQuantity();
	public void removeItem();
	public void setTrackerGui(TrackerGui t);
	public List<Item> getInvetory();
	public int getBound_leftx();
	
	public int getBound_rightx();
	
	public int getBound_topy();
	
	public int getBound_boty();

	public void addRole(Role r);

	public void removeRole(Role r);

	public String getName();

	public void setGui(PersonGui g);

	public void setAnimationPanel(CityAnimationPanel panel);

	public PersonGui getGui();

	public boolean hasCar();

	public void WaitForAnimation();
	public void DoneWithAnimation();
	public void WaitForBus();
	public void DoneWithBus();
	public void setMetro(TransportationCompanyAgent m);
	public BusStopAgent closestBusStop();
	public double checkBusStopDistance(BusStopAgent x);
	public void closestCheckpoint();
	public double checkPointDistance(int x, int y);
	public void transportationStatusBus();
	public void msgWakeUp();
	public void msgGetPaid();
	public void msgLeaveWork();
	public void msgLeftWork(Role r, double balance);	
	public void msgLeavingHome(Role r, double balance);
	public void msgGoToWork();
	public void msgGoToHome(String purpose);
	public void msgGoToRestaurant();
	public void msgLeavingRestaurant(Role r, float myMoney);
	public void msgGoToBank(String purpose, double amt);
	public void msgLeavingBank(BankCustomerRole r, double balance);
	public void msgNewAccount(BankCustomerRole bankCustomerRole, Account acct);
	public void msgNewLoan(BankCustomerRole bankCustomerRole, Loan loan);
	public void msgAtBusStop();
	public void setDestinationStop(BusStopAgent P);
	public BusStopAgent getDestinationBusStop();
	public void setPosition(int X, int Y);
	public void msgGoToMarket(String purpose, double quantity);
	public void msgLeavingMarket(MarketCustomerRole r, double balance, String item, int quantRec);
	
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/

	@Override
	public boolean pickAndExecuteAnAction() {

		//Not exactly sure where this next bit of code has to go or when it will be called
		// it is called when a person needs to go to work and the SimCity has determined that it has to
		// take a Bus to get somehere so perhaps before other actions are performed. Will need to work this out in PersonAgent later on
		//If you're hungry and outside, go to the restaurant. Preliminary.

		if (job.type == JobType.noAI || noAI){	
			print("sched hit");
			if (Status.getWork() == workStatus.notWorking &&
					Status.getDestination() == destination.work) {
				print("Scheduler realized the person wants to go to work");
				GoToWork();
				return true;
			}
			if (Status.getNourishmnet() == nourishment.Hungry &&
					Status.getLocation() == location.outside && CheckRestOpen()) {
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

		//print(" role no active, should be true" + noRoleActive());
		//print("is gui busy, should be false" + gui.getBusy());
		//print("ai type, should be anything but NOAI" + job.type);
		//print("at work, should be anything be work" + Status.getWork());
		if (!gui.getBusy() && ! noAI && job.type != JobType.noAI && Status.getWork() != workStatus.working && noRoleActive()){	
			//	if (job.type != JobType.none && TimeManager.getInstance().getHour() > (Job.timeStart - 2) && TimeManager.getInstance().getHour() < Job.timeEnd){
			if (job.type != JobType.none && TimeManager.getInstance().getHour() > (0) && TimeManager.getInstance().getHour() < Job.timeEnd){
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

			if(needsToBuy()){
				GoToMarket();
				return true;
			}	

			if(isTired()){
				GoToSleep();
				return true;
			}


		}

		//TODO: THIS SECTION RELIES ON TIMERS / OUTSIDE MESSAGES	
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
				System.out.println("Caught Concurrent Modification error. Catching it and re-running action.");
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
		//TODO
		//if (!gui.getBusy()  && job.type != JobType.noAI){	//  && job.type != JobType.noAI used to have this
		//		WalkAimlessly();
		//		homePurpose = "Sleep";
		//		GoHomeToDoX();
		//	}
		if (!gui.getBusy() && job.type != JobType.noAI){
			WalkAimlessly();
			//			homePurpose = "Sleep";
			//				GoHomeToDoX();
		}
		return false;	

	}

	
	
	public void GoToSleep() {
		homePurpose = "Sleep";
		GoHomeToDoX();
	}

	public boolean CheckRestOpen() {
		print("I need to go to the restaurant!");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "I need to go to the restaurant!", new Date()));
		Restaurant r = null;
		synchronized(buildings) {
			for (Building b: buildings){
				if (b.getType() == buildingType.restaurant){
					//print("found b");
					r = (Restaurant) b;
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

	public void GoEat() {
		Boolean restaurant = false;
		if (CheckRestOpen()){
			int num = (int)(Math.random() * ((10 - 0) + 0));
			if (wealthLevel == WealthLevel.wealthy && num <= 9){
					restaurant = true;
			}
			else if (num <=7){
					restaurant = true;
			}
		}
		else
			restaurant = false;
		
		if (restaurant){
			GoToRestaurant();
		}
		else{
			homePurpose = "Cook";
			GoHomeToDoX();
		} 
	}

	public boolean isHungry() {
		if (TimeManager.getInstance().getCurrentSimTime() - timeSinceLastAte > 60000){
			print("Hmm... I'm hungry. I better eat soon");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "Hmm... I'm hungry. I better eat soon", new Date()));
			
			return true;
		}
		else
			return false;
	}
	
	public boolean isTired() {
		if (TimeManager.getInstance().getCurrentSimTime() - timeSinceLastSlept > 60000){
			print("Hmm... I'm tired. I better sleep soon");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.PERSON, "Person Agent", "Hmm... I'm tired. I better sleep soon", new Date()));
			
			return true;
		}
		else
			return false;
	}

	public void WalkAimlessly() {
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

		stateChanged();
	}

	public boolean noRoleActive() {
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

	public Boolean CheckBankOpen() {
		print("I need to go to the bank!");
		//trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "I need to go to the bank!", new Date()));
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
			//trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "Person Agent", "Aww... bank is closed :(", new Date()));

			return false;
		}
	}


	public void DelayGoToWork() {
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

	public boolean needsToBuy() {
		if (inventory.size() > 0){
			for (Item i : inventory){
				//print("type " + i.type + " quantHas " + i.quantity + " quantwnats" + i.threshold);
				if(i.quantity < i.threshold && canAfford(i)){
					marketPurpose = i.type;
					marketQuantity = i.threshold - i.quantity;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean canAfford(Item i){
		if (i.type == "Car")
			return (cash > 20000);
		else
			return true;
	}

	public boolean needsBankTransaction() {
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

	public double getTotalMoney() {
		double temp = cash;
		for (Account a : accounts){
			temp += a.getBalance();
		}
		return temp;
	}

	public boolean hasLoan() {
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

	public void GoHomeToDoX()
	{
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going home to " + homePurpose);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going home to " + homePurpose, new Date()));
		Status.setHousingStatus(houseStatus.goingHome);
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(0); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('G');
		gui.DoGoToCheckpoint('H');
		gui.DoGoToCheckpoint('I');
		
		this.Status.setLocation(location.home);
		gui.setPresent(false);
		
		//Role terminologies
		HousingCustomerRole c = new HousingCustomerRole(this.getName(), cash, inventory);
		c.setPerson(this);
		c.setTrackerGui(trackingWindow);
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
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to bank to " + bankPurpose, new Date()));
		Status.setMoneyStatus(bankStatus.goingToBank);

	//	gui.DoGoToLocation(80,76);
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(0); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('G');
		gui.DoGoToCheckpoint('J');
		gui.DoGoToCheckpoint('K');
		
		this.Status.setLocation(location.bank);
		
		if (CheckBankOpen()){
		gui.setPresent(false);
		BankCustomerRole c = new BankCustomerRole(this.getName(), bankPurpose, bankAmount, cash);
		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
		c.setTrackerGui(trackingWindow);
		r.panel.customerPanel.addCustomer((BankCustomer) c);
		}
		else 
			stateChanged();
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
			WorkAtRest();
		}
		if (job.type == JobType.landLord || job.type == JobType.repairman){
			WorkAtApartment();
		}

		Status.loc = location.work;

		
	}
	
	
	public void WorkAtApartment() {
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(0); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
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

		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(2); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('B');
		gui.DoGoToCheckpoint('A');
		Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);
		//Role c = null;
		Restaurant r = null;
		synchronized(buildings)
		{
			for (Building b: buildings){
				if (b.getType() == buildingType.restaurant)
				{
					r = (Restaurant) b;
				}
			}
		}

		//TODO: PLEASE ADD ADD HOST, COOK, CASHIER, WAITER FUNCTIONS HERE:
		if (job.type == JobType.restHost)
		{
			HostRole c = new HostRole(this.getName(), this.cash);
			r.panel.addHost((HostRole) c);
			c.setTrackerGui(trackingWindow);
			c.setPerson(this);
			roles.add(c);
			c.setActivity(true);
		}
		
		if (job.type == JobType.cook){
			CookRole c = new CookRole(this.getName(), this.cash);
			r.panel.addCook((CookRole) c);
			c.setPerson(this);
			c.setRestaurant(r);
			c.setTrackerGui(trackingWindow);
			roles.add(c);
			c.setActivity(true);
		}
		if (job.type == JobType.cashier){
			CashierRole c = new CashierRole(this.getName(), this.cash);
			r.panel.addCashier((CashierRole) c);
			c.setPerson(this);
			c.setRestaurant(r);
			c.setTrackerGui(trackingWindow);
			roles.add(c);
			c.setActivity(true);
		}
		if (job.type == JobType.waiter){
			waiterindex++;
			if (waiterindex % 2 == 0)
			{
				ModernWaiterRole c = new ModernWaiterRole(this.getName(), r, this.cash);
				r.panel.addWaiter((ModernWaiterRole) c);
				c.setPerson(this);
				c.setTrackerGui(trackingWindow);
				roles.add(c);
				c.setActivity(true);
			}
			else
			{
				TraditionalWaiterRole c = new TraditionalWaiterRole(this.getName(), r);
				r.panel.addWaiter((TraditionalWaiterRole) c);
				c.setPerson(this);
				c.setTrackerGui(trackingWindow);
				roles.add(c);
				c.setActivity(true);
			}
		}
	}

	public void WorkAtBank() {
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(0); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('G');
		gui.DoGoToCheckpoint('J');
		gui.DoGoToCheckpoint('K');
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
			c.setTrackerGui(trackingWindow);
			r.host = (BankHostRole) c;
			r.panel.customerPanel.addHost((BankHost) c);
		}
		if (job.type == JobType.teller){
			c = new TellerRole(this.getName());
			c.setTrackerGui(trackingWindow);
			r.panel.customerPanel.addTeller((Teller) c);
		}

		c.setPerson(this);
		roles.add(c);
		c.setActivity(true);
	}

	public void GoToRestaurant()
	{
		gui.setPresent(true);
		gui.setBusy(true);
		print("Going to restaurant");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to restaurant.", new Date()));
		Status.setNourishment(nourishment.goingToFood);


//		TODO
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(2); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('B');
		gui.DoGoToCheckpoint('A');
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);

		//Role terminologies
		CustomerRole c = new CustomerRole(this.getName(), cash);
		c.setPerson(this);
		roles.add(c);
		//this.roles.get(0).setActivity(true);
		c.setActivity(true);

		//restaurants.get(0).panel.host.msgCheckForASpot((Customer)roles.get(0));

		synchronized(buildings)
		{
			for (Building b: buildings){
				//print(" type: " + b.getType() + " n: ");
				if (b.getType() == buildingType.restaurant){
					Restaurant r = (Restaurant) b;
					r.panel.customerPanel.customerHungryCheckBox.setSelected(true);
					r.panel.customerPanel.addCustomer((Customer) c, r);
				}
			}
		}
	}


	public void GoToWithdrawFromBank()
	{
		Status.setMoneyStatus(bankStatus.goingToBank);
	
		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(0); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('G');
		gui.DoGoToCheckpoint('J');
		gui.DoGoToCheckpoint('K');
		//Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
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
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "Going to market to buy " + marketQuantity + " of " + marketPurpose, new Date()));

		Status.market = marketStatus.waiting;

		if(Status.getTransportationStatus() == transportStatus.bus && !hasCar()){
			curStop = this.closestBusStop();
			destinationStop = metro.stops.get(1); // two is the busStop closest to restaurant top left is 0, top right is 6
			gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
			gui.setPresent(false);
			curStop.msgImAtStop(this);
			this.WaitForBus();
		}
		else
			closestCheckpoint();
		
		gui.DoGoToCheckpoint('D');
		gui.DoGoToCheckpoint('E');
		gui.DoGoToCheckpoint('F');
		//Status.setWorkStatus(workStatus.working);
		this.Status.setLocation(location.bank);
		gui.setPresent(false);
		this.Status.setLocation(location.market);
		print("At market entrance");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "PersonAgent", "At market entrance", new Date()));

		gui.setPresent(false);
		
		MarketCustomerRole c = new MarketCustomerRole(this.getName(), marketPurpose, marketQuantity, cash);
		c.setTrackerGui(trackingWindow);
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

	public void setAI(Boolean noAI) {
		this.noAI = noAI;
	}

}
