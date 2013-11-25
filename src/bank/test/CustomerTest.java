package bank.test;

import bank.BankCustomerRole;
import bank.test.mock.*;
import junit.framework.TestCase;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 */
public class CustomerTest extends TestCase
{
    //these are instantiated for each test separately via the setUp() method.
	MockTeller teller;
    MockHost host;
    BankCustomerRole cust;
    
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
          
            
            host = new MockHost("host");
          //  cust = new BankCustomerRole("customer");
            teller = new MockTeller("teller");
            
    }  
    
    //TEST 1 - WAITER GIVES CHECK, CUSTOMER PAYS (NORMAL)
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	
    		//SET UP SCENARIO
        	
        	
        	
//            customer.cashier = cashier; //You can do almost anything in a unit test. 
//            waiter.cashier = cashier;
//            
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

            }//end one normal customer scenario
 
        
}