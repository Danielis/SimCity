package restaurantA.gui;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import restaurantA.HostAgent;
import restaurantA.interfaces.Customer;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent agent = null;

    private int numWait;
    public static final int xTable = 200;
    public static final int yTable = 250;

    int currentX, currentY;
    
    public HostGui(HostAgent agent) {
        this.agent = agent;
        this.numWait = agent.waitingCustomers.size();
    }

    public void updatePosition() {
      
    }

    public void draw(Graphics2D g) {
        
       //	g.setColor(Color.ORANGE);
       // g.fillRect(30, 30, 20, 20);
        
//        if (numWait > 0){
//           	g.setColor(Color.BLACK);
//            g.drawString("(" + numWait + ")", 3, 35);
//            }
  
       
     
    }

    public boolean isPresent() {
        return true;
    }
    
    public void updateNumWait(){
    	numWait = agent.waitingCustomers.size();
    }
}
