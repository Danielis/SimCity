package restaurantA.gui;

import restaurantA.interfaces.Customer;
import restaurantA.CashierAgent;
import restaurantA.CookAgent;
import restaurantA.CustomerAgent;
import restaurantA.HostAgent;
import restaurantA.RestaurantA;
import restaurantA.WaiterAgent;

import javax.swing.*;

import bank.interfaces.BankHost;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JTextField nameInput = new JTextField();
    
    public JCheckBox hungry = new JCheckBox("Hungry?");
    private RestaurantPanel restPanel;
    private String type;
 

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
    	hungry.addActionListener(this);
        restPanel = rp;
        this.type = type;
        
        setLayout(new FlowLayout());
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        nameInput.setPreferredSize( new Dimension( 200, 24 ) );
        add(nameInput);
        
        if (type == "Customers")
        add(hungry);
        
   

        addPersonB.addActionListener(this);
        add(addPersonB);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        pane.setPreferredSize( new Dimension( 200, 200 ) );
        add(pane);
        
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	addPerson(nameInput.getText());
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp:list){
                if (e.getSource() == temp){
                    restPanel.showInfo(type, temp.getText());
               // System.out.println("item pressed");
                }
            }
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 10));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
         
            restPanel.addPerson(type, name, hungry.isSelected());//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            nameInput.setText("");
            hungry.setSelected(false);
            validate();
            
        }
    }

	public void addCustomer(Customer c, RestaurantA r) {
		 JButton button = new JButton(c.getName());
         button.setBackground(Color.white);

         Dimension paneSize = pane.getSize();
         Dimension buttonSize = new Dimension(paneSize.width - 20,
                 (int) (paneSize.height / 10));
         button.setPreferredSize(buttonSize);
         button.setMinimumSize(buttonSize);
         button.setMaximumSize(buttonSize);
         button.addActionListener(this);
         list.add(button);
         view.add(button);
      
         restPanel.addCustomer(type, c, true);//puts customer on list
         restPanel.showInfo(type, c.getName());//puts hungry button on panel
         nameInput.setText("");
         hungry.setSelected(false);
         validate();
	}

	public void addHost(HostAgent c) {
		restPanel.addHost(c);
	}

	public void addWaiter(WaiterAgent c) {
		restPanel.addWaiter(c);
	}

	public void addCook(CookAgent c) {
		restPanel.addCook(c);
	}

	public void addCashier(CashierAgent c) {
		restPanel.addCashier(c);
	}
}
