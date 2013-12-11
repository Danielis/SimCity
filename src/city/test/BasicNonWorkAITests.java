package city.test;

import bank.Bank;
import junit.framework.TestCase;
import logging.TrackerGui;
import market.Market;
import restaurant.Restaurant;
import restaurant.roles.CustomerRole;
import city.guis.CityGui;
import city.test.mock.TestPerson;
import city.test.mock.TestPerson.JobType;
import city.test.mock.TestPerson.ProfessorState;
import city.test.mock.TestPerson.StudentState;
import city.test.mock.TestPerson.WealthLevel;
import city.test.mock.TestPerson.destination;
import city.test.mock.TestPerson.location;
import city.test.mock.TestPerson.workStatus;

public class BasicNonWorkAITests extends TestCase{
	
	TestPerson person;
	CityGui gui;
	TrackerGui trackerWindow;
	Restaurant r;

	public void setUp() throws Exception{
		super.setUp();
	}
	
    public void test1_BasicPersonWithNoAI() throws Exception
    {
    	System.out.println("TEST 1");
		person = new TestPerson("TestPerson", "No AI", "Average");
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a none", person.job.type == JobType.noAI);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		
		assertFalse("The person should not be running any actions", person.pickAndExecuteAnAction());
    }
    
    public void test2_PersonWithNoAIGoesToRestaurant() throws Exception
    {
    	System.out.println("TEST 2");
		person = new TestPerson("TestPerson", "No AI", "Average");
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a none", person.job.type == JobType.noAI);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		assertFalse("The person should not be running any actions", person.pickAndExecuteAnAction());
		
		MockRestaurant r = new MockRestaurant(null, "Norman");
		
		//This is triggered by the gui, not the scheduler
		person.GoToRestaurantN(r);
		
		assertTrue("The person now has a customer role in their list", person.roles.size() == 1);
		assertTrue("The person set for the role should be this one", person.roles.get(0).getTestPerson() == person);
		assertTrue("The role should be active now", person.roles.get(0).getActivity() == true);
		
		assertFalse("The person should have nothing to do now", person.pickAndExecuteAnAction());
    }
    
    public void test3_PersonWithNoAIGoesToBankClosed() throws Exception
    {
    	System.out.println("TEST 3");
		person = new TestPerson("TestPerson", "No AI", "Average");
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a none", person.job.type == JobType.noAI);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		assertFalse("The person should not be running any actions", person.pickAndExecuteAnAction());
		
		MockBank b = new MockBank(null, "Aleena");
		
		//This is triggered by the gui, not the scheduler
		person.GoToBank();
		
		assertFalse("The bank does not have any people isnide so technically it should not be open", b.isOpen());
		assertFalse("The person should have nothing to do now", person.pickAndExecuteAnAction());
    }
    
    public void test4_PersonWithNoAIGoesToBankOpen() throws Exception
    {
    	System.out.println("TEST 4");
		person = new TestPerson("TestPerson", "No AI", "Average");
		person.bankForce = true;
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a none", person.job.type == JobType.noAI);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		assertFalse("The person should not be running any actions", person.pickAndExecuteAnAction());
		
		MockBank b = new MockBank(null, "Aleena");
		//We force the bank to be open, even if it is not

		person.bankPurpose = "New Account";
		
		//This is triggered by the gui, not the scheduler
		person.GoToBank(b);
		
		//assertTrue(person.roles.size() == 1);
		assertTrue("The person should now be at the bank", person.Status.loc == location.bank);
		assertTrue("The person should have a role", person.roles.size() == 1);
		assertTrue("The person's role should be active", person.roles.get(0).getActivity());
    }
    
    public void test5_PersonWithNoAIGoesToMArket() throws Exception
    {
    	System.out.println("TEST 5");
		person = new TestPerson("TestPerson", "No AI", "Average");
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a none", person.job.type == JobType.noAI);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
		assertFalse("The person should not be running any actions", person.pickAndExecuteAnAction());
		
		MockMarket m = new MockMarket("Market", null);
		//We force the bank to be open, even if it is not

		person.marketPurpose = null;
		
		//This is triggered by the gui, not the scheduler
		person.GoToMarket(m);
		
		//assertTrue(person.roles.size() == 1);
		assertTrue("The person should now be at the market", person.Status.loc == location.market);
		assertTrue("The person should have a role", person.roles.size() == 1);
		assertTrue("The person's role should be active", person.roles.get(0).getActivity());
    }
}
