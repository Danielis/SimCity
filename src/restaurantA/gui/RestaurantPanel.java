package restaurantA.gui;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

import restaurantA.CashierAgent;
import restaurantA.CookAgent;
import restaurantA.CustomerAgent;
import restaurantA.HostAgent;
import restaurantA.Table;
import restaurantA.WaiterAgent;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    public HostAgent host = new HostAgent("Aleena");
    public CashierAgent cashier = new CashierAgent("Cashier");
    public CookAgent cook = new CookAgent("Chef", cashier);
  
	
    
    private HostGui hostGui = new HostGui(host);
    private int numTables = 4;
    public ArrayList<Table> tables;

    public Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    public Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
    	
    	setTables();
    	
        this.gui = gui;
        host.setGui(hostGui);
        gui.animationPanel.addGui(hostGui);
      
        CookGui g = new CookGui(cook, gui);
    	gui.animationPanel.addGui(g);// dw
    	cook.setGui(g);
        
//        for (int ix = 0; ix < 1; ix++) {
//    		WaiterAgent newWaiter = new WaiterAgent("Waiter " + ix, host);
//    		newWaiter.startThread();
//    		WaiterGui waiterGui = new WaiterGui(newWaiter, newWaiter.tables);
//    		//newWaiter.setHost(host);
//    		newWaiter.setGui(waiterGui);
//  
//    		gui.animationPanel.addGui(waiterGui);
//    		host.addWaiter(newWaiter);
//    	}

        gui.animationPanel.addGui(hostGui);
        gui.animationPanel.setHost(host);
        gui.animationPanel.setTables(host.tables);
        host.startThread();
        cashier.startThread();
        cook.startThread();	

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);
        
        initRestLabel();
        //add(restLabel);
        add(group);
    }

    private void setTables() {
		// TODO Auto-generated method stub
    	int startingX = 70;
		int startingY = 50;
		int spacerY = 70;
		int spacerX = spacerY + 50;
				
		// make some tables
		tables = new ArrayList<Table>(numTables);
		for (int ix = 0; ix < numTables/2; ix++) {
			Table tempTable = new Table(ix, startingX+spacerX*ix, startingY);
			tables.add(tempTable);//how you add to a collections
		}
		for (int ix = 0; ix < numTables/2; ix++) {
			Table tempTable = new Table(ix, startingX+spacerX*ix, startingY + spacerY);
			tables.add(tempTable);//how you add to a collections
		}
		
		host.tables = this.tables;
	}

	/**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new FlowLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        
       
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                
                if (name.isEmpty())
        			name = "Customer";
                
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        
        if (type.equals("Waiters")) {
        	//System.out.println("waiter pressed");

            for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
                
                if (name.isEmpty())
        			name = "Waiter";
                
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean isHungry) {

    	if (type.equals("Customers")) {
    		if (name.isEmpty())
    			name = "Customer";
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    		
    		if (isHungry){
    		g.setHungry();
    		}
    		
    		
    		
    		//REMOVE AT SOME POINT. currently for testing purposes
    		//TODO
//    		Integer temp = Integer.valueOf(name);
//    		
//    		if (temp >= 0)
//    			for (int i = 0; i < temp; i++){
//    				CustomerAgent cust = new CustomerAgent("Cust" + i);
//    				customers.add(cust);
//    				cust.setHost(host);
//    				cust.startThread();
//    				
//    				CustomerGui gc = new CustomerGui(cust, gui);
//    				gui.animationPanel.addGui(gc);
//    				cust.setGui(gc);
//    				if (isHungry){
//    		    		gc.setHungry();
//    		    		}
//    				
//    				System.out.println("added " + cust.getCustomerName());
//    			}
    	}
    	
    	if (type.equals("Waiters")){
			WaiterAgent newWaiter = new WaiterAgent(name, host, cook, cashier);
    
    		WaiterGui waiterGui = new WaiterGui(newWaiter, newWaiter.tables, 60 + 30 * waiters.size(), 0);
    		newWaiter.setGui(waiterGui);
    		newWaiter.startThread();
    		host.waiterAdded();
    		waiters.add(newWaiter);
  
    		gui.animationPanel.addGui(waiterGui);
    		host.addWaiter(newWaiter);
		}
    }
    
//    public void addWaiter(String name){
//    	WaiterAgent w = new restaurant.WaiterAgent(name);
// //   	WaiterGui g = newWaiterGui(w, gui);
// //   	gui.animationPanel.addGui(g);
//    	
//    	w.setHost(host);
//  //  	w.setGui(g);
//    	w.startThread();
//    	host.setWaiterAgent(w);
//    }

}
