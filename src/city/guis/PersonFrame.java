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
import city.PersonAgent.JobType;

public class PersonFrame extends JFrame{
	
	PersonAgent agent;
	JPanel imgPanel;
	JPanel infoPanel;
	JLabel nameLabel;
	JLabel moneyLabel;
	JLabel inventoryLabel;
	JLabel imgLabel;
	JLabel locationLabel;
	ImageIcon imgTrainer;
	 

	PersonFrame(PersonAgent p)
	{
		if (p.job != null)
		{
			if (p.job.type.equals(JobType.bankHost)){
	      		imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/bankHost2.png"));
	       	}
	    	else if (p.job.type.equals(JobType.teller)){
	    		imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/teller2.png"));
	     	}
	    	else if (p.job.type.equals(JobType.restHost)){
	    		imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/restHost2.png")); 
	      	}
	    	else if (p.job.type.equals(JobType.waiter)){
	   			imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/waiter2.png")); 
	     	}
	    	else if (p.job.type.equals(JobType.cashier)){
	     		imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/cashier2.png")); 
	       	}
	    	else if (p.job.type.equals(JobType.cook)){
	   			imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/cook2.png")); 
	     	}
	    	else if (p.job.type.equals(JobType.crook)){
	     			 imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/crook2.png"));
	       	}
	    	else if (p.job.type.equals(JobType.student)){
    			 imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/student3.png"));
	    	}
	    	else if (p.job.type.equals(JobType.professor)){
    			 imgTrainer = new ImageIcon(getClass().getResource("/resources/globalSprites/professor3.png"));
	    	}
	       	else
	       	{
	    		imgTrainer = new ImageIcon(getClass().getResource("/resources/trainer_2.png"));
	    	}  
	    }
        
		imgPanel = new JPanel();
		imgPanel.setLayout(new BorderLayout());
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		
		//nameLabel = new JLabel("Name: " + p.getName());
		moneyLabel = new JLabel("Money: " + p.cash+"");
		inventoryLabel = new JLabel("Inventory: " + "Test String");
		imgLabel = new JLabel(imgTrainer);
		locationLabel = new JLabel("Going: " + p.Status.getDestination());
		
		this.setLayout(new BorderLayout());
		
		//imgPanel.add(imgLabel, BorderLayout.NORTH);
		infoPanel.add(imgLabel, BorderLayout.NORTH);
		infoPanel.add(locationLabel, BorderLayout.CENTER);
		infoPanel.add(moneyLabel, BorderLayout.SOUTH);
		//infoPanel.add(inventoryLabel, BorderLayout.SOUTH);
		
		this.add(imgLabel, BorderLayout.NORTH);
		this.add(infoPanel, BorderLayout.CENTER);
		
		this.setBounds(860, 550, 250, 150);
        setTitle(p.getName());
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
	}
}
