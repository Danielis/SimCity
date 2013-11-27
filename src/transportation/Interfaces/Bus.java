package transportation.Interfaces;

import java.util.List;

import city.PersonAgent;
import transportation.BusStopAgent;
import transportation.gui.BusGui;

public interface Bus {
	
	public String getName();
	public BusGui getGui();
	public void msgAtStop(BusStop B);
	public void msgNextStop();
	public void msgBusGuiMoved();
	public List<PersonAgent> getPeopleList();
	
	public boolean pickAndExecuteAnAction();
}