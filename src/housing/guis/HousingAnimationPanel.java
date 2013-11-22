package housing.guis;

import javax.imageio.ImageIO;
import javax.swing.*;

import restaurant.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class HousingAnimationPanel extends JPanel implements ActionListener {

	private final int WINDOWX_ANIM = 700;
	private final int WINDOWY_ANIM = 700;

	int HOUSE_X = 50;
	int HOUSE_Y = 100;
	int HOUSE_SIZE = 50;

	private Image wood_floor;
	private Image kitchen; 
	
	private List<Gui> guis = new ArrayList<Gui>();

	public HousingAnimationPanel() {
		setSize(WINDOWX_ANIM, WINDOWY_ANIM);
		setVisible(true);

		try
        {
            wood_floor = ImageIO.read(getClass().getResource("/resources/wood_floor.png"));
        } catch (IOException e ) {}
		
		try
        {
            kitchen = ImageIO.read(getClass().getResource("/resources/kitchen.png"));
        } catch (IOException e ) {}
		

		Timer timer = new Timer(20, this );
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {


		Graphics2D house = (Graphics2D)g;
		Graphics2D background = (Graphics2D)g;

		//COLORS		
		Color brown = new Color(245, 201, 114);
		Color backgroundColor = new Color(255, 255, 255);
		background.setColor(backgroundColor);
		background.drawImage(wood_floor, 0, 0, this);
		background.drawImage(kitchen, 0, 0, 288, 241, this);
		
		//BACKGROUND INITIATION
		house.setColor(brown);
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
	public void addGui(HousingWorkerGui gui) {
		guis.add(gui);
		System.out.println("Gui size: " + guis.size());
	}
}
