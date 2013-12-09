package transportation.test;


import java.util.concurrent.Semaphore;

import city.Interfaces.*;
import city.PersonAgent;
import city.test.mock.MockPerson;
import junit.framework.TestCase;
import restaurant.CashierAgent;
import restaurant.CashierAgent.CheckState;
import restaurant.CashierAgent.PaymentState;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockWaiter;
import restaurant.test.mock.MockMarket;
import transportation.BusAgent;
import transportation.BusStopAgent;
import transportation.Interfaces.Bus;
import transportation.Interfaces.BusStop;
import transportation.Interfaces.TransportationCompany;
import transportation.test.mock.MockBus;
import transportation.test.mock.MockBusStop;
import transportation.test.mock.EventLog;



/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 */
public class BusTest extends TestCase
{
    //these are instantiated for each test separately via the setUp() method.
    Bus bus;
    BusStop stop; //MockBusStop stop;
    //TransportationCompany company; //won't need to unti test busStop since it mostly interacts with bus
    PersonAgent poorPerson;
    PersonAgent wealthyPerson;
    
    public void setUp() throws Exception{
            super.setUp();                
            System.out.println("SET UP");
            bus = new MockBus("Bus");
            stop = new MockBusStop("Stop");
            poorPerson = new PersonAgent("Poor","No AI","Poor");  
            wealthyPerson = new PersonAgent("Wealthy","No AI","Wealthy");       
            
    }  
    
    //TEST 1 - BusStop get's a person
    public void test1_NormalBusAtStopNoPeopleScenario() throws Exception{
        	System.out.println("TEST 1");

            //SET UP SHOULD RUN BEFORE THIS FIRST TEST
            
            //PRECONDITIONS
            assertEquals("BusAgent should have 0 people in list. It doesn't", bus.getPeopleList().size(), 0);
            
            //PART 1 - Try and find a person on the bus
            bus.msgAtStop(stop);
            assertFalse("The Bus should not run an action since I am not calling stateChanged() here. It didn't.", stop.pickAndExecuteAnAction());
            assertEquals("The bus should only have one person in the stop. It Doesn't.", bus.getPeopleList().size(), 0);
            
    }
    
    public void test2_AddingMorePeople() throws Exception{
    	System.out.println("TEST 2");

    	//SET UP SHOULD RUN BEFORE THIS FIRST TEST

    	//PRECONDITIONS
    	assertEquals("BusStopAgent should have 0 people in list. It doesn't", stop.getPeopleList().size(), 0);

    	//PART 1 - Add a person to Bus and check to see if they are added to bus
        stop.msgImAtStop(poorPerson);
        bus.msgAtStop(stop);
//        assertFalse("The Bus should not run an action since I am not calling stateChanged() here. It didn't.", stop.pickAndExecuteAnAction());
//   //    assertEquals("The bus should only have one person in the stop. It Doesn't.", bus.getPeopleList().size(), 1);
//     
//       
//
//    	//PART 2 - Go To stop again and drop the person off
//        bus.msgAtStop(stop);
//        assertFalse("The Bus should not run an action since I am not calling stateChanged() here. It didn't.", stop.pickAndExecuteAnAction());
////        assertEquals("The bus not pick anyone else up since no one was added since It Doesn't.", bus.getPeopleList().size(), 1);
//        
//        stop.getPeopleList().add(bus.getPeopleList().get(0));
//        bus.getPeopleList().remove(0);
//    	assertEquals("Stop should now have 1 people again but really has " + stop.getPeopleList().size(), stop.getPeopleList().size(),1);
//    	assertEquals("Bus should now have 0 people. It doesn't", bus.getPeopleList().size(),0);
//

    }//end Test Two

}