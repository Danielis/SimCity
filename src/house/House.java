package house;


import house.guis.HouseGui;
import house.guis.HousePanel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import roles.Building;
import apartment.ApartmentCustomerRole;
import apartment.ApartmentWorkerRole;
import apartment.LandlordRole;


public class House extends Building{
	public HouseGui gui;
	public HousePanel panel;
	public String name; //Name of the restaurant
    public LandlordRole landlord;
    public List<ApartmentWorkerRole> workers = new ArrayList<ApartmentWorkerRole>();
    public List<ApartmentCustomerRole> tenants = new ArrayList<ApartmentCustomerRole>();
    //public String customerRole; //value is something like "Restaurant1CustomerRole"
    //public String type;
    
    public House(String name, HouseGui gui)
    {
    	type = buildingType.housingComplex;
    	this.gui = gui;
    	this.panel = gui.housingPanel;
    	this.name = name;
    	type = buildingType.housingComplex;
        gui.setTitle(name);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setAlwaysOnTop(true);
        gui.setAlwaysOnTop(false);
        gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}