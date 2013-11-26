package bank.test;

import bank.*;
import city.*;
import bank.test.mock.*;
import bank.interfaces.*;
import junit.framework.TestCase;
import bank.Bank;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 */
public class TellerTest extends TestCase
{
    //these are instantiated for each test separately via the setUp() method.
	TellerRole teller;
    MockHost host;
    MockCustomer cust;
    PersonAgent per;
    Bank b;
    
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
            
            teller = new TellerRole();
            host = new MockHost("host");
            cust = new MockCustomer("customer");
            
            per = new PersonAgent("Person", "None", "Wealthy");     
            teller.setPerson(per);
            
            b = new Bank();
            teller.setBank(b);
    }  
    
    //TEST 1 - WAITER GIVES CHECK, CUSTOMER PAYS (NORMAL)
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	//teller.IWantAccount(cust, 200);
    		//SET UP SCENARIO
        	
       
        	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
        	System.out.println("tell" + teller);
        	System.out.println("cust" + cust);
        	teller.IAmLeaving();
        	teller.IWantAccount(cust, 200);
        	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
        	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
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