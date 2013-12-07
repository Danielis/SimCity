package house.interfaces;

import house.guis.HouseOwnerGui;

import java.awt.image.ImageObserver;

import javax.swing.Icon;

public interface HouseOwner {

	boolean hungry = false;
	boolean houseNeedsRepairs = false;

	public abstract void DoneWithAnimation();

	public abstract ImageObserver copyOfAnimationPanel();

	public abstract void WaitForAnimation();

	public abstract void msgDoSomething();

	public abstract void setPurpose(String homePurpose);

	public abstract void setGui(HouseOwnerGui g);

	public abstract String getName();

	public abstract void EatAtHome();

	public abstract void MyHouseNeedsRepairs();

}
