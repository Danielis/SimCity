package restaurantA.gui;
import javax.swing.*;

import restaurantA.HostAgent;
import restaurantA.Table;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 950;
    private final int WINDOWY = 650;
    private final int HEIGHT = 30;
    private final int WIDTH = 50;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private List<Gui> guis = new ArrayList<Gui>();

    private HostAgent host = null;
    private ArrayList<Table> tables = null;

    

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
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

        //Here is the table
        g2.setColor(Color.ORANGE);

      
        
        for(Table t: tables){
        	g2.fillRect(t.getxPos(), t.getyPos(), WIDTH, HEIGHT);
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        
    	g2.setColor(Color.BLUE);
		g2.fillRect(30, 0, 20, 20);

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
}
