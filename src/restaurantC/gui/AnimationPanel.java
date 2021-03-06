package restaurantC.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import restaurantC.RestaurantC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 400;
    private final int WINDOWY = 400;
    private static final int TABLESIDE = 50;
    private static final int TABLEX = 150;
    private static final int TABLEY = 175;
    private static final int TABLEX2 = 225;
    private static final int TABLEY2 = 175;
    private static final int TABLEX3 = 300;
    private static final int TABLEY3 = 175;
    private static final int COUNTERSIDE1 = 75;
    private static final int COUNTERSIDE2 = 35;
    private static final int COUNTERSIDE3 = 110;
    private static final int COUNTERX = 350;
    private static final int COUNTERY = 10;
    private static final int COUNTERY2 = 85;
    
    private Image blue_floor;
    private Image table;
    private Image person;
   
    private class myOrder{
    	public String choice;
    	WaiterGui gui;
    	myOrder(String c, WaiterGui wg) {
    		gui = wg;
    		choice = c;
    	}
    }
    
    private class cookingOrder {
    	public String choice;
    	public boolean cooked;
    	cookingOrder(String c) {
    		choice = c;
    		cooked = false;
    	}
    }
    
    private class tableOrder {
    	public String choice;
    	int table;
    	tableOrder(String c, int t) {
    		choice = c;
    		table = t;
    	}
    }
    private Vector<myOrder> orders = new Vector<myOrder>();
    private Vector<tableOrder> tableIcons = new Vector<tableOrder>();
    //vector of strings to put strings in kitchen
    private Vector<cookingOrder> cookIcons = new Vector<cookingOrder>();
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
	public RestaurantC rest;
	public boolean hasCashier = false;

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setBorder(BorderFactory.createTitledBorder("Animation"));
        setVisible(true);

        bufferSize = this.getSize();
        
        //initialize table icons
        tableIcons.add(new tableOrder("", 1));
        tableIcons.add(new tableOrder("", 2));
        tableIcons.add(new tableOrder("", 3));
 
        
        //images
		try
        {
            blue_floor = ImageIO.read(getClass().getResource("/resources/blue_floor.png"));
        } catch (IOException e ) {}
		
		try
        {
            table = ImageIO.read(getClass().getResource("/resources/desk.png"));
        } catch (IOException e ) {}
		
		try
        {
            person = ImageIO.read(getClass().getResource("/resources/trainer2.png"));
        } catch (IOException e ) {}
		
		
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        g2.drawImage(blue_floor, 0, 0, this);

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.drawImage(table, TABLEX, TABLEY, this);
        //Here is the second table, same color
        g2.drawImage(table, TABLEX2, TABLEY2, this);
        //here is the third table, same color
        g2.drawImage(table, TABLEX3, TABLEY3, this);

        g2.drawImage(table,  480, 480, this);
        
        if(hasCashier) {
        	g2.drawImage(person, 500, 500, this);
        }
 
        
        //here is the counter 
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(COUNTERX, COUNTERY, COUNTERSIDE2, COUNTERSIDE1);
        g2.fillRect(COUNTERX, COUNTERY2, COUNTERSIDE3, COUNTERSIDE2);
       
        //draw the appropriate waiter orders
        g2.setColor(Color.BLACK);
        for(myOrder order:orders) {
        	g2.drawString(order.choice, order.gui.getXPos(), order.gui.getYPos());
        }
        
        //draw the appropriate orders in the kitchen
        g2.setColor(Color.BLACK);
        int i = 0;
        int j = 0;
        for(cookingOrder order:cookIcons) {
        	if(order.cooked) {
        		g2.drawString(order.choice, 350, 50 + 10 * i);
        		++i;
        	}
        	else {
        		g2.drawString(order.choice, 385, 95 + 10 * j);
        		++j;
        	}
        }
        
        
        //draw stuff for table icons
        for(tableOrder t:tableIcons) {
        	if(t.table == 1) {
        		g2.drawString(t.choice, TABLEX, TABLEY + 30);
        	}
        	else if(t.table == 2) {
        		g2.drawString(t.choice, TABLEX2, TABLEY2 + 30);
        	}
        	if(t.table == 3) {
        		g2.drawString(t.choice, TABLEX3, TABLEY3 + 30);
        	}
        }
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }


	private boolean hasCashier() {
		// TODO Auto-generated method stub
		return false;
	}

	public void addGui(CustomerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
    	guis.add(gui);
    }
    
    //string displays for waiter
    public void showOrder(String s, WaiterGui wg) {
    	orders.add(new myOrder(s + "?", wg));
    }
    public void finalizeOrder(String s, WaiterGui wg) {
    	orders.add(new myOrder(s, wg));
    	removeOrder(s);
    } 
    public void removeOrder(WaiterGui wg) {
    	for(myOrder m:orders){
    		if(m.gui == wg) {
    			orders.remove(m);
    			return;
    		}
    	}
    }
    
    //string displays for cooking area
    public void removeOrder(String s) {
    	for(cookingOrder c:cookIcons) {
    		if(c.choice.equals(s)) {
    			cookIcons.remove(c);
    			return;
    		}
    	}
    }
    public void showOrder(String s) {
    	cookIcons.add(new cookingOrder(s + "?"));
    }
    public void finalizeOrder(String s) {
    	for(cookingOrder c:cookIcons) {
    		if(c.choice.equals(s + "?")) {
    			c.choice = s;
    			c.cooked = true;
    		}
    	}
    }
 
    //table icons
    public void finalizeOrder(String s, int table) {
    	tableIcons.get(table - 1).choice = s;
    }
    
    public void removeOrder(int table) {
    	tableIcons.get(table - 1).choice = "";
    }

	public void setRest(RestaurantC rest) {
		this.rest = rest;
	}
}
