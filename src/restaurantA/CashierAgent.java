package restaurantA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import restaurantA.Check;
import restaurantA.Check.checkState;
import restaurantA.CookAgent.MyMenuItem;
import restaurantA.HostAgent.MyWaiter;
import restaurantA.interfaces.*;
import agent.Agent;

public class CashierAgent extends Agent implements Cashier {
	private String name;
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());

	private double savings;
	public CashierAgent(String name){
		super();
		this.name = name;
	}
	
	public class Bill{
		private int amount;
		private Market m;
		private Boolean paid;
		public Bill(Market m, int amount){
			this.setM(m);
			this.setAmount(amount);
			this.setPaid(false);
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public Boolean getPaid() {
			return paid;
		}
		public void setPaid(Boolean paid) {
			this.paid = paid;
		}
		public Market getM() {
			return m;
		}
		public void setM(Market m) {
			this.m = m;
		}
	}
	
	//* messages *//
	public void msgHereIsBill(Market m, int bill){
		print("Received bill of $" + bill + " from " + m.getName());
		bills.add(new Bill(m,bill));
		stateChanged();
	}
	public void msgRequestCheck(Waiter w, Customer c){
		createCheck(c,w);
		stateChanged();
	}
	public void msgHereIsMoney(Check check, double d){
		check.setS(checkState.paying);
		setSavings(getSavings() + d);
		check.setAmountPaid(d);
		check.setAmountChange(check.getAmountPaid() - check.getAmountOwed());
		check.setAmountOwed(0);
		checks.add(check);
		stateChanged();
	}
	
	public void msgICantPay(Check check , double money){
		check.setS(checkState.cantbePaid);
		check.setAmountPaid(money);
		check.setAmountOwed(check.getAmountOwed() - money);
		checks.add(check);
		savings += money;
		stateChanged();
	}

	// * scheduler *//
	public boolean pickAndExecuteAnAction() {
		synchronized(bills){
		for (Bill b : bills){
			if (!b.getPaid())
				PayBill(b);
		}
		}
		synchronized(checks){
		for (Check c: checks){
			if (c.getS() == checkState.calculated)
			{
				GiveCheck(c);
			}
			if (c.getS() == checkState.cantbePaid)
			{
				TellOffCustomer(c);
			}
			if (c.getS() == checkState.paying)
			{
				CheckIfNeedsChange(c);
			}
		}
		}
		return false;
	}
	
	//* actions *//
	private void PayBill(Bill b){
		setSavings(getSavings() - b.getAmount());
		print("Sent payment of $" + b.getAmount() + " to " + b.getM().getName());
		b.getM().msgHereIsPayment(b.getAmount());
		b.setPaid(true);
	}
	private void GiveCheck(Check c){
		print("Please pick-up the check for " + c.c.getCustomerName());
		c.setS(checkState.notPaid);
		c.w.msgPickUpCheck(c);
	}
	
	private void TellOffCustomer(Check c){
		print("You better pay next time.");
		c.setS(checkState.waiting);
		c.c.addMoneyAmountOwed(c.getAmountOwed());
		c.c.msgGetOut();
	}

	private void CheckIfNeedsChange(Check c){
		if (c.getAmountChange() > 0)
			GiveChange(c);
		else
			CheckGood(c);
	}
	
	private void CheckGood(Check c){
		c.setS(checkState.Paid);
		print("You are good to go");
		c.c.msgYouAreGoodToGo();
	}
	
	private void GiveChange(Check c){
		c.setS(checkState.needsChange);
		print("Here is your change: $" + c.getAmountChange());
		setSavings(getSavings() - c.getAmountChange());
		c.c.msgHereIsChange(c.getAmountChange());
		CheckGood(c);
		
	}
	
	//* helper functions *//
	public String getName() {
		return name;
	}
	
	private void createCheck(Customer c, Waiter w) {
		if (c.check == null){
		Check temp = new Check(c.choice, c.table, c);		
		temp.setS(checkState.calculated);
		temp.w = w;
		//c.check = temp; REMOVE THIS COMMENT
		checks.add(temp);
		}
	}
	public double getSavings() {
		return savings;
	}
	public void setSavings(double d) {
		this.savings = d;
	}


}
