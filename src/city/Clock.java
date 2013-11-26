package city;

import java.util.*;
import roles.*;
import housing.*;

import city.PersonAgent.location;
import city.TimeManager.Day;
import agent.Agent;

public class Clock extends Agent {
String name = "Clock";
List<PersonAgent> people = new ArrayList<PersonAgent>();
Day msgWakeLastSent = Day.thursday;
Day msgHomeLastSent = Day.thursday;
Day msgPayLastSent = Day.thursday;
	@Override
	
	
	protected boolean pickAndExecuteAnAction() {
		if (WakeUp() && DayOverWake())
			msgPeopleWake();
		if (GoHome() && DayOverHome())
			msgStopWorking();
		return true;
	}

	private boolean DayOverWake() {
		return (msgWakeLastSent != TimeManager.getInstance().getDay());
	}
	
	private boolean DayOverHome() {
		return (msgHomeLastSent != TimeManager.getInstance().getDay());
	}
	
	private Boolean GoHome(){
		return (TimeManager.getInstance().getHour() + 1 == 16);
	}

	private void msgPeopleWake() {
		msgWakeLastSent = TimeManager.getInstance().getDay();
		print("5AM. TIME TO WAKE UP!");
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
		print("6PM. TIME TO GO HOME!");
		for (PersonAgent p : people){
			p.msgWakeUp();
			if (p.Status.getLocation() == location.work){
				for (Role r : p.roles)
					r.msgLeaveWork();
			}
		}
		
	}
	
	private void msgPeoplePayOut() {
		msgPayLastSent = TimeManager.getInstance().getDay();
		print("12PM. Time to get payed!");
		for( PersonAgent p : people){
			p.msgWakeUp();
			if(p.Status.getLocation() == location.work){
				p.msgGetPaid();
			}
		}
	}
	
	private boolean WakeUp() {
		return (TimeManager.getInstance().getHour() + 1 == 7);
	}

	public void setPeople(Vector<PersonAgent> people2) {
		people = people2;
	}
	
	public String getName(){
		return name;
	}

}
