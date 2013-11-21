package housing.interfaces;

import housing.LandlordAgent.HousingComplex;

public interface HousingWorker {

	public abstract void HereIsMoney(HousingComplex complex, double bill);

	public abstract void GoRepair(HousingComplex complex);

}
