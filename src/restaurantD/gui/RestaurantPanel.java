package restaurantD.gui;

import restaurantD.CashierAgent;
import restaurantD.CookAgent;
import restaurantD.CustomerAgent;
import restaurantD.HostAgent;
import restaurantD.MarketAgent;
import restaurantD.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener{
	
    //Host, cook, waiters and customers
     private CookAgent cook = new CookAgent("Chef Kirby");
     private HostAgent host = new HostAgent("Mario",cook); //Hack Setting cook to Host
     private CashierAgent cashier = new CashierAgent("Greed");
    
   
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private ListPanel marketPanel = new ListPanel(this, "Markets");
    private JPanel group = new JPanel();
    
    private JButton pause = new JButton("Pause");

    private RestaurantGui gui; //reference to main gui
    private CookGui g = new CookGui(cook,gui);


    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
        gui.animationPanel.addGui(g);
    	cook.setGui(g);
        
    	host.startThread();
        cook.startThread();//Hack to start thread
        cashier.startThread(); // taken off to be able to run tests in unit testing
        //cashier.setmenu(cook.getMenu(), cook.getMenuPrice());
        

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        group.add(marketPanel);
        group.add(waiterPanel);
        group.add(customerPanel);

        initRestLabel();
        pause.addActionListener(this);
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("     "), BorderLayout.EAST);
        restLabel.add(new JLabel("     "), BorderLayout.WEST);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == pause){
    		if(pause.getText() == "Pause"){
    			pause();
    			pause.setText("Restart");
    		}
    		else{
    			restart();
    			pause.setText("Pause");
    		}

    	}
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
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if(type.equals("Waiters")){
        	for(int i=0; i<waiters.size();i++){
        		WaiterAgent temp = waiters.get(i);
        		if(temp.getName() == name)
        			gui.updateInfoPanel(temp);
        	}
        }
        if(type.equals("Markets")){
        	for(int i=0; i<markets.size();i++){
        		MarketAgent temp = markets.get(i);
        		if(temp.getName() == name)
        			gui.updateInfoPanel(temp);
        	}
        }
    }
    public void pause(){
    	cook.togglePause();
    	host.togglePause();
    	for(int i=0;i<waiters.size();i++){
      	   waiters.get(i).togglePause();;
         }
    	for(int i=0;i<customers.size();i++){
    		customers.get(i).togglePause();
    	}
    }
    public void restart(){
    	cook.pauseRelease();
    	host.pauseRelease();
    	for(int i=0;i<waiters.size();i++){
     	   waiters.get(i).pauseRelease();
        }
    	for(int i=0;i<customers.size();i++){
    		customers.get(i).pauseRelease();
    	}
    }
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {
    	System.out.println("The Person's name is: " + name);
    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui, customers.size());

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		g.updatePosition();
    		customers.add(c);
    		c.startThread();
    	}
    	else if(type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui g = new WaiterGui(w,gui,waiters.size());
    		
    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setGui(g);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setmenu(cook.getMenu(), cook.getMenuPrice());
    		waiters.add(w);
    		host.addWaiter(w);
    		w.startThread();  
    		
    	}
    	else if(type.equals("Markets")){
    		MarketAgent m = new MarketAgent(name);
    		//MarketGui NA yet
    		cook.addMarket(m);
    		m.setCook(cook);
    		m.setCashier(cashier);
    		markets.add(m);
    		m.startThread();
    	}
    }
    
}
