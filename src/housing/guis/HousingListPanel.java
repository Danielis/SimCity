package housing.guis;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import housing.HousingCustomerAgent;

import javax.swing.*;

import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class HousingListPanel extends JPanel implements ActionListener {
    
    //CUSTOMER STUFF
    public JScrollPane personPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewForPerson = new JPanel();
    private JPanel topPart_person = new JPanel();
    private JPanel bottomPart_person = new JPanel();
    private List<JButton> listForPeople = new ArrayList<JButton>();
    private JButton addPersonButton = new JButton("Add");
    private JTextField nameFieldForPerson = new JTextField("");
    private JCheckBox personHungryCheckBox = new JCheckBox("Make Hungry");
    private HousingCustomerAgent currentPerson;

    private HousingCustomerAgent lastPersonClicked;
    

    //GENERAL STUFF
    private HousingPanel housingPanel;
    String type;
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public HousingListPanel(HousingPanel rp, String type) {
        housingPanel = rp;
        this.type = type;

        //setLayout(new GridLayout(0,1,1,1));
        topPart_person.setLayout(new BorderLayout());
        bottomPart_person.setLayout(new BorderLayout());
        
        setLayout(new BorderLayout());
        JLabel name = new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>");
        name.setAlignmentY(CENTER_ALIGNMENT);
    
    	topPart_person.add(name, BorderLayout.NORTH);
        addPersonButton.addActionListener(this);
        topPart_person.add(nameFieldForPerson, BorderLayout.CENTER);
        //customerHungryCheckBox.addActionListener(this);
        topPart_person.add(personHungryCheckBox, BorderLayout.SOUTH);
        personHungryCheckBox.setMinimumSize(new Dimension(250,100));
        viewForPerson.setLayout(new BoxLayout((Container) viewForPerson, BoxLayout.Y_AXIS));
        personPane.setViewportView(viewForPerson);
        bottomPart_person.add(addPersonButton, BorderLayout.NORTH);
        bottomPart_person.add(personPane, BorderLayout.CENTER);
        add(topPart_person, BorderLayout.NORTH);
        add(bottomPart_person, BorderLayout.CENTER);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonButton) 
        {
        	addPerson(nameFieldForPerson.getText());
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp1:listForPeople){
                if (e.getSource() == temp1)
                {
                    housingPanel.showPersonInfo(temp1.getText());
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

            Dimension paneSize = personPane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 10));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            listForPeople.add(button);
            viewForPerson.add(button);
            housingPanel.addTenant(name);
            housingPanel.showPersonInfo(name);
            validate();
        }
    }
 
    
    public void updatePersonInfoPanel(HousingCustomerAgent p) {
    	this.lastPersonClicked = p;
       	personHungryCheckBox.setVisible(true);
        currentPerson = p;
        HousingCustomerAgent person = p;
        personHungryCheckBox.setText("Hungry?");
        //personHungryCheckBox.setSelected(person.getGui().isHungry());
        //personHungryCheckBox.setEnabled(!person.getGui().isHungry());

    }
    public void updatePersonPanel()
    {
       // personHungryCheckBox.setSelected(lastPersonClicked.getGui().isHungry());
       // personHungryCheckBox.setEnabled(!lastPersonClicked.getGui().isHungry());
    }
   
   
    public void updatePerson(HousingCustomerAgent temp)
    {
    	currentPerson = temp;
        	if (personHungryCheckBox.isSelected())
        	{
        		personHungryCheckBox.setSelected(false);
        		HousingCustomerAgent p = currentPerson;
        		//p.getGui().setHungry();
        	}
    }
}
