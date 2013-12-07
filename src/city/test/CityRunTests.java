package city.test;

import city.*;
import city.PersonAgent.JobType;
import city.PersonAgent.WealthLevel;
import city.guis.CityGui;
import junit.framework.TestCase;
import logging.TrackerGui;
import restaurant.*;
import restaurant.gui.*;
import restaurant.interfaces.Waiter;
import restaurant.roles.*;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import roles.Role;

public class CityRunTests extends TestCase {
	
	PersonAgent person;
	CityGui gui;
	TrackerGui trackerWindow;
	Restaurant r;
    public void setUp() throws Exception{
    	
    	super.setUp();
    }  
    
    //TEST 1 - Checks the additon of people into the restaurant
    public void test1_TestCityRunScenario() throws Exception
    {
    	System.out.println("TEST 1");
    	gui = new CityGui();
		trackerWindow = new TrackerGui();
    	gui.cityPanel.addPerson("Test", "Restaurant Host", "Average");
    	assertEquals(gui.cityPanel.people.size(), 1);
    	assertEquals(gui.cityPanel.people.get(0).roles.size(), 0);
    }
    
    //Testing the addition of Roles, creation of Restaurants
    public void test2_TestAddingRoles() throws Exception
    {
    	System.out.println("TEST 2");
    	person = new PersonAgent("Test", "No Ai", "Average");
    	person.addRole(new HostRole("Test", 1000));
    	assertEquals("Person should have only one role", person.roles.size(), 1);
    	Restaurant testRestaurant = new Restaurant(new RestaurantGui(), "Norman");
    	Role t = new TraditionalWaiterRole("Test", testRestaurant, 100);
    	person.addRole(t);
    	assertEquals("Person should now have two roles", person.roles.size(), 2);
    	testRestaurant.addWaiter((Waiter)t);
    	assertEquals("The test restaurant should have only one waiter", testRestaurant.panel.waiters.size(), 1);
    }
}