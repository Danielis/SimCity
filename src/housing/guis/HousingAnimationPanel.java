package housing.guis;

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

public class HousingAnimationPanel extends JPanel implements ActionListener {

	private final int WINDOWX_ANIM = 700;
	private final int WINDOWY_ANIM = 700;

	int HOUSE_X = 50;
	int HOUSE_Y = 100;
	int HOUSE_SIZE = 50;

	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();

	public HousingAnimationPanel() {
		setSize(WINDOWX_ANIM, WINDOWY_ANIM);
		setVisible(true);

		bufferSize = this.getSize();

		Timer timer = new Timer(20, this );
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {


		Graphics2D house = (Graphics2D)g;

		Graphics2D g1 = (Graphics2D)g;
		Graphics2D g2 = (Graphics2D)g;
		Graphics2D g3 = (Graphics2D)g;
		Graphics2D g4 = (Graphics2D)g;

		//COLORS		
		Color brown = new Color(245, 201, 114);
		Color backgroundColor = new Color(167, 92, 86);


		//BACKGROUND INITIATION
		g1.setColor(backgroundColor);
		g1.fillRect(0, 0, WINDOWX_ANIM, WINDOWY_ANIM);

		//temporary rectangle, could represent restaurant
		house.setColor(brown);
		house.fillRect(HOUSE_X, HOUSE_Y, HOUSE_SIZE, HOUSE_SIZE);

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(house);
			}
		}
	}
	public void addGui(HousingCustomerGui gui) {
		guis.add(gui);
	}
}
