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
import roles.Building;
import roles.Building.buildingType;
import roles.CustomerRole;
import roles.Restaurant;
import roles.Role;
import market.*;
import market.interfaces.MarketCustomer;




import city.guis.PersonGui.Coordinate; //trans: added for trans
import transportation.BusStopAgent; // needed for BusStop variable


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
	double money = 500;
	String name;
	PersonStatus Status = new PersonStatus();
	public Semaphore animSemaphore = new Semaphore(0, true);
	public Semaphore busSemaphore = new Semaphore(0, true);

	public CityAnimationPanel CityAnimPanel;	
	public RestaurantPanel restPanel;
	
	
	String bankPurpose, marketPurpose;
	double marketQuantity;
	double bankAmount;
	
	

	BusStopAgent destinationStop;//trans: added
	BusStopAgent curStop;//trans: addded
	boolean goingToStop;//trans: added can be made to work with bus States instead
	boolean gettingoff;//trans: added can be made to work with bus States instead
	
	public PersonAgent(String name){
		this.name = name;
		System.out.println("Added person " + name);
	}	
	
	//Class Declarations

	/*****************************************************************************
										CLASSES
	 ******************************************************************************/
	class PersonStatus
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

		public void setNourishment(nourishment state)
		{
			nour = state;
		}

		public nourishment getNourishmnet()
		{
			return nour;
		}

		public void setMoneyStatus(bankStatus state)
		{
			bank = state;
		}

		public bankStatus getMoneyStatus()
		{
			return bank;
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
	enum nourishment{notHungry,Hungry,goingToFood} // may not need goingToFood
	enum location{outside,home,restaurant,bank,market,transportation,work}
	enum destination{outside,home,restaurant,bank,market,transportation,work}
	enum workStatus{notWorking,working,onBreak,goingToWork}
	enum bankStatus{nothing,withdraw,deposit,owe,goingToBank}
	enum houseStatus{notHome,home,noHome,goingHome} //no home may be used for deadbeats
	enum marketStatus{nothing,buying,waiting}
	enum transportStatus{nothing, walking,car,bus}
	enum morality{good,bad} // may be used for theifs later on for non-norms
	//other potentials: rent, 


	/*****************************************************************************
									 UTILITIES
	 ******************************************************************************/


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
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/

	/*
	//Housing
	//Passes an inactive role which contains location and otherinfo needed later
	public void msgGoToHome(Role r){
<<<<<<< HEAD
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is     required. Otherwise don't need to add role.
=======
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is     required. Otherwise don�t need to add role.
>>>>>>> Transportation
	    Status.setHouse(houseStatus.goingHome);
	    stateChanged();
	}
	public void msgLeavingHome(Role r){
	    r.setInactive();
	    Status.setLoc(location.outside);
	    Status.setDes(destination.outside);
	    Status.setHouse(houseStatus.notHome);
	    gui.isVisible();
	    stateChanged();
	}*/

	//Restaurant
	public void msgGoToRestaurant(){ // sent from gui
		Status.setNourishment(nourishment.Hungry);
		Status.setDestination(destination.restaurant);
		gui.setPresent(false);
		stateChanged();
	}

	public void msgLeavingRestaurant(Role r, float myMoney){
		//	print("GOT HERE!!!!!!!!!!!!!!!!!!!!");
		r.setActivity(false);
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		Status.setNourishment(nourishment.notHungry);
		gui.setPresent(true);
		gui.DoGoToCheckpoint('D');
		gui.DoGoToCheckpoint('C');
		gui.DoGoToCheckpoint('B');
		gui.DoGoToCheckpoint('A');
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
		gui.DoGoToCheckpoint('D');
		roles.remove(r);
	}
	//Transportation
	
	public void msgAtBusStop(){
		//this.DoneWithBus(); // msgBusStopReached() should release agent to do other actions
		gettingoff = true;
		print(this.name + "Going to Restaurant now since I reached bus Stop");
		stateChanged();
	}
	public void msgGoToStop(BusStopAgent curStop,BusStopAgent dest){
		print("Person going to STOP");
		this.curStop = curStop;
		this.destinationStop = dest;
		this.goingToStop = true;
		stateChanged();
	}
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
	/*
	//Work
	public void msgGoToWork(Role r){
<<<<<<< HEAD
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don't need to add role.
=======
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
>>>>>>> Transportation
	    Status.setWork(workStatus.goingToWork);
	    stateChanged();
	}
	public void msgLeavingWork(Role r){
	    r.setInactive();
	    Status.setLoc(location.outside);
	    Status.setDes(destination.outside);
	    Status.setWork(workStatus.notWorking);
	    gui.isVisible();
	    stateChanged();
	}

	//Banks
	public void goToBank(Role r){
<<<<<<< HEAD
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don't need to add role.
=======
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
>>>>>>> Transportation
	    Status.setBank(bankStatus.goingToBank);
	    stateChanged();
	}
	public void leavingBank(Role r){
	    r.setInactive();
	    Status.setLoc(location.outside);
	    Status.setDes(destination.outside);
	    Status.setBank(bankStatus.nothing);
	    gui.isVisible();
	    stateChanged();
	}
	//Markets
	public void goToMarket(Role r){
<<<<<<< HEAD
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don't need to add role.
=======
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
>>>>>>> Transportation
	    Status.setMarket(marketStatus.goingToMarket);
	    stateChanged();
	}
	public void leavingMarket(Role r){
	    r.setInactive();
	    Status.setLoc(location.outside);
	    Status.setDes(destination.outside);
	    Status.setMarket(marketStatus.nothing);
	    gui.isVisible();
	    stateChanged();
	}
	 */
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/

	@Override
	protected boolean pickAndExecuteAnAction() {
		//Not exactly sure where this next bit of code has to go or when it will be called
		// it is called when a person needs to go to work and the SimCity has determined that it has to
		// take a Bus to get somehere so perhaps before other actions are performed. Will need to work this out in PersonAgent later on
		if(goingToStop){
			ActionGoToBusStop();
		}
		if(gettingoff){
			GoToRestaurant();
		}
		if (Status.getNourishmnet() == nourishment.Hungry &&
				Status.getLocation() == location.outside) {
			GoToRestaurant();
			return true;
		}
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
	private void GoToRestaurant()
	{
		print("Going to restaurant");
		Status.setNourishment(nourishment.goingToFood);
		//Transportation t = ChooseTransportation();
		//gui.DoGoToCheckpoint('A');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('C');

		gettingoff = false; // state change which will be Bus State
		
		gui.DoGoToCheckpoint('D');
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


		/*
		Restaurant r = PickARestaurant();
		//Transportation t = ChooseTransportation();
		//DoGoTo(r.location, t);
		CustomerRole c = new CustomerRole(this.getName());
		roles.add(c);
		c.setActivity(true);
		r.host.msgCheckForASpot(c);*/

		/*
		Restaurant r = restaurants.ChooseOne() ; //restaurants comes from the contact list
	    TransportationMethod tm = PickOne(r);    //Someone has to do this.
	    DoGoTo(r.location, tm);                  //It's probably more complicated than this.
	    Role c = SimCity201.CustomerFactory(r.customerRole); 
	    roles.add(c);
	    c.active = T;
	    r.getHost().ImHungry((Customer) c);
		 */

	}
	private void ActionGoToBusStop(){
		goingToStop = false;
		gui.DoGoToLocation(curStop.getGui().getXPosition(),curStop.getGui().getYPosition());
		gui.setPresent(false);
		curStop.msgImAtStop(this);
		//WaitForBus();
	
	}

	public void GoToWithdrawFromBank()
	{
		Status.setMoneyStatus(bankStatus.goingToBank);
		gui.DoGoToCheckpoint('A');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('C');
		//gui.DoGoToCheckpoint('D');
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
