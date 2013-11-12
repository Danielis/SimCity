// HOUSING WORKER DATA
Landlord l;
List<Jobs>;
double balance;
Collection<Job> jobs;

Job{
HousingComplex c;
double bill;
double amountReceived;
jobState s;
}
enum jobState {created, completed, billed, paid};

// HOUSING WORKER MESSAGES
GoRepair(HousingComplex c){
	int b = CalculateBill(c);
	jobs.add( new Job(c, b, 0, created) );
}

HereIsMoney(HousingComplex c, double amount){
	comp = jobs.find(c, completed);
	balance += amount;
	comp.state = paid;
}

// HOUSING WORKER SCHEDULER
if there exists a j in jobs such that j.s == completed{
	AskForPay(j);
	return true;
}

if there exists a j in jobs such that j.s == created{
	CompleteJob(j);
	return true;	
}

return false;

// HOUSING WORKER ACTIONS
CompleteJob(Job j){
	DoGoToComplex(j.c);
	DoRepairComplex(j.c);
	// Timer
	j.state = completed;
}

AskForPay(Job j){
	RepairsCompleted(j.c, j.bill);
	j.state = billed;
}