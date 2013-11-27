package city.test;

import java.util.List;
import java.util.Vector;

import logging.TrackerGui;
import city.Person;
import city.PersonAgent;
import city.PersonAgent.Item;
import city.PersonAgent.JobType;
import city.PersonAgent.WealthLevel;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import restaurant.test.mock.Mock;
import roles.Building;
import roles.Role;
import transportation.BusStopAgent;
import transportation.TransportationCompanyAgent;

public class PersonMock extends Mock implements Person{

	public PersonMock(String name) {
		super(name);
	}

	@Override
	public double setWealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCashThresholdUp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCashThresholdLow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMoneyThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WealthLevel parseWealth(String wealth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobType parseJob(String job) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setThreshold(int q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getQuantity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeItem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrackerGui(TrackerGui t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Item> getInvetory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBound_leftx() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBound_rightx() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBound_topy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBound_boty() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(PersonGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimationPanel(CityAnimationPanel panel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersonGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void WaitForAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoneWithAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WaitForBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoneWithBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMetro(TransportationCompanyAgent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BusStopAgent closestBusStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double checkBusStopDistance(BusStopAgent x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void closestCheckpoint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double checkPointDistance(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void transportationStatusBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWakeUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeftWork(Role r, double balance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingHome(Role r, double balance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToHome(String purpose) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingRestaurant(Role r, float myMoney) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBusStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDestinationStop(BusStopAgent P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BusStopAgent getDestinationBusStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(int X, int Y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void GoToSleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean CheckRestOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void GoEat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isHungry() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void WalkAimlessly() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean noRoleActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void DelayGoToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needsToBuy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canAfford(Item i) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getTotalMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void GoToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WorkAtRest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WorkAtBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBuildings(Vector<Building> buildings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPersonList(Vector<PersonAgent> people) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAI(Boolean noAI) {
		// TODO Auto-generated method stub
		
	}

}
