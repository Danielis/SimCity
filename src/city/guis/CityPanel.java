package city.guis;

import restaurant.CustomerAgent;
import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.Restaurant;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.MarketAgent;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
import roles.Building;
//adding transportation
import transportation.*;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;

import javax.imageio.ImageIO;
import javax.swing.*;

import bank.Bank;
import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class CityPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Oprah");
    private HostGui hostGui = new HostGui(host);
    public CashierAgent cashier = new CashierAgent("Squidward");
    private MarketAgent market1 = new MarketAgent("Market 1");
    private MarketAgent market2 = new MarketAgent("Market 2");
    private MarketAgent market3 = new MarketAgent("Market 3");
    public CookAgent cook = new CookAgent("Gordon Ramsay");
    private CookGui cookGui;
    public RestaurantPanel restPanel;
    public TransportationCompanyAgent metro = new TransportationCompanyAgent("Metro");
    
    int waiterindex = 0; 		//To assign waiters individual locations
    
    public Vector<Building> buildings = new Vector<Building>();
    public Vector<PersonAgent> people = new Vector<PersonAgent>();
    

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
        
        /*
        //Setting Metro and adding buses and busStops MODIFY FOR CITY CHANGES
        metro = new TransportationCompanyAgent("Metro");
        metro.startThread();
        
        BusAgent B = new BusAgent("Bus1");
        BusGui Bg = new BusGui(B,gui);
        gui.cityAnimationPanel.addGui(Bg);
        B.setGui(Bg);
        Bg.setPosition(295,250);
        Bg.isPresent();
        B.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBus(B);
        B.startThread();

        
        BusStopAgent S = new BusStopAgent("BusStop1");
        BusStopGui Sg = new BusStopGui(S,gui);
        gui.cityAnimationPanel.addGui(Sg);
        S.setGui(Sg);
        Sg.setPosition(395,250);
        Sg.isPresent();
        S.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S);
        Sg.isPresent();
        S.startThread();*/
        
        //Need to set bus destination to A so that it begins it's route then it should act alone, BusStop should react to person reaching
        // A by having person first move to Bus Stop A. This can be done by passing a command to go to Bus Stop A from cityGui.tranportationCompany 
        // Maybe people have a cityGui to know where the bus stops are
        //placing error to mark place
        
        //B.getGui().DoGoToCheckpoint('a'); // This makes 2 frames not show?
        
        //PersonAgent p1 = new PersonAgent("P1", "")
        
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
    	gui.trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.GENERAL_CITY, "CityPanel", "Person Created", new Date()));
    	PersonAgent p = new PersonAgent(name, job, wealth);
		PersonGui g = new PersonGui(p, gui);
		gui.cityAnimationPanel.addGui(g);
		p.setGui(g);
		p.setAnimationPanel(gui.cityAnimationPanel);
		p.setMetro(metro);
		p.setBuildings(buildings);
		people.add(p);
		p.setTrackerGui(gui.trackingWindow);
		p.startThread();
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
        Bg.setPosition(435,300);//395,125);
        Bg.setPresent(true);
        B.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBus(B);
        B.startThread();
        
        BusAgent B2 = new BusAgent("Bus2");
        BusGui B2g = new BusGui(B2,gui);
        gui.cityAnimationPanel.addGui(B2g);
        B2.setCompany(metro);
        B2.setGui(B2g);
        B2g.setPosition(395,300);//435,500);
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
        Sg.setPosition(395,125);
        S.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S);
        Sg.setPresent(true);
        S.startThread();
        
        BusStopAgent S2 = new BusStopAgent("BusStop2");
        BusStopGui S2g = new BusStopGui(S2,gui);
        gui.cityAnimationPanel.addGui(S2g);
        S2.setGui(S2g);
        S2g.setPosition(395,300);
        S2.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S2);
        S2g.setPresent(true);
        S2.startThread();
        
        BusStopAgent S3 = new BusStopAgent("BusStop3");
        BusStopGui S3g = new BusStopGui(S3,gui);
        gui.cityAnimationPanel.addGui(S3g);
        S3.setGui(S3g);
        S3g.setPosition(395,500);
        S3.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S3);
        S3g.setPresent(true);
        S3.startThread();
        
        BusStopAgent S4 = new BusStopAgent("BusStop4");
        BusStopGui S4g = new BusStopGui(S4,gui);
        gui.cityAnimationPanel.addGui(S4g);
        S4.setGui(S4g);
        S4g.setPosition(435,500);
        S4.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S4);
        S4g.setPresent(true);
        S4.startThread();
        
        BusStopAgent S5 = new BusStopAgent("BusStop5");
        BusStopGui S5g = new BusStopGui(S5,gui);
        gui.cityAnimationPanel.addGui(S5g);
        S5.setGui(S5g);
        S5g.setPosition(435,300);
        S5.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S5);
        S5g.setPresent(true);
        S5.startThread();
        
        BusStopAgent S6 = new BusStopAgent("BusStop6");
        BusStopGui S6g = new BusStopGui(S6,gui);
        gui.cityAnimationPanel.addGui(S6g);
        S6.setGui(S6g);
        S6g.setPosition(435,125);
        S6.setAnimationPanel(gui.cityAnimationPanel);
        metro.addBusStop(S6);
        S6g.setPresent(true);
        S6.startThread();
        
       
    }
    
    public void sendPersonToStop(){ //Trans: should be done by person AI 
    	this.personPanel.addPerson("TestPerson", "NO AI", "Wealthy");
    	PersonAgent testPerson = people.lastElement();
    	testPerson.transportationStatusBus();
    	testPerson.msgGoToRestaurant();
    	//testPerson.msgGoToBank("Deposit", 50);
    	//testPerson.msgGoToHome("Sleep");
    	//testPerson.msgGoToMarket("Buy", 1);
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
}
