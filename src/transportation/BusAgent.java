package transportation;

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


public class BusAgent extends Agent implements Bus
{
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/
	
	//Variable
	BusGui gui = null;
	double money;
	String name;
	boolean atStop;
	boolean nextStop;
	BusStopAgent curStop;
	
	public List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	public CityAnimationPanel CityAnimPanel;

	
	public BusAgent(String name){
		this.name = name;
		System.out.println("Added Bus " + name);
	}
	
	public String getName(){
		return name;
	}
	//Class Declarations
	

	
	/*****************************************************************************
									 UTILITIES
	******************************************************************************/

	public void setGui(BusGui g)
	{
		this.gui = g;
	}
	public void setAnimationPanel(CityAnimationPanel panel) {
		CityAnimPanel = panel;
	}

	public BusGui getGui() {
		return gui;
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
	
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/
	

	//
	public void msgAtStop(BusStopAgent B){ // sent from BusCompany when Bus Gui matches Bus Stop Gui
	    atStop= true;
	    curStop = B;
	    print("Got to the stop");
	    stateChanged();
	}
	
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity
	
	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/
	
	@Override
	protected boolean pickAndExecuteAnAction() {

		if (atStop) {
			ActionAtStop();
			return true;
		}
		if (nextStop && !atStop){
			ActionNextStop();
			return true;
		}
		return false;
	}
	
	/*****************************************************************************
										ACTIONS
	 ******************************************************************************/
	
	private void ActionAtStop()
	{
		System.out.println("ActionsAtStop");
		//Take in people from stop
		for(int i=0;i<curStop.people.size();i++){
			this.people.add(curStop.people.get(i));
			curStop.people.remove(curStop.people.get(i));
		}
		//Msg stop that people have left stop
		curStop.msgPeopleGone(); // Used to tell curStop that the people were taken in so Gui can change
		//Leave people at stop
		/*
		for(int i=0;i<this.people.size();i++){
			if(this.people.get(i).getDestinationBusStop() == curStop){
				this.people.get(i).setLocation(curStop.location); // will set person at the curStop
				this.people.get(i).msgAtBusStop(); // will then free person to continue walking since statechanged
			}
		}
		*/
		this.atStop = false;
		stateChanged();
	}
	
	private void ActionNextStop(){
		nextStop = false;
		gui.NextStop();
		this.WaitForAnimation();
		nextStop = true;
		stateChanged();
	}
}
