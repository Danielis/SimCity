package transportation.test.mock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import restaurant.HostAgent;
import restaurant.CustomerState;
import restaurant.MyCustomer;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import transportation.BusAgent;
import transportation.BusStopAgent;
import transportation.Interfaces.Bus;
import transportation.Interfaces.BusStop;
import transportation.Interfaces.TransportationCompany;

public class MockTransportationCompany extends Mock implements TransportationCompany {

	//Variable
		String name;
		
		public List<BusStop> stops = Collections.synchronizedList(new ArrayList<BusStop>());
		public List<Bus> buses = Collections.synchronizedList(new ArrayList<Bus>());
		
		public Semaphore animSemaphore = new Semaphore(0, true);
		
		public CityAnimationPanel CityAnimPanel;

		
		public void TransportationCompanyAgent(String name){
			this.name = name;
			System.out.println("Added Bus Company " + name);
		}
		
		public String getName(){
			return name;
		}
		//Class Declarations
        public EventLog log;

        public MockTransportationCompany(String name) 
        {
                super(name);
                log = new EventLog();
        }
//      After any movement of Bus the transportation must check the location of bus with the busStops so call after Bus movement
    	public void busMoved(){ // need to call in Bus when it's gui makes a move.
    		//stateChanged();
    		Check(); //basically removed semaphore since it was slowing process down
    		// Semaphore would build up too many permits and execution of code would at times be slower than it had to be so a bus would not officially take a person until some time after he had already left the stop
    		// With this the function is called right away every time the bus moves and like a computer executed when the bus is at the stop.
    	}
        public void CarMoved(PersonGui g){
    		//Collision(g);
    	}
    	public void BusMoved(BusAgent g){
    		//Collision(g.gui);
    	}
    	@Override
    	public boolean pickAndExecuteAnAction() {
    		// This function checks bus and busStops to see if they are in the same position, Calls Bus msg that they are at stop
    		Check(); // no need to return true since every time buses move they call this function which is fine because as long as they move they should be checked
    		//super.clearSemaphore(); // potentially risky move but since this is called all the time since Buses move all the time it is done so that the pickandexecute doesn't always run unless a bus calls on it. Otherwise, this would run if the buses suddenly stopped moving due to past calls by multiple buses.
    		
    		return false;
    	}
    	private void Check()
    	{
    		for(int i=0;i<buses.size();i++){
    			for(int j=0;j<stops.size();j++){
    				if(buses.get(i).getGui().getXPosition() == stops.get(j).getGui().getXPosition() && buses.get(i).getGui().getYPosition() == stops.get(j).getGui().getYPosition() ){//compare locations of both
    					buses.get(i).msgAtStop((stops.get(j)));    					
    					//print("At Stop: " + stops.get(j).getName());
    				}
    			}
    		}
    	}
    	

    	public void msgReadyToOrder(Customer c) 
    	{
    		//EMPTY
    	}
    	
    	public void msgHereIsMyOrder(Customer c, String choice)
    	{
    		//EMPTY
    	}

    	public void msgOutOfFood(List<Boolean> foods, String choice, int table)
    	{
    		//EMPTY
    	}
    	
    	public void msgOrderIsReady(String choice, int table)
    	{
    		//EMPTY
    	}
    	
    	public void msgDoneEating(Customer c)
    	{
    		//EMPTY
    	}
    	
    	public void msgPayingAndLeaving(Customer c)
    	{
    		//EMPTY
    	}
    	
    	public void msgCheckIsComputed(Customer c, String choice, float owed)
    	{
    		log.add(new LoggedEvent("Received msgCheckIsComputed from cashier. Amount = " + owed));
    	}

		
}
