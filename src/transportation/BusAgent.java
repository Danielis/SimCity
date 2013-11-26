package transportation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	BusStopAgent tempStop; //used to hold a stop so that the curStop doesn't change and the people are able to instantly get off
	TransportationCompany company;
	Timer timer;
	BusAgent myself;

	
	public List<PersonAgent> people = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	public Semaphore animSemaphore = new Semaphore(0, true);
	
	public CityAnimationPanel CityAnimPanel;

	
	public BusAgent(String name){
		this.name = name;
//		System.out.println("Added Bus " + name);
		atStop=false;
		nextStop = true;
		timer = new Timer();
		myself =this;
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
	
	public void setCompany(TransportationCompany M){
		this.company = M;
	}
	
	/*****************************************************************************
	 								 MESSAGES
	 ******************************************************************************/
	

	//
	public void msgAtStop(BusStopAgent B){ // sent from BusCompany when Bus Gui matches Bus Stop Gui
	    atStop= true;
	    curStop = B;
//	    print("CurStop now equals " + B.getName());
	    stateChanged();
	}
	public void msgNextStop(){
		nextStop=true;
		stateChanged();
	}
	public void msgBusGuiMoved(){
		company.busMoved();
	}
	
	//Figure out how we are going to incorporate Bus,Car and walking into SimCity
	
	/*****************************************************************************
	 								SCHEDULER
	 ******************************************************************************/
	
	@Override
	protected boolean pickAndExecuteAnAction() {

		if (atStop) {
//			print("Called ActionAtStop for " + curStop.getName());
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
		//Leave people at stop
//		print("Size of PEOPLE IN BUS: " +people.size() + "      " +"CurStop: " + curStop.getName());
		for(int i=0;i<this.people.size();i++){
			if(curStop.getName().equals(people.get(i).getDestinationBusStop().getName())){
//				print("CurStop: " + curStop.getName() + "  should equal  " + people.get(i).getDestinationBusStop().getName() );
				//if(this.people.get(i).getDestinationBusStop().getGui().getXPosition() == curStop.getGui().getXPosition() && this.people.get(i).getDestinationBusStop().getGui().getYPosition() == curStop.getGui().getYPosition()){	
//				print("Match FOUND PERSON REACHED HIS BUSTSTOP");
				people.get(i).getGui().setPresent(true);
				this.people.get(i).setPosition(people.get(i).getDestinationBusStop().getGui().getXPosition(),people.get(i).getDestinationBusStop().getGui().getYPosition()); // will set person at the curStop
				this.people.get(i).msgAtBusStop(); // will then free person to continue walking since statechanged
				people.remove(i);
			}
		}
		//Take in people from stop
		for(int i=0;i<curStop.people.size();i++){
			this.people.add(curStop.people.get(i));
			print("Added Person " + curStop.people.get(i).getName());
			curStop.people.remove(curStop.people.get(i));
		}
		//Msg stop that people have left stop
		curStop.msgPeopleGone(); // Used to tell curStop that the people were taken in so Gui can change
		
		timer.schedule(new TimerTask()
		{
			public void run()
			{	
				myself.DoneWithAnimation();
//				print("The time has finished");
			}
		}, 1000);
		this.WaitForAnimation();
		this.atStop = false;
		msgNextStop();
	}
	
	public void ActionNextStop(){
		nextStop = false;
		gui.NextStop();
	}

	@Override
	public void msgImAtStop(BusStop busStop) {
		// TODO Auto-generated method stub
		
	}
}
