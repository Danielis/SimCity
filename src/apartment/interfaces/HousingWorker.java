package apartment.interfaces;

import apartment.LandlordRole;
import apartment.LandlordRole.HousingComplex;
import apartment.guis.ApartmentWorkerGui;
import logging.TrackerGui;

public interface HousingWorker {

	String name = null;
	
	public abstract String getName();

	public abstract void HereIsMoney(HousingComplex complex, double bill);

	public abstract void GoRepair(HousingComplex complex);

	public abstract void setGui(ApartmentWorkerGui g);

	public abstract void setLandlord(LandlordRole landlord);

	public abstract void setTrackerGui(TrackerGui trackingWindow);

}
