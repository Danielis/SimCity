package restaurantD.test;

import restaurantD.CashierAgent;
import restaurantD.CashierAgent.Check;
import restaurantD.CashierAgent.CheckState;
import restaurantD.test.mock.EventLog;
import restaurantD.test.mock.MockCustomer;
import restaurantD.test.mock.MockMarket;
import restaurantD.test.mock.MockWaiter;
import junit.framework.*;


/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * I modified the code to fit my cashier and am now implementing tests for grading in V2.2B
 *
 * @author Daniel Silva
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	MockMarket market2;
	
	EventLog log;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("Market1");
		market2 = new MockMarket("Market3");
		log = new EventLog();
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.checks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		//System.out.println("Checks: "+ cashier.checks.size());
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		
		cashier.getCheck(customer,"Chicken",waiter);//send the message from a waiter
		
		
		//System.out.println("Checks: "+ cashier.checks.size());
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.checks.size(), 1);
		
		//Not running pick and execute otherwise it will run before checking other post conditions
		//assertTrue("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//step 2 of the test
		// making a call to calculate the cost of the check in checks so that the check can enter 
		cashier.calculateCost(cashier.checks.get(0));
		
		//check postconditions for step 2 / preconditions for step 3
		//this line is due to the unit testing being instantanious and not having to wait for the timecalc to occur otherwise it would have a state called calculating while timer was running
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.checks.get(0).getState() == CheckState.done);

		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Calculated Cost"));

		assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
				+ cashier.checks.get(0).getNum(), cashier.checks.get(0).getNum() == 10.99);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.checks.get(0).getCustomer() == customer);
		
		//cashier.giveCheckToWaiter(cashier.checks.get(0));
		
		//step 3
		//NOTE: I am calling the scheduler to both check that return should be true and that the check is handed to the waiter correctly
		assertTrue("Cashier's scheduler should have returned true (needs to react to Check in done state), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockWaiter should have logged an event for receiving \"itCost\" with the correct balance, but his last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received Check of 10.99"));
	
			
		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Check sent to Waiter"));
		
		customer.hereIsYourCheck(10.99); //mockwaiter does not normally communicate with mockcustomer so I'm passing a value to customer
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourCheck\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received hereIsYourCheck from Waiter. Total = 10.99"));
		
		//step 4
		cashier.pay(customer, 10.99);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received youPayed from cashier."));
	
		
		assertTrue("CashierBill should contain a bill with state == resolved. It doesn't.",
				cashier.checks.get(0).getState() == CheckState.resolved);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario
	
	
	//note almost the same as testone but step4 is different
	public void testTwoCustomerCantPayScenario()
	{
		//setUp() runs first before this test!
		
				customer.cashier = cashier;//You can do almost anything in a unit test.			
				
				//check preconditions
				assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.checks.size(), 0);		
				assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
								+ cashier.log.toString(), 0, cashier.log.size());
				//System.out.println("Checks: "+ cashier.checks.size());
				//step 1 of the test
				//public Bill(Cashier, Customer, int tableNum, double price) {
				
				cashier.getCheck(customer,"Chicken",waiter);//send the message from a waiter
				
				
				//System.out.println("Checks: "+ cashier.checks.size());
				//check postconditions for step 1 and preconditions for step 2
				assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
								+ waiter.log.toString(), 0, waiter.log.size());
				
				assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.checks.size(), 1);
				
				//Not running pick and execute otherwise it will run before checking other post conditions
				//assertTrue("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
				
				assertEquals(
						"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
								+ waiter.log.toString(), 0, waiter.log.size());
				
				assertEquals(
						"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
								+ waiter.log.toString(), 0, waiter.log.size());
				
				//step 2 of the test
				// making a call to calculate the cost of the check in checks so that the check can enter 
				cashier.calculateCost(cashier.checks.get(0));
				
				//check postconditions for step 2 / preconditions for step 3
				//this line is due to the unit testing being instantanious and not having to wait for the timecalc to occur otherwise it would have a state called calculating while timer was running
				assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
						cashier.checks.get(0).getState() == CheckState.done);

				assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Calculated Cost"));

				assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
						+ cashier.checks.get(0).getNum(), cashier.checks.get(0).getNum() == 10.99);
				
				assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
							cashier.checks.get(0).getCustomer() == customer);
				
				//cashier.giveCheckToWaiter(cashier.checks.get(0));
				
				//step 3
				//NOTE: I am calling the scheduler to both check that return should be true and that the check is handed to the waiter correctly
				assertTrue("Cashier's scheduler should have returned true (needs to react to Check in done state), but didn't.", 
							cashier.pickAndExecuteAnAction());
				
				//check postconditions for step 3 / preconditions for step 4
				assertTrue("MockWaiter should have logged an event for receiving \"itCost\" with the correct balance, but his last event logged reads instead: " 
						+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received Check of 10.99"));
			
					
				assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Check sent to Waiter"));
				
				customer.hereIsYourCheck(10.99); //mockwaiter does not normally communicate with mockcustomer so I'm passing a value to customer
				
				assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourCheck\" with the correct balance, but his last event logged reads instead: " 
						+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received hereIsYourCheck from Waiter. Total = 10.99"));
				
				//step 4
				cashier.cantPay(customer);
				
				assertTrue("Cashier's scheduler should have returned true (needs to react to customer's unable to pay), but didn't.", 
							cashier.pickAndExecuteAnAction());
				
				//check postconditions for step 4
				assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
						+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received doDishes from cashier."));
			
				
				assertTrue("CashierBill should contain a bill with state == resolved. It doesn't.",
						cashier.checks.get(0).getState() == CheckState.resolved);
				
				assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
						cashier.pickAndExecuteAnAction());
	}
	
	public void testThreeOneMarketScenario()
	{
		//setUp() runs first before this test!
		
		//Step 1 will begin as Market sends a bill to Cashier
		   //Checking preconditions before Cashier gets first bill
		assertTrue("Cashier should have no MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's marketBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		
		cashier.marketBill(market, 50.0);
		   //Testing post conditions for cashier
		assertTrue("Cashier should have one MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 1);
		assertTrue("Cashier should have placed the marketbill as placed in state, but instead has it as " + cashier.marketChecks.get(0).getState(),
				cashier.marketChecks.get(0).getState() == CheckState.placed);
		//Step 2 will now use the scheduler to send the amount of money to Market from Cashier
		
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		assertTrue("Market should contain a debt of 100 before a payment is made, but instead has " + market.getDebt(),
				market.getDebt()==100.0);
		   //using this to both check if true and call scheduler
		assertTrue("Cashier's scheduler should have returned true (marketChecks has one in placed state), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have sent the money and logged the event as Money sent to Market with 50.0, but instead reads " + cashier.log.getLastLoggedEvent().toString(),
				 cashier.log.containsString("Money sent to Market with 50.0"));
		assertTrue("Market should contain a logged event after the call to marketPayment, and should read -Received Cashier Payment of 50.0- instead has " + market.log.getLastLoggedEvent().toString()
					,market.log.containsString("Received Cashier Payment of 50.0"));
		assertTrue("Market should contain a debt of 50 after a payment is made, but instead has " + market.getDebt(),
					market.getDebt()==50.0);
		assertFalse("Scheduler should return false since there is nothing left to do, but didn't",
				cashier.pickAndExecuteAnAction());
		
	}
		
	public void testFourTwoMarketScenario()
	{
		//setUp() runs first before this test!
		
		//Step 1 will begin as Market and market2 send a bill to Cashier
		   //Checking preconditions before Cashier gets first bill
		assertTrue("Cashier should have no MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's marketBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		assertEquals("Market2 should not contain any logged events before the call to marketPayment, but instead has " + market2.log.size()
				,0,market2.log.size());
		
		cashier.marketBill(market, 50.0);
		cashier.marketBill(market2, 100.0);
		   //Testing post conditions for cashier
		assertTrue("Cashier should have one MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 2);
		assertTrue("Cashier should have placed the first marketbill as -placed- in state, but instead has it as " + cashier.marketChecks.get(0).getState(),
				cashier.marketChecks.get(0).getState() == CheckState.placed);
		assertTrue("Cashier should have placed the second marketbill as -placed- in state, but instead has it as " + cashier.marketChecks.get(1).getState(),
				cashier.marketChecks.get(1).getState() == CheckState.placed);
		//Step 2 will now use the scheduler to send the amount of money to Market from Cashier
		
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		assertTrue("Market should contain a debt of 100 before a payment is made, but instead has " + market.getDebt(),
				market.getDebt()==100.0);
		assertEquals("Market2 should not contain any logged events before the call to marketPayment, but instead has " + market2.log.size()
				,0,market2.log.size());
		assertTrue("Market2 should contain a debt of 100 before a payment is made, but instead has " + market2.getDebt(),
				market2.getDebt()==100.0);
		   //using this to both check if true and call scheduler
		assertTrue("Cashier's scheduler should have returned true (marketChecks has one in placed state), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have sent the money and logged the event as Money sent to Market with 50.0, but instead reads " + cashier.log.getLastLoggedEvent().toString(),
				 cashier.log.containsString("Money sent to Market with 50.0"));
		assertTrue("Market should contain a logged event after the call to marketPayment, and should read -Received Cashier Payment of 50.0- instead has " + market.log.getLastLoggedEvent().toString()
					,market.log.containsString("Received Cashier Payment of 50.0"));
		assertTrue("Market should contain a debt of 50 after a payment is made, but instead has " + market.getDebt(),
					market.getDebt()==50.0);
		
		// Step 3 will now check on the second check
		
		   //Since the scheduler returned true, but not ran, I am now checking it returns true again and am calling it so that it runs for marketcheck 2
		assertTrue("Cashier's scheduler should have returned true (marketChecks has one in placed state), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have sent the money and logged the event as Money sent to Market2 with 100.0, but instead reads " + cashier.log.getLastLoggedEvent().toString(),
				 cashier.log.containsString("Money sent to Market with 100.0"));
		assertTrue("Market2 should contain a logged event after the call to marketPayment, and should read -Received Cashier Payment of 100.0- instead has " + market2.log.getLastLoggedEvent().toString()
					,market2.log.containsString("Received Cashier Payment of 100.0"));
		assertTrue("Market should contain a debt of 00 after a payment is made, but instead has " + market2.getDebt(),
					market2.getDebt() == 0.0);
		assertFalse("Scheduler should return false again since there is nothing left to do, but didn't",
				cashier.pickAndExecuteAnAction());
	}	
	public void testFiveCashierCantPayOneMarketScenario()
	{
		//setUp() runs first before this test!
		
		//Step 1 will begin as Market sends a bill to Cashier
		   //Checking preconditions before Cashier gets first bill
		assertTrue("Cashier should have no MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's marketBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		
		cashier.marketBill(market, 150.0);
		   //Testing post conditions for cashier
		assertTrue("Cashier should have one MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 1);
		assertTrue("Cashier should have placed the marketbill as placed in state, but instead has it as " + cashier.marketChecks.get(0).getState(),
				cashier.marketChecks.get(0).getState() == CheckState.placed);
		//Step 2 will now use the scheduler to send the amount of money to Market from Cashier
		
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		assertTrue("Market should contain a debt of 100 before a payment is made, but instead has " + market.getDebt(),
				market.getDebt()==100.0);
		   //using this to both check if true and call scheduler
		assertTrue("Cashier's scheduler should have returned true (marketChecks has one in placed state), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have sent the money and logged the event as Money sent to Market with 50.0, but instead reads " + cashier.log.getLastLoggedEvent().toString(),
				 cashier.log.containsString("Money sent to Market with 150.0"));
		assertTrue("Market should contain a logged event after the call to marketPayment, and should read -Received Cashier Payment of 150.0- instead has " + market.log.getLastLoggedEvent().toString()
					,market.log.containsString("Received Cashier Payment of 150.0"));
		assertTrue("Market should contain a debt of 50 after a payment is made, but instead has " + market.getDebt(),
					market.getDebt()==-50.0);
		assertFalse("Scheduler should return false since there is nothing left to do, but didn't",
				cashier.pickAndExecuteAnAction());
		
	}
	public void testSixOneMarketDoubleChargeScenario()
	{
		//setUp() runs first before this test!
		
		//Step 1 will begin as Market sends a bill to Cashier
		   //Checking preconditions before Cashier gets first bill
		assertTrue("Cashier should have no MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's marketBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		
		cashier.marketBill(market, 50.0);
		cashier.marketBill(market, 50.0);
		   //Testing post conditions for cashier
		assertTrue("Cashier should have one MarketCheck in marketChecks, but instead has " + cashier.marketChecks.size(), 
				cashier.marketChecks.size() == 2);
		assertTrue("Cashier should have placed the marketbill as placed in state, but instead has it as " + cashier.marketChecks.get(0).getState(),
				cashier.marketChecks.get(0).getState() == CheckState.placed);
		assertTrue("Cashier should have placed the marketbill as placed in state, but instead has it as " + cashier.marketChecks.get(0).getState(),
				cashier.marketChecks.get(1).getState() == CheckState.placed);
		//Step 2 will now use the scheduler to send the amount of money to Market from Cashier
		
		assertEquals("Market should not contain any logged events before the call to marketPayment, but instead has " + market.log.size()
				,0,market.log.size());
		assertTrue("Market should contain a debt of 100 before a payment is made, but instead has " + market.getDebt(),
				market.getDebt()==100.0);
		   //using this to both check if true and call scheduler
		assertTrue("Cashier's scheduler should have returned true (marketChecks has one in placed state), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have sent the money and logged the event as Money sent to Market with 50.0, but instead reads " + cashier.log.getLastLoggedEvent().toString(),
				 cashier.log.containsString("Money sent to Market with 50.0"));
		assertTrue("Market should contain a logged event after the call to marketPayment, and should read -Received Cashier Payment of 50.0- instead has " + market.log.getLastLoggedEvent().toString()
					,market.log.containsString("Received Cashier Payment of 50.0"));
		assertTrue("Market should contain a debt of 50 after a payment is made, but instead has " + market.getDebt(),
					market.getDebt()==50.0);
		//step 3 will now check what happens with the second check
		assertEquals("Market should not contain one logged event before the call to marketPayment again, but instead has " + market.log.size()
				,1,market.log.size());
		assertTrue("Market should contain a debt of 50 before a second payment is made, but instead has " + market.getDebt(),
				market.getDebt()==50.0);
		   //using this to both check if true and call scheduler, this is calling it a second time for the next check
		assertTrue("Cashier's scheduler should have returned true (marketChecks has one in placed state), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have sent the money and logged the event as Money sent to Market with 50.0, but instead reads " + cashier.log.getLastLoggedEvent().toString(),
				 cashier.log.containsString("Money sent to Market with 50.0"));
		assertTrue("Market should contain a logged event after the call to marketPayment, and should read -Received Cashier Payment of 50.0- instead has " + market.log.getLastLoggedEvent().toString()
					,market.log.containsString("Received Cashier Payment of 50.0"));
		assertTrue("Market should contain a debt of 0 after a second payment is made, but instead has " + market.getDebt(),
					market.getDebt()== 0.0);
		//Now nothing should be left to run so this should return false
		assertFalse("Scheduler should return false since there is nothing left to do, but didn't",
				cashier.pickAndExecuteAnAction());
		
	}
}
