package city;

import java.util.*;

import city.PersonAgent.location;
import city.TimeManager.Day;
import agent.Agent;

public class Clock extends Agent {
String name = "Clock";
List<PersonAgent> people = new ArrayList<PersonAgent>();
Day msgLastSent = Day.thursday;
	@Override
	
	
	protected boolean pickAndExecuteAnAction() {
		if (WakeUp() && DayOver())
			msgPeople();
		return true;
	}

	private boolean DayOver() {
		return (msgLastSent != TimeManager.getInstance().getDay());
	}

	private void msgPeople() {
		msgLastSent = TimeManager.getInstance().getDay();
		for (PersonAgent p : people){
			print("5AM. TIME TO WAKE UP!");
			p.msgWakeUp();
			if (p.Status.getLocation() == location.home){
				p.msgLeaveHome();
			}
		}
		
	}

	private boolean WakeUp() {
		return (TimeManager.getInstance().getHour() == 5);
	}

	public void setPeople(Vector<PersonAgent> people2) {
		people = people2;
	}

}
