package restaurantA.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import restaurant.CashierAgent.cashierBillState;
//import restaurant.WaiterAgent.Bill;
import restaurantA.CashierAgent;
import restaurantA.Check;
import restaurantA.Table;
import restaurantA.Check.checkState;
import restaurantA.interfaces.Cashier;
import restaurantA.test.mock.MockCook;
import restaurantA.test.mock.MockCustomer;
import restaurantA.test.mock.MockMarket;
import restaurantA.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are i nstantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockCustomer cust1, cust2, cust3;
	MockMarket market, market2;
	MockCook cook;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("test");		
		customer = new MockCustomer("mockcustomer");		
		cust1 = new MockCustomer("mockcustomer1");	
		cust2 = new MockCustomer("mockcustomer2");	
		cust3 = new MockCustomer("mockcustomer3");	
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		market2 = new MockMarket("mockmarket2");
		cook = new MockCook("mockcook");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	
	public void test(){
	customer.cashier = cashier;
	assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0); 
		
	}
	
	/* This tests the scenario where the cashier receives a single bill from one market and pays it
	 * 
	 */
	public void testNormalMarketBillScenario(){
		assertEquals("Cashier should not have any money", cashier.getSavings(), 0);
		assertEquals("Cashier should have 0 bills", cashier.bills.size(), 0);
		cashier.msgHereIsBill(market, 20);
		assertEquals("Cashier should have 1 bill", cashier.bills.size(), 1);
		
		//validate bill attributes
		assertTrue("Bill amount is correct", cashier.bills.get(0).getAmount() == 20);
		assertTrue("Bill should not be paid", cashier.bills.get(0).getPaid() == false);
		assertTrue("Bill should contain correct market", cashier.bills.get(0).getM() == market);
		
        assertEquals(market.log.size(), 0);

        cashier.pickAndExecuteAnAction();

        assertTrue("Market should receive payment message", market.log.containsString("Received message msgHereIsPayment from cashier"));
	
        assertTrue("Bill state should be paid", cashier.bills.get(0).getPaid() == true);
        
        assertEquals("Market log size should be 1", market.log.size(), 1);
        assertEquals("Cashier should have less money", cashier.getSavings(), -20);

	}
	
	/*
	 * This scenario tests when the cashier receives two bills from two different markets for the payment of one order.
	 */
	public void testTwoMarketBillsScenario(){
		assertEquals("Cashier should not have any money", cashier.getSavings(), 0);
		assertEquals("Cashier should have 0 bills", cashier.bills.size(), 0);
		cashier.msgHereIsBill(market, 20);
		assertEquals("Cashier should have 1 bill", cashier.bills.size(), 1);
		cashier.msgHereIsBill(market2, 10);
		assertEquals("Cashier should have 2 bills", cashier.bills.size(), 2);

		//validate bill attributes
		assertTrue("Bill amount is correct", cashier.bills.get(0).getAmount() == 20);
		assertTrue("Bill should not be paid", cashier.bills.get(0).getPaid() == false);
		assertTrue("Bill should contain correct market", cashier.bills.get(0).getM() == market);
		
		assertTrue("Bill amount is correct", cashier.bills.get(1).getAmount() == 10);
		assertTrue("Bill should not be paid", cashier.bills.get(1).getPaid() == false);
		assertTrue("Bill should contain correct market", cashier.bills.get(1).getM() == market2);
		
        assertEquals("Market should not have any logs", market.log.size(), 0);
        assertEquals("Market should not have any logs", market2.log.size(), 0);

        cashier.pickAndExecuteAnAction();

        assertTrue("Market should have 1 log message", market.log.containsString("Received message msgHereIsPayment from cashier"));
        assertTrue("Market should have 1 log message", market2.log.containsString("Received message msgHereIsPayment from cashier"));

        assertTrue("Bill state should be paid", cashier.bills.get(0).getPaid() == true);
        
        assertEquals("Market log size should be 1", market.log.size(), 1);
        assertEquals("Cashier should have less money", cashier.getSavings(), -30);

	}
	
	/* 
	 * This tests the normative check paying scenario. The customer pays for the check and the cashier's register amount updates accordingly
	 */
	public void testNormativeCheckScenario(){
		assertEquals("Cashier should not have any money", cashier.getSavings(), 0);
		assertEquals("Cashier should have 0 checks", cashier.checks.size(), 0);
		Table table = new Table(1, 0, 0);
		Check temp = new Check("Salad", table, customer);
		temp.setAmount("Salad");
		assertTrue("Check should have correct amount owed value", temp.getAmountOwed() == 6);
		
		assertEquals("Customer should not have received any messages", customer.log.size(), 0);
		cashier.msgHereIsMoney(temp, 6);
		
		assertTrue("Cashier should have 1 check", cashier.checks.size() == 1);
		assertTrue("Check should be in paying state", cashier.checks.get(0).getS() == checkState.paying);
		assertTrue("Check should have correct amount paid value", cashier.checks.get(0).getAmountPaid() == 6);
		assertTrue("Check should have correct amount owed value after payment", cashier.checks.get(0).getAmountOwed() == 0);
		assertTrue("Change should be " + cashier.checks.get(0).getAmountChange(), cashier.checks.get(0).getAmountChange() == 0);
	
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Customer log should be 1", customer.log.size(), 1);
        assertTrue("Customer log should contain message from cashier that they can leave", customer.log.containsString("Received message msgYouAreGoodToGo from cashier"));
        
        assertTrue("Check should now be in state Paid", cashier.checks.get(0).getS() == checkState.Paid);
        assertEquals("Cashier should have some money", cashier.getSavings(), 6);
	}
	
	/*
	 * This tests the scenario where the customer gives too much money and needs change back.
	 */
	public void testChangeScenario(){
		assertEquals("Cashier should not have any money", cashier.getSavings(), 0);
		assertEquals("Cashier should have 0 checks", cashier.checks.size(), 0);
		Table table = new Table(1, 0, 0);
		Check temp = new Check("Steak", table, customer);
		temp.setAmount("Steak");
		assertTrue("Check should have correct amount owed", temp.getAmountOwed() == 16);
		
		assertEquals("Customer should not have any messages in log", customer.log.size(), 0);
		cashier.msgHereIsMoney(temp, 20);
		
		assertTrue("Cashier should have one check", cashier.checks.size() == 1);
		assertTrue("Check should be in state paying", cashier.checks.get(0).getS() == checkState.paying);
		assertTrue("Check should have correct amount paid", cashier.checks.get(0).getAmountPaid() == 20);
		assertTrue("Check should have correct amount owed after payment", cashier.checks.get(0).getAmountOwed() == 0);
		assertTrue("Change should be " + cashier.checks.get(0).getAmountChange(), cashier.checks.get(0).getAmountChange() == 4);
	
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Customer log should be 2", customer.log.size(), 2);
        assertTrue("Customer log should contain message that they received change", customer.log.containsString("Received message msgHereIsChange from cashier"));
        assertTrue("Customer log should contain message that they are welcome to leave", customer.log.containsString("Received message msgYouAreGoodToGo from cashier"));
        
        assertTrue("Check should now be in state Paid", cashier.checks.get(0).getS() == checkState.Paid);
        assertEquals("Cashier should have some money", cashier.getSavings(), 16);

	}

	/* 
	 * This tests the non-normative scenario where the customer does not have enough money for his meal. 
	 * The customer pays as much as he can for the meal.
	 */
	public void testCantPayScenario(){
		assertEquals("Cashier should not have any money", cashier.getSavings(), 0);
		assertEquals("Cashier should have 0 checks", cashier.checks.size(), 0);
		Table table = new Table(1, 0, 0);
		Check temp = new Check("Steak", table, customer);
		temp.setAmount("Steak");
		assertTrue("Check should have correct amount owed value", temp.getAmountOwed() == 16);
		
		assertEquals("Customer should not have any messages in log", customer.log.size(), 0);
		cashier.msgICantPay(temp, 3);
		
		assertTrue("Cashier should have one check", cashier.checks.size() == 1);
		assertTrue("Check should have correct state of can't be paid", cashier.checks.get(0).getS() == checkState.cantbePaid);
		assertTrue("Check should have correct value of amount paid, after the customer says how much he can pay", cashier.checks.get(0).getAmountPaid() == 3);
		assertTrue("Check should have correct new value of amount owed after customer pays", cashier.checks.get(0).getAmountOwed() == 13);
		assertTrue("Change should be " + cashier.checks.get(0).getAmountChange(), cashier.checks.get(0).getAmountChange() == 0);
	
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Customer log should be 1", customer.log.size(), 1);
        assertTrue("Customer should receive kick out message", customer.log.containsString("Received message msgGetOut from cashier"));
        
        assertTrue("Check state should now be waiting", cashier.checks.get(0).getS() == checkState.waiting);
        assertEquals("Cashier should have some money", cashier.getSavings(), 3);

	}
	
	/* 
	 * This tests the cashier handling multiple (3) customers and checks.
	 */
	public void testMultipleCustomersCashier(){
		assertEquals("Cashier should not have any money", cashier.getSavings(), 0);
		assertEquals("Cashier should have 0 checks", cashier.checks.size(), 0);
		Table table = new Table(1, 0, 0);
		Check temp = new Check("Steak", table, cust1);
		temp.setAmount("Steak");
		assertTrue("Check should have correct amount owed", temp.getAmountOwed() == 16);
		
		Check temp1 = new Check("Salad", table, cust2);
		temp1.setAmount("Salad");
		assertTrue("Check should have correct amount owed", temp1.getAmountOwed() == 6);
		
		Check temp2 = new Check("Pizza", table, cust3);
		temp2.setAmount("Pizza");
		assertTrue("Check should have correct amount owed", temp2.getAmountOwed() == 9);
		
		assertEquals("Customer log should not have any messages", cust1.log.size(), 0);
		assertEquals("Customer log should not have any messages", cust2.log.size(), 0);
		assertEquals("Customer log should not have any messages", cust3.log.size(), 0);
		cashier.msgHereIsMoney(temp, 16);
		assertTrue("Cashier should now have 1 check", cashier.checks.size() == 1);
		
		cashier.msgHereIsMoney(temp1, 6);
		assertTrue("Cashier should now have 2 checks", cashier.checks.size() == 2);
		
		cashier.msgHereIsMoney(temp2, 9);
		assertTrue("Cashier should now have 3 checks", cashier.checks.size() == 3);
		
		assertTrue("Check should be in paying state", cashier.checks.get(0).getS() == checkState.paying);
		assertTrue("Check should contain correct amount paid", cashier.checks.get(0).getAmountPaid() == 16);
		assertTrue("Check should contain correct amount owed", cashier.checks.get(0).getAmountOwed() == 0);
		assertTrue("Change should be " + cashier.checks.get(0).getAmountChange(), cashier.checks.get(0).getAmountChange() == 0);
		
		assertTrue("Check should be in paying state", cashier.checks.get(1).getS() == checkState.paying);
		assertTrue("Check should contain correct amount paid", cashier.checks.get(1).getAmountPaid() == 6);
		assertTrue("Check should contain correct amount owed", cashier.checks.get(1).getAmountOwed() == 0);
		assertTrue("Change should be " + cashier.checks.get(1).getAmountChange(), cashier.checks.get(0).getAmountChange() == 0);
		
		assertTrue("Check should be in paying state", cashier.checks.get(2).getS() == checkState.paying);
		assertTrue("Check should contain correct amount paid", cashier.checks.get(2).getAmountPaid() == 9);
		assertTrue("Check should contain correct amount owed", cashier.checks.get(2).getAmountOwed() == 0);
		assertTrue("Change should be " + cashier.checks.get(2).getAmountChange(), cashier.checks.get(0).getAmountChange() == 0);
	
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Customer log should be 1", cust1.log.size(), 1);
		assertEquals("Customer log should be 1", cust2.log.size(), 1);
		assertEquals("Customer log should be 1", cust3.log.size(), 1);
        assertTrue("Customer should have received message that they can leave", cust1.log.containsString("Received message msgYouAreGoodToGo from cashier"));
        assertTrue("Customer should have received message that they can leave", cust2.log.containsString("Received message msgYouAreGoodToGo from cashier"));
        assertTrue("Customer should have received message that they can leave", cust3.log.containsString("Received message msgYouAreGoodToGo from cashier"));
        
        assertTrue("Check should be in Paid state", cashier.checks.get(0).getS() == checkState.Paid);
        assertTrue("Check should be in Paid state", cashier.checks.get(1).getS() == checkState.Paid);
        assertTrue("Check should be in Paid state", cashier.checks.get(2).getS() == checkState.Paid);

        assertEquals("Cashier should have some money", cashier.getSavings(), 16+9+6);

		
	}
	
//	public void testOneNormalCustomerScenario()
//	{
//		//setUp() runs first before this test!
//		
//		customer.cashier = cashier;//You can do almost anything in a unit test.			
//		
//		//check preconditions
//		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.checks.size(), 0);		
//		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		//step 1 of the test
//		//public Bill(Cashier, Customer, int tableNum, double price) {
//		Check check = new Check(customer, 2, 7.98);
//		cashier.HereIsBill(check);//send the message from a waiter
//
//		//check postconditions for step 1 and preconditions for step 2
//		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.checks.size(), 1);
//		
//		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
//		
//		assertEquals(
//				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		assertEquals(
//				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		//step 2 of the test
//		cashier.ReadyToPay(customer, check);
//		
//		//check postconditions for step 2 / preconditions for step 3
//		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
//				cashier.checks.get(0).state == checkState.customerApproached);
//		
//		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received ReadyToPay"));
//
//		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
//				+ cashier.checks.get(0).bill.netCost, cashier.checks.get(0).bill.netCost == 7.98);
//		
//		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
//					cashier.checks.get(0).bill.customer == customer);
//		
//		
//		//step 3
//		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
//		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//					cashier.pickAndExecuteAnAction());
//		
//		//check postconditions for step 3 / preconditions for step 4
//		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
//				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
//	
//			
//		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
//		
//		
//		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
//				+ cashier.checks.get(0).changeDue, cashier.checks.get(0).changeDue == 0);
//		
//		
//		
//		//step 4
//		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//					cashier.pickAndExecuteAnAction());
//		
//		//check postconditions for step 4
//		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
//				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
//	
//		
//		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
//				cashier.checks.get(0).state == checkState.done);
//		
//		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
//				cashier.pickAndExecuteAnAction());
//		
//	
//	}//end one normal customer scenario
	
	
}
