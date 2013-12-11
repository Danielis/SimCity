package city.test;

import junit.framework.TestCase;
import logging.TrackerGui;
import restaurant.Restaurant;
import city.PersonAgent;
import city.guis.CityGui;
import city.test.mock.TestPerson;

public class PersonAITest extends TestCase{

	TestPerson person;
	CityGui gui;
	TrackerGui trackerWindow;
	Restaurant r;

	public void setUp() throws Exception{

		super.setUp();
	}
	
    public void test1_TestCityRunScenario() throws Exception
    {
    	System.out.println("TEST 1");
		person = new TestPerson("TestPerson", "Waiter", "Average");
    }

}
