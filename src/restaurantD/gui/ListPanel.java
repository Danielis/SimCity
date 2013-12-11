package restaurantD.gui;

import restaurantA.RestaurantA;
import restaurantD.CustomerAgent;
import restaurantD.HostAgent;
import restaurantD.RestaurantD;

import javax.swing.*;

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
    private JPanel buttonPanel = new JPanel();
    private JButton addPersonB = new JButton("Add");
    private JTextField nameField = new JTextField();
    
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
  

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        JLabel typeLabel = new JLabel(type);
        typeLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        add(typeLabel);
        
        Dimension nameFieldSize = new Dimension(100,25);
        nameField.setPreferredSize(nameFieldSize);
        nameField.setMinimumSize(nameFieldSize);
        nameField.setMaximumSize(nameFieldSize);
        add(nameField);
        
        
        addPersonB.addActionListener(this);
        Dimension buttonHolder = new Dimension(100,25);
        buttonPanel.setPreferredSize(buttonHolder);
        buttonPanel.setMinimumSize(buttonHolder);
        buttonPanel.setMaximumSize(buttonHolder);
        buttonPanel.add(addPersonB);
        add(buttonPanel);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == addPersonB) {
//        	// Chapter 2.19 describes showInputDialog()
//            addPerson(nameField.getText());
//        }
//        else {
//        	// Isn't the second for loop more beautiful?
//            /*for (int i = 0; i < list.size(); i++) {
//                JButton temp = list.get(i);*/
//        	for (JButton temp:list){
//                if (e.getSource() == temp)
//                    restPanel.showInfo(type, temp.getText());
//            }
//        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(CustomerAgent c, RestaurantD r) {
     
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
        validate();
        
    }
    /**
     * If Pause button is pressed then it will flip it's text and send a message to the Agent Base class
     * This base class will then aquire or release a new semaphore that will interrupt execution of it's code
     */
    
}
