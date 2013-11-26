package bank.test;

import bank.BankCustomerRole;
import bank.BankCustomerRole.customerPurpose;
import bank.test.mock.*;
import bank.Bank.*;
import bank.Bank;
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
	Bank b;
    MockHost host;
    BankCustomerRole cust;
    
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
          
            
            host = new MockHost("host");
            teller = new MockTeller("teller");
            
            teller.bank = b;
          
            
    }  
    
    //TEST 1 - CREATE ACCOUNT SCENARIO, normative
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	
    		//SET UP SCENARIO
            cust = new BankCustomerRole("customer", "New Account", 1500, 2500);
        	cust.h = host; 
        	host.IWantService(cust);
        	Account acct = null;
        	
        	assertTrue("Customer should have a balance of 2500", cust.getBalance() == 2500);
        	assertTrue("Customer should want to deposit 1500 into new account", cust.getBankAmount() == 1500);
        	assertTrue("Customer should have a purpose to create a new account", cust.getPurpose() == customerPurpose.createAccount);
        	//cust.AccountCreated(null);
        //	assertTrue("Customer should have run an action", cust.pickAndExecuteAnAction());
        	
        	
        	
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