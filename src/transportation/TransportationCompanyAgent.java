package transportation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import transportation.gui.*;
import transportation.Interfaces.*;


public class TransportationCompanyAgent extends Agent implements TransportationCompany
{
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/
	
	//Variable
	String name;
	
	public List<BusStopAgent> stops = Collections.synchronizedList(new ArrayList<BusStopAgent>());
	public List<BusAgent> buses = Collections.synchronizedList(new ArrayList<BusAgent>());
	public List<PersonGui> people = Collections.synchronizedList(new ArrayList<PersonGui>());
	
	//Need to add People Gui's as they are created and then when a gui moves I need to check for collision with
	//People who are cars or cars on cars which is the same thing
	
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	public CityAnimationPanel CityAnimPanel;

	
	public TransportationCompanyAgent(String name){
		this.name = name;
		System.out.println("Added Bus Company " + name);
	}
	
	public String getName(){
		return name;
	}
	//Class Declarations
	

	
	/*****************************************************************************
									 UTILITIES
	******************************************************************************/

	public void setAnimationPanel(CityAnimationPanel panel) {
		CityAnimPanel = panel;
	}

	public void WaitForAnimation()
	{
		try
		{
			this.animSemaphore.acquire();	
		} catch (InterruptedException e) {
            // no action - expected when stopping or when deadline changed
        } catch (Exception e) {
            print("Unexpected exception caught in Agent thread:" , e);
        }
	}
	
	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}
	
	public void addBus(BusAgent B){
		buses.add(B);
	}
	public void addBusStop(BusStopAgent B){
		stops.add(B);
	}
	public void addPersonGui(PersonGui g){
		people.add(g);
	}
	
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/
	

	// After any movement of Bus the transportation must check the location of bus with the busStops so call after Bus movement
	public void busMoved(){ // need to call in Bus when it's gui makes a move.
		//stateChanged();
		Check(); //basically removed semaphore since it was slowing process down
		// Semaphore would build up too many permits and execution of code would at times be slower than it had to be so a bus would not officially take a person until some time after he had already left the stop
		// With this the function is called right away every time the bus moves and like a computer executed when the bus is at the stop.
	}
	public void CarMoved(PersonGui g){
		Collision(g);
	}
	public void BusMoved(BusAgent g){
		Collision(g.gui);
	}
	
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity
	
	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/
	
	@Override
	public boolean pickAndExecuteAnAction() {
		// This function checks bus and busStops to see if they are in the same position, Calls Bus msg that they are at stop
		Check(); // no need to return true since every time buses move they call this function which is fine because as long as they move they should be checked
		//super.clearSemaphore(); // potentially risky move but since this is called all the time since Buses move all the time it is done so that the pickandexecute doesn't always run unless a bus calls on it. Otherwise, this would run if the buses suddenly stopped moving due to past calls by multiple buses.
			
		return false;
	}
	
	/*****************************************************************************
										ACTIONS
	 ******************************************************************************/
	private void Check()
	{
		for(int i=0;i<buses.size();i++){
			for(int j=0;j<stops.size();j++){
				if(buses.get(i).getGui().getXPosition() == stops.get(j).getGui().getXPosition() && buses.get(i).getGui().getYPosition() == stops.get(j).getGui().getYPosition() ){//compare locations of both
					buses.get(i).msgAtStop(stops.get(j));
					//print("At Stop: " + stops.get(j).getName());
				}
			}
		}
	}

	private void Collision(PersonGui g){
		//Car collided with someone
		// range of x and y of object in question
		int x = g.getXPosition();
		int x2= g.getXPosition() + g.getImgX();
		int y = g.getYPosition();
		int y2= g.getYPosition() + g.getImgY();
		boolean colliding = false;

		for(int i=0;i<people.size();i++){
			if(g == people.get(i))
				continue;
			// range of x and y for second object
			int ox = people.get(i).getXPosition();
			int ox2= people.get(i).getXPosition() + people.get(i).getImgX();
			int oy = people.get(i).getYPosition();
			int oy2= people.get(i).getYPosition() + people.get(i).getImgY();

			if( ((ox <= x && x <= ox2) || (ox <= x2 && x2 <= ox2)) && ((oy <= y && y <= oy2) || (oy <= y2 && y2 <= oy2)) ){
				System.out.println("First:	" + x + "  " + x2 + "  " + y + "  " + y2);
				System.out.println("Second: " + ox + "  " + ox2 + "  " + oy + "  " + oy2);
				colliding = true;
				if(people.get(i).hasCar()){
					print("Car on Car Collision");
					people.get(i).removeCar();
					//					people.get(i).setPosition(ox+50, 100);
				}
				else{
					print("Car hit a Person");
					if(people.get(i).isPresent()){
						g.collision();
					}
					//					people.get(i).setPosition(ox+100,oy);
				}
			}

		}
		for(int i=0;i<buses.size();i++){
			// range of x and y for second object
			int ox = buses.get(i).getGui().getXPosition();
			int ox2= buses.get(i).getGui().getXPosition() + buses.get(i).getGui().getImgX();
			int oy = buses.get(i).getGui().getYPosition();
			int oy2= buses.get(i).getGui().getYPosition() + buses.get(i).getGui().getImgY();

			if( ((ox <= x && x <= ox2) || (ox <= x2 && x2 <= ox2)) && ((oy <= y && y <= oy2) || (oy <= y2 && y2 <= oy2)) ){
				System.out.println("First:	" + x + "  " + x2 + "  " + y + "  " + y2);
				System.out.println("Second: " + ox + "  " + ox2 + "  " + oy + "  " + oy2);
				colliding = true;
				print("Car on Bus Collision");
				g.collision();		
			}

		}
		if(!colliding)
			g.releaseGuiAnimation();
	}
	
	private void Collision(BusGui g){
		//Car collided with someone
		// range of x and y of object in question
		int x = g.getXPosition();
		int x2= g.getXPosition() + g.getImgX();
		int y = g.getYPosition();
		int y2= g.getYPosition() + g.getImgY();
		boolean colliding = false;

		for(int i=0;i<people.size();i++){
			// range of x and y for second object
			int ox = people.get(i).getXPosition();
			int ox2= people.get(i).getXPosition() + people.get(i).getImgX();
			int oy = people.get(i).getYPosition();
			int oy2= people.get(i).getYPosition() + people.get(i).getImgY();

			if( ((ox <= x && x <= ox2) || (ox <= x2 && x2 <= ox2)) && ((oy <= y && y <= oy2) || (oy <= y2 && y2 <= oy2)) ){
				//				System.out.println("First:	" + x + "  " + x2 + "  " + y + "  " + y2);
				//				System.out.println("Second: " + ox + "  " + ox2 + "  " + oy + "  " + oy2);
				colliding = true;
				if(people.get(i).hasCar()){
					print("Bus on Car Collision");
					people.get(i).removeCar();
					//g.collision();
					//					people.get(i).setPosition(ox+50, 100);
				}
				else{
					print("Bus hit a Person");
					if(people.get(i).isPresent()){
						g.collision();
					}
					//					people.get(i).setPosition(ox+100,oy);
				}
			}

		}
		for(int i=0;i<buses.size();i++){
			if(g == buses.get(i).getGui())
				continue;
			// range of x and y for second object
			int ox = buses.get(i).getGui().getXPosition();
			int ox2= buses.get(i).getGui().getXPosition() + buses.get(i).getGui().getImgX();
			int oy = buses.get(i).getGui().getYPosition();
			int oy2= buses.get(i).getGui().getYPosition() + buses.get(i).getGui().getImgY();

			if( ((ox <= x && x <= ox2) || (ox <= x2 && x2 <= ox2)) && ((oy <= y && y <= oy2) || (oy <= y2 && y2 <= oy2)) ){
				System.out.println("First:	" + x + "  " + x2 + "  " + y + "  " + y2);
				System.out.println("Second: " + ox + "  " + ox2 + "  " + oy + "  " + oy2);
				//colliding = true;
				//print("Bus on Bus Collision");
				//g.getAgent().setBusStopTime(10000);
				//g.collision();
			}
			if(!colliding)
				g.releaseGuiAnimation();
		}
	}
}