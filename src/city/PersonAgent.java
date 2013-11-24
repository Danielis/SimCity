package city;

//Package Imports
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

	public CityAnimationPanel copyOfCityAnimPanel;	
	public RestaurantPanel restPanel;


	public PersonAgent(String name){
		this.name = name;
		System.out.println("Added person " + name);

	}

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
		copyOfCityAnimPanel = panel;
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

	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/

	/*
	//Housing
	//Passes an inactive role which contains location and otherinfo needed later
	public void msgGoToHome(Role r){
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is     required. Otherwise don't need to add role.
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

	public void msgGoToBank()
	{
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
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		gui.setPresent(true);
		gui.DoGoToCheckpoint('D');
		roles.remove(r);
		stateChanged();
	}
	
	public void msgGoToMarket()
	{
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
		Status.setLocation(location.outside);
		Status.setDestination(destination.outside);
		gui.setPresent(true);
		gui.DoGoToCheckpoint('D');
		roles.remove(r);
		stateChanged();
	}
	/*
	//Work
	public void msgGoToWork(Role r){
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don't need to add role.
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don't need to add role.
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don't need to add role.
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
	//Transportation

	//Figure out how we are going to incorporate Bus,Car and walking into SimCity

	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/

	@Override
	protected boolean pickAndExecuteAnAction() {

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
				print(" type: " + b.getType() + " n: ");
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

	public void GoToWithdrawFromBank()
	{
		Status.setMoneyStatus(bankStatus.goingToBank);
		gui.DoGoToCheckpoint('A');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('C');
		//gui.DoGoToCheckpoint('D');
		this.Status.setLocation(location.bank);
		gui.setPresent(false);

		BankCustomerRole c = new BankCustomerRole(this.getName(), "New Account", 20, money);
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
		
		MarketCustomerRole c = new MarketCustomerRole(this.getName(), "Eggs", 4, money);
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
