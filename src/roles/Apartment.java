package roles;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import apartment.ApartmentCustomerRole;
import apartment.ApartmentWorkerRole;
import apartment.LandlordRole;
import apartment.guis.ApartmentGui;
import apartment.guis.ApartmentPanel;

public class Apartment extends Building{
	public ApartmentGui gui;
	public ApartmentPanel panel;
	public String name; //Name of the restaurant
    public LandlordRole landlord;
    public List<ApartmentWorkerRole> workers = new ArrayList<ApartmentWorkerRole>();
    public List<ApartmentCustomerRole> tenants = new ArrayList<ApartmentCustomerRole>();
    //public String customerRole; //value is something like "Restaurant1CustomerRole"
    //public String type;
    
    public Apartment(String name, ApartmentGui gui)
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