package city;

//Package Imports
import restaurant.CustomerAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Customer;
import agent.Agent;
import restaurant.gui.CustomerGui;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import roles.CustomerRole;
import roles.Restaurant;
import roles.Role;




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
	Vector<Restaurant> restaurants = new Vector<Restaurant>(); //City Gui won't let me implement Lists
	//For housing: List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	
	//Variable
	Restaurant currentRestaurant; //Restaurant that the person chooses
	PersonGui gui = null;
	double money;
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
	
	public void setRestaurants(Vector<Restaurant> res)
	{
		restaurants = res;
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
		copyOfCityAnimPanel = panel;
	}

	public PersonGui getGui() {
		return gui;
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is     required. Otherwise don’t need to add role.
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
	    gui.setPresent(false);
	    stateChanged();
	}
	
	public void msgLeavingRestaurant(Role r){
	    r.setActivity(false);
	    Status.setLocation(location.outside);
	    Status.setDestination(destination.outside);
	    Status.setNourishment(nourishment.notHungry);
	    gui.setPresent(true);
	    stateChanged();
	}
	
	/*
	//Work
	public void msgGoToWork(Role r){
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don’t need to add role.
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don’t need to add role.
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don’t need to add role.
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

		Boolean anytrue = false;
		for(Role r : roles)
		{
			if(r.active)
			{
				anytrue = anytrue || r.pickAndExecuteAnAction();
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
		Status.setNourishment(nourishment.goingToFood);
		//Transportation t = ChooseTransportation();
		//gui.DoGoToCheckpoint('A');
		//gui.DoGoToCheckpoint('B');
		//gui.DoGoToCheckpoint('C');
		gui.DoGoToCheckpoint('D');
		this.Status.setLocation(location.restaurant);
		gui.setPresent(false);
		
		//Role terminologies
		CustomerRole c = new CustomerRole(this.getName());
		c.setPerson(this);
		/*c.setHost(restaurants.get(0).panel.host);
		c.setCashier(restaurants.get(0).panel.cashier);
		CustomerGui gui1 = new CustomerGui(c, restaurants.get(0).gui);
		c.setGui(gui1);
		restaurants.get(0).gui.animationPanel.addGui(gui1);
		c.setAnimPanel(restaurants.get(0).gui.animationPanel);
		restaurants.get(0).panel.customers.add(c);*/
		roles.add(c);
		this.roles.get(0).setActivity(true);
		
		
		//restaurants.get(0).panel.host.msgCheckForASpot((Customer)roles.get(0));
		restaurants.get(0).panel.customerPanel.customerHungryCheckBox.setSelected(true);
		//restaurants.get(0).panel.host.msgCheckForASpot((Customer)roles.get(0));
		//restaurants.get(0).panel.customerPanel.customerHungryCheckBox.setSelected(true);
		restaurants.get(0).panel.customerPanel.addCustomer(this.getName());
		
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
	
	private Restaurant PickARestaurant()
	{
		return restaurants.get(0); //Only my restaurant is in here right now
	}
}
