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

public class PersonRestaurantWorkTest extends TestCase {
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
    
    //TEST 1 - WAITER GIVES CHECK, CUSTOMER PAYS (NORMAL)
    public void test1_TestWorkScenario() throws Exception{
        	System.out.println("TEST 1");
        	
        	//Create the variables
        	//person = new PersonAgent("TestPerson", "Restaurant Host", "Average");
        	gui = new CityGui();
    		trackerWindow = new TrackerGui();
        	gui.cityPanel.addPerson("Test", "Restaurant Host", "Average");
        	r = new Restaurant(new RestaurantGui(), "Test");
        	r.panel = new RestaurantPanel(new RestaurantGui());
        	person.job.type = JobType.noAI;
        	person.wealthLevel = WealthLevel.average;
        	assertTrue(person.pickAndExecuteAnAction());
        	//person.WorkAtRest();
        	assertFalse(person.pickAndExecuteAnAction());
        	r = new Restaurant(new RestaurantGui(), "Test");
        	assertEquals("Person's assigned role should be a hostrole", r.panel.host, person.roles.get(0));
        	
        	
        	//r.panel.host = new HostRole("TestHost", 1000);
        	//r.panel.cashier = new CashierRole("TestCashier", 1000);
        	//r.panel.cook = new CookRole("TestCook", 1000);
        	//r.panel.waiters.add(new TraditionalWaiterRole("TestWaiter", r));


            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	
    		//SET UP SCENARIO
//            customer.cashier = cashier; //You can do almost anything in a unit test. 
//            waiter.cashier = cashier;
            
//            //PRECONDITIONS
//            assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.getChecks().size(), 0);
//            
//            //PART 1 - Add a check
//            cashier.msgHereIsACheck(waiter, customer, "Steak"); 
//            assertEquals("The cashier should have only one check. It doesn't.", cashier.getChecks().size(), 1);
//            assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
//            assertEquals("The cashier's customer should owe 15.99.", cashier.getChecks().get(0).owedAmount, 15.99f);
//            assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
//            assertEquals("The waiter should have one log item", 1, waiter.log.size());
//            assertEquals("The customer should have one log.", 0, customer.log.size());
//
//            //PART 2 - Pay the bill
//            assertEquals(cashier.getChecks().get(0).state, CheckState.computed);
//            cashier.msgHereIsMyPayment(customer, 15.99f);
//            assertTrue("The cashier should have ran an action", cashier.pickAndExecuteAnAction());
//            assertEquals("The cashier's cash should be 500+15.99", 515.99f, cashier.getAccountBalance());
//            assertEquals("The cashier should have no more checks now that the custoer paid.", cashier.getChecks().size(), 0);
//            assertFalse("The cashier's Action look should return false now", cashier.pickAndExecuteAnAction());
    }
    
}