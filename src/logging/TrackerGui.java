package logging;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;

public class TrackerGui {
	
	JFrame trackerFrame;

	public TrackerGui() {
		trackerFrame = new JFrame("Tracker");
	    trackerFrame.setSize(new Dimension(500,100));
	    trackerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    trackerFrame.setResizable(false);
	    trackerFrame.setVisible(true);
	    
	    TracePanel tracker = new TracePanel();
        trackerFrame.getContentPane().add(tracker);
        tracker.setVisible(true);
        tracker.showAlertsForAllLevels();
        tracker.showAlertsForAllTags();
        tracker.alertOccurred(new Alert(AlertLevel.MESSAGE, AlertTag.GENERAL_CITY, "chris", "test message", new Date()));
	}
}
