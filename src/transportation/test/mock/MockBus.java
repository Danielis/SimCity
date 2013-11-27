package transportation.test.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.guis.CityAnimationPanel;
import transportation.BusAgent;
import transportation.BusStopAgent;
import transportation.Interfaces.Bus;
import transportation.Interfaces.BusStop;
import transportation.Interfaces.TransportationCompany;
import transportation.gui.BusGui;

public class MockBus extends Mock implements Bus {
//Data
	BusGui gui = null;
	double money;
	String name;
	boolean atStop;
	boolean nextStop;
	BusStop curStop;
	BusStopAgent tempStop; //used to hold a stop so that the curStop doesn't change and the people are able to instantly get off
	TransportationCompany company;
	Timer timer;
	BusAgent myself;

	
	public List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	public CityAnimationPanel CityAnimPanel;

    public EventLog log;
//constructor
    public MockBus(String name) 
    {
            super(name);
            log = new EventLog();
    }
//Messages
    public String getName(){ return super.getName();}
	public BusGui getGui(){ return gui;}
    
	public void msgAtStop(BusStop B){ // sent from BusCompany when Bus Gui matches Bus Stop Gui
    	atStop = true;
		log.add(new LoggedEvent("Recieved curStop as " + B.getName()));
    	curStop = B;
	}
	public void msgNextStop(){
		log.add(new LoggedEvent("Recieved message to continue to the next stop"));

	}
	public void msgBusGuiMoved(){
		log.add(new LoggedEvent("Recieved message that will tell metro company that the gui for me has moved"));

	}
	public boolean pickAndExecuteAnAction() {

		if (atStop) {
//			print("Called ActionAtStop for " + curStop.getName());
			curStop.getPeople(this);
			atStop=false;
			return true;
		}
		
		return false;
	}
	//utility
	public List<PersonAgent> getPeopleList(){
		return people;
	}
}