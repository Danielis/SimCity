package housing.interfaces;

import housing.LandlordAgent;
import housing.LandlordAgent.HousingComplex;
import housing.guis.HousingWorkerGui;

public interface HousingWorker {

	public abstract void HereIsMoney(HousingComplex complex, double bill);

	public abstract void GoRepair(HousingComplex complex);

	public abstract void setGui(HousingWorkerGui g);

	public abstract void setLandlord(LandlordAgent landlord);

}
