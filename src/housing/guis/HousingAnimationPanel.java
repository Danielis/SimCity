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
	private Image landlord_desk;
	private Image chair;
	
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
		
		try
        {
            landlord_desk = ImageIO.read(getClass().getResource("/resources/desk.png"));
        } catch (IOException e ) {}
		
		try
        {
            chair = ImageIO.read(getClass().getResource("/resources/houseChair.png"));
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
		Color backgroundColor = new Color(0, 0, 0);
		background.setColor(backgroundColor);
		background.setStroke(new BasicStroke(3));
		background.drawImage(wood_floor, 0, 0, this);
		background.drawImage(kitchen, 0, 0, 288, 241, this);
		background.drawImage(chair, 337, 10, this);
		background.drawImage(landlord_desk, 320, 30, this);
		//first row of beds
		background.drawImage(bed, 525, 100, this);
		background.drawImage(bed, 550, 100, this);
		background.drawImage(bed, 575, 100, this);
		background.drawImage(bed, 600, 100, this);
		background.drawImage(bed, 625, 100, this);
		background.drawLine(500, 60, 500, 135);
		background.drawLine(500, 135, 700, 135);
		background.drawImage(bed, 525, 200, this);
		background.drawImage(bed, 550, 200, this);
		background.drawImage(bed, 575, 200, this);
		background.drawImage(bed, 600, 200, this);
		background.drawImage(bed, 625, 200, this);		background.drawLine(500, 170, 500, 235);
		background.drawLine(500, 235, 700, 235);
		background.drawImage(bed, 525, 300, this);
		background.drawImage(bed, 550, 300, this);
		background.drawImage(bed, 575, 300, this);
		background.drawImage(bed, 600, 300, this);
		background.drawImage(bed, 625, 300, this);		background.drawLine(500, 270, 500, 335);
		background.drawLine(500, 335, 700, 335);
		background.drawImage(bed, 525, 400, this);
		background.drawImage(bed, 550, 400, this);
		background.drawImage(bed, 575, 400, this);
		background.drawImage(bed, 600, 400, this);
		background.drawImage(bed, 625, 400, this);		background.drawLine(500, 370, 500, 435);
		background.drawLine(500, 435, 700, 435);
		background.drawImage(bed, 525, 500, this);
		background.drawImage(bed, 550, 500, this);
		background.drawImage(bed, 575, 500, this);
		background.drawImage(bed, 600, 500, this);
		background.drawImage(bed, 625, 500, this);		background.drawLine(500, 470, 500, 535);
		background.drawLine(500, 535, 700, 535);
		background.drawLine(500, 570, 500, 735);
		background.drawImage(bed, 525, 610, this);
		background.drawImage(bed, 550, 610, this);
		background.drawImage(bed, 575, 610, this);
		background.drawImage(bed, 600, 610, this);
		background.drawImage(bed, 625, 610, this);
		//worker chairs
		for(int i = 0; i < 6; i++) {
			background.drawImage(chair, 10, 300 + 25 * i, this);
		}
		
		//BACKGROUND INITIATION
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
	}
	public void addGui(LandlordGui gui) {
		guis.add(gui);
	}
}
