package restaurantA.test;

import java.util.Date;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import junit.framework.TestCase;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import restaurantA.HostAgent.*;
import restaurantA.CookAgent;
import restaurantA.ModernWaiterAgent;
import restaurantA.ProducerConsumerMonitor.Ticket;
import restaurantA.CashierAgent;
import restaurantA.HostAgent;
import restaurantA.RestaurantA;
import restaurantA.gui.RestaurantGui;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Waiter;
import restaurantA.test.mock.MockCook;
import restaurantA.test.mock.MockCustomer;
import restaurantA.test.mock.MockWaiter;
import restaurantA.test.mock.MockMarket;
import restaurantA.test.mock.MockCook.*;

public class ProducerConsumerTests extends TestCase
{
	
	RestaurantA testRest;
	CookAgent cook;
	ModernWaiterAgent MWR;
	MockCustomer customer;
	PersonAgent myPerson;
	TrackerGui trackingWindow;
    
	public void setUp() throws Exception
	{
        super.setUp();                
        System.out.println("SET UP");
        testRest = new RestaurantA(new RestaurantGui(), "Test");
        MWR = new ModernWaiterAgent("Waiter", testRest, 100);
        cook = new CookAgent("MockCook");
        MWR.setCook(cook);
        customer = new MockCustomer("Mock Customer");
        
        myPerson = new PersonAgent("TestPerson", "No AI", "Average");
        
        MWR.setPerson(myPerson);
        trackingWindow = new TrackerGui();
        MWR.setTrackerGui(trackingWindow); //Solves errors
	}  

	//Test the monitor functionality
//	public void test_TestingProducerConsumer() throws Exception
//	{
//		//Test Conditions
//		assertTrue("Waiter should not be null", MWR != null);
//		assertTrue("Cook should not be null", cook!= null);
//		assertTrue("Customer should not be null", customer!=null);
//		MWR.msgSitAtTable(customer, 0);
//		assertEquals("Waiter should have one item", MWR.myCustomers.size(), 1);
//		MWR.msgHereIsMyOrder(customer, "Steak");
//		assertEquals("Customer should have chosen steak", MWR.myCustomers.get(0).choice, "Steak");
//		//Create a ticket for the consumer producer monitor
//		Ticket data = MWR.produce_item(MWR, "Steak", 0);
//		//Give the monitor to the cook
//        cook.msgHereIsMonitor(MWR.getTheMonitor());
//        //Insert data through the waiter
//        MWR.getTheMonitor().insert(data);
//        //Make sure the cook runs his scheduler to grab the ticket
//        assertTrue(cook.pickAndExecuteAnAction());
//        //Check conditions
//		assertEquals("Cook should have grabbed the ticket from the monitor", cook.orders.size(), 1);
//		assertEquals("Cook's item order should have been steak", cook.orders.get(0).choice, "Steak");
//		assertEquals("The table should be zero.", cook.orders.get(0).table, 0);
//		assertEquals("The cook's waiter should be MWR", cook.orders.get(0).w, MWR);
//		assertTrue("Scheduer should run for the order that is done cooking", cook.pickAndExecuteAnAction());
//		assertEquals("The state of the item should be done.", cook.orders.get(0).s, state.done);
//		assertTrue("Scheduler should have ran true", cook.pickAndExecuteAnAction());
//		assertEquals("the order of the customer's food should be ready", MWR.myCustomers.get(0).s, CustomerState.orderReady);
//		//Test is complete, Cook properly receives tickets. Waiter Receives back orders
//	}
}