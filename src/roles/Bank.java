package roles;

import javax.swing.JFrame;

import bank.gui.BankGui;
import bank.gui.BankPanel;



public class Bank {
	public BankGui gui;
	public BankPanel panel;
	public String name; //Name of the restaurant
    public Location location;
    
    public Bank(BankGui gui, String name)
    {
    	this.gui = gui;
    	this.panel = gui.restPanel;
    	this.name = name;
        gui.setTitle(name);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}