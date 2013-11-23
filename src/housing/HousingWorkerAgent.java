package housing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import housing.LandlordAgent.HousingComplex;
import housing.guis.HousingAnimationPanel;
import housing.guis.HousingGui;
import housing.guis.HousingWorkerGui;
import housing.interfaces.Landlord;

public class HousingWorkerAgent extends Agent {

	// HOUSING WORKER DATA
	LandlordAgent landlord;
	public String name;
	private List<Job> myJobs = new ArrayList<Job>();
	double balance;
	private Semaphore waitingForAnimation = new Semaphore(0);
	private HousingAnimationPanel animationPanel;
	private HousingWorkerGui gui;
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
	public HousingWorkerAgent(String name1){
		this.name = name1;
		System.out.println("Housing Worker Created.");
		balance = 0;
	}

	public void setLandlord(LandlordAgent l) {
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
	protected boolean pickAndExecuteAnAction() {
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
	}

	private void AskForPay(Job job){
		gui.DoGoHome();
		landlord.RepairsCompleted(job.c, job.bill);
		job.s = jobState.billed;
		System.out.println("Worker: Asking for pay.");
	}
}
