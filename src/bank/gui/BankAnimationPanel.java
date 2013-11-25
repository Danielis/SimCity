package bank.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import bank.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class BankAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX_ANIM = 700;
    private final int WINDOWY_ANIM = 700;
        
 
    int TABLESIZE = 50;
    int ENTRANCE_X = 14;
    int ENTRANCE_Y = 82;
    int TELLERTABLE_X = 125;
    int TELLERTABLE_Y = 140;
    int ENTRANCE_DOOR_X = -40;
    int ENTRANCE_DOOR_Y = 70;
    int CARPET_X = 75;
    int CARPET_Y = 220;
    int CARPET_SIZEX = 500;
    int CARPET_SIZEY = 350;
    int WAITING_AREA_CARPET_X = 70;
    int WAITING_AREA_CARPET_Y = 40;
    int HOST_TABLE_X = 380;
    int HOST_TABLE_Y = 330;
    int HOST_X = HOST_TABLE_X + 5;
    int HOST_Y = HOST_TABLE_Y - 10;
    int COOK_CARPET_X = 340;
    int COOK_CARPET_Y = 40;
    int GRILL_X = 560;
    int GRILL_Y = 55;
    int FRIDGE_X = 350;
    int FRIDGE_Y = 30;
    
    Image BankMap;
    
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public BankAnimationPanel() {
    	setSize(WINDOWX_ANIM, WINDOWY_ANIM);
        setVisible(true);
        
        try
        {
        	BankMap = ImageIO.read(getClass().getResource("/resources/bankbg.png"));
        } catch (IOException e ) {}

        
        bufferSize = this.getSize();
        
    	Timer timer = new Timer(20, this );
    	timer.start(); //commenting out so that only city runs
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {

    	//Color backgroundColor = new Color(167, 92, 86);

		
  
        Graphics2D host = (Graphics2D)g;
        Graphics2D cashier = (Graphics2D)g;
    	
        Graphics2D g1 = (Graphics2D)g;
  
        Graphics2D Bank = (Graphics2D)g;
        
        Graphics2D entrance_door = (Graphics2D)g;
    
        //COLORS
        Color brown_1 = new Color(210,180,140);
        Color carpetColor = new Color(174, 105, 90);
        Color entranceColor = new Color(238,203,173);
        Color brown = new Color(245, 201, 114);
        Color backgroundColor = new Color(0, 0, 0);
        Color silver = new Color(205,197,191);
        Color hostColor = new Color(109, 146, 155);
        Color cashierColor = new Color(51, 102, 153);

        //BACKGROUND INITIATION
        g1.setColor(backgroundColor);
       	g1.fillRect(0, 0, WINDOWX_ANIM, WINDOWY_ANIM);
       	Bank.drawImage(BankMap, 100, 50, this);
    	
       	
        //OTHER INITIATION
      // entrance_door.setColor(entranceColor);
      //entrance_door.fillRect(ENTRANCE_DOOR_X, ENTRANCE_DOOR_Y, 1000, 1000);
        
        //Carpets
      //  entrance.setColor(carpetColor);
      //  entrance.fillRect(ENTRANCE_X, ENTRANCE_Y, TABLESIZE * 3/5, TABLESIZE * 3/2);
      //  waitingcarpet.setColor(carpetColor);
      //  waitingcarpet.fillRect(WAITING_AREA_CARPET_X, WAITING_AREA_CARPET_Y, 200, 120);
       // cookcarpet.setColor(carpetColor);
      //  cookcarpet.fillRect(COOK_CARPET_X, COOK_CARPET_Y, 240, 120);
      //  carpet.setColor(carpetColor);
       // carpet.fillRect(CARPET_X, CARPET_Y, CARPET_SIZEX, CARPET_SIZEY);
        
        //Agent Tables and other objects
       // cook_table.setColor(brown_1);
      //  cook_table.fillRect(TELLERTABLE_X, TELLERTABLE_Y, TABLESIZE*8, TABLESIZE*3/4);
      //  grill.setColor(silver);
     //   grill.fillRect(GRILL_X, GRILL_Y, TABLESIZE, TABLESIZE*5/3);
       // fridge.setColor(Color.white);
      //  fridge.fillRect(FRIDGE_X, FRIDGE_Y, TABLESIZE*3/4, TABLESIZE*3/4);
       // host_table.setColor(brown_1);
       // host_table.fillRect(HOST_TABLE_X, HOST_TABLE_Y, 30, 30);
       // host.setColor(hostColor);
       // host.fillRect(HOST_X, HOST_Y, 20, 20);
        
        
                

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
            	gui.draw(cashier);
            	gui.draw(host);
            }
        }
        
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(TellerGui gui) {
    	guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
   
}
