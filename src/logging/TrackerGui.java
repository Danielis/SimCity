package logging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TrackerGui {

	public JFrame trackerFrame;
	public TracePanel tracker;
	JButton cityButton;
	JButton bankButton;
	JButton restButton;
	JButton marketButton;
	JButton houseButton;
	JButton allButton;
	JPanel leftGroup = new JPanel();
	JPanel rightGroup = new JPanel();

	public TrackerGui() {
		//creates window
		trackerFrame = new JFrame("Tracker");
		trackerFrame.setSize(new Dimension(500,100));
		trackerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		trackerFrame.setResizable(true);
		trackerFrame.setVisible(true);
		trackerFrame.setAlwaysOnTop(true);

		//creates tracker panel.  
		tracker = new TracePanel(this);
		trackerFrame.getContentPane().add(tracker, BorderLayout.NORTH);
		tracker.setVisible(true);
		tracker.showAlertsForAllLevels();
		tracker.showAlertsForAllTags();

		cityButton = new JButton("City Tag");
		bankButton = new JButton("Bank Tag");
		restButton = new JButton("Restaurant Tag");
		marketButton = new JButton("Market Tag");
		houseButton = new JButton("Housing Tag");
		allButton = new JButton("All Tags");
		

		leftGroup.add(restButton, BorderLayout.NORTH);
		leftGroup.add(bankButton, BorderLayout.SOUTH);
		trackerFrame.getContentPane().add(leftGroup, BorderLayout.WEST);
		rightGroup.add(marketButton, BorderLayout.NORTH);
		rightGroup.add(houseButton, BorderLayout.SOUTH);
		trackerFrame.getContentPane().add(rightGroup, BorderLayout.EAST);
		trackerFrame.getContentPane().add(cityButton, BorderLayout.CENTER);
		trackerFrame.getContentPane().add(allButton, BorderLayout.SOUTH);
		//test message
		tracker.alertOccurred(new Alert(AlertLevel.ERROR, AlertTag.GENERAL_CITY, "chris", "test message", new Date()));
	}
}
