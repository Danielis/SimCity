package city.guis;



import restaurant.CustomerAgent;
//import bank.gui.BankGui;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;
import roles.Restaurant;
import housing.guis.HousingGui;

import javax.imageio.ImageIO;
import javax.swing.*;

import bank.gui.BankGui;
import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class CityGui extends JFrame implements ActionListener {
	
	
	//Variables
	
	
	//Java Structure
	public JFrame animationFrame = new JFrame("Restaurant Animation");
	
	//The City Animation
	public CityAnimationPanel cityAnimationPanel = new CityAnimationPanel();
	
	//City Panels with buttons and etc
    public CityPanel cityPanel = new CityPanel(this);
	
	//Contains a lot of information for the city
	public JPanel cityInformationAndButtons = new JPanel();
    
    //City JPanels for information
    private JPanel personInformationPanel;
    private JPanel InformationPanel;
    private JPanel buttonPanel;
    private JLabel infoCustomerLabel;

    //Useful Checkboxes
    private JCheckBox personHungryCheckBox;

    private PersonAgent currentPerson;

    private JButton pauseButton;
    private JButton refreshButton;
    
    
    
    Boolean isPaused = false;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CityGui() {
        int WINDOWX = 500;
        int WINDOWY = 500;

        
        cityInformationAndButtons.setLayout(new BorderLayout());
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBounds(25, 25, WINDOWX+700, WINDOWY+150);
    	setVisible(true);

        setLayout(new BorderLayout());
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .86));
        cityPanel.setPreferredSize(restDim);
        cityPanel.setMinimumSize(restDim);
        cityPanel.setMaximumSize(restDim);
        
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(this);
        refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(this);
        
        
        
        
        //PERSON INFORMATION STRUCTURE
        Dimension infoDimCustomer = new Dimension(WINDOWX, (int) (WINDOWY * .30));
        personInformationPanel = new JPanel();
        personInformationPanel.setPreferredSize(infoDimCustomer);
        personInformationPanel.setMinimumSize(infoDimCustomer);
        personInformationPanel.setMaximumSize(infoDimCustomer);
        personInformationPanel.setBorder(BorderFactory.createTitledBorder("Customers"));
        personHungryCheckBox = new JCheckBox();
        personHungryCheckBox.setVisible(false);
        personHungryCheckBox.addActionListener(this);
        personInformationPanel.setLayout(new BorderLayout());
        infoCustomerLabel = new JLabel(); 
        infoCustomerLabel.setText("<html><p><p>Click Add to make people</p></p></html>");
        personInformationPanel.add(infoCustomerLabel, BorderLayout.NORTH);
        personInformationPanel.add(personHungryCheckBox, BorderLayout.SOUTH);
        cityInformationAndButtons.add(cityPanel, BorderLayout.NORTH);
        InformationPanel.add(personInformationPanel, BorderLayout.NORTH);
        cityInformationAndButtons.add(InformationPanel, BorderLayout.CENTER);
        buttonPanel.add(pauseButton, BorderLayout.CENTER);
        buttonPanel.add(refreshButton, BorderLayout.EAST);
        cityInformationAndButtons.add(buttonPanel, BorderLayout.SOUTH);
        add(cityAnimationPanel, BorderLayout.CENTER);
        add(cityInformationAndButtons, BorderLayout.EAST);
        
        cityAnimationPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            int x=e.getX();
            int y=e.getY();
            System.out.println(x+","+y);//these co-ords are relative to the component
          
            
            if ((x<159) && (y<85) && (x>0) && (y>0)){
                  BankGui gui3 = new BankGui();
                   gui3.setTitle("Aleena's Bank");
                   gui3.setVisible(true);
                   gui3.setResizable(false);
                   gui3.setDefaultCloseOperation(HIDE_ON_CLOSE);   
            }
            
            if ((x<314) && (y<468) && (x>210) && (y>370)){
             RestaurantGui gui2 = new RestaurantGui();
              gui2.setTitle("Norman's Restaurant");
              gui2.setVisible(true);
              gui2.setResizable(false);
              gui2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
            }
            
            if ((x<603) && (y<261) && (x>530) && (y>202)){
            	HousingGui gui4 = new HousingGui();
        		gui4.setTitle("Housing View");
        		gui4.setVisible(true);
        		gui4.setResizable(false);
        		gui4.setDefaultCloseOperation(HIDE_ON_CLOSE);  
               }
            
            }

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
        });
    }
    /**
     * updatepersonInformationPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     */
    public void updatePersonInformationPanel(PersonAgent temp) {
        personHungryCheckBox.setVisible(true);
        currentPerson = temp;
        PersonAgent person = temp;
        personHungryCheckBox.setText("Hungry?");
        personHungryCheckBox.setSelected(person.getGui().isHungry());
        personHungryCheckBox.setEnabled(!person.getGui().isHungry());
        infoCustomerLabel.setText(
           "<html><pre>     Name: " + person.getName() + " </pre></html>");
        personInformationPanel.validate();
    }
    public void updateLastCustomer()
    {
    	if (currentPerson != null)
    	{
	        personHungryCheckBox.setSelected(currentPerson.getGui().isHungry());
	        personHungryCheckBox.setEnabled(!currentPerson.getGui().isHungry());
	        personInformationPanel.validate();
    	}
    }
    
    //ACTION LISTENER
    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == personHungryCheckBox) 
        {
            PersonAgent c = (PersonAgent) currentPerson;
            c.getGui().setHungry();
            personHungryCheckBox.setEnabled(false);
        }
        if (e.getSource() == pauseButton)
        {
        	if (isPaused)
        	{
        		pauseButton.setText("PAUSE");
        		cityPanel.resume();
        		isPaused = false;
        	}
        	else if(isPaused == false)
        	{
        		pauseButton.setText("RESUME");
        		cityPanel.pause();
        		isPaused = true;
        	}
        }
        if (e.getSource() == refreshButton)
        {
        	cityPanel.refresh();
        }
    }
    
    public void createRestaurant(String name, int index)
    {
    	Restaurant r = new Restaurant(new RestaurantGui(), "Norman's Restaurant");
    }
    
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(PersonAgent p) {
        PersonAgent per = currentPerson;
        if (p.equals(per)) 
        {
            personHungryCheckBox.setEnabled(true);
            personHungryCheckBox.setSelected(false);
        }
}
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) 
    {    
        CityGui gui = new CityGui();
        //gui.cityPanel.setRestPanel(gui2.restPanel);
        gui.setTitle("Team 05's City");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

}
