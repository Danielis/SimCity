package housing.test;

import junit.framework.TestCase;
import logging.TrackerGui;
import restaurant.CashierAgent;
import restaurant.CashierAgent.CheckState;
import restaurant.CashierAgent.PaymentState;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockWaiter;
import restaurant.test.mock.MockMarket;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 */
/*
public class LandlordTest extends TestCase
{
    //these are instantiated for each test separately via the setUp() method.
    LandlordAgent landlord;
    MockHousingCustomer customer;
    MockHousingWorker worker;
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
            landlord = new LandlordAgent();                
            customer = new MockHousingCustomer("mockcustomer");                
            worker = new MockHousingWorker("worker");
    }  
    
    //TEST 1 - WAITER GIVES CHECK, CUSTOMER PAYS (NORMAL)
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

            setUp();
    	
    		//SET UP SCENARIO
            landlord.addCustomer(customer);
            landlord.addWorker(worker);
            
            //PRECONDITIONS
            assertEquals("Landlord should have one apartment complex.", landlord.complexes.size(), 1);
            assertEquals("Landlord should have no payments.", landlord.payments.size(), 0);
            assertEquals("Landlord should have one maintenance worker.", landlord.workers.size(), 1);
            assertEquals("Landlord should have a balance of 10000", landlord.balance, 10000.0);
            
            //Part 1...add a payment
            landlord.EveryoneOwesRent();
            landlord.setTrackerGui(new TrackerGui());
            assertEquals("Landlord should have one payment now.", landlord.payments.size(), 1);
            assertTrue("This payment should be under the created state.", landlord.payments.get(0).s == paymentState.created);
            //Assert the scheduler can now return true
            assertTrue("Scheduler should have something to do.", landlord.pickAndExecuteAnAction());
            //this should call send bill
            assertTrue("The payment should now be under the issued state.", landlord.payments.get(0).s == paymentState.issued);
            //mockhousing customer should have received the message
            assertEquals("MockHousingCustomer should now have a logged event saying it received the here is rent bill message.", "HereIsRentBill", customer.log.getLastLoggedEvent().getMessage());
            
            //part 2...receive
            landlord.HereIsRent(customer, 1000.0);
            assertTrue("This payment should be under the paying state.", landlord.payments.get(0).s == paymentState.paying);
            assertEquals("The landlord should now have 11000", landlord.balance, 11000.0);
            assertTrue("Scheduler should have something to do.", landlord.pickAndExecuteAnAction());
            //this should call update bill
            assertTrue("The payment should be under the completed state.", landlord.payments.get(0).s == paymentState.completed);
            assertEquals("MockHousingCustomer should now have a logged event saying it received the here is rent bill message.", "RentIsPaid", customer.log.getLastLoggedEvent().getMessage());

            
    }//end one normal customer scenario
    
    //TEST 2 - WAITER GIVES CHECK, CUSTOMER PAYS (NORMAL)
    public void test2_NormalWorkerScenario() throws Exception{
        	System.out.println("TEST 2");

            setUp();
    	
    		//SET UP SCENARIO
            landlord.addCustomer(customer);
            landlord.addWorker(worker);
            
            //PRECONDITIONS
            assertEquals("Landlord should have one apartment complex.", landlord.complexes.size(), 1);
            assertEquals("Landlord should have no tickets.", landlord.tickets.size(), 0);
            assertEquals("Landlord should have one maintenance worker.", landlord.workers.size(), 1);
            assertEquals("Landlord should have a balance of 10000", landlord.balance, 10000.0);
            
            //Part 1...ticket
            landlord.MyHouseNeedsRepairs(customer);
            landlord.setTrackerGui(new TrackerGui());
            assertEquals("Landlord should have one ticket now.", landlord.tickets.size(), 1);
            assertTrue("This payment should be under the created state.", landlord.tickets.get(0).s == ticketStatus.unassigned);
            //Assert the scheduler can now return true
            assertTrue("Scheduler should have something to do.", landlord.pickAndExecuteAnAction());
            //this should call send bill
            assertTrue("The payment should now be under the issued state.", landlord.tickets.get(0).s == ticketStatus.assigned);
            //mockhousing customer should have received the message
            assertEquals("MockHousingWorker should now have a logged event saying it received the here is go repair.", "GoRepair", worker.log.getLastLoggedEvent().getMessage());
            
            //part 2...worker wants payment
            landlord.RepairsCompleted(landlord.complexes.get(0), 400.0);
            assertTrue("This ticket should be under the completed state.", landlord.tickets.get(0).s == ticketStatus.completed);
            assertEquals("The landlord should have 10000", landlord.balance, 10000.0);
            assertTrue("Scheduler should have something to do.", landlord.pickAndExecuteAnAction());
            //this should call update bill
            assertTrue("The ticket should be under the paid state.", landlord.tickets.get(0).s == ticketStatus.paid);
            assertEquals("MockHousingWorker should now have a logged event saying it received HereIsMoney Message.", "HereIsMoney", worker.log.getLastLoggedEvent().getMessage());
            assertEquals("The landlord should have 9600.0", landlord.balance, 9600.0);
    }//end one normal customer scenario
        
}
*/