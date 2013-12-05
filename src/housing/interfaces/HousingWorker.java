package housing.interfaces;

import logging.TrackerGui;
import housing.LandlordRole;
import housing.LandlordRole.HousingComplex;
import housing.guis.HousingWorkerGui;

public interface HousingWorker {

	String name = null;
	
	public abstract String getName();

	public abstract void HereIsMoney(HousingComplex complex, double bill);

	public abstract void GoRepair(HousingComplex complex);

	public abstract void setGui(HousingWorkerGui g);

	public abstract void setLandlord(LandlordRole landlord);

	public abstract void setTrackerGui(TrackerGui trackingWindow);

}
