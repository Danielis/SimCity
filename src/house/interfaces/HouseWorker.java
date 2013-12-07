package house.interfaces;

import house.guis.HouseWorkerGui;

import javax.swing.Icon;

public interface HouseWorker {

	public String name = null;

	public abstract String getName();

	public abstract void setGui(HouseWorkerGui g);

}
