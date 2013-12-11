package restaurantC.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

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

	//creates scroll panes for waiters and customers
	//customer
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //waiter
    public JScrollPane waiterPane = 
    		new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    //creates some stuff for customers, I add waiters
    private JPanel view = new JPanel();
    private JPanel waiterView = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private List<JButton> waiterList = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Customer");
    private JButton addWaiter = new JButton("Waiter");
    
	JTextField userInput = new JTextField(20);

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
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        restPanel.inputButton.addActionListener(this);
        restPanel.inputText.addActionListener(this);
        restPanel.waiterButton.addActionListener(this);
        restPanel.waiterText.addActionListener(this);
        restPanel.pauseButton.addActionListener(this);
        
        addPersonB.addActionListener(this);
        add(addPersonB);
        
       addWaiter.addActionListener(this);
       add(addWaiter);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        waiterView.setLayout(new BoxLayout((Container) waiterView, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        waiterPane.setViewportView(waiterView);
        add(pane);
        add(waiterPane);
    }


    // Method from the ActionListener interface.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB || e.getSource() == addWaiter) {
        	restPanel.inputPanel.setVisible(true);
        	restPanel.inputHunger.setEnabled(false);
        	restPanel.inputButton.setEnabled(false);
        	restPanel.waiterButton.setEnabled(false);
        }
        else if(e.getSource() == restPanel.inputButton) {
        	addPerson(restPanel.inputText.getText(), restPanel.inputHunger.isSelected());
        	restPanel.inputText.setText("");
        	restPanel.inputHunger.setEnabled(false);
        	restPanel.inputButton.setEnabled(false);
        	restPanel.inputText.setEnabled(true);
        	restPanel.inputPanel.setVisible(false);
        }
        else if(e.getSource() == restPanel.waiterButton) {
        	//add waitier
        	addWaiter(restPanel.waiterText.getText());
        	restPanel.waiterText.setEnabled(true);
        	restPanel.inputPanel.setVisible(false);
        	restPanel.waiterText.setText("");
        }
        else if(e.getSource() == restPanel.inputText && restPanel.inputText.getText() != "")
        {
        	restPanel.inputHunger.setEnabled(true);
        	restPanel.inputButton.setEnabled(true);
        	restPanel.inputText.setEnabled(false);
        }
        else if(e.getSource() == restPanel.waiterText  && restPanel.waiterText.getText() != "")
        {
        	restPanel.waiterButton.setEnabled(true);
        	restPanel.waiterText.setEnabled(false);
        }
        else if(e.getSource() == restPanel.pauseButton) {
        	restPanel.pause();
        }
        else {
        	// Isn't the second for loop more beautiful?
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        	for (JButton temp2:waiterList) {
        		if(e.getSource() == temp2)
        			restPanel.showWaiterInfo(type, temp2.getText());
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
    public void addPerson(String name, boolean hunger) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name, hunger);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void addWaiter(String name) {
    	JButton button = new JButton(name);
    	button.setBackground(Color.white);
    	Dimension waiterPaneSize = waiterPane.getSize();
    	Dimension waiterButtonSize = new Dimension(waiterPaneSize.width - 20, (int) (waiterPaneSize.height/7));
    	button.setPreferredSize(waiterButtonSize);
    	button.setMinimumSize(waiterButtonSize);
    	button.setMaximumSize(waiterButtonSize);
    	button.addActionListener(this);;
    	waiterList.add(button);
    	waiterView.add(button);
    	restPanel.addPerson("Waiters", name, false);
    }
}
