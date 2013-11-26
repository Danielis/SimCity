package logging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import market.MarketCustomerRole;
import market.MarketWorkerAgent;

public class TrackerGui implements ActionListener {

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
		trackerFrame = new JFrame("Tracker - Choose a tag to filter - Move/Resize as desired");
		trackerFrame.setLocation(700, 50);
		trackerFrame.setSize(new Dimension(550,250));
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

		cityButton = new JButton("City");
		bankButton = new JButton("Bank");
		restButton = new JButton("Restaurant");
		marketButton = new JButton("Market");
		houseButton = new JButton("Housing");
		allButton = new JButton("All");

		cityButton.addActionListener(this);
		bankButton.addActionListener(this);
		restButton.addActionListener(this);
		marketButton.addActionListener(this);
		houseButton.addActionListener(this);
		allButton.addActionListener(this);

		leftGroup.add(restButton, BorderLayout.NORTH);
		leftGroup.add(bankButton, BorderLayout.SOUTH);
		trackerFrame.getContentPane().add(leftGroup, BorderLayout.WEST);
		rightGroup.add(marketButton, BorderLayout.NORTH);
		rightGroup.add(houseButton, BorderLayout.SOUTH);
		trackerFrame.getContentPane().add(rightGroup, BorderLayout.EAST);
		trackerFrame.getContentPane().add(cityButton, BorderLayout.CENTER);
		trackerFrame.getContentPane().add(allButton, BorderLayout.SOUTH);
		trackerFrame.validate();
	}
	public void actionPerformed(ActionEvent e) {
	    
        if (e.getSource() == allButton)  {
        	tracker.showAlertsForAllTags();
        }
        if(e.getSource() == cityButton) {
        	tracker.showAlertsWithTag(AlertTag.GENERAL_CITY);
        }
        if(e.getSource() == bankButton) {
        	tracker.showAlertsWithTag(AlertTag.BANK);
        }
        if(e.getSource() == restButton) {
        	tracker.showAlertsWithTag(AlertTag.RESTAURANT);
        }
        if(e.getSource() == houseButton) {
        	tracker.showAlertsWithTag(AlertTag.HOUSING);
        }
        if(e.getSource() == marketButton) {
        	tracker.showAlertsWithTag(AlertTag.MARKET);
        }
//      
        
    }
}
