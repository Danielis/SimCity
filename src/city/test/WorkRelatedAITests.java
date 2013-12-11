package city.test;

import junit.framework.TestCase;
import logging.TrackerGui;
import restaurant.Restaurant;
import city.guis.CityGui;
import city.test.mock.TestPerson;
import city.test.mock.TestPerson.JobType;
import city.test.mock.TestPerson.ProfessorState;
import city.test.mock.TestPerson.StudentState;
import city.test.mock.TestPerson.WealthLevel;
import city.test.mock.TestPerson.destination;
import city.test.mock.TestPerson.location;
import city.test.mock.TestPerson.workStatus;

public class WorkRelatedAITests extends TestCase {


	TestPerson person;
	CityGui gui;
	TrackerGui trackerWindow;

	public void setUp() throws Exception{
		super.setUp();
	}
	
    public void test1_PersonIsCreatedProperly() throws Exception
    {
    	System.out.println("TEST 1");
		person = new TestPerson("TestPerson", "No AI", "Average");
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a waiter", person.job.type == JobType.noAI);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
    }
    
    public void test2_WaitersCreatedProperly() throws Exception
    {
    	System.out.println("TEST 2");
		person = new TestPerson("TestPerson", "Waiter", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a Waiter", person.job.type == JobType.waiter);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		MockRestaurant restaurant = new MockRestaurant(null, "Norman");
		person.job.workBuilding = restaurant;
		
		person.WorkAtRest(restaurant);
		
		assertTrue("The person should have one waiter role inside", person.roles.size() == 1);
		assertTrue("The role's person should be this", person.roles.get(0).getTestPerson() == person);
		assertTrue("The person should now be at the restaurant", person.Status.loc == location.restaurant);
		assertTrue("The role should be active in the restaurant", person.roles.get(0).getActivity() == true);
    }
    
    public void test3_RestaurantHostsCreatedProperly() throws Exception
    {
    	System.out.println("TEST 3");
		person = new TestPerson("TestPerson", "Restaurant Host", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a Restaurant Host", person.job.type == JobType.restHost);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		MockRestaurant restaurant = new MockRestaurant(null, "Norman");
		person.job.workBuilding = restaurant;
		
		person.WorkAtRest(restaurant);
		
		assertTrue("The person should have one waiter role inside", person.roles.size() == 1);
		assertTrue("The role's person should be this", person.roles.get(0).getTestPerson() == person);
		assertTrue("The person should now be at the restaurant", person.Status.loc == location.restaurant);
		assertTrue("The role should be active in the restaurant", person.roles.get(0).getActivity() == true);
    }
    
    public void test4_RestaurantCookCreatedProperly() throws Exception
    {
    	System.out.println("TEST 4");
		person = new TestPerson("TestPerson", "Cook", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a cook", person.job.type == JobType.cook);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		MockRestaurant restaurant = new MockRestaurant(null, "Norman");
		person.job.workBuilding = restaurant;
		
		person.WorkAtRest(restaurant);
		
		assertTrue("The person should have one waiter role inside", person.roles.size() == 1);
		assertTrue("The role's person should be this", person.roles.get(0).getTestPerson() == person);
		assertTrue("The person should now be at the restaurant", person.Status.loc == location.restaurant);
		assertTrue("The role should be active in the restaurant", person.roles.get(0).getActivity() == true);
    }
    
    public void test5_RestaurantCashierCreatedProperly() throws Exception
    {
    	System.out.println("TEST 5");
		person = new TestPerson("TestPerson", "Cashier", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a cashier", person.job.type == JobType.cashier);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		MockRestaurant restaurant = new MockRestaurant(null, "Norman");
		person.job.workBuilding = restaurant;
		
		person.WorkAtRest(restaurant);
		
		assertTrue("The person should have one waiter role inside", person.roles.size() == 1);
		assertTrue("The role's person should be this", person.roles.get(0).getTestPerson() == person);
		assertTrue("The person should now be at the restaurant", person.Status.loc == location.restaurant);
		assertTrue("The role should be active in the restaurant", person.roles.get(0).getActivity() == true);
    }
    
    public void test6_BankHostCreatedProperly() throws Exception
    {
    	System.out.println("TEST 6");
		person = new TestPerson("TestPerson", "Bank Host", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a Bank Host", person.job.type == JobType.bankHost);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		MockBank bank = new MockBank();
		person.job.workBuilding = bank;
		
		person.WorkAtBank(bank);
		
		assertTrue("The person should have one waiter role inside", person.roles.size() == 1);
		assertTrue("The role's person should be this", person.roles.get(0).getTestPerson() == person);
		assertTrue("The person should now be at the restaurant", person.Status.loc == location.bank);
		assertTrue("The role should be active in the restaurant", person.roles.get(0).getActivity() == true);
    }
    
    public void test6_BanktellerCreatedProperly() throws Exception
    {
    	System.out.println("TEST 6");
		person = new TestPerson("TestPerson", "Teller", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a Bank Host", person.job.type == JobType.teller);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		MockBank bank = new MockBank();
		person.job.workBuilding = bank;
		
		person.WorkAtBank(bank);
		
		assertTrue("The person should have one waiter role inside", person.roles.size() == 1);
		assertTrue("The role's person should be this", person.roles.get(0).getTestPerson() == person);
		assertTrue("The person should now be at the restaurant", person.Status.loc == location.bank);
		assertTrue("The role should be active in the restaurant", person.roles.get(0).getActivity() == true);
    }
}
