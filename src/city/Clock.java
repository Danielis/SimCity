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
Day LastChecked = Day.monday;
	@Override
	
	
	protected boolean pickAndExecuteAnAction() {
		if (WakeUp() && DayOverWake())
			msgPeopleWake();
		if (GoHome() && DayOverHome())
			msgStopWorking();

		if (GetPaid() && DayOverPay()){ // need to call message people around 12 so they can get paid then need to pay people a flat fee then
			msgPeoplePayOut();
			print("Clock Printed out at mid day!");
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
	private Boolean GetPaid(){
		return (TimeManager.getInstance().getHour() + 1 == 12);
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
		print("8PM. TIME TO GO HOME!");
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
		print("12PM. Time to get paid!");
		for( PersonAgent p : people){
			p.msgWakeUp();
			if(p.Status.getLocation() == location.work){
				p.msgGetPaid();
			}
		}
	}
	
	private boolean WakeUp() {
		return (TimeManager.getInstance().getHour() == 5);
	}

	public void setPeople(Vector<PersonAgent> people2) {
		people = people2;
	}
	
	public String getName(){
		return name;
	}

}
