package transportation.Interfaces;

import city.guis.PersonGui;
import transportation.BusAgent;

public interface TransportationCompany {

	
	//Messages
	public abstract void busMoved();
	public abstract void CarMoved(PersonGui g);
	public abstract void BusMoved(BusAgent g);
	public abstract boolean pickAndExecuteAnAction();
}
