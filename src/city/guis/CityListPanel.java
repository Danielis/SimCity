package city.guis;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;

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
public class CityListPanel extends JPanel implements ActionListener {
    
	//Person Variables
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
    //private JCheckBox personNeedsMoneyCheckBox = new JCheckBox("Make Withdrawal");
    private PersonAgent currentPerson;

    private PersonAgent lastPersonClicked;

    
    private CityPanel cityPanel;
    String type;

    
    
    // ********** person creation panel
    
    JPanel newCreation = new JPanel();
    
    String[] jobOptions = { "No AI", "None", "Restaurant Host", "Cook", "Waiter", "Cashier", "Bank Host", "Teller", "Landlord", "Repairman", "Crook" };
    JComboBox jobList = new JComboBox(jobOptions);
   
    String[] wealthLevels = { "Average", "Wealthy", "Poor" };
    JComboBox wealthList = new JComboBox(wealthLevels);
    
  //  JCheckBox takesBusCheckBox = new JCheckBox("Takes Bus?");
    
    // ************* end person creation panel   
    
    //CONSTRUCTOR
    public CityListPanel(CityPanel rp, String type) {
        cityPanel = rp;
        this.type = type;
      
        // ***** new ppl creation panel
        newCreation.setLayout(new FlowLayout());
        newCreation.add(jobList);
        newCreation.add(wealthList);
        //newCreation.add(takesBusCheckBox);
        
        //****** end new ppl creation panel
 
        //setLayout(new GridLayout(0,1,1,1));
        topPart_person.setLayout(new BorderLayout());
        bottomPart_person.setLayout(new BorderLayout());
        
        setLayout(new BorderLayout());
        JLabel name = new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>");
        name.setAlignmentY(CENTER_ALIGNMENT);
    
    	topPart_person.add(name, BorderLayout.NORTH);
        addPersonButton.addActionListener(this);
        topPart_person.add(nameFieldForPerson, BorderLayout.CENTER);
        topPart_person.add(newCreation, BorderLayout.SOUTH);
        
        //customerHungryCheckBox.addActionListener(this);
        //topPart_person.add(personHungryCheckBox, BorderLayout.SOUTH);
       // personHungryCheckBox.setMinimumSize(new Dimension(250,100));
        viewForPerson.setLayout(new BoxLayout((Container) viewForPerson, BoxLayout.Y_AXIS));
        personPane.setViewportView(viewForPerson);
        bottomPart_person.add(addPersonButton, BorderLayout.NORTH);
        bottomPart_person.add(personPane, BorderLayout.CENTER);
        add(topPart_person, BorderLayout.NORTH);
        add(bottomPart_person, BorderLayout.CENTER);
    }

    //Action Listener
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonButton) 
        {
    		String job = jobList.getSelectedItem().toString();
    		String wealth = wealthList.getSelectedItem().toString();
        	addPerson(nameFieldForPerson.getText(), job, wealth);
        }
        else {
        	for (JButton temp1:listForPeople){
                if (e.getSource() == temp1)
                {
                    cityPanel.showPersonInfo(temp1.getText());
                }
            }
        }
    }

    //Add Person to the List
    public void addPerson(String name, String job, String wealth) {
        if (name != null) {
        	System.out.println("AddPerson in CityPanelList reached and person added.");
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
            cityPanel.addPerson(name, job, wealth);
            cityPanel.showPersonInfo(name);
            validate();
        }
    }
    
    public void updatePersonInfoPanel(PersonAgent p) {
    	this.lastPersonClicked = p;
       	personHungryCheckBox.setVisible(true);
        currentPerson = p;
        PersonAgent person = p;
        personHungryCheckBox.setText("Hungry?");
        personHungryCheckBox.setSelected(person.getGui().isHungry());
        personHungryCheckBox.setEnabled(!person.getGui().isHungry());

    }
    public void updatePersonPanel()
    {
        personHungryCheckBox.setSelected(lastPersonClicked.getGui().isHungry());
        personHungryCheckBox.setEnabled(!lastPersonClicked.getGui().isHungry());
      }
   
   
    public void updatePerson(PersonAgent person)
    {
    	currentPerson = person;
        	if (personHungryCheckBox.isSelected())
        	{
        		personHungryCheckBox.setSelected(false);
        		PersonAgent p = currentPerson;
        		p.getGui().setHungry();
        	}
        	
    }
}
