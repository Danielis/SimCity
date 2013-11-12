// MARKET CUSTOMER DATA
// inherits from PersonAgent class
MarketHost h;
marketCustomerState state;
MarketWorker w;
String item;
int quantityWanted;
int quantityReceived;
double bill;
double balance;

enum marketCustomerState {entered, waiting, assigned, ordered, paying, received, exited}

// MARKET CUSTOMER MESSAGES
WantsToOrder(String item, int quantity){ //called from Person agent
	this.item = item;
	int.quantity = quantity;
	this.state = entered;
}

GoToWorker(MarketWorker w){
	this.w = w;
	state = assigned;
}

CanOnlyFullfillPartial(String item, int quantity){
}

OutOfItem(String item){
	state = done;
}

HereIsBill(MarketWorker w, double amount){
	bill = amount;
	state = paying;
}

HereIsItem(MarketWorkerAgent m, String item, int quantity){
	quantityReceived += quantity;
	state = received;
}

// MARKET CUSTOMER SCHEDULER
if (state == entered){
	TellHost();
	return true;
}

if (state == assigned){
	GiveOrder();
	return true;
}

if (state == paying){
	PayWorker();
	return true;
}

if (state == received || state == done){
	LeaveMarket();
	return true;
}

return false;

// MARKET CUSTOMER ACTIONS
TellHost(){
	DoEnterMarket();
	h.IWantService();
	state = waiting;
}

GiveOrder(){
	DoGiveOrder();
	w.IWantToBuy(item, quantityWanted);
	state = ordered;
}

PayWorker(){
	if (balance >= bill){
		balance -= bill;
		w.HereIsPayment(this, bill);
	}
	else
		w.DontWantAnymore(this);

}

LeaveMarket(){
	DoLeaveMarket();
	state = exited;
}