package city.guis;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.TimeManager;
import restaurant.HostAgent;
import restaurant.gui.Gui;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;

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

public class CityAnimationPanel extends JPanel implements ActionListener 
{
    private final int WINDOWX_ANIM = 934;
    private final int WINDOWY_ANIM = 645;
 
    
    int RESTAURANT_X = 50;
    int RESTAURANT_Y = 100;
    int RESTAURANT_SIZE = 50;
    
    Image CityMap;
    
	Coordinate c_position1;
	Coordinate c_position2;
	Coordinate c_position3;
	
	BufferedImage cloud1;
	BufferedImage cloud2;
	BufferedImage cloud3;
	
	int ticker = 0;
	
    public class Coordinate
    {
    	int x;
    	int y;
    	
    	Coordinate()
    	{
    		x = 0;
    		y = 0;
    	}
    	Coordinate(int a, int b)
    	{
    		x = a;
    		y = b;
    	}
    }

    private List<Gui> guis = new ArrayList<Gui>();

    public CityAnimationPanel() {
            setSize(WINDOWX_ANIM, WINDOWY_ANIM);
            setVisible(true);
            
            try
            {
            	CityMap = ImageIO.read(getClass().getResource("/resources/CityMap3.png"));
            } catch (IOException e ) {}
            
            try
            {
            	cloud1 = ImageIO.read(getClass().getResource("/resources/cloud.png"));
            } catch (IOException e ) {}
            try
            {
            	cloud2 = ImageIO.read(getClass().getResource("/resources/cloud.png"));
            } catch (IOException e ) {}
            try
            {
            	cloud3 = ImageIO.read(getClass().getResource("/resources/cloud.png"));
            } catch (IOException e ) {}
            
    		c_position1 = new Coordinate(50,50);
    		c_position2 = new Coordinate(200,250);
    		c_position3 = new Coordinate(450,450);
            
            Timer timer = new Timer(20, this );
            timer.start();
         
    }

    public void actionPerformed(ActionEvent e) {
            repaint();  //Will have paintComponent called
    }

    public void paintComponent(Graphics g) {

    		//if (ticker % 5 == 0)
    		updateClouds();

    		//ticker++;
    		Graphics2D images = (Graphics2D)g;
    	
    		Graphics2D City = (Graphics2D)g;
    		
    		Graphics2D random = (Graphics2D)g;
    		
            //Graphics2D g1 = (Graphics2D)g;

            //COLORS                
            //Color brown = new Color(245, 201, 114);
            Color backgroundColor = new Color(167, 92, 86);

    		City.drawImage(CityMap, 0, 0, this);
	
			Graphics2D c1 = (Graphics2D)g;
			Graphics2D c2 = (Graphics2D)g;
			Graphics2D c3 = (Graphics2D)g;
			


			synchronized(guis){
            for(Gui gui : guis) {
                    if (gui.isPresent()) {
                    	gui.updatePosition();
                    }
            }
			}
			
			synchronized(guis){
            for(Gui gui : guis) {
                    if (gui.isPresent()) {
                    	gui.draw(images);
                    }
            }
			}
			
			c1.drawImage(cloud1, c_position1.x, c_position1.y, this);
			c2.drawImage(cloud2, c_position2.x, c_position2.y, this);
			c3.drawImage(cloud3, c_position3.x, c_position3.y, this);
			
            g.setColor(Color.BLACK);
            g.drawString(TimeManager.getInstance().TimeStr(), 520, 615);
            
            
    }
    
    public void updateClouds()
    {
    	c_position1.x -= 1;
    	c_position2.x -= 3;
    	c_position3.x -= 2;
		if (c_position1.x < -300)
		{
			c_position1.x = 800;
		}
		
		if (c_position2.x < -300)
		{
			c_position2.x = 800;
		}
		
		if (c_position3.x < -300)
		{
			c_position3.x = 800;
		}
    }
    
    public void addGui(PersonGui gui) {
    	guis.add(gui);
    }

    public void addGui(BusGui gui) {
    	guis.add(gui);
    }
    public void addGui(BusStopGui gui) {
    	guis.add(gui);
    }

}
