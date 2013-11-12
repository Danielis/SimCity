// MARKET HOST DATA
Collection<MarketCustomer> customers;
Collection<MarketWorker> workers;

// MARKET HOST MESSAGES
IWantService(MarketCustomer c){
	customers.add(c);
}

// MARKET HOST SCHEDULER
if (customers.size > 0 && !workers.allBusy()){
	if there exists a w in workers where !w.IsBusy()
		assignCustomer(customers.get(0), w);
}

// MARKET HOST ACTIONS
assignCustomer(MarketCustomer c, MarketWorker w){
	customers.remove(0);
	c.GoToWorker(w);
}