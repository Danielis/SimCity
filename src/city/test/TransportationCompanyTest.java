package city.test;

import junit.framework.TestCase;
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
public class TransportationCompanyTest extends TestCase
{
	/*
    //these are instantiated for each test separately via the setUp() method.
    CashierAgent cashier;
    MockWaiter waiter;
    MockCustomer customer;
    MockMarket market1;
    MockMarket market2;
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
            cashier = new CashierAgent("cashier");                
            customer = new MockCustomer("mockcustomer");                
            waiter = new MockWaiter("mockwaiter");
            market1 = new MockMarket("Market 1");
            market2 = new MockMarket("Market 2");
    }  
    
    //TEST 1 - WAITER GIVES CHECK, CUSTOMER PAYS (NORMAL)
    public void test1_NormalCustomerScenario() throws Exception{
        	System.out.println("TEST 1");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	
    		//SET UP SCENARIO
            customer.cashier = cashier; //You can do almost anything in a unit test. 
            waiter.cashier = cashier;
            
            //PRECONDITIONS
            assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.getChecks().size(), 0);
            
            //PART 1 - Add a check
            cashier.msgHereIsACheck(waiter, customer, "Steak"); 
            assertEquals("The cashier should have only one check. It doesn't.", cashier.getChecks().size(), 1);
            assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
            assertEquals("The cashier's customer should owe 15.99.", cashier.getChecks().get(0).owedAmount, 15.99f);
            assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
            assertEquals("The waiter should have one log item", 1, waiter.log.size());
            assertEquals("The customer should have one log.", 0, customer.log.size());

            //PART 2 - Pay the bill
            assertEquals(cashier.getChecks().get(0).state, CheckState.computed);
            cashier.msgHereIsMyPayment(customer, 15.99f);
            assertTrue("The cashier should have ran an action", cashier.pickAndExecuteAnAction());
            assertEquals("The cashier's cash should be 500+15.99", 515.99f, cashier.getAccountBalance());
            assertEquals("The cashier should have no more checks now that the custoer paid.", cashier.getChecks().size(), 0);
            assertFalse("The cashier's Action look should return false now", cashier.pickAndExecuteAnAction());
    }//end one normal customer scenario
    //TEST 2 - WAITER GIVES CHECK, CUSTOMER PAYS NOTHING
    public void test2_CustomerOwesMoney() throws Exception {
    	System.out.println("TEST 2");

    	//SET UP SHOULD RUN BEFORE THIS TEST
    	
		//SET UP SCENARIO
        customer.cashier = cashier;
        waiter.cashier = cashier;
        
        //PRECONDITIONS
        assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.getChecks().size(), 0);
        
        //PART 1 - Add a check
        cashier.msgHereIsACheck(waiter, customer, "Steak"); 
        assertEquals("The cashier should have only one check. It doesn't.", cashier.getChecks().size(), 1);
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier's customer should owe 15.99.", cashier.getChecks().get(0).owedAmount, 15.99f);
        assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
        assertEquals("The waiter should have one log item", 1, waiter.log.size());
        assertEquals("The customer should have one log.", 0, customer.log.size());

        //PART 2 - Pay the bill with no money
        assertEquals(cashier.getChecks().get(0).state, CheckState.computed);
        cashier.msgHereIsMyPayment(customer, 0.0f);
        assertTrue("The cashier should have ran an action", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier's cash should be 500 dollars", 500.0f, cashier.getAccountBalance());
        assertEquals("The cashier should a check to keep track of the customer.", cashier.getChecks().size(), 1);
        assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
        assertFalse("The cashier's Action look should return false now", cashier.pickAndExecuteAnAction());
    }
    //TEST 3 - WAITER GIVES CHECK, CUSTOMER PAYS SOME
    public void test3_CustomerPaysSomeAmount() throws Exception{
    	System.out.println("TEST 3");

    	//SET UP SHOULD RUN BEFORE THIS TEST
    	
		//SET UP SCENARIO
        customer.cashier = cashier;
        waiter.cashier = cashier;
        
        //PRECONDITIONS
        assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.getChecks().size(), 0);
        
        //PART 1 - Add a check
        cashier.msgHereIsACheck(waiter, customer, "Steak"); 
        assertEquals("The cashier should have only one check. It doesn't.", cashier.getChecks().size(), 1);
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier's customer should owe 15.99.", cashier.getChecks().get(0).owedAmount, 15.99f);
        assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
        assertEquals("The waiter should have one log item", 1, waiter.log.size());
        assertEquals("The customer should have one log.", 0, customer.log.size());

        //PART 2 - Pay the bill with less money
        assertEquals(cashier.getChecks().get(0).state, CheckState.computed);
        cashier.msgHereIsMyPayment(customer, 10.0f);
        assertTrue("The cashier should have ran an action", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier's cash should be 510 dollars", 510.0f, cashier.getAccountBalance());
        assertEquals("The cashier should a check to keep track of the customer.", cashier.getChecks().size(), 1);
        assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
        assertFalse("The cashier's Action look should return false now", cashier.pickAndExecuteAnAction());
    }
    //TEST 4 - MARKET ORDERS PAYMENT, CASHIER PAYS (NORMAL)
    public void test4_NormalMarketScenario() throws Exception{
    	System.out.println("TEST 4");

    	//SET UP SHOULD RUN BEFORE THIS TEST
    	
		//SET UP SCENARIO - Cashier has enough money
    	cashier.setBalance(500.0f);
        market1.cashier = cashier;
        market2.cashier = cashier;
        
        //PRECONDITIONS
        assertEquals("Cashier should have 0 payment items in it. It doesn't.", cashier.getPayments().size(), 0);
        
        //PART 1 - Add a payment order - Orders from Market1 3 Steaks
        cashier.msgHereAMarketOrder(market1, "Steak", 3);
        assertEquals("The cashier should have only one payment item. It doesn't.", cashier.getPayments().size(), 1);
        assertEquals("The cashier should have 3 items to calculate payment.", cashier.getPayments().get(0).amount, 3);
        assertEquals("The cashier's order should be steak.", cashier.getPayments().get(0).order, "Steak");
        assertTrue("The cashier's payment market should be market1", cashier.getPayments().get(0).m == market1);
        assertEquals("The cashier's payment state should be needsPayment", cashier.getPayments().get(0).state, PaymentState.needsPayment);
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier should have no more payments since he fully paid the market", cashier.getPayments().size(),0);
        assertEquals("The market should have received a log of his payment", market1.log.size(), 1);
        assertTrue("Market should have logged the price of his payment to be \"47.97\" but didn't", market1.log.containsString("47.97"));
    }
    //TEST 5 - MARKET ORDERS PAYMENT, CASHIER RUNS OUT OF MONEY, MARKET ORDERS AGAIN
    public void test5_CashierNotEnoughBalance() throws Exception{
    	System.out.println("TEST 5");

    	//SET UP SHOULD RUN BEFORE THIS TEST
    	
		//SET UP SCENARIO - Cashier has enough money
    	cashier.setBalance(10.0f);
        market1.cashier = cashier;
        market2.cashier = cashier;
        
        //PRECONDITIONS
        assertEquals("Cashier should have 0 payment items in it. It doesn't.", cashier.getPayments().size(), 0);
        
        //PART 1 - Add a payment order - Orders from Market1 3 Steaks
        cashier.msgHereAMarketOrder(market1, "Steak", 3);
        assertEquals("The cashier should have only one payment item. It doesn't.", cashier.getPayments().size(), 1);
        assertEquals("The cashier should have 3 items to calculate payment.", cashier.getPayments().get(0).amount, 3);
        assertEquals("The cashier's order should be steak.", cashier.getPayments().get(0).order, "Steak");
        assertTrue("The cashier's payment market should be market1", cashier.getPayments().get(0).m == market1);
        assertEquals("The cashier's payment state should be needsPayment", cashier.getPayments().get(0).state, PaymentState.needsPayment);
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier should have no more payment items", cashier.getPayments().size(),0);
        assertEquals("The cashier's owed amount should be 37.97", cashier.getOwes().get(0), 37.97f);
        assertEquals("The market should have received a log of his payment", market1.log.size(), 1);
        assertTrue("Market should have logged the price of his payment to be \"10\" but didn't", market1.log.containsString("10"));
        
        //PART 2 - That same market returns an order of another food - Chicken (Checking retainment of payment item)
        cashier.msgHereAMarketOrder(market1, "Chicken", 3);
        assertEquals("The cashier should have only one payment item. It doesn't.", cashier.getPayments().size(), 1);
        assertEquals("The cashier should have 3 items to calculate payment.", cashier.getPayments().get(0).amount, 3);
        assertEquals("The cashier's order should be steak.", cashier.getPayments().get(0).order, "Chicken");
        assertTrue("The cashier's payment market should be market1", cashier.getPayments().get(0).m == market1);
        assertEquals("The cashier's payment state should be needsPayment", cashier.getPayments().get(0).state, PaymentState.needsPayment);
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier's owed amount should be 37.97 + 32.97 = 70.94.", cashier.getOwes().get(0), 70.94f);
        assertEquals("The cashier should have no money anymore", cashier.getAccountBalance(), 0f);
        assertEquals("The market should have received a second log of his payment", market1.log.size(), 2);
        assertTrue("Market should have logged the price of his payment to be \"10\" but didn't", market1.log.containsString("10"));
    }
    //TEST 6 - TWO MARKETS ORDER PAYMENTS - CASHIER PAYS BOTH
    public void test6_CashierPaysTwoBills() throws Exception{
    	System.out.println("TEST 6");

    	//SET UP SHOULD RUN BEFORE THIS TEST
    	
		//SET UP SCENARIO - Cashier has enough money
    	cashier.setBalance(500.0f);
    	
        market1.cashier = cashier;
        market2.cashier = cashier;
        
        //PRECONDITIONS
        assertEquals("Cashier should have 0 payment items in it. It doesn't.", cashier.getPayments().size(), 0);
        
        //PART 2 - Add a payment order - Two Market orders come in
        cashier.msgHereAMarketOrder(market1, "Steak", 3);
        cashier.msgHereAMarketOrder(market2, "Salad", 4);
        assertEquals("The cashier should have two payment items. It doesn't.", cashier.getPayments().size(), 2);
        assertEquals("The cashier should have 3 items to calculate payment for market1.", cashier.getPayments().get(0).amount, 3);
        assertEquals("The cashier should have 4 items to calculate payment for market2.", cashier.getPayments().get(1).amount, 4);
        assertEquals("The cashier's market1 order should be steak.", cashier.getPayments().get(0).order, "Steak");
        assertEquals("The cashier's market2 order should be salad.", cashier.getPayments().get(1).order, "Salad");
        assertTrue("The cashier's first payment market should be market1", cashier.getPayments().get(0).m == market1);
        assertTrue("The cashier's second payment market should be market2", cashier.getPayments().get(1).m == market2);
        assertEquals("The cashier's market 1's payment state should be needsPayment", cashier.getPayments().get(0).state, PaymentState.needsPayment);
        assertEquals("The cashier's market 2's payment state should be needsPayment", cashier.getPayments().get(1).state, PaymentState.needsPayment);
        
        //PART 3 - First Action
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
        assertEquals("The cashier's balance should be 500 - 47.97 = 452.03", cashier.getAccountBalance(), 452.03f);
        assertEquals("The cashier have one payment left", cashier.getPayments().size(), 1);
        assertEquals("The market1 should have received a log of his payment", market1.log.size(), 1);
        assertTrue("Market1 should have logged the price of his payment to be \"47.97\" but didn't", market1.log.containsString("47.97"));
        
        //Part 4 - Second Action
        assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());   
        assertEquals("The cashier should not have any more payments.", cashier.getPayments().size(), 0);
        System.out.println(cashier.getAccountBalance());
        assertEquals("The cashier's balance should be 452.03 - 23.96 = 428.07", cashier.getAccountBalance(), 428.07f);
        assertEquals("The market2 should have received a log of his payment", market2.log.size(), 1);
        assertTrue("Market2 should have logged the price of his payment to be \"23.96\" but didn't", market2.log.containsString("23.96"));
        assertFalse("The cashier should not have any more actions to run.", cashier.pickAndExecuteAnAction());
    }
    //TEST 7 - INTERLEAVING MARKET/CUSTOMER/WAITER INTERACTIONS
    public void test7_CashierGetsMarketOrderAndCustomerPayment() throws Exception{
        	System.out.println("TEST 7");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
    	
    		//SET UP SCENARIO
        	cashier.setBalance(500.0f);
            customer.cashier = cashier; //You can do almost anything in a unit test. 
            waiter.cashier = cashier;
            market1.cashier = cashier;
            
            //PRECONDITIONS
            assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.getChecks().size(), 0);
            
            //PART 1 - Add a check from the waiter
            cashier.msgHereIsACheck(waiter, customer, "Steak"); 
            assertEquals("The cashier should have only one check. It doesn't.", cashier.getChecks().size(), 1);
            assertTrue("The cashier did not run any actions. It should have.", cashier.pickAndExecuteAnAction());
            assertEquals("The cashier's customer should owe 15.99.", cashier.getChecks().get(0).owedAmount, 15.99f);
            assertTrue("The cashier should have MockCustomer in the list", cashier.getChecks().get(0).c == customer);
            assertEquals("The waiter should have one log item", 1, waiter.log.size());
            assertEquals("The customer should have one log.", 0, customer.log.size());
            assertEquals(cashier.getChecks().get(0).state, CheckState.computed);

            //PART 2 - Message from Market and Customer
            cashier.msgHereAMarketOrder(market1, "Pizza", 5);
            cashier.msgHereIsMyPayment(customer, 15.99f);
            assertEquals("The cashier should have one payment order.", cashier.getPayments().size(), 1);
            assertEquals("The cashier should one check order", cashier.getChecks().size(), 1);
            assertTrue("The cashier's payment should have the market1", cashier.getPayments().get(0).m == market1);
            assertTrue("The cashier's check should include customer", cashier.getChecks().get(0).c == customer);
            assertEquals(cashier.getChecks().get(0).state, CheckState.justPaid);
            assertEquals("The cashier's payment state should be needsPayment", cashier.getPayments().get(0).state, PaymentState.needsPayment);

            //Part 3 - First Action
            assertTrue("The cashier should have ran an action", cashier.pickAndExecuteAnAction());
            assertEquals("The cashier's cash should be 500+15.99", 515.99f, cashier.getAccountBalance());
            assertEquals("The cashier should have no more checks now that the custoer paid.", cashier.getChecks().size(), 0);
            assertEquals("The payment item for cashier should still be there", cashier.getPayments().size(), 1);
            
            //PART 4 - Second Action
            assertTrue("The cashier should have ran an action", cashier.pickAndExecuteAnAction());
            assertEquals("The cashier should have no more payments since he fully paid the market", cashier.getPayments().size(),0);
            assertEquals("The cashier's balance should now be 515.99-44.95 = 471.04", cashier.getAccountBalance(), 471.04f);
            assertEquals("The market should have received a log of his payment", market1.log.size(), 1);
            assertTrue("Market should have logged the price of his payment to be \"44.95\" but didn't", market1.log.containsString("44.95"));
    }*/
        
}