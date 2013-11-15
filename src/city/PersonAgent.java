package city;

import java.util.ArrayList;
import java.util.Collections;

import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.interfaces.*;
import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.guis.CityAnimationPanel;
import city.guis.PersonGui;


public class PersonAgent extends Agent implements Person
{
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/

	//Notes for ourselves
	/*
	 * 
	 * 	//Table_globalinfo_home_locations_restaurants_banks_...

	 * 
	 */
	
	
	//Lists
	List<Role> myRoles = Collections.synchronizedList(new ArrayList<Role>());
	
	//Instances
	city.guis.PersonGui gui;
	double money;
	String name;
	PersonStatus Status = new PersonStatus();

	public CityAnimationPanel copyOfAnimPanel;	
	
	
	public PersonAgent(String name){
		this.name = name;
		System.out.println("Added person " + name);
	}
	
	public String getName(){
		return name;
	}
	//Class Declarations
	class Role
	{
		//Vairalbes
		PersonAgent myPerson;
		Boolean active;
		Boolean finishedRole;
		
		//Utilities
		public void setPerson(PersonAgent a)
		{
			myPerson = a;
		}
		
		public PersonAgent getPersonAgent()
		{
			return myPerson;
		}
		
		private void stateChanged()
		{
			myPerson.stateChanged();
		}
		
		public void setActivity(Boolean b)
		{
			active = b;
		}
	
		public Boolean getActivity()
		{
			return active;
		}
	}
	
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
		
		public nourishment getNourishment()
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
	enum morality{good,bad} // may be used for thieves later on for non-norms
	//other potentials: rent, 

	
	/*****************************************************************************
									 UTILITIES
	******************************************************************************/

	public void setGui(city.guis.PersonGui g)
	{
		this.gui = g;
	}
	
	
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/
	
	/*
	//Housing
	//Passes an inactive role which contains location and otherinfo needed later
	public void msgGoToHome(Role r){
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is     required. Otherwise don�t need to add role.
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
	public void msgGoToRestaurant(Role r){ // sent from gui
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
	    Status.setNourishment(nourishment.goingToFood);
	    stateChanged();
	}
	public void msgLeavingRestaurant(Role r){
	    r.setActivity(false);
	    Status.setLocation(location.outside);
	    Status.setDestination(destination.outside);
	    Status.setNourishment(nourishment.notHungry);
	    //gui.isVisible();
	    stateChanged();
	}


	
	/*
	//Work
	public void msgGoToWork(Role r){
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
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
	    //Utility function which checks myRoles to see if Role already exists will choose if add(r) is required. Otherwise don�t need to add role.
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
		
		
		// TODO basic ai stuff here
		// work stuff
		// deposit money, withdraw money, buy items, go home to eat, go home to watch TV
		
		
		// TODO Auto-generated method stub
		if (Status.getNourishment() == nourishment.goingToFood) {
			//GoToRestaurant();
		}
		return false;
	}


	public void setAnimationPanel(CityAnimationPanel panel) {
		copyOfAnimPanel = panel;
	}


	public PersonGui getGui() {
		// TODO Auto-generated method stub
		return gui;
	}

}
