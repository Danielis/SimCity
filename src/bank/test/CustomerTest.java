package bank.test;

import java.util.concurrent.Semaphore;

import city.PersonAgent;
import bank.BankCustomerRole;
import bank.BankCustomerRole.customerPurpose;
import bank.BankCustomerRole.bankCustomerState;
import bank.test.mock.*;
import bank.Bank.*;
import logging.TrackerGui;
import bank.Bank;
import junit.framework.TestCase;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 */





// ********* Didn't have time to finish :( ************ //







public class CustomerTest extends TestCase
{
    //these are instantiated for each test separately via the setUp() method.
	MockTeller teller;
	Bank b;
    MockHost host;
    BankCustomerRole cust;
    BankCustomerRole cust2;
    PersonAgent per;
   
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");

            host = new MockHost("host");
            teller = new MockTeller("teller");
            cust = new BankCustomerRole("customer", "Deposit", 1500, 2500);
            cust2 = new BankCustomerRole("customer", "New Loan", 1500, 2500);
        	cust.h = host; 
            cust.setTrackerGui(new TrackerGui());
        	b = new Bank();
            
            teller.bank = b;
            per = new PersonAgent("Person", "None", "Wealthy");
            cust.setPerson(per);

            cust2.setPerson(per);
            
    }  
    
    //TEST 1 - CREATE ACCOUNT SCENARIO, normative
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	
    		//SET UP SCENARIO
        	
        	assertTrue("Customer should have a balance of 2500", cust.getBalance() == 2500);
        	assertTrue("Customer should want to deposit 1500 into new account", cust.getBankAmount() == 1500);
        	assertTrue("Customer should have a purpose to deposit into a new account", cust.getPurpose() == customerPurpose.deposit);
        	
        	cust.GoToTeller(teller);
        	assertTrue("Customer should have a state assigned", cust.getState() == bankCustomerState.assigned);
      
        	assertTrue("Customer scheduler should return true", cust.pickAndExecuteAnAction());
         	assertTrue("Customer should have a state assigned", cust.getState() == bankCustomerState.atCounter);
        	//assertTrue("Customer scheduler should return true", cust.pickAndExecuteAnAction());
         	//assertTrue("Customer should have a state waiting", cust.getState() == bankCustomerState.waiting);
        
        	//assertTrue("Teller should receive purpose message", 
        	//		teller.log.containsString("Received message DepositMoney from customer"));
           

            }//end one normal customer scenario
 
    //TEST 2 - account created
    public void test2_NormalCustomerScenario() throws Exception{
    	System.out.println("TEST 2");
    	Account a = teller.bank.createAccount(cust);
    	cust.t = teller;
        //SET UP SHOULD RUN BEFORE THIS FIRST TEST
	
		//SET UP SCENARIO
    	
    	assertTrue("Customer should have a balance of 2500", cust.getBalance() == 2500);
    	assertTrue("Customer should want to deposit 1500 into new account", cust.getBankAmount() == 1500);
    	assertTrue("Customer should have a purpose to deposit into a new account", cust.getPurpose() == customerPurpose.deposit);
    	assertTrue("Customer does not have an account", cust.getAccount() == null);
    	
    	cust.AccountCreated(a);
    	assertTrue("Customer does not have an account", cust.getAccount() != null);
  
    
    	//assertTrue("Teller should receive leaving message", 
    	//		teller.log.containsString("Received message IAmLeaving from customer"));
       

        }//end one normal customer scenario
    
    //TEST 2 - loan created
    public void test3_NormalCustomerScenario() throws Exception{
    	System.out.println("TEST 2");
    	Loan a = teller.bank.createLoan(cust2, 2000);
    	cust2.t = teller;
        //SET UP SHOULD RUN BEFORE THIS FIRST TEST
	
		//SET UP SCENARIO
    	assertTrue("Customer has no loan", cust.getLoan() == null);
    	assertTrue("Customer should have a balance of 2500", cust2.getBalance() == 2500);
    	assertTrue("Customer should want to take out a loan of 1500", cust2.getBankAmount() == 1500);
    	assertTrue("Customer should have a purpose to take out a loan", cust2.getPurpose() == customerPurpose.takeLoan);
   
    	cust2.LoanCreated(2500, a);
    	assertTrue("Customer now has a loan", cust2.getLoan() != null);
  
    
    	//assertTrue("Teller should receive leaving message", 
    	//		teller.log.containsString("Received message IAmLeaving from customer"));
       

        }//end one normal customer scenario
 
}