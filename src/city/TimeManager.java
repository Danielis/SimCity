package city;

import city.TimeManager.Day;

public class TimeManager {
	
	public enum Day{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
	
	private static TimeManager instance = null;
	private static final long simStartTime = System.currentTimeMillis();
	private Day dayOfWeek = null;
	
	private int divider = 2;
	private int seconds;
	private int minutes;
	private int hours;
	private int days;
	private int weeks;
	private long offset = 0;
	private int dayOffset = 0;
	
	TimeManager(){
	}

	
	public static synchronized TimeManager getInstance() {
        if (instance == null) {
                instance = new TimeManager();
        }
        return instance;
}
	
	
	public long getCurrentSimTime() {
	    long currentTime = System.currentTimeMillis() + offset;
	    return currentTime - simStartTime;
	}
	
	
	public String TimeStr(){
		updateTime();
		String sec, min, hrs, day;
		sec = Integer.toString(seconds);
		if (seconds < 10)
			sec = "0" + sec;
		min = Integer.toString(minutes);
		if (minutes < 10)
			min = "0" + min;
		hrs = Integer.toString(hours);
		if (hours < 10)
			hrs = "0" + hrs;
		day = dayString();
		return day + " " + hrs + ":" + min + ":" + sec;
	}
	
	public void updateTime(){
		int mil = (int) getCurrentSimTime();
		
		seconds = (mil / divider) | 0;
		mil -= seconds * divider;
	
		minutes = (seconds / 60) | 0;
		seconds -= minutes * 60;
	
		hours = (minutes / 60) | 0;
		minutes -= hours * 60;
	
		days = (hours / 24) | 0;
		hours -= days * 24;
	
		weeks = (days / 7) | 0;
		days -= weeks * 7;
		
		if (days % 7 == (dayOffset + 6) % 7)
			dayOfWeek = Day.sunday;
		if (days % 7 == (dayOffset + 5) % 7)
			dayOfWeek = Day.saturday;
		if (days % 7 == (dayOffset + 4) % 7)
			dayOfWeek = Day.friday;
		if (days % 7 == (dayOffset + 3) % 7)
			dayOfWeek = Day.thursday;
		if (days % 7 == (dayOffset + 2)  % 7)
			dayOfWeek = Day.wednesday;
		if (days % 7 == (dayOffset + 1) % 7)
			dayOfWeek = Day.tuesday;
		if (days % 7 == dayOffset % 7)
			dayOfWeek = Day.monday;
	}
	
	public int getHour(){
		updateTime();
		return hours;
	}
	
	public Boolean isHourGreater(int temp){
		if (this.getHour() > temp)
			return true;
		else 
			return false;
	}
	
	
	public String dayString(){
		if (dayOfWeek == Day.monday)
			return "Monday";
		if (dayOfWeek == Day.tuesday)
			return "Tuesday";
		if (dayOfWeek == Day.wednesday)
			return "Wednesday";
		if (dayOfWeek == Day.thursday)
			return "Thursday";
		if (dayOfWeek == Day.friday)
			return "Friday";
		if (dayOfWeek == Day.saturday)
			return "Saturday";
		if (dayOfWeek == Day.sunday)
			return "Sunday";
		return "Error";
	}


	public Day getDay() {
		updateTime();
		return dayOfWeek;
	}


	public void setDivider(int i) {
		divider = i;		
	}


	public void setOffset(long i) {
		offset = i;
	}

	public void setDayOffset(int i) {
		dayOffset = -i;
	}
	
}

