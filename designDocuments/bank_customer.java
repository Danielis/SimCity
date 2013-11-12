// MARKET CUSTOMER DATA
// inherits from PersonAgent class
BankHost h;
marketCustomerState state;
BankTeller t;
double balance;
customerPurpose purpose;
double amount; //amount they want to deposit, withdraw, pay loan off of, or take loan out of
int accountID;

enum customerPurpose {createAccount, withdraw, deposit, takeLoan, payLoan};
enum bankCustomerState {entered, waiting, assigned, };

// MARKET CUSTOMER MESSAGES
WantsToOrder(String visitPurpose, int quantity){ //called from Person agent
	purpose = convert(visitPurpose);
	this.state = entered;
}

GoToWorker(MarketWorker w){
	this.w = w;
	state = assigned;
}

AccountCreated(){
	state = done;
}

MoneySuccesfullyDeposited(){
	state = done;
}

LoanCreated(){
	balance += amount;
	state = done;
}

CannotCreatLoan(){
	state = done;
}

CreditNotGoodEnough(){
	state = done;
}

YourLoanIsPaidOff(){
	state = done;
}

YouStillOwe(){
	state = done;
}

MoneySuccesfullyDeposited(){
	state = done;
}

HereIsWithdrawal(double amount){
	balance += amount;
	state = done;
}

HereIsPartialWithdrawal(double amount){
	balance += amount;
	state = done;
}

NoMoney(){ // in account
	state = done;
}

// MARKET CUSTOMER SCHEDULER
if (state == entered){
	TellHost();
	return true;
}

if (state == assigned){
	AskForAssistance();
	return true;
}

if (state == done){
	LeaveBank();
}

return false;

// MARKET CUSTOMER ACTIONS
TellHost(){
	DoEnterMarket();
	h.IWantService();
	state = waiting;
}

AskForAssistance(){
	DoGiveOrder();

	if (purpose == creatAccount){
		balance -= amount;
		t.IWantAccount(this, amount);
	}
	if (purpose == withdraw)
		t.WithdrawMoney(this, accountID, amount);
	if (purpose == deposit){
		balance -= amount;
		t.DepositMoney(this, accountID, amount);
	}
	if (purpose == takeLoan)
		t.IWantLoan(this, amount);
	if (purpose == payLoan){
		balance -= amount;
		t.PayMyLoan(this, amount);
	}

	
	state = waiting;
}



LeaveBank(){
	DoLeaveBank();
	state = exited;
}