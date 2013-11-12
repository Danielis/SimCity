// BANK TELLER DATA

double balance;
List <MyCustomer> myCustomers;
List <Transaction> transactions;
List <Account> accounts;
List <Loan> loans;

Transaction{
	double amount;
	Account account;
	Loan loan;
	transactionType type;
	transactionStatus status;
}
enum transactionType {withdrawal, deposit, newAccount, newLoan, loanPayment};
enum transactionStatus {unresolved, resolved};

MyCustomer{
	PersonAgent p;
}

Account {
	int id; // auto increment
	PersonAgent p;
	double balance;
}

Loan {
	PersonAgent p;
	double balanceOwed;
	double balancePaid;
	int dayCreated;
	int dayOwed;
	loanState s;
}
enum loanState {unpaid, partiallyPaid, paid};

// BANK TELLER MESSAGES

IWantAccount(PersonAgent p, double amount){
	acct = new Account(p, amount);
	transactions.add(new Transaction(acct, amount, newAccount, unresolved));
}

DepositMoney(PersonAgent p, int accountID, double amount){
	acct = accounts.find(accountID);
	transactions.add(new Transaction(acct, amount, deposit, unresolved));
}

WithdrawMoney(PersonAgent p, int accountID, double amount){
	acct = accounts.find(accountID);
	transactions.add(new Transaction(acct, amount, withdrawal, unresolved));
}

IWantLoan(PersonAgent p, double amount){
	time = generateTime(); // stub
	loan = new Loan(p, amount, time);
	transactions.add(new Transaction(loan, amount, newLoan, unresolved));
}

PayMyLoan(PersonAgent p, double amount){
	loan = loans.find(p);
	transactions.add(new Transaction(loan, amount, loanPayment, unresolved));
}


// BANK TELLER SCHEDULER

if there exists t in transactions such that t.status == unresolved {
	if there exists t in transactions such that t.type == deposit{
		Deposit(t);
		return true;
	}
	if there exists t in transactions such that t.type == withdrawal{
		Withdraw(t);
		return true;
	}
	if there exists t in transactions such that t.type == newAccount{
		NewAccount(t);
		return true;
	}
	if there exists t in transactions such that t.type == newLoan{
		CreateLoan(t);
		return true;
	}		
	if there exists t in transactions such that t.type == loanPayment{
		LoanPayBack(t);
		return true;
	}		
}

return false;


// BANK TELLER ACTIONS

Deposit(Transaction t){
	t.account.balance += t.amount;
	balance += t.amount;
	t.status = resolved;
	t.account.p.MoneySuccesfullyDeposited();
}

Withdraw(Transaction t){
	if (t.account >= t.amount){
		t.account.balance -= t.amount;
		balance -= t.amount;
		t.account.p.HereIsWithdrawal(amount);
	}
	else if (t.account > 0){
		t.account.balance -= t.amount;
		balance -= t.amount;
		t.account.p.HereIsPartialWithdrawal(amount);
	}
	else
		t.account.p.NoMoney();
	t.status = resolved;
}

NewAccount(Transaction t){
	t.account.balance += amount;
	balance += amount;
	accounts.add(t.account);
	t.status = resolved;
	t.account.p.AccountCreated();
}

CreateLoan(Transaction t){
	t.status = resolved;
	if HasGoodCredit(t.loan.p) && EnoughFunds(t.loan.balanceOwed){ //stub function to see if bank has enough funds
		loans.add(loan);
		t.loan.p.LoanCreated();
	}
	else if HasGoodCredit(t.loan.p) && !EnoughFunds(t.loan.balanceOwed){
		t.loan.p.CannotCreatLoan();
	}
	else // bad credit
		t.loan.p.CreditNotGoodEnough();
}

LoanPayBack(Transaction t){
	t.loan.balancePaid += t.amount;
	balance += t.amount;
	t.status = resolved;
	if (t.loan.balancePaid >= t.loan.balanceOwed){
		loan.s = paid;
		t.loan.p.YourLoanIsPaidOff();
	}
	else
		t.loan.p.YouStillOwe(t.loan.balanceOwed - t.loan.balancePaid, dayCreated - dayOwed)
}