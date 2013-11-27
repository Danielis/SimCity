package city.test;

import city.*;
import city.PersonAgent.JobType;
import city.PersonAgent.WealthLevel;
import city.guis.CityGui;
import junit.framework.TestCase;
import logging.TrackerGui;
import restaurant.*;
import restaurant.gui.*;
import restaurant.roles.*;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;

public class CityRunTests extends TestCase {
    //these are instantiated for each test separately via the setUp() method.
//    CashierAgent cashier;
//    MockWaiter waiter;
//    MockCustomer customer;
//    MockMarket market1;
//    MockMarket market2;
	
	PersonAgent person;
	CityGui gui;
	TrackerGui trackerWindow;
	Restaurant r;
    
    public void setUp() throws Exception{
    	
    	super.setUp();
    }  
    
    //TEST 1 - Checks the additon of people into the restaurant
    public void test1_TestCityRunScenario() throws Exception{
        	System.out.println("TEST 1");
        	        	//person = new PersonAgent("TestPerson", "Restaurant Host", "Average");
        	gui = new CityGui();
    		trackerWindow = new TrackerGui();
        	gui.cityPanel.addPerson("Test", "Restaurant Host", "Average");
        	assertEquals(gui.cityPanel.people.size(), 1);
        	assertEquals(gui.cityPanel.people.get(0).roles.size(), 0);
        	//gui.cityPanel.people.get(0).roles.add(new HostRole("TestHost", 1000));
        	//gui.cityPanel.people.get(0).WorkAtRest();
//        	person.job.type = JobType.noAI;
//        	person.wealthLevel = WealthLevel.average;
//        	assertTrue(person.pickAndExecuteAnAction());
//        	//person.WorkAtRest();
//        	assertFalse(person.pickAndExecuteAnAction());
//        	r = new Restaurant(new RestaurantGui(), "Test");
//        	assertEquals("Person's assigned role should be a hostrole", r.panel.host, person.roles.get(0));
    }
    
}