package housing;

import housing.interfaces.HousingCustomer;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class LandlordAgent extends Agent {

	//--------------------------------------------------------
	//-------------------Data---------------------------------
	//--------------------------------------------------------
	private List<HousingComplex> complexes = new ArrayList<HousingComplex>();
	private List<RepairTicket> tickets = new ArrayList<RepairTicket>();
	private List<Payment> payments = new ArrayList<Payment>();
	private List<MaintanenceWorker> workers;
	double balance;

	public class HousingComplex {
		List<HousingCustomerAgent> inhabitants = new ArrayList<HousingCustomerAgent>();
		complexType type;
		double rent;
	}
	enum complexType {apartment, house};

	private class Payment{
		HousingComplex complex;
		HousingCustomerAgent inhabitant;
		double amountOwed;
		double amountPaid;
		paymentState s;
		public Payment(HousingComplex c, HousingCustomerAgent i, double owed, paymentState initialState) {
			complex = c;
			inhabitant = i;
			amountOwed = owed;
			s = initialState;
		}
	}
	enum paymentState {created, issued, paying, completed};

	private class RepairTicket{
		HousingComplex complex;
		MaintanenceWorker w;
		double bill;
		ticketStatus s;
		public RepairTicket(HousingComplex c, ticketStatus ts) {
			complex = c;
			s = ts;
		}
	}
	enum ticketStatus {unassigned, assigned, completed, paid};

	private class MaintanenceWorker{
		HousingWorkerAgent p;
		int jobs;
	}

	//--------------------------------------------------------
	//-------------------Messages-----------------------------
	//--------------------------------------------------------
	public void EveryoneOwesRent(){ //called by gui or timer or something
		for(HousingComplex c: complexes) {
			for(HousingCustomerAgent i: c.inhabitants) {
				payments.add(new Payment(c, i, c.rent / c.inhabitants.size(), paymentState.created));	        	}
		}
	}


	public void HereIsRent(HousingCustomerAgent inhabitant, double amount){
		for(Payment p: payments) {
			if(p.inhabitant == inhabitant) {
				balance += amount;
				p.amountPaid += amount;
				p.s = paymentState.paying;
				p.amountOwed -= amount;
			}
		}
	}

	public void MyHouseNeedsRepairs(HousingCustomerAgent p){
		for(HousingComplex c: complexes) {
			for(HousingCustomerAgent h: c.inhabitants) {
				if(h == p) {
					tickets.add(new RepairTicket(c, ticketStatus.unassigned));
				}
			}
		}
	}

	public void RepairsCompleted(HousingComplex complex, double amount){
		for(RepairTicket t: tickets) {
			if(t.complex == complex) {
				t.bill = amount;
				t.s = ticketStatus.completed;
			}
		}
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
		t.w = workers.get(0); //stub
		//there should be a better way of picking workers
		t.s = ticketStatus.assigned;
		t.w.p.GoRepair(t.complex);
	}

	private void SendBill(Payment p){
		p.s = paymentState.issued;
		p.inhabitant.HereIsRentBill(p.amountOwed);
	}

	private void UpdateBill(Payment p){
		if (p.amountPaid > p.amountOwed){
			p.s = paymentState.completed;
			balance -= p.amountPaid - p.amountOwed;
			p.inhabitant.HereIsChange(p.amountPaid - p.amountOwed);
			p.amountPaid = p.amountOwed;
			p.inhabitant.RentIsPaid();
		}
		else if (p.amountPaid == p.amountOwed){
			p.s = paymentState.completed;
			p.inhabitant.RentIsPaid();
		}
		else{
			p.s = paymentState.issued;
			p.inhabitant.YouStillOwe(p.amountOwed - p.amountPaid);
		}
	}

	private void PayTicket(RepairTicket t){
		if (balance > t.bill){
			balance -= t.bill;
			t.s = ticketStatus.paid;
			t.w.p.HereIsMoney(t.complex, t.bill);
		}
		else{
			//TakeOutLoan(t.bill); //stub
		}
	}

}
