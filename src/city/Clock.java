package city;

import java.util.*;

import city.PersonAgent.location;
import city.TimeManager.Day;
import agent.Agent;

public class Clock extends Agent {
String name = "Clock";
List<PersonAgent> people = new ArrayList<PersonAgent>();
Day msgWakeLastSent = Day.thursday;
Day msgHomeLastSent = Day.thursday;
	@Override
	
	
	protected boolean pickAndExecuteAnAction() {
		if (WakeUp() && DayOverWake())
			msgPeopleWake();
		if (GoHome() && DayOverHome())
			msgPeopleGoHome();
		return true;
	}

	private boolean DayOverWake() {
		return (msgWakeLastSent != TimeManager.getInstance().getDay());
	}
	
	private boolean DayOverHome() {
		return (msgHomeLastSent != TimeManager.getInstance().getDay());
	}
	
	private Boolean GoHome(){
		return (TimeManager.getInstance().getHour() == 18);
	}

	private void msgPeopleWake() {
		msgWakeLastSent = TimeManager.getInstance().getDay();
		print("5AM. TIME TO WAKE UP!");
		for (PersonAgent p : people){
			p.msgWakeUp();
			if (p.Status.getLocation() == location.home){
				p.msgLeaveHome();
			}
		}
		
	}

	private void msgPeopleGoHome() {
		msgHomeLastSent = TimeManager.getInstance().getDay();
		print("6PM. TIME TO GO HOME!");
		for (PersonAgent p : people){
			p.msgWakeUp();
			if (p.Status.getLocation() == location.work){
				p.msgLeaveWork();
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
