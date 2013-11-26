package housing;

import housing.interfaces.HousingCustomer;
import housing.interfaces.HousingWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import logging.TrackerGui;
import agent.Agent;

public class LandlordAgent extends Agent {


	//--------------------------------------------------------
	//-------------------Utilities----------------------------
	//--------------------------------------------------------
	//constructor
	public LandlordAgent() {
		complexes.add(new HousingComplex());
		System.out.println("Landlord created.");
		balance = 10000;
	}
	
	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}
	
	public void addCustomer(HousingCustomer hc){
		complexes.get(0).inhabitants.add(hc);
	}
	
	public void addWorker(HousingWorkerAgent worker) {
		workers.add(new MaintenanceWorker(worker, 0));
	}

	//--------------------------------------------------------
	//-------------------Data---------------------------------
	//--------------------------------------------------------
	private List<HousingComplex> complexes = new ArrayList<HousingComplex>();
	private List<RepairTicket> tickets = new ArrayList<RepairTicket>();
	private List<Payment> payments = new ArrayList<Payment>();
	private List<MaintenanceWorker> workers = new ArrayList<MaintenanceWorker>();
	double balance;
	public TrackerGui trackingWindow;

	public class HousingComplex {
		List<HousingCustomer> inhabitants = new ArrayList<HousingCustomer>();
		complexType type;
		double rent;
		public HousingComplex() {
			type = complexType.apartment;
			rent = 1000;
		}
	}
	enum complexType {apartment, house};

	private class Payment{
		HousingComplex complex;
		HousingCustomer inhabitant;
		double amountOwed;
		double amountPaid;
		paymentState s;
		public Payment(HousingComplex c, HousingCustomer i, double owed, paymentState initialState) {
			complex = c;
			inhabitant = i;
			amountOwed = owed;
			s = initialState;
		}
	}
	enum paymentState {created, issued, paying, completed};

	private class RepairTicket{
		HousingComplex complex;
		MaintenanceWorker w;
		double bill;
		ticketStatus s;
		public RepairTicket(HousingComplex c, ticketStatus ts) {
			complex = c;
			s = ts;
		}
	}
	enum ticketStatus {unassigned, assigned, completed, paid};

	private class MaintenanceWorker{
		HousingWorkerAgent p;
		int jobs;
		//constructor
		public MaintenanceWorker(HousingWorkerAgent h, int j) {
			p = h;
			jobs = j;
		}
	}

	//--------------------------------------------------------
	//-------------------Messages-----------------------------
	//--------------------------------------------------------
	public void EveryoneOwesRent(){ //called by gui or timer or something
		System.out.println("Landlord: time to figure out rent.");
		for(HousingComplex c: complexes) {
			for(HousingCustomer i: c.inhabitants) {
				payments.add(new Payment(c, i, c.rent / c.inhabitants.size(), paymentState.created));
				System.out.println("Landlord: payment request added.");
			}
		}
		stateChanged();
	}

	public void HereIsRent(HousingCustomer inhabitant, double amount){
		for(Payment p: payments) {
			if(p.inhabitant == inhabitant) {
				balance += amount;
				p.amountPaid += amount;
				p.s = paymentState.paying;
				p.amountOwed -= amount;
			}
		}
		stateChanged();
	}

	public void MyHouseNeedsRepairs(HousingCustomer p){
		for(HousingComplex c: complexes) {
			for(HousingCustomer h: c.inhabitants) {
				if(h == p) {
					tickets.add(new RepairTicket(c, ticketStatus.unassigned));
				}
			}
		}
		stateChanged();
	}

	public void RepairsCompleted(HousingComplex complex, double amount){
		for(RepairTicket t: tickets) {
			if(t.complex == complex) {
				t.w.jobs--;
				t.bill = amount;
				t.s = ticketStatus.completed;
			}
		}
		stateChanged();
	}

	//--------------------------------------------------------
	//------------------Scheduler-----------------------------
	//--------------------------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction() {
		for(RepairTicket t:tickets) {
			if(t.s == ticketStatus.unassigned) {
				AssignTicket(t);
				return true;
			}
			if(t.s == ticketStatus.completed) {
				PayTicket(t);
				return true;
			}
		}
		for(Payment p: payments) {
			if(p.s == paymentState.created) {
				SendBill(p);
				return true;
			}
			if(p.s == paymentState.paying){
				UpdateBill(p);
				return true;
			}
		}
		return false;
	}

	//--------------------------------------------------------
	//------------------Actions-----------------------------
	//--------------------------------------------------------
	private void AssignTicket(RepairTicket t){
		MaintenanceWorker min = workers.get(0);
		for(MaintenanceWorker current:workers){
			if(current.jobs<min.jobs)
				min = current;
		}
		t.w = min; 
		t.w.jobs++;
		t.s = ticketStatus.assigned;
		t.w.p.GoRepair(t.complex);
		System.out.println("Landlord: Ticket assigned.");
	}

	private void SendBill(Payment p){
		p.s = paymentState.issued;
		p.inhabitant.HereIsRentBill(p.amountOwed);
		System.out.println("Landlord: Bill sent to tenant.");
	}

	private void UpdateBill(Payment p){
		if (p.amountOwed < 0){
			p.s = paymentState.completed;
			balance -= p.amountPaid - p.amountOwed;
			p.inhabitant.HereIsChange((-1) * p.amountOwed);
			p.amountPaid = p.amountOwed;
			p.inhabitant.RentIsPaid();
			System.out.println("Landlord: Money received, change is owed.");
		}
		else if (p.amountOwed == 0){
			p.s = paymentState.completed;
			p.inhabitant.RentIsPaid();
			System.out.println("Landlord: Money received, no change is owed.");
		}
		else{
			p.s = paymentState.issued;
			p.inhabitant.YouStillOwe(p.amountOwed - p.amountPaid);
			System.out.println("Landlord: Money is still owed.");
		}
	}

	private void PayTicket(RepairTicket t){
		if (balance > t.bill){
			balance -= t.bill;
			t.s = ticketStatus.paid;
			t.w.p.HereIsMoney(t.complex, t.bill);
			System.out.println("Landlord: Ticket being paid.");
		}
		else{
			//TakeOutLoan(t.bill); //stub
			System.out.println("Landlord: Loan needed.");
		}
	}

}
