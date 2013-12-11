package city.test;

import java.util.concurrent.Semaphore;

import city.*;
import city.PersonAgent.JobType;
import city.PersonAgent.WealthLevel;
import city.guis.CityGui;
import junit.framework.TestCase;
import logging.TrackerGui;
import market.Market;
import restaurant.*;
import restaurant.gui.*;
import restaurant.interfaces.Waiter;
import restaurant.roles.*;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import restaurantA.RestaurantA;
import restaurantA.TraditionalWaiterAgent;
import roles.Role;
import bank.*;
import bank.gui.BankGui;
import bank.interfaces.Teller;
public class CityRunTests extends TestCase {
	
	PersonAgent person;
	CityGui gui;
	TrackerGui trackerWindow;
	Restaurant r;
	
    public void setUp() throws Exception{
    	
    	super.setUp();
    }  
    
    //TEST 1 - Checks the addition of people into the restaurant
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
    public void test2_TestAddingRolesRestN() throws Exception
    {
    	System.out.println("TEST 2");
    	person = new PersonAgent("Test", "No AI", "Average");
    	person.addRole(new HostRole("Test", 1000));
    	assertEquals("Person should have only one role", person.roles.size(), 1);
    	Restaurant testRestaurant = new Restaurant(new RestaurantGui(), "Norman");
    	Role t = new TraditionalWaiterRole("Test", testRestaurant, 100);
    	person.addRole(t);
    	assertEquals("Person should now have two roles", person.roles.size(), 2);
    	testRestaurant.addWaiter((Waiter)t);
    	assertEquals("The test restaurant should have only one waiter", testRestaurant.panel.waiters.size(), 1);
    }
    
    public void test3_TestAddingRolesBank() throws Exception
    {
    	System.out.println("TEST 3");
    	person = new PersonAgent("Test", "No AI", "Average");
    	assertEquals("Person should haven't have any roles", person.roles.size(), 0);
    	person.addRole(new BankHostRole("Test"));
    	assertEquals("Person should have only one role", person.roles.size(), 1);
    	Bank testBank = new Bank(new BankGui(), "Aleena");
    	assertTrue("The test restaurant should not have a host", testBank.workingHost == null);
    	testBank.workingHost = (BankHostRole) person.roles.get(0);
    	assertTrue("The test restaurant should have a host", testBank.workingHost != null);
    	Role t = new TellerRole("Test");
    	person.addRole(t);
    	assertEquals("Person should now have two roles", person.roles.size(), 2);
    	testBank.addTeller((Teller) t);
    	assertEquals("The test restaurant should have only one teller", testBank.workingTellers.size(), 1);
    }
    
    public void test4_TestAddingRolesRestA() throws Exception
    {
    	System.out.println("TEST 4");
    	person = new PersonAgent("Test", "No AI", "Average");
    	person.addRole(new restaurantA.HostAgent("Test"));
    	assertEquals("Person should have only one role", person.roles.size(), 1);
    	RestaurantA testRestaurant = new RestaurantA(new restaurantA.gui.RestaurantGui(), "Aleena");
    	assertEquals("The test restaurant should have no waiters", testRestaurant.workingWaiters.size(), 0);
    	assertTrue("The test restaurant should not have a host", testRestaurant.workingHost == null);
    	testRestaurant.workingHost = (restaurantA.HostAgent) person.roles.get(0);
    	assertTrue("The test restaurant should have a host", testRestaurant.workingHost != null);
    	Role t = new TraditionalWaiterAgent("Test", testRestaurant, 100);
    	person.addRole(t);
    	assertEquals("Person should now have two roles", person.roles.size(), 2);
    	testRestaurant.setWorkingWaiter((restaurantA.WaiterAgent) t);
    	testRestaurant.setWaiter();
    	assertEquals("The test restaurant should have only one waiter", testRestaurant.workingWaiters.size(), 1);
    }    
    
    public void test5_TestAddingRolesMarket() throws Exception
    {
    	System.out.println("TEST 5");
    	person = new PersonAgent("Test", "No AI", "Average");
    	person.addRole(new market.MarketCustomerRole("Test", "Car", 1, 1000, "None"));
    	assertEquals("Person should have only one role", person.roles.size(), 1);
    	Market testMarket = new Market("Aleena", new market.gui.MarketGui()); 
    	
   }  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}