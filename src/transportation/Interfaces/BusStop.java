package transportation.Interfaces;

import java.util.List;

import city.PersonAgent;
import transportation.BusAgent;
import transportation.gui.BusStopGui;

public interface BusStop {
	public String getName();
	public BusStopGui getGui();
	public void msgImAtStop(PersonAgent P);
	
	public void msgPeopleGone();
	public boolean pickAndExecuteAnAction();
	
	public void getPeople(Bus B);
	public List<PersonAgent> getPeopleList();
}
