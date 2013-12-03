package housing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import housing.HousingWorkerAgent.jobState;
import housing.LandlordAgent.HousingComplex;
import housing.guis.HousingAnimationPanel;
import housing.guis.HousingWorkerGui;
import housing.interfaces.HousingWorker;
import housing.interfaces.Landlord;
import roles.Role;

public class HousingWorkerRole extends Role implements HousingWorker{


	// HOUSING WORKER DATA
	LandlordRole landlord;
	public String name;
	private List<Job> myJobs = new ArrayList<Job>();
	double balance;
	private Semaphore waitingForAnimation = new Semaphore(0);
	private HousingAnimationPanel animationPanel;
	private HousingWorkerGui gui;
	public TrackerGui trackingWindow;
	private class Job{
		HousingComplex c;
		double bill;
		double amountReceived;
		jobState s;
		public Job(HousingComplex hc, double charged, double received, jobState state) {
			c = hc;
			bill = charged;
			amountReceived = received;
			s = state;
		}
	}
	enum jobState {created, completed, billed, paid};

	//------------------------------------------------------
	//-----------------Utilities----------------------------
	//------------------------------------------------------
	//constructor
	public HousingWorkerRole(String name1){
		this.name = name1;
		System.out.println("Housing Worker Created.");
		balance = 0;
	}
	
	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}

	public void setLandlord(LandlordRole l) {
		landlord = l;
	}

	public HousingAnimationPanel copyOfAnimationPanel() {
		return animationPanel;
	}

	public void setGui(HousingWorkerGui g) {
		gui = g;
	}

	public void WaitForAnimation() {
		try {
			waitingForAnimation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DoneWithAnimation() {
		waitingForAnimation.release();
	}

	//------------------------------------------------------
	//-----------------Messages-----------------------------
	//------------------------------------------------------
	public void GoRepair(HousingComplex c){
		//a bill will be calculated here eventually
		myJobs.add(new Job(c, 100.0, 0.0, jobState.created) );
		stateChanged();
	}

	public void HereIsMoney(HousingComplex c, double amount){
		for(Job j:myJobs) {
			if(j.c == c && j.s == jobState.completed) {
				balance += amount;
				j.s = jobState.completed;
			}
		}
		stateChanged();
	}

	//------------------------------------------------------
	//-----------------Scheduler-----------------------------
	//------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		for(Job job:myJobs) {
			if(job.s == jobState.completed) {
				AskForPay(job);
				return true;
			}
			if(job.s == jobState.created) {
				CompleteJob(job);
				return true;
			}
		}
		return false;
	}

	//------------------------------------------------------
	//-----------------Actions-----------------------------
	//------------------------------------------------------
	private void CompleteJob(Job job){
		gui.DoGoToComplex();
		//DoRepairComplex(j.c);
		job.s = jobState.completed;
		System.out.println("Worker: Job completed.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingWorkerAgent", "Job Completed", new Date()));
	}

	private void AskForPay(Job job){
		gui.DoGoHome();
		landlord.RepairsCompleted(job.c, job.bill);
		job.s = jobState.billed;
		System.out.println("Worker: Asking for pay.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "HousingWorkerAgent", "Asking for Pay", new Date()));
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}
}
