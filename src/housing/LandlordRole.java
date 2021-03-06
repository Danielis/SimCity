package housing;

import housing.guis.LandlordGui;
import housing.interfaces.HousingCustomer;
import housing.interfaces.HousingWorker;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import roles.Apartment;
import roles.Role;
import city.PersonAgent;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import agent.Agent;

public class LandlordRole extends Role {


	//--------------------------------------------------------
	//-------------------Utilities----------------------------
	//--------------------------------------------------------
	//constructor
	public LandlordRole(String s) {
		complexes.add(new HousingComplex());
		System.out.println("Landlord created.");
		balance = 10000;
		name = s;
	}
	
	public void setAparment(Apartment a) {
		complexes.get(0).building = a;
	}
	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}
	
	public void addCustomer(HousingCustomer hc){
		complexes.get(0).inhabitants.add(hc);
	}
	
	public void addWorker(HousingWorker worker) {
		workers.add(new MaintenanceWorker(worker, 0));
	}

	//--------------------------------------------------------
	//-------------------Data---------------------------------
	//--------------------------------------------------------
	public List<HousingComplex> complexes = new ArrayList<HousingComplex>();
	public List<RepairTicket> tickets = new ArrayList<RepairTicket>();
	public List<Payment> payments = new ArrayList<Payment>();
	public List<MaintenanceWorker> workers = new ArrayList<MaintenanceWorker>();
	public double balance;
	public TrackerGui trackingWindow;
	public String name;
	private LandlordGui gui;

	public class HousingComplex {
		List<HousingCustomer> inhabitants = new ArrayList<HousingCustomer>();
		complexType type;
		double rent;
		Apartment building;
		public HousingComplex() {
			type = complexType.apartment;
			rent = 1000;
		}
	}
	
	enum complexType {apartment, house};

	public class Payment{
		HousingComplex complex;
		HousingCustomer inhabitant;
		PersonAgent inhabitantPerson;
		double amountOwed;
		double amountPaid;
		public paymentState s;
		public Payment(HousingComplex c, HousingCustomer i, double owed, paymentState initialState) {
			complex = c;
			inhabitant = i;
			amountOwed = owed;
			s = initialState;
		}
	}
	public enum paymentState {created, issued, paying, completed};

	public class RepairTicket{
		HousingComplex complex;
		MaintenanceWorker w;
		double bill;
		public ticketStatus s;
		public RepairTicket(HousingComplex c, ticketStatus ts) {
			complex = c;
			s = ts;
		}
	}
	public enum ticketStatus {unassigned, assigned, completed, paid};

	private class MaintenanceWorker{
		HousingWorker p;
		int jobs;
		//constructor
		public MaintenanceWorker(HousingWorker worker, int j) {
			p = worker;
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
	public boolean pickAndExecuteAnAction() {
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
		//trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Ticket Assigned", new Date()));
	}

	private void SendBill(Payment p){
		p.s = paymentState.issued;
		p.inhabitant.HereIsRentBill(p.amountOwed);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Bill sent to tenant", new Date()));
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
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Money received.  Change is owed", new Date()));
		}
		else if (p.amountOwed == 0){
			p.s = paymentState.completed;
			p.inhabitant.RentIsPaid();
			System.out.println("Landlord: Money received, no change is owed.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Money received.  No change is owed", new Date()));
		}
		else{
			p.s = paymentState.issued;
			p.inhabitant.YouStillOwe(p.amountOwed - p.amountPaid);
			System.out.println("Landlord: Money is still owed.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Money is still owed.", new Date()));
		}
	}

	private void PayTicket(RepairTicket t){
		if (balance > t.bill){
			balance -= t.bill;
			t.s = ticketStatus.paid;
			t.w.p.HereIsMoney(t.complex, t.bill);
			System.out.println("Landlord: Ticket being paid.");
			//trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Ticket Being paid", new Date()));
		}
		else{
			//TakeOutLoan(t.bill); //stub
			System.out.println("Landlord: Loan needed.");
			trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.HOUSING, "LandlordAgent", "Loan needed", new Date()));
		}
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetPaid() {
		// TODO Auto-generated method stub
		
	}

	public void DoneWithAnimation() {
		// TODO Auto-generated method stub
		
	}

	public ImageObserver copyOfAnimationPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void WaitForAnimation() {
		// TODO Auto-generated method stub
		
	}

	public void setGui(LandlordGui g) {
		this.gui = g;
	}

}
