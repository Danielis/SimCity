package bank.test;


import bank.test.mock.*;
import bank.interfaces.*;
import bank.*;
import junit.framework.TestCase;
import city.*;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 */
public class HostTest extends TestCase
{
    //these are instantiated for each test separately via the setUp() method.
	MockTeller teller;
	MockTeller teller2;
	BankHostRole host;
    MockCustomer cust;
    MockCustomer cust2;
    PersonAgent per;
    Bank b;
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
            
            per = new PersonAgent("Person", "None", "Wealthy");
            host = new BankHostRole("host");
            cust = new MockCustomer("customer");
            cust2 = new MockCustomer("customer");
            teller = new MockTeller("tel");
            teller2 = new MockTeller("tel");
            teller.setTableNum(1);
            teller2.setTableNum(2);
            host.setPerson(per);
            b = new Bank();
            b.addTeller(teller);
            host.setTrackerGui(new TrackerGui());
    }  
    
    //TEST 1 - NORMATIVE: 1 TELLER, 1 CUSTOMER
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");
        	
        	assertEquals("Host should have no tellers", host.getmyTellers().size(), 0);
        	assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
        	host.setTellers(b.getTellers());
        	assertEquals("Host should have 1 teller", host.getmyTellers().size(), 1);
        	host.IWantService(cust);
        	assertEquals("Host should have 1 waiting customers. It doesn't.", host.getCustomers().size(), 1);
        	assertTrue("Host scheduler should return true", host.pickAndExecuteAnAction());
            assertTrue("Customer should receive payment message", cust.log.containsString("Received message GoToTeller from host"));
            assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
            }//end one normal customer scenario
 
    //TEST 2 - bank is closed scenario (no tellers)
    public void test2_NoTellers() throws Exception{
    	System.out.println("TEST 2");
    	
    	assertEquals("Host should have no tellers", host.getmyTellers().size(), 0);
    	assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
    	host.IWantService(cust);
    	assertEquals("Host should have 1 waiting customers. It doesn't.", host.getCustomers().size(), 1);
    	assertTrue("Host scheduler should return true", host.pickAndExecuteAnAction());
        assertTrue("Customer should receive payment message", cust.log.containsString("Received message BankIsClosed from host"));
        assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
        }//end one normal customer scenario

    // multiple customers
    public void test3_MultiCustomerScenario1Teller() throws Exception{
    	System.out.println("TEST 3");
    	
    	assertEquals("Host should have no tellers", host.getmyTellers().size(), 0);
    	assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
    	host.setTellers(b.getTellers());
    	assertEquals("Host should have 1 teller", host.getmyTellers().size(), 1);
    	host.IWantService(cust);
    	assertEquals("Host should have 1 waiting customers. It doesn't.", host.getCustomers().size(), 1);
    	host.IWantService(cust2);
    	assertEquals("Host should have 2 waiting customers. It doesn't.", host.getCustomers().size(), 2);
    	assertTrue("Host scheduler should return true", host.pickAndExecuteAnAction());
        assertTrue("Customer should receive payment message", cust.log.containsString("Received message GoToTeller from host"));
        assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 1);
    	host.IAmFree(teller);
    	assertTrue("Host scheduler should return true", host.pickAndExecuteAnAction());
        assertTrue("Customer should receive payment message", cust2.log.containsString("Received message GoToTeller from host"));
        assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
        }//end one normal customer scenario
    
    public void test4_MultiCustomer_MultiTeller() throws Exception{
    	System.out.println("TEST 3");
    	
    	assertEquals("Host should have no tellers", host.getmyTellers().size(), 0);
    	assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
    	host.setTellers(b.getTellers());
    	assertEquals("Host should have 1 teller", host.getmyTellers().size(), 1);
    	host.IWantService(cust);
    	assertEquals("Host should have 1 waiting customers. It doesn't.", host.getCustomers().size(), 1);
    	host.IWantService(cust2);
    	assertEquals("Host should have 2 waiting customers. It doesn't.", host.getCustomers().size(), 2);
    	assertTrue("Host scheduler should return true", host.pickAndExecuteAnAction());
        assertTrue("Customer should receive payment message", cust.log.containsString("Received message GoToTeller from host"));
        assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 1);
    	host.setTellers(b.getTellers());
    	assertTrue("Host scheduler should return true", host.pickAndExecuteAnAction());
        assertTrue("Customer should receive payment message", cust2.log.containsString("Received message GoToTeller from host"));
        assertEquals("Host should have 0 waiting customers.", host.getCustomers().size(), 0);
        }//end one normal customer scenario
}