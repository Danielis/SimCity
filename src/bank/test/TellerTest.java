package bank.test;

import bank.*;
import city.*;
import bank.test.mock.*;
import bank.interfaces.*;
import junit.framework.TestCase;
import logging.TrackerGui;
import bank.Bank.*;
import bank.TellerRole.*;
import bank.gui.*;
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
    TellerGui gui;
    
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
            teller.setGui(new TellerGui(teller, b.gui, 0));

            teller.setTrackerGui(new TrackerGui());
    }  
    
    //TEST 1 - Open new account with initial money deposit
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

      	
        	assertEquals("Bank should have 0 accounts", b.getAccounts().size(), 0);
        	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
        	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
        	teller.IWantAccount(cust, 200);
        	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
        	assertTrue("Teller's transaction should be type new account", 
        			teller.transactions.get(0).type == transactionType.newAccount);
        	assertTrue("Teller's transaction should be status unresolved", 
        			teller.transactions.get(0).status == transactionStatus.unresolved);
        	assertTrue("Teller's transaction amount be  200", teller.transactions.get(0).amount == 200);
        	assertTrue("Teller's transaction account should be 0", 
        			teller.transactions.get(0).getAccount().getBalance() == 0);
        	assertTrue("Teller's transaction account should have the right customer", 
        			teller.transactions.get(0).getAccount().getCustomer() == cust);
        	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
        	assertTrue("Customer should receive new account message", 
        			cust.log.containsString("Received message AccountCreated from teller"));
        	assertTrue("Teller's transaction should be status resolved", 
        			teller.transactions.get(0).status == transactionStatus.resolved);
        	assertTrue("Teller's transaction account should have 200", 
        			teller.transactions.get(0).getAccount().getBalance() == 200);
        	assertTrue("Teller's transaction account should have the right customer", 
        			teller.transactions.get(0).getAccount().getCustomer() == cust);
        	assertTrue("Teller's transaction account should have an id of 1", 
        			teller.transactions.get(0).getAccount().getID() == 1);
        	assertEquals("Bank now has 1 account", b.getAccounts().size(), 1);
        	assertTrue("Bank has correct ending balance", b.getBalance() == 50200);
     
            }//end one normal customer scenario
    
    //TEST 2 - Deposit money, no account, opens new account, then deposits
    public void test2_NormalCustomerScenario() throws Exception{
    	System.out.println("TEST 1");

  	
    	assertEquals("Bank should have 0 accounts", b.getAccounts().size(), 0);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	teller.DepositMoney(cust, 0, 1200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type deposit", 
    			teller.transactions.get(0).type == transactionType.deposit);
    	assertTrue("Teller's transaction should be status no account", 
    			teller.transactions.get(0).status == transactionStatus.noAccount);
    	assertTrue("Teller's transaction amount be  1200", teller.transactions.get(0).amount == 1200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive new account message", 
    			cust.log.containsString("Received message WantAccount from teller"));
    	teller.IWantAccount(cust, 1200);
    	assertEquals("Teller should have 2 transaction", teller.transactions.size() , 2);
    	assertTrue("Teller's transaction should be type new account", 
    			teller.transactions.get(1).type == transactionType.newAccount);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(1).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 200", teller.transactions.get(0).amount == 1200);
    	assertTrue("Teller's transaction account should be 0", 
    			teller.transactions.get(1).getAccount().getBalance() == 0);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(1).getAccount().getCustomer() == cust);
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive new account message", 
    			cust.log.containsString("Received message AccountCreated from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(1).status == transactionStatus.resolved);
    	assertTrue("Teller's transaction account should have 1200", 
    			teller.transactions.get(1).getAccount().getBalance() == 1200);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(1).getAccount().getCustomer() == cust);
    	assertTrue("Teller's transaction account should have an id of 1", 
    			teller.transactions.get(1).getAccount().getID() == 1);
    	assertEquals("Bank now has 1 account", b.getAccounts().size(), 1);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 51200);
    
        }//end one normal customer scenario

    // TEST 3 - try to withdraw, no account, create new account instead?
    public void test3_NormalCustomerScenario() throws Exception{
    	System.out.println("TEST 1");

  	
    	assertEquals("Bank should have 0 accounts", b.getAccounts().size(), 0);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	teller.WithdrawMoney(cust, 0, 2200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type withdrawal", 
    			teller.transactions.get(0).type == transactionType.withdrawal);
    	assertTrue("Teller's transaction should be status no account", 
    			teller.transactions.get(0).status == transactionStatus.noAccount);
    	assertTrue("Teller's transaction amount be  2200", teller.transactions.get(0).amount == 2200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive new account message", 
    			cust.log.containsString("Received message WantAccount from teller"));
    	teller.IWantAccount(cust, 2200);
    	assertEquals("Teller should have 2 transaction", teller.transactions.size() , 2);
    	assertTrue("Teller's transaction should be type new account", 
    			teller.transactions.get(1).type == transactionType.newAccount);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(1).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2200", teller.transactions.get(1).amount == 2200);
    	assertTrue("Teller's transaction account should be 0", 
    			teller.transactions.get(1).getAccount().getBalance() == 0);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(1).getAccount().getCustomer() == cust);
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive new account message", 
    			cust.log.containsString("Received message AccountCreated from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(1).status == transactionStatus.resolved);
    	assertTrue("Teller's transaction account should have 2200", 
    			teller.transactions.get(1).getAccount().getBalance() == 2200);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(1).getAccount().getCustomer() == cust);
    	assertTrue("Teller's transaction account should have an id of 1", 
    			teller.transactions.get(1).getAccount().getID() == 1);
    	assertEquals("Bank now has 1 account", b.getAccounts().size(), 1);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 52200);
    	
        }//end one normal customer scenario
        
    // TEST 5 - try to withdraw, has an account, enough balance
    public void test5_WithdrawPrevAccountScenario() throws Exception{
    	System.out.println("TEST 5");
    	Account acct = b.createAccount(cust);
    	acct.setBalance(20000);
    	b.addAccount(acct);
    	
    	assertEquals("Bank should have 1 account", b.getAccounts().size(), 1);
    	assertTrue("Account should have balance of 20,000", b.getAccounts().get(0).getBalance() == 20000);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.WithdrawMoney(cust, 0, 2200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type withdrawal", 
    			teller.transactions.get(0).type == transactionType.withdrawal);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2200", teller.transactions.get(0).amount == 2200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive withdrawal message", 
    			cust.log.containsString("Received message HereIsWithdrawal from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(0).status == transactionStatus.resolved);
    	assertTrue("Teller's transaction account should be less 2200", 
    			teller.transactions.get(0).getAccount().getBalance() == 17800);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(0).getAccount().getCustomer() == cust);
    	assertTrue("Teller's transaction account should have an id of 1", 
    			teller.transactions.get(0).getAccount().getID() == 1);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 47800);
    	
        }
    
    // TEST 4 - ask for new loan, good credit, creates loan
    public void test4_NormalCustomerScenario() throws Exception{
    	System.out.println("TEST 4");

    	assertEquals("Bank should have 0 accounts", b.getAccounts().size(), 0);
    	assertEquals("Bank should have 0 loans", b.getLoans().size(), 0);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.IWantLoan(cust, 2200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type new loan", 
    			teller.transactions.get(0).type == transactionType.newLoan);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2200", teller.transactions.get(0).amount == 2200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive new loan created", 
    			cust.log.containsString("Received message LoanCreated from teller"));
    	assertEquals("Bank now has 1 loan", b.getLoans().size(), 1);
    	assertTrue("Bank loan amount paid is 0", b.getLoans().get(0).getAmountPaid() == 0);
    	assertTrue("Bank loan is greater in value of 2200", b.getLoans().get(0).getAmountOwed() > 2200);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 47800);
    	
        }
    
    // TEST 6 - try to withdraw, has an account, partial balance
    public void test6_WithdrawPrevAccountScenario() throws Exception{
    	System.out.println("TEST 5");
    	Account acct = b.createAccount(cust);
    	acct.setBalance(2000);
    	b.addAccount(acct);
    	
    	assertEquals("Bank should have 1 account", b.getAccounts().size(), 1);
    	assertTrue("Account should have balance of 2000", b.getAccounts().get(0).getBalance() == 2000);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.WithdrawMoney(cust, 0, 2200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type withdrawal", 
    			teller.transactions.get(0).type == transactionType.withdrawal);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2200", teller.transactions.get(0).amount == 2200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive partial withdrawal message", 
    			cust.log.containsString("Received message HereIsPartialWithdrawal from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(0).status == transactionStatus.resolved);
    	assertTrue("Teller's transaction account should be 0", 
    			teller.transactions.get(0).getAccount().getBalance() == 0);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(0).getAccount().getCustomer() == cust);
    	assertTrue("Teller's transaction account should have an id of 1", 
    			teller.transactions.get(0).getAccount().getID() == 1);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 48000);
    	
        }
      
    // TEST 7 - try to withdraw, has an account, no balance
    public void test7_WithdrawPrevAccountScenario() throws Exception{
    	System.out.println("TEST 5");
    	Account acct = b.createAccount(cust);
    	acct.setBalance(0);
    	b.addAccount(acct);
    	
    	assertEquals("Bank should have 1 account", b.getAccounts().size(), 1);
    	assertTrue("Account should have balance of 0", b.getAccounts().get(0).getBalance() == 0);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.WithdrawMoney(cust, 0, 2200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type withdrawal", 
    			teller.transactions.get(0).type == transactionType.withdrawal);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2200", teller.transactions.get(0).amount == 2200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive no withdrawal message", 
    			cust.log.containsString("Received message NoMoney from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(0).status == transactionStatus.resolved);
    	assertTrue("Teller's transaction account should be 0", 
    			teller.transactions.get(0).getAccount().getBalance() == 0);
    	assertTrue("Teller's transaction account should have the right customer", 
    			teller.transactions.get(0).getAccount().getCustomer() == cust);
    	assertTrue("Teller's transaction account should have an id of 1", 
    			teller.transactions.get(0).getAccount().getID() == 1);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 50000);
    	
        }
    
    // TEST 8 - Pay back loan, in full
    public void test8_PayLoanFullScenario() throws Exception{
    	System.out.println("TEST 5");
    	Loan loan = b.createLoan(cust, 2200);
    	b.addLoan(loan);
    	
    	assertEquals("Bank should have 1 loan", b.getLoans().size(), 1);
    	assertTrue("Account should have owed balance of greater than 2200", b.getLoans().get(0).getAmountOwed() > 2200);
    	assertTrue("Account should have paid balance of 0", b.getLoans().get(0).getAmountPaid() == 0);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.PayMyLoan(cust, 2400);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type loan payment", 
    			teller.transactions.get(0).type == transactionType.loanPayment);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2400", teller.transactions.get(0).amount == 2400);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive loan paid off message", 
    			cust.log.containsString("Received message YourLoanIsPaidOff from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(0).status == transactionStatus.resolved);
    	assertTrue("Account should have owed balance of greater than 2200", b.getLoans().get(0).getAmountOwed() > 2200);
    	assertTrue("Account should have paid balance equal to balance owed", b.getLoans().get(0).getAmountPaid() ==  b.getLoans().get(0).getAmountOwed());
    	assertTrue("Bank has correct ending balance", b.getBalance() == (50000 +  b.getLoans().get(0).getAmountOwed()));
        }
    
    // TEST 9 - Pay back loan, partial
    public void test9_PayLoanPartialScenario() throws Exception{
    	System.out.println("TEST 5");
    	Loan loan = b.createLoan(cust, 2200);
    	b.addLoan(loan);
    	
    	assertEquals("Bank should have 1 loan", b.getLoans().size(), 1);
    	assertTrue("Account should have owed balance of greater than 2200", b.getLoans().get(0).getAmountOwed() > 2200);
    	assertTrue("Account should have paid balance of 0", b.getLoans().get(0).getAmountPaid() == 0);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.PayMyLoan(cust, 1400);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type loan payment", 
    			teller.transactions.get(0).type == transactionType.loanPayment);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 1400", teller.transactions.get(0).amount == 1400);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive loan partially paid off message", 
    			cust.log.containsString("Received message YouStillOwe from teller"));
    	assertTrue("Teller's transaction should be status resolved", 
    			teller.transactions.get(0).status == transactionStatus.resolved);
    	assertTrue("Account should have owed balance of greater than 2200", b.getLoans().get(0).getAmountOwed() > 2200);
    	assertTrue("Account should have paid balance equal to balance owed", b.getLoans().get(0).getAmountPaid() ==  1400);
    	assertTrue("Bank has correct ending balance", b.getBalance() == (50000 +  b.getLoans().get(0).getAmountPaid()));
        }
    
    // TEST 10 - ask for new loan, bad credit, no new loan
    public void test10_NormalCustomerScenario() throws Exception{
    	System.out.println("TEST 4");
    	Loan loan = b.createLoan(cust, 2200);
    	b.addLoan(loan);

    	assertEquals("Bank should have 0 accounts", b.getAccounts().size(), 0);
    	assertEquals("Bank should have 1 loan", b.getLoans().size(), 1);
    	assertTrue("Bank has correct starting balance", b.getBalance() == 50000);
    	assertEquals("Teller should have 0 transactions", teller.transactions.size() ,0);
    	
    	teller.IWantLoan(cust, 2200);
    	assertEquals("Teller should have 1 transaction", teller.transactions.size() , 1);
    	assertTrue("Teller's transaction should be type new loan", 
    			teller.transactions.get(0).type == transactionType.newLoan);
    	assertTrue("Teller's transaction should be status unresolved", 
    			teller.transactions.get(0).status == transactionStatus.unresolved);
    	assertTrue("Teller's transaction amount be 2200", teller.transactions.get(0).amount == 2200);
    	assertTrue("Teller's transaction should have the right customer", 
    			teller.transactions.get(0).getCust() == cust);
    	
    	assertTrue("Teller should run action", teller.pickAndExecuteAnAction());
    	assertTrue("Customer should receive new loan created", 
    			cust.log.containsString("Received message CreditNotGoodEnough from teller"));
    	assertEquals("Bank has 1 loan", b.getLoans().size(), 1);
    	assertTrue("Bank has correct ending balance", b.getBalance() == 50000);
        }
    
}