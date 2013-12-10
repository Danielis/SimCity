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
 
    public ListPanel(RestaurantPanel rp, String type) {
    	hungry.addActionListener(this);
        restPanel = rp;
        this.type = type;
        setLayout(new FlowLayout());
        nameInput.setPreferredSize( new Dimension( 200, 24 ) );
        addPersonB.addActionListener(this);
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        pane.setPreferredSize( new Dimension( 200, 200 ) );
        add(pane);
        
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	//addPerson(nameInput.getText());
        }
        else {
        	for (JButton temp:list){
                if (e.getSource() == temp){
                    restPanel.showInfo(type, temp.getText());
                }
            }
        }
    }


	public void addCustomer(CustomerAgent c, RestaurantA r) {
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
