package logging;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TrackerGui {
	
	public JFrame trackerFrame;
	public TracePanel tracker;

	public TrackerGui() {
		//creates window
		trackerFrame = new JFrame("Tracker");
	    trackerFrame.setSize(new Dimension(500,100));
	    trackerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    trackerFrame.setResizable(true);
	    trackerFrame.setVisible(true);

	    //creates tracker panel.  
	    tracker = new TracePanel(this);
        trackerFrame.getContentPane().add(tracker);
        tracker.setVisible(true);
        tracker.showAlertsForAllLevels();
        tracker.showAlertsForAllTags();
        
        //test message
        tracker.alertOccurred(new Alert(AlertLevel.ERROR, AlertTag.GENERAL_CITY, "chris", "test message", new Date()));
	}
}
