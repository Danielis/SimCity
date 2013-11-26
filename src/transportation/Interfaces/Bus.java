package transportation.Interfaces;

import city.PersonAgent;
import transportation.BusStopAgent;
import transportation.gui.BusGui;

public interface Bus {
	
	
	
	public BusGui getGui();

	
	public void msgImAtStop(BusStop busStop);
}