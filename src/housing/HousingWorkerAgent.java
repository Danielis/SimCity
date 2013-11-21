package housing;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import housing.LandlordAgent.HousingComplex;
import housing.interfaces.Landlord;

public class HousingWorkerAgent extends Agent {

	// HOUSING WORKER DATA
	LandlordAgent landlord;
	private List<Job> myJobs = new ArrayList<Job>();
	double balance;

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
	public HousingWorkerAgent(){
		System.out.println("Housing Worker Created.");
		balance = 0;
	}
	
	public void setLandlord(LandlordAgent l) {
		landlord = l;
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
	        //DoGoToComplex(j.c);
	        //DoRepairComplex(j.c);
	        job.s = jobState.completed;
	        System.out.println("Job completed.");
	}

	private void AskForPay(Job job){
	        landlord.RepairsCompleted(job.c, job.bill);
	        job.s = jobState.billed;
	        System.out.println("Asking for pay.");
	}
}
