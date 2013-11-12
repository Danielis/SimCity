// LANDLORD DATA
List<HousingComplex> complexes;
List<RepairTicket> tickets;
List<Payment> payments;
List<MaintanenceWorker> workers;
double balance;

HousingComplex {
	List<HousingCustomer> inhabitants;
	complexType type;
	double rent;
}
enum complexType {apartment, house};

Payment{
	HousingComplex complex;
	HousingCustomer i;
	double amountOwed;
	double amountPaid;
	paymentState s;
}
enum paymentState {created, issued, paying, completed};

RepairTicket{
	HousingComplex complex;
	MaintanenceWorker w;
	double bill;
	ticketStatus s;
}
enum ticketStatus {unassigned, assigned, completed, paid};

MaintanenceWorker{
	HousingCustomer p;
	int jobs;
}

// LANDLORD MESSAGES

EveryoneOwesRent(){ //called by gui or timer or something
	for c in complexes {
		for i in complexes.inhabitants{
		payments.add(new Payment(c, i, c.rent / c.inhabitants.size(), created))
		}
	}
}


HereIsRent(HousingCustomer p, double amount){
	payment = payments.find(p);
	balance += amount;
	payment.amountPaid += amount;
	payment.s = paying;
}

MyHouseNeedsRepairs(HousingCustomer p){
	comp = complexes.find(p);
	tickets.add(new RepairTicket(comp, unassigned));
}

RepairsCompleted(HousingComplex complex, double amount){
	t = tickets.find(complex);
	t.bill = amount;
	t.s = completed;
}

// LANDLORD SCHEDULER

if there exists a t in tickets such that t.s == unassigned{
	AssignTicket(t);
	return true;
}
if there exists a t in tickets such that t.s == completed{
	PayTicket(t);
	return true;
}
if there exists a p in payments such that p.s == created{
	SendBill(p);
	return true;
}
if there exists a p in payments such that p.s == paying{
	UpdateBill(p);
	return true;
}
return false;

// LANDLORD ACTIONS

AssignTicket(RepairTicket t){
	t.w = FindLowestJobs(workers); //stub
	t.s = assigned;
	t.w.GoRepair(t.complex);
}

SendBill(Payment p){
	p.s = issued;
	p.i.HereIsRentBill(p.amountOwed);
}

UpdateBill(Payment p){
	if (p.amountPaid > p.amountOwed){
		p.s = completed;
		balance -= p.amountPaid - p.amountOwed;
		p.i.HereIsChange(p.amountPaid - p.amountOwed);
		p.amountPaid = amountOwed;
		p.i.RentIsPaid();
	}
	else if (p.amountPaid == p.amountOwed){
		p.s = completed;
		p.i.RentIsPaid();
	}
	else{
		p.s = issued;
		p.i.YouStillOwe(p.amountOwed - p.amountPaid);
	}
}

PayTicket(RepairTicket t){
	if (balance > t.bill){
		balance -= bill;
		t.s = paid;
		t.w.HereIsMoney(double bill);
	}
	else{
		TakeOutLoan(bill); //stub
	}
}