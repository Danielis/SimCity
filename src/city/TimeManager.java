package city;

public class TimeManager {
	
	enum Day{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
	
	private static TimeManager instance = null;
	private static final long simStartTime = System.currentTimeMillis();
	private Day dayOfWeek = null;
	
	private int milliInWeek = 10080000;
	private int milliInDay = 1440000;
	private int milliInHour = 60000;
	
	TimeManager(){
	}

	
	public static synchronized TimeManager getInstance() {
        if (instance == null) {
                instance = new TimeManager();
        }
        return instance;
}
	
	
	public long getCurrentSimTime() {
	    long currentTime = System.currentTimeMillis();
	    return currentTime - simStartTime;
	}
	
	public int returnWeek(){
		return (int) (getCurrentSimTime() % milliInWeek);
	}
	
	
	public Day returnDay(){
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 0)) == 0)
			return Day.monday;
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 1)) == 0)
			return Day.tuesday;
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 2)) == 0)
			return Day.wednesday;
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 3)) == 0)
			return Day.thursday;
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 4)) == 0)
			return Day.friday;
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 5)) == 0)
			return Day.saturday;
		if (getCurrentSimTime() % (returnWeek() * milliInWeek + (milliInDay * 6)) == 0)
			return Day.sunday;
		return null;
	}
	
	
}

