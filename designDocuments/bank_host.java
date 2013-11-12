// BANK HOST DATA
Collection<BankCustomer> customers;
Collection<BankTeller> tellers;

// BANK HOST MESSAGES
IWantService(BankCustomer c){
	customers.add(c);
}

// BANK HOST SCHEDULER
if (customers.size > 0 && !tellers.allBusy()){
	if there exists a t in tellers where !t.IsBusy()
		assignCustomer(customers.get(0), t);
}

// BANK HOST ACTIONS
assignCustomer(BankCustomer c, BankTeller t){
	customers.remove(0);
	c.GoToTeller(t);
}