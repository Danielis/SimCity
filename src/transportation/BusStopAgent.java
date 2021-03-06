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
import transportation.test.mock.LoggedEvent;
import transportation.Interfaces.*;


public class BusStopAgent extends Agent implements BusStop
{
	/*****************************************************************************
	 								VARIABLES
	 ******************************************************************************/
	
	//Variable
	BusStopGui gui = null;
	String name;
	boolean atStop;
	boolean nextStop;
	TransportationCompany company;
	
	public List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	public CityAnimationPanel CityAnimPanel;

	
	public BusStopAgent(String name){
		this.name = name;
		System.out.println("Added BusStop " + name);
	}
	
	public String getName(){
		return name;
	}
	//Class Declarations
	

	
	/*****************************************************************************
									 UTILITIES
	******************************************************************************/
	public List<PersonAgent> getPeopleList(){
		return people;
	}
	public void setGui(BusStopGui g)
	{
		this.gui = g;
	}
	public void setAnimationPanel(CityAnimationPanel panel) {
		CityAnimPanel = panel;
	}

	public BusStopGui getGui() {
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
	public void setCompany(TransportationCompany M){
		this.company = M;
	}
	public void getPeople(Bus B){
		for(int i=0;i<people.size();i++){
			B.getPeopleList().add(people.get(i));
			print("Added Person " + people.get(i).getName());
			people.remove(i);
		}
	}
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/
	

	//
	public void msgImAtStop(PersonAgent P){ // sent from BusCompany when Bus Gui matches Bus Stop Gui
	    this.people.add(P);
	    print("Person clicked Stop");
	    stateChanged();
	}
	
	public void msgPeopleGone(){
		stateChanged(); // invokes scheduler when people have boarded bus in case no one is there anymore.
	}
	
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity
	
	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/
	
	
	public boolean pickAndExecuteAnAction() {
		ActionChangeGui();
		return false;
	}
	
	/*****************************************************************************
										ACTIONS
	 ******************************************************************************/
	
	private void ActionChangeGui()
	{

		if (people.isEmpty()) {
			gui.setStopGui(false); //if no one is at stop then change gui to empty gui
		}
		else{
			gui.setStopGui(true); // if someone is at stop then keep gui to not empty gui
		}
		stateChanged();
	}
	
}
