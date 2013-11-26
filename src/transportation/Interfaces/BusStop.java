package transportation.Interfaces;

import city.PersonAgent;
import transportation.gui.BusStopGui;

public interface BusStop {
	public BusStopGui getGui() ;
	public void msgImAtStop(PersonAgent P);
	
	public void msgPeopleGone();
}
