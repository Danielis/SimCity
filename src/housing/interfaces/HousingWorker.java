package housing.interfaces;

import housing.LandlordRole;
import housing.LandlordRole.HousingComplex;
import housing.guis.HousingWorkerGui;

public interface HousingWorker {

	public abstract void HereIsMoney(HousingComplex complex, double bill);

	public abstract void GoRepair(HousingComplex complex);

	public abstract void setGui(HousingWorkerGui g);

	public abstract void setLandlord(LandlordRole landlord);

}
