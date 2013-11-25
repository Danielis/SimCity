package city.guis;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import city.PersonAgent;

public class PersonFrame extends JFrame{
	
	PersonAgent agent;
	JPanel imgPanel;
	JPanel infoPanel;
	JLabel nameLabel;
	JLabel moneyLabel;
	JLabel inventoryLabel;
	JLabel imgLabel;
	ImageIcon imgTrainer;
	 

	PersonFrame(PersonAgent p)
	{
		imgTrainer = new ImageIcon(getClass().getResource("/resources/trainer.png"));
        
		imgPanel = new JPanel();
		imgPanel.setLayout(new BorderLayout());
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		
		nameLabel = new JLabel("Name: " + p.getName());
		moneyLabel = new JLabel("Money: " + p.money+"");
		inventoryLabel = new JLabel("Inventory: " + "Test String");
		imgLabel = new JLabel(imgTrainer);
		
		this.setLayout(new BorderLayout());
		
		imgPanel.add(imgLabel);
		infoPanel.add(nameLabel, BorderLayout.NORTH);
		infoPanel.add(moneyLabel, BorderLayout.CENTER);
		infoPanel.add(inventoryLabel, BorderLayout.SOUTH);
		
		this.add(imgLabel, BorderLayout.NORTH);
		this.add(infoPanel, BorderLayout.CENTER);
		
		this.setBounds(860, 550, 100, 100);
        setTitle("Person Agent");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
