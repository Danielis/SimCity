package roles;

import java.util.ArrayList;
import java.util.List;

import housing.HousingCustomerRole;
import housing.HousingWorkerRole;
import housing.LandlordRole;
import housing.guis.HousingGui;
import housing.guis.HousingPanel;

import javax.swing.JFrame;

import roles.Building.buildingType;

public class Apartment extends Building{
	public HousingGui gui;
	public HousingPanel panel;
	public String name; //Name of the restaurant
    public LandlordRole landlord;
    public List<HousingWorkerRole> workers = new ArrayList<HousingWorkerRole>();
    public List<HousingCustomerRole> tenants = new ArrayList<HousingCustomerRole>();
    //public String customerRole; //value is something like "Restaurant1CustomerRole"
    //public String type;
    
    public Apartment(String name, HousingGui gui)
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

	@Override
	public Boolean isOpen() {
		return true;
	}
}