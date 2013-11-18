package city.guis;

import javax.imageio.ImageIO;
import javax.swing.*;

import restaurant.HostAgent;
import restaurant.gui.Gui;

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

public class CityAnimationPanel extends JPanel implements ActionListener {

        private final int WINDOWX_ANIM = 934;
        private final int WINDOWY_ANIM = 645;

        int RESTAURANT_X = 50;
        int RESTAURANT_Y = 100;
        int RESTAURANT_SIZE = 50;
        
        Image CityMap;

        private List<Gui> guis = new ArrayList<Gui>();

        public CityAnimationPanel() {
                setSize(WINDOWX_ANIM, WINDOWY_ANIM);
                setVisible(true);
                
                try
                {
                	CityMap = ImageIO.read(getClass().getResource("/resources/CityMap.png"));
                } catch (IOException e ) {}

                Timer timer = new Timer(20, this );
                timer.start();
        }

        public void actionPerformed(ActionEvent e) {
                repaint();  //Will have paintComponent called
        }

        public void paintComponent(Graphics g) {

        		Graphics2D City = (Graphics2D)g;
        		
                Graphics2D g1 = (Graphics2D)g;

                //COLORS                
                Color brown = new Color(245, 201, 114);
                //Color backgroundColor = new Color(167, 92, 86);

                //BACKGROUND INITIATION
                //g1.setColor(backgroundColor);
                g1.fillRect(0, 0, WINDOWX_ANIM, WINDOWY_ANIM);
                
        		City.drawImage(CityMap, 0, 0, this);

                for(Gui gui : guis) {
                        if (gui.isPresent()) {
                                gui.updatePosition();
                        }
                }

                for(Gui gui : guis) {
                        if (gui.isPresent()) {
                                //gui.draw(restaurant);
                        }
                }

        }

        public void addGui(PersonGui gui) {
                guis.add(gui);
        }
}
