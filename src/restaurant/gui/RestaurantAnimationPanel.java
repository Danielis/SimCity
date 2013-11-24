package restaurant.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import restaurant.HostAgent;

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

public class RestaurantAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX_ANIM = 700;
    private final int WINDOWY_ANIM = 700;
        
    int TABLES_Y = 450;
    int TABLESIZE = 50;
    int ENTRANCE_X = 14;
    int ENTRANCE_Y = 82;
    int COOKTABLE_X = 386;
    int COOKTABLE_Y = 140;
    int ENTRANCE_DOOR_X = -40;
    int ENTRANCE_DOOR_Y = 70;
    int CASHIERTABLE_X = 230;
    int CASHIERTABLE_Y = 30;
    int CARPET_X = 75;
    int CARPET_Y = 220;
    int CARPET_SIZEX = 500;
    int CARPET_SIZEY = 350;
    int WAITING_AREA_CARPET_X = 70;
    int WAITING_AREA_CARPET_Y = 40;
    int HOST_TABLE_X = 80;
    int HOST_TABLE_Y = 30;
    int COOK_CARPET_X = 340;
    int COOK_CARPET_Y = 40;
    int GRILL_X = 560;
    int GRILL_Y = 55;
    int FRIDGE_X = 350;
    int FRIDGE_Y = 30;
    int CASHIER_X = 230+25;
    int CASHIER_Y = 30-10;
    int HOST_X = 80+5;
    int HOST_Y = 30-10;
    
    private BufferedImage imgRestaurant;
    
    //private Image bufferImage;
    //private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    private Vector<Graphics2D> tables = new Vector<Graphics2D>();

    public RestaurantAnimationPanel() {
    	setSize(WINDOWX_ANIM, WINDOWY_ANIM);
        setVisible(true);
        
        try
	       {
			 imgRestaurant = ImageIO.read(getClass().getResource("/resources/restaurant.png"));
	       } catch (IOException e ) {}
        
        
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {

        Graphics2D carpet = (Graphics2D)g;
        Graphics2D entrance = (Graphics2D)g;
        Graphics2D waitingcarpet = (Graphics2D)g;
        Graphics2D cookcarpet = (Graphics2D)g;
        Graphics2D grill = (Graphics2D)g;
        Graphics2D fridge = (Graphics2D)g;
        Graphics2D host = (Graphics2D)g;
        Graphics2D cashier = (Graphics2D)g;
    	
        Graphics2D g1 = (Graphics2D)g;
        Graphics2D g2 = (Graphics2D)g;
        Graphics2D g3 = (Graphics2D)g;
        Graphics2D g4 = (Graphics2D)g;
        
        Graphics2D entrance_door = (Graphics2D)g;
        Graphics2D cook_table = (Graphics2D)g;
        Graphics2D cashier_table = (Graphics2D)g;
        Graphics2D host_table = (Graphics2D)g;
        
        //COLORS
        Color brown_1 = new Color(210,180,140);
        Color carpetColor = new Color(174, 105, 90);
        Color entranceColor = new Color(238,203,173);
        Color brown = new Color(245, 201, 114);
        Color backgroundColor = new Color(167, 92, 86);
        Color silver = new Color(205,197,191);
        Color hostColor = new Color(109, 146, 155);
        Color cashierColor = new Color(51, 102, 153);

        //BACKGROUND INITIATION
        g1.setColor(backgroundColor);
       	g1.fillRect(0, 0, WINDOWX_ANIM, WINDOWY_ANIM);
       	
        //OTHER INITIATION
        entrance_door.setColor(entranceColor);
        entrance_door.fillRect(ENTRANCE_DOOR_X, ENTRANCE_DOOR_Y, TABLESIZE, TABLESIZE*2);
        
        //Carpets
        entrance.setColor(carpetColor);
        entrance.fillRect(ENTRANCE_X, ENTRANCE_Y, TABLESIZE * 3/5, TABLESIZE * 3/2);
        waitingcarpet.setColor(carpetColor);
        waitingcarpet.fillRect(WAITING_AREA_CARPET_X, WAITING_AREA_CARPET_Y, 200, 120);
        cookcarpet.setColor(carpetColor);
        cookcarpet.fillRect(COOK_CARPET_X, COOK_CARPET_Y, 240, 120);
        carpet.setColor(carpetColor);
        carpet.fillRect(CARPET_X, CARPET_Y, CARPET_SIZEX, CARPET_SIZEY);
        
        //Agent Tables and other objects
        cook_table.setColor(brown_1);
        cook_table.fillRect(COOKTABLE_X, COOKTABLE_Y, TABLESIZE*3, TABLESIZE*3/4);
        cashier_table.setColor(brown_1);
        cashier_table.fillRect(CASHIERTABLE_X, CASHIERTABLE_Y, 70, 50);
        grill.setColor(silver);
        grill.fillRect(GRILL_X, GRILL_Y, TABLESIZE, TABLESIZE*5/3);
        fridge.setColor(Color.white);
        fridge.fillRect(FRIDGE_X, FRIDGE_Y, TABLESIZE*3/4, TABLESIZE*3/4);
        host_table.setColor(brown_1);
        host_table.fillRect(HOST_TABLE_X, HOST_TABLE_Y, 30, 30);
        host.setColor(hostColor);
        host.fillRect(HOST_X, HOST_Y, 20, 20);
        cashier.setColor(cashierColor);
        cashier.fillRect(CASHIER_X,  CASHIER_Y,  20, 20);
        
        
                
        //TABLE INITIATION FOR CUSTOMERS
        //TABLE X's are 150 + 100 * i;
        g1.setColor(brown);
        g1.fillRect(150, TABLES_Y, TABLESIZE, TABLESIZE);
        g2.setColor(brown);
        g2.fillRect(250, TABLES_Y, TABLESIZE, TABLESIZE);
        g3.setColor(brown);
        g3.fillRect(350, TABLES_Y, TABLESIZE, TABLESIZE);
        g4.setColor(brown);
        g4.fillRect(450, TABLES_Y, TABLESIZE, TABLESIZE);
        
    	Graphics2D Res = (Graphics2D)g;
    	Res.drawImage(imgRestaurant, 0, 0, this);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
            
            	gui.draw(carpet);
            	gui.draw(cookcarpet);
            	gui.draw(waitingcarpet);
            	gui.draw(grill);
            	gui.draw(fridge);
            	gui.draw(g1);
            	gui.draw(g2);
            	gui.draw(g3);
            	gui.draw(g4);
            	gui.draw(entrance_door);
            	gui.draw(cook_table);
            	gui.draw(cashier_table);
            	gui.draw(host_table);
            	gui.draw(entrance);
            	gui.draw(cashier);
            	gui.draw(host);
            }
        }
        
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
    	guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui){
    	guis.add(gui);
    }
}
