package restaurant.test;

import java.util.Date;

import city.PersonAgent;
import junit.framework.TestCase;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import restaurant.HostAgent.*;
import restaurant.CustomerState;
import restaurant.ProducerConsumerMonitor.Ticket;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.Restaurant;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.roles.*;
import restaurant.test.mock.MockCook;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockWaiter;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockCook.state;

public class WaiterTest extends TestCase
{
	
	Restaurant testRest;
	MockCook cook;
	ModernWaiterRole MWR;
	MockCustomer customer;
	PersonAgent myPerson;
	TrackerGui trackingWindow;
	
	public void setUp() throws Exception
	{
        super.setUp();                
        System.out.println("SET UP");
        testRest = new Restaurant(new RestaurantGui(), "Test");
        MWR = new ModernWaiterRole("Waiter", testRest);
        cook = new MockCook("MockCook", 1000);
        MWR.setCook(cook);
        customer = new MockCustomer("Mock Customer");
        myPerson = new PersonAgent("TestPerson", "No AI", "Average");
        MWR.setPerson(myPerson);
        trackingWindow = new TrackerGui();
        MWR.setTrackerGui(trackingWindow); //Solves errors
	}  

	//Test the monitor functionality
	public void test_TestingProducerConsumer() throws Exception
	{
		//Test Conditions
		assertTrue("Waiter should not be null", MWR != null);
		assertTrue("Cook should not be null", cook!= null);
		assertTrue("Customer should not be null", customer!=null);
		MWR.msgSitAtTable(customer, 0);
		assertEquals("Waiter should have one item", MWR.myCustomers.size(), 1);
		MWR.msgHereIsMyOrder(customer, "Steak");
		assertEquals("Customer should have chosen steak", MWR.myCustomers.get(0).choice, "Steak");
		//Create a ticket for the consumer producer monitor
		Ticket data = MWR.produce_item(MWR, "Steak", 0);
		//Give the monitor to the cook
        cook.msgHereIsMonitor(MWR.theMonitor);
        //Insert data through the waiter
        MWR.theMonitor.insert(data);
        //Make sure the cook runs his scheduler to grab the ticket
        assertTrue(cook.pickAndExecuteAnAction());
        //Check conditions
		assertEquals("Cook should have grabbed the ticket from the monitor", cook.orders.size(), 1);
		assertEquals("Cook's item order should have been steak", cook.orders.get(0).choice, "Steak");
		assertEquals("The table should be zero.", cook.orders.get(0).table, 0);
		assertEquals("The cook's waiter should be MWR", cook.orders.get(0).w, MWR);
		assertTrue("Scheduer should run for the order that is done cooking", cook.pickAndExecuteAnAction());
		assertEquals("The state of the item should be done.", cook.orders.get(0).s, state.done);
		assertTrue("Scheduler should have ran true", cook.pickAndExecuteAnAction());
		assertEquals("the order of the customer's food should be ready", MWR.myCustomers.get(0).s, CustomerState.orderReady);
		//Test is complete, Cook properly receives tickets. Waiter Receives back orders
	}
}