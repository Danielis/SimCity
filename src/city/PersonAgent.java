package city;

import java.util.ArrayList;
import java.util.Collections;
import restaurant.interfaces.*;
import agent.Agent;
import java.util.*;
import java.util.concurrent.Semaphore;

public class PersonAgent extends Agent implements Person
{
	//VARIABLES*********************************************
	
	List<Role> myRoles = new Collections.synchronizedList(new ArrayList<Role>());
	add to myRoles and delete Role access functions needed
	PersonGui gui;
	Table_globalinfo_home_locations_restaurants_banks_...
	double money;
	
	class Role{
		
	}

	enum nourishmnet{notHungry,Hungry,goingToFood} // may not need goingToFood
	enum curLoc{outside,home,restaurant,bank,market,transportation,work}
	enum curDes{outside,home,restaurant,bank,market,transportation,work}
	enum workStatus{notWorking,working,break,goingToWork}
	enum bankStatus{nothing,withdraw,deposit,owe,goingToBank}
	enum houseStatus{notHome,home,noHome,goingHome} //no home may be used for deadbeats
	enum marketStatus{nothing,buying,waiting}
	enum transportStatus{walking,car,bus}
	enum mortality{good,bad} // may be used for theifs later on for non-norms

	PersonStatus Status = new PersonStatus(___default parameters__);

	class PersonStatus{
	nourishment nour;
	curLoc loc;
	curDes des;
	workStatus work;
	bankStatus bank;
	houseStatus house;
	marketStatus market;
	transportStatus trans;
	mortality moral;    
	… // will require constructor and set and gets for the components
	}
}
