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

}
