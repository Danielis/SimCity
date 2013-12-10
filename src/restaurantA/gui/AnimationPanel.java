package restaurantA.gui;
import javax.imageio.ImageIO;

import restaurantA.*;

import javax.swing.*;

import restaurantA.HostAgent;
import restaurantA.RestaurantA;
import restaurantA.Table;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 950;
    private final int WINDOWY = 650;
    private final int HEIGHT = 30;
    private final int WIDTH = 50;
    private Image bufferImage;
    private Dimension bufferSize;
    private RestaurantA rest;
    private List<Gui> guis = new ArrayList<Gui>();

    private HostAgent host = null;
    private ArrayList<Table> tables = null;

    Image restBG, table, cashMachine, cookTable, hostimg, cashierimg, tablec;
    
  //  RestaurantA rest;

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
    	Timer timer = new Timer(20, this );
    	timer.start();
    	
    	 try
         {
    		 restBG = ImageIO.read(getClass().getResource("/resources/restSprites/A/bg.png"));
         } catch (IOException e ) {}
    	 
    	 try
         {
    		 table = ImageIO.read(getClass().getResource("/resources/restSprites/A/table.png"));
         } catch (IOException e ) {}
    	 try
         {
    		 cashMachine = ImageIO.read(getClass().getResource("/resources/restSprites/A/cashmachine.png"));
         } catch (IOException e ) {}
		 try
	        {
	        	tablec = ImageIO.read(getClass().getResource("/resources/restSprites/A/cooktable.png"));
	        } catch (IOException e ) {}
    	 try
         {
    		 cookTable = ImageIO.read(getClass().getResource("/resources/restSprites/A/cooktable.png"));
         } catch (IOException e ) {}
    	 try
         {
    		 hostimg = ImageIO.read(getClass().getResource("/resources/globalSprites/restHost/down0.png"));
         } catch (IOException e ) {}
    	 try
         {
    		 cashierimg = ImageIO.read(getClass().getResource("/resources/globalSprites/cashier/down0.png"));
         } catch (IOException e ) {}
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        g2.drawImage(restBG, 0, 0, this);

        //Here is the table
        g2.setColor(Color.ORANGE);
        
		g.drawImage(tablec, 400 - 20, 50 - 10, this);

      
        
        for(Table t: tables){
        //	g2.fillRect(t.getxPos(), t.getyPos(), WIDTH, HEIGHT);
        	g2.drawImage(table, t.getxPos(), t.getyPos(), this);
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        
    	g2.setColor(Color.BLUE);
		//g2.fillRect(30, 0, 20, 20);
        g2.drawImage(cashMachine, 60, 0, this);
       
        if (rest.workingHost != null){
        g2.drawImage(hostimg, 20, 7, this);
         }
        
        if (rest.workingCashier != null){
        g2.drawImage(cashierimg, 80, 7, this);
         }
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui){
    	guis.add(gui);
    }
    public void setHost(HostAgent h){
    	this.host = h;
    }

	public void setTables(ArrayList<Table> tables2) {
		tables = tables2;
	}

	public void addGui(CookGui g) {
		guis.add(g);
	}

	public void setRest(RestaurantA rest2) {
		rest = rest2;
	}
}
