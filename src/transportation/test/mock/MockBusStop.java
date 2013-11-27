package transportation.test.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.guis.CityAnimationPanel;
import agent.RestaurantMenu;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;
import restaurant.test.mock.*;
import transportation.BusAgent;
import transportation.Interfaces.Bus;
import transportation.Interfaces.BusStop;
import transportation.Interfaces.TransportationCompany;
import transportation.gui.BusStopGui;
import transportation.test.mock.EventLog;

public class MockBusStop extends Mock implements BusStop {
	//Variable
	BusStopGui gui = null;
	String name;
	boolean atStop;
	boolean nextStop;
	TransportationCompany company;

	public List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());

	public Semaphore animSemaphore = new Semaphore(0, true);

	public CityAnimationPanel CityAnimPanel;

	public EventLog log;

	public MockBusStop(String name) 
	{
            super(name);
            log = new EventLog();
    }
    
	public void msgHereIsAPayment(float amount)
	{
		log.add(new LoggedEvent("Received payment from cashier. Total = "+ amount));
	}
	public String getName(){ return super.getName();}
	//Stuff
	public BusStopGui getGui(){ return gui;}
	public List<PersonAgent> getPeopleList(){
		return people;
	}
	//Messages
	public void msgImAtStop(PersonAgent P){
		log.add(new LoggedEvent("Received person"));
		people.add(P);
	}
	
	public void msgPeopleGone(){
		log.add(new LoggedEvent("Notified that people were picked up by bus"));
	}

	public void getPeople(Bus B){
		log.add(new LoggedEvent("Adding people to the bus that called this"));
		for(int i=0;i<people.size();i++){
			B.getPeopleList().add(people.get(i));
			people.remove(i);
		}
		for(int i=0;i<people.size();i++){
			B.getPeopleList().add(people.get(i));
			people.remove(i);
		}
	}
	
	public boolean pickAndExecuteAnAction() {
		log.add(new LoggedEvent("PickandExecute was called to check on Gui depending on the size of people"));
		return false;
	}
}