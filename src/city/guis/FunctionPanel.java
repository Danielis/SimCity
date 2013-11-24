package city.guis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FunctionPanel extends JPanel implements ActionListener {

	private JPanel personPanel = new JPanel();
    private JLabel personLabel = new JLabel(); 
    
    private JPanel bankPanel = new JPanel();
    private JLabel bankLabel = new JLabel(); 
    
    private JPanel marketPanel = new JPanel();
    private JLabel marketLabel = new JLabel(); 
    
    private JPanel housingPanel = new JPanel();
    private JLabel housingLabel = new JLabel(); 
    
    private JPanel restaurantPanel = new JPanel();
    private JLabel restaurantLabel = new JLabel(); 
	
    private JButton pauseButton;
    
    private CityGui gui;
	
	FunctionPanel(CityGui gui){
		this.gui = gui;
		System.out.println("const called");
		
		personLabel.setText("<html>person</html>");
		bankLabel.setText("bank");
		marketLabel.setText("market");
		housingLabel.setText("housing");
		restaurantLabel.setText("rest");
		personPanel.add(personLabel);
		bankPanel.add(bankLabel);
		marketPanel.add(marketLabel);
		housingPanel.add(housingLabel);
		restaurantPanel.add(restaurantLabel);
	    personLabel.setLayout(new FlowLayout());
		setLayout(new FlowLayout());
		
		pauseButton = new JButton("PAUSE");
		add(pauseButton);
		add(personPanel);
		add(bankPanel);
		add(marketPanel);
		add(housingPanel);
		add(restaurantPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
