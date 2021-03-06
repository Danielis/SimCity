package city;

import java.util.*;
import roles.*;
import housing.*;

import city.PersonAgent.location;
import city.TimeManager.Day;
import agent.Agent;

public class Government extends Agent {
String name = "Government";
List<PersonAgent> people = new ArrayList<PersonAgent>();
Day msgWakeLastSent = Day.thursday;
Day msgHomeLastSent = Day.thursday;
Day msgPayLastSent = Day.thursday;
Day LastChecked = Day.monday;
	@Override
	
	
	protected boolean pickAndExecuteAnAction() {
		if (WakeUp() && DayOverWake())
			msgPeopleWake();
		
		if (GoHome() && DayOverHome())
			msgStopWorking();

		if (GetPaid() && DayOverPay()){ // need to call message people around 12 so they can get paid then need to pay people a flat fee then
			msgPeoplePayOut();
			print("Major: It's noon! Pay time.");
		}

		if (NewDay())
			print("It is now " + TimeManager.getInstance().dayString());

		return true;
	}

	private boolean NewDay() {
		if (LastChecked != TimeManager.getInstance().getDay()){
			LastChecked = TimeManager.getInstance().getDay();
			return true;
		}
		return false;
	}

	private boolean DayOverWake() {
		return (msgWakeLastSent != TimeManager.getInstance().getDay());
	}
	
	private boolean DayOverHome() {
		return (msgHomeLastSent != TimeManager.getInstance().getDay());
	}
	
	private boolean DayOverPay() {
		return (msgPayLastSent != TimeManager.getInstance().getDay());
	}
	
	private Boolean GoHome(){
		return (TimeManager.getInstance().getHour() + 1 == 20);
	}	
	
	private boolean WakeUp() {
		return (TimeManager.getInstance().getHour() == 4);
	}

	private Boolean GetPaid(){
		return (TimeManager.getInstance().getHour() + 1 == 12);
	}

	private void msgPeopleWake() {
		msgWakeLastSent = TimeManager.getInstance().getDay();
		print("Major: 5AM. TIME TO WAKE UP!");
		for (PersonAgent p : people){
			p.msgWakeUp();
			if (p.Status.getLocation() == location.home){
				for (Role r : p.roles){
					HousingCustomerRole x = (HousingCustomerRole) r;
					x.msgLeaveHome();
				}
			}
		}
		
	}

	private void msgStopWorking() {
		msgHomeLastSent = TimeManager.getInstance().getDay();
		print("Major: 8PM. TIME TO GO HOME!");
		for (PersonAgent p : people){
			p.msgWakeUp();
			if (p.Status.getLocation() == location.work){
				try{
				for (Role r : p.roles)
					r.msgLeaveWork();
				} catch (ConcurrentModificationException e){}
			}
		}
		
	}
	
	private void msgPeoplePayOut() {
		msgPayLastSent = TimeManager.getInstance().getDay();
		print("12PM. Time to get paid!");
		for( PersonAgent p : people){
			if(p.Status.getLocation() == location.work){
				p.msgGetPaid();
			}
		}
	}

	public void setPeople(Vector<PersonAgent> people2) {
		people = people2;
	}
	
	public String getName(){
		return name;
	}

}
