package city.guis;

import restaurant.gui.RestaurantPanel;
import roles.Building;
//adding transportation
import transportation.*;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;

import javax.swing.*;

import city.PersonAgent;
import city.PersonAgent.Item;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class CityPanel extends JPanel {

    public RestaurantPanel restPanel;
    public TransportationCompanyAgent metro = new TransportationCompanyAgent("Metro");
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    public Vector<Building> buildings = new Vector<Building>();
    public Vector<PersonAgent> people = new Vector<PersonAgent>();
    
	public Boolean noAI = false;

    private CityListPanel personPanel = new CityListPanel(this, "People");
    private JPanel group = new JPanel();
        
    //Image Related
    ImageIcon iconOwner;
    JLabel picOwner;
    ImageIcon iconMenu;
    JLabel picMenu;
    
    private CityGui gui; //reference to main gui
        
    public CityPanel(CityGui gui) {
        this.gui = gui;

        metro.startThread();
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(personPanel);
        add(group);
    }

    //Displays the person information
    public void showPersonInfo(String name)
    {
        for (int i = 0; i < people.size(); i++) {
        	
            PersonAgent temp = people.get(i);
            if (temp.getName() == name)
            {
                personPanel.updatePerson(temp);
                gui.updatePersonInformationPanel(temp);
                personPanel.updatePerson(temp);
            }
        }
    }
    
	//Adds a person to the city
    public void addPerson(String name, String job, String wealth) 
    {
    	//gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Person Created", new Date()));
    	PersonAgent p = new PersonAgent(name, job, wealth, buildings);
		PersonGui g = new PersonGui(p, gui);
		p.setTrackerGui(gui.trackingWindow);
		gui.cityAnimationPanel.addGui(g);
		p.setGui(g);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setMetro(metro);
		p.setBuildings(buildings);
		if(Math.random() > .49)
			p.setBus(true);
		if(p.getName().equals("Bus"))
			p.setBus(true);
		people.add(p);
		p.startThread();
    }
    
    public void addPerson(PersonAgent p) 
    {
    	//gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Person Created", new Date()));
		p.setBuildings(buildings);
    	PersonGui g = new PersonGui(p, gui);
		p.setTrackerGui(gui.trackingWindow);
		gui.cityAnimationPanel.addGui(g);
		p.setAI(noAI);
		p.setGui(g);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setMetro(metro);
		//p.setBuildings(buildings);
		if(Math.random() > .49)
			p.setBus(true);
		if(p.getName().equals("Bus"))
			p.setBus(true);
		people.add(p);
		p.startThread();
    }
    
    public void addPerson(PersonAgent p, boolean b) 
    {
    	//gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Person Created", new Date()));
		p.setBuildings(buildings);
    	PersonGui g = new PersonGui(p, gui, b);
		p.setTrackerGui(gui.trackingWindow);
		gui.cityAnimationPanel.addGui(g);
		p.setAI(noAI);
		p.setGui(g);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setMetro(metro);
		//p.setBuildings(buildings);
		if(Math.random() > .49)
			p.setBus(true);
		if(p.getName().equals("Bus"))
			p.setBus(true);
		people.add(p);
		p.startThread();
		
    }
    
    public void addWorker(String name, String job, String wealth) 
    {
    	//gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Person Created", new Date()));
    	PersonAgent p = new PersonAgent(name, job, wealth, buildings);
		PersonGui g = new PersonGui(p, gui);
		p.setTrackerGui(gui.trackingWindow);
		gui.cityAnimationPanel.addGui(g);
		p.setAI(noAI);
		p.setGui(g);
		
		
		int offsetX = (int)(Math.random() * 300);
		offsetX -= 150;
		int offsetY = (int)(Math.random() * 20);
		
		
		g.setPosition(p.job.workBuilding.entrance.x + offsetX, p.job.workBuilding.entrance.y + offsetY);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setMetro(metro);
		//p.setBuildings(buildings);
		if(Math.random() > .49)
			p.setBus(true);
		if(p.getName().equals("Bus"))
			p.setBus(true);
		people.add(p);
		p.startThread();
		p.setBus(false);
	
    }
    
    public void addStudent(String name, String job, String wealth) 
    {
    	//gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Person Created", new Date()));
    	PersonAgent p = new PersonAgent(name, job, wealth, buildings);
		PersonGui g = new PersonGui(p, gui);
		p.setTrackerGui(gui.trackingWindow);
		gui.cityAnimationPanel.addGui(g);
		p.setAI(noAI);
		p.setGui(g);
		
		System.out.println("************************");
		System.out.println(" " + gui);
		
		Random rand = new Random();
		
		g.setPosition(100,100);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setMetro(metro);
		if(Math.random() > .49)
			p.setBus(true);
		if(p.getName().equals("Bus"))
			p.setBus(true);
		people.add(p);
		p.startThread();
		p.setBus(false);
	
    }
    public void createBusSystem() //Trans: Once AI and adding implementations are done, we can make this just an addBus or addStop function
    {
    	gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Bus System Created", new Date()));
        //Buses
        BusAgent B = new BusAgent("Bus1");
        BusGui Bg = new BusGui(B,gui);
        gui.cityAnimationPanel.addGui(Bg);
        B.setCompany(metro);
        B.setGui(Bg);
        Bg.setPosition(430,300);//395,125);
        Bg.setPresent(true);
        B.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBus(B);
        B.startThread();
        
        BusAgent B2 = new BusAgent("Bus2");
        BusGui B2g = new BusGui(B2,gui);
        gui.cityAnimationPanel.addGui(B2g);
        B2.setCompany(metro);
        B2.setGui(B2g);
        B2g.setPosition(402,300);//435,500);
        B2g.setPresent(true);
        B2.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBus(B2);
        B2.startThread();
        
        
        //BusStops
        BusStopAgent S = new BusStopAgent("BusStop1");
        BusStopGui Sg = new BusStopGui(S,gui);
        gui.cityAnimationPanel.addGui(Sg);
        S.setCompany(metro);
        S.setGui(Sg);
        Sg.setPosition(402,125);
        S.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S);
        Sg.setPresent(true);
        S.startThread();
        
        BusStopAgent S2 = new BusStopAgent("BusStop2");
        BusStopGui S2g = new BusStopGui(S2,gui);
        gui.cityAnimationPanel.addGui(S2g);
        S2.setGui(S2g);
        S2g.setPosition(402,300);
        S2.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S2);
        S2g.setPresent(true);
        S2.startThread();
        
        BusStopAgent S3 = new BusStopAgent("BusStop3");
        BusStopGui S3g = new BusStopGui(S3,gui);
        gui.cityAnimationPanel.addGui(S3g);
        S3.setGui(S3g);
        S3g.setPosition(402,500);
        S3.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S3);
        S3g.setPresent(true);
        S3.startThread();
        
        BusStopAgent S4 = new BusStopAgent("BusStop4");
        BusStopGui S4g = new BusStopGui(S4,gui);
        gui.cityAnimationPanel.addGui(S4g);
        S4.setGui(S4g);
        S4g.setPosition(430,500);
        S4.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S4);
        S4g.setPresent(true);
        S4.startThread();
        
        BusStopAgent S5 = new BusStopAgent("BusStop5");
        BusStopGui S5g = new BusStopGui(S5,gui);
        gui.cityAnimationPanel.addGui(S5g);
        S5.setGui(S5g);
        S5g.setPosition(430,300);
        S5.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S5);
        S5g.setPresent(true);
        S5.startThread();
        
        BusStopAgent S6 = new BusStopAgent("BusStop6");
        BusStopGui S6g = new BusStopGui(S6,gui);
        gui.cityAnimationPanel.addGui(S6g);
        S6.setGui(S6g);
        S6g.setPosition(430,125);
        S6.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S6);
        S6g.setPresent(true);
        S6.startThread();
        
       
    }
    
    public void setRestPanel(RestaurantPanel panel)
    {
    	restPanel = panel;
    }
    
    public void pause()
    {

    }
    
    public void resume()
    {

    }

    public void refresh()
    {
    	//gui.updateLastPerson();
    }

	public void setBuildings(Vector<Building> buildings2) {
		buildings = buildings2;
	}

	public void setNoAI(boolean b) {
		noAI = true;
	}
}
