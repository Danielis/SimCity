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
	private Image bed;
	
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
		
		try
        {
            bed = ImageIO.read(getClass().getResource("/resources/bed.png"));
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
		Color backgroundColor = new Color(0, 0, 0);
		background.setColor(backgroundColor);
		background.drawImage(wood_floor, 0, 0, this);
		background.drawImage(kitchen, 0, 0, 288, 241, this);
		background.drawImage(bed, 600, 100, this);
		background.drawLine(500, 60, 500, 135);
		background.drawLine(500, 135, 700, 135);
		background.drawImage(bed, 600, 200, this);		
		background.drawLine(500, 160, 500, 235);
		background.drawLine(500, 235, 700, 235);
		background.drawImage(bed, 600, 300, this);
		background.drawLine(500, 260, 500, 335);
		background.drawLine(500, 335, 700, 335);
		background.drawImage(bed, 600, 400, this);
		background.drawLine(500, 360, 500, 435);
		background.drawLine(500, 435, 700, 435);
		background.drawImage(bed, 600, 500, this);
		background.drawLine(500, 460, 500, 535);
		background.drawLine(500, 535, 700, 535);
		background.drawLine(500, 560, 500, 735);

		background.drawImage(bed, 600, 600, this);

		
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