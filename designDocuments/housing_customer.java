// HOUSING CUSTOMER DATA
LandLord l;
billState state;
double balance;
double bill;
Boolean needsLoan;
Boolean houseNeedsRepairs;

// HOUSING CUSTOMER MESSAGES
HereIsRentBill(double amount){
	bill = amount;
}

HereIsChange(double amount){
	balance += amount;
}

RentIsPaid(){
	bill = 0;
}

YouStillOwe(double amount){
	needsLoan = true;
}

MyHouseNeedsRepairs(){
	houseNeedsRepairs = true;
}

// HOUSING CUSTOMER SCHEDULER
if (needsLoan){
	TakeOutLoan();
	return true;
}
if (houseNeedsRepairs){
	CallLandlordRepairs();
	return true;
}
if (bill > 0){
	PayBill();
	return true;
}
return false;

// HOUSING CUSTOMER ACTIONS
PayBill(){
	if (balance > bill){
		balance -= bill;
		l.HereIsRent(this, bill);
		bill = 0;
	}
	else{
		l.HereIsRent(this, balance);
		bill -= balance;
		balance = 0;
	}
}

CallLandlordRepairs(){
	houseNeedsRepairs = false;
	l.MyHouseNeedsRepairs(this);
}

TakeOutLoan(){
	GoToBank(); //stub;
	needsLoan = false;
}