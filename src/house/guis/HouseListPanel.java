package house.guis;

import house.HouseOwnerRole;
import house.interfaces.HouseOwner;
import house.interfaces.HouseWorker;

import javax.swing.*;

import apartment.ApartmentCustomerRole;
import apartment.ApartmentWorkerRole;
import apartment.interfaces.ApartmentCustomer;
import apartment.interfaces.ApartmentWorker;
import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class HouseListPanel extends JPanel implements ActionListener {

	//CUSTOMER STUFF
	public JScrollPane personPane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel viewForTenant = new JPanel();
	private JPanel viewForWorker = new JPanel();
	private JPanel topPart_person = new JPanel();
	private JPanel bottomPart_person = new JPanel();
	private List<JButton> listForTenants = new ArrayList<JButton>();
	private List<JButton> listForWorkers = new ArrayList<JButton>();
	private JButton addPersonButton = new JButton("Add");
	private JTextField nameFieldForPerson = new JTextField("");
	private HouseOwner currentTenant;
	private HouseOwnerRole lastPersonClicked;
	private HouseWorker currentWorker;

	//GENERAL STUFF
	private HousePanel housingPanel;
	String type;

	//--------------------Constructor-------------------------
	public HouseListPanel(HousePanel rp, String type) {
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
		//topPart_person.add(personHungryCheckBox, BorderLayout.SOUTH);
		viewForTenant.setLayout(new BoxLayout((Container) viewForTenant, BoxLayout.Y_AXIS));
		personPane.setViewportView(viewForTenant);
		bottomPart_person.add(addPersonButton, BorderLayout.NORTH);
		bottomPart_person.add(personPane, BorderLayout.CENTER);
		add(topPart_person, BorderLayout.NORTH);
		add(bottomPart_person, BorderLayout.CENTER);
	}

	//--------------------------------------------------
	//----------------Action Performed------------------
	//--------------------------------------------------
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPersonButton)  {
			if(type.equals("Tenants")) {
				/*
				System.out.println("The type is " + this.type);
				addTenant(nameFieldForPerson.getText());*/
			}
			else{
				//System.out.println("The type is " + this.type);
				//addWorker(nameFieldForPerson.getText());
			}
		}
		else {
			for (JButton temp1: listForTenants){
				if (e.getSource() == temp1)
				{
					housingPanel.showTenantInfo(temp1.getText());
				}
			}
			for (JButton temp2: listForWorkers){
				if (e.getSource() == temp2)
				{
					housingPanel.showWorkerInfo(temp2.getText());
				}
			}
		}
	}

	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 * @param homePurpose 
	 *
	 * @param name name of new person
	 */

	
	public void addTenant(HouseOwner c, String homePurpose) {
			JButton button = new JButton(c.getName());
			button.setBackground(Color.white);
			Dimension paneSize = personPane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height / 10));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			listForTenants.add(button);
			viewForTenant.add(button);
			housingPanel.addTenant(c, (listForTenants.size()-1), homePurpose);
			housingPanel.showTenantInfo(c.getName());
			validate();
	}
	
	public void addWorker(HouseWorker w) {
			JButton button = new JButton(w.getName());
			button.setBackground(Color.white);
			Dimension paneSize = personPane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height / 10));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			listForWorkers.add(button);
			viewForWorker.add(button);
			housingPanel.addWorker(w);
			housingPanel.showWorkerInfo(w.getName());
			validate();
	}


	public void updatePersonPanel()
	{
		// personHungryCheckBox.setSelected(lastPersonClicked.getGui().isHungry());
		// personHungryCheckBox.setEnabled(!lastPersonClicked.getGui().isHungry());
	}


	//this is where I got to.  When a button is pressed, this method is invoked.  
	//need two checkboxes.  one for broken stuff, one for eating
	public void updateTenant(HouseOwner temp)
	{
		currentTenant = temp;
	}
	public void updateWorker(HouseWorker temp)
	{
		currentWorker = temp;
	}
}
