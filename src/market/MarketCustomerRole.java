package market;

import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import market.gui.MarketAnimationPanel;
import market.gui.MarketCustomerGui;
import market.interfaces.*;
import agent.Agent;
import bank.BankCustomerRole.customerPurpose;

import java.util.*;
import java.util.concurrent.Semaphore;

import roles.Role;


//Customer Agent
//It still is a finite state machine, instead of events it still uses the state enum.
//I used this design from the designs drawn from class.

public class MarketCustomerRole extends Role implements MarketCustomer {

	//To show icon
	public enum iconState
	{
		none, question, steak, chicken, salad, pizza,
	};



	//VARIABLES*************************************************
	MarketHost h;
	marketCustomerState state;
	public MarketAnimationPanel copyOfAnimPanel; // for gui
	MarketWorker t;
	double balance = 0;
	public String job = "None";
	private MarketCustomerGui customerGui;
	public Semaphore animSemaphore = new Semaphore(0, true);
	public String name;
	Timer timer = new Timer();

	Boolean isHappy = true;

	String item;
	int quantityWanted = 0;
	int quantityReceived = 0; 
	double amountOwed;
	public TrackerGui trackingWindow;


	//Constructor
	public MarketCustomerRole(String name, MarketHost h){
		super();
		this.name = name;
		this.h = h;
		state = marketCustomerState.outside;
	}

	public MarketCustomerRole(String name, String item, double marketQuantity, double money, String j) {
		super();
		this.name = name;
		job = j;
		this.item = item;
		quantityWanted = (int) marketQuantity;
		balance = money;
		state = marketCustomerState.outside;
	}

	public void msgGetPaid(){
		//balance =+50;
	}


	//UTILITIES**************************************************


	public void setTrackerGui(TrackerGui t) {
		trackingWindow = t;
	}

	private Boolean enoughBalance(){
		if (amountOwed > balance){
			print("I just realized I do not have enough money");
			state = marketCustomerState.done;
			stateChanged();
			return false;
		}
		else{
			balance -= amountOwed;
			return true;
		}
	}

	//CLASSES/ENUMS**********************************************


	enum marketCustomerState{outside, entered, waiting, assigned, atCounter, canPay, needsPay, done, exited};


	//MESSAGES*************************************************

	public void MarketIsClosed(){
		state = marketCustomerState.done;
		//customerGui.setSpeechBubble("oksadcust");
		stateChanged();
	}

	public void msgWantsToBuy(){ //called from gui
		state = marketCustomerState.outside;
		stateChanged();
	}

	public void	WantsToDo(String visitPurpose, int quantity){ //called from Person agent
		//purpose = convert(visitPurpose);
		this.state = marketCustomerState.entered;
		stateChanged();
	}

	public void	GoToTeller(MarketWorker t){
		//print("received teller info");
		this.t = t;
		state = marketCustomerState.assigned;
		stateChanged();
	}

	public void OutOfStock(){
		state = marketCustomerState.done;
		isHappy = false;
		stateChanged();
	}

	public void YouOwe(double amount){
		if (balance >= amount){
			amountOwed = amount;
			state = marketCustomerState.canPay;
		}
		else{
			print("I can't afford...");
			isHappy = false;
			state = marketCustomerState.done;
		}
		stateChanged();
	}

	public void PleasePay(double amount){
		amountOwed = amount;
		state = marketCustomerState.needsPay;
		stateChanged();
	}

	public void HereIsOrder(String i, int q){
		quantityReceived = q;
		state = marketCustomerState.done;
		stateChanged();
	}



	//SCHEDULER*************************************************
	public boolean pickAndExecuteAnAction() 
	{
		//print("reached sched, state: " + state);
		if (state == marketCustomerState.outside){
			GoToMarket();
			return true;
		}
		if (state == marketCustomerState.entered){
			TellHost();
			return true;
		}

		if (state == marketCustomerState.assigned){
			WalkToTeller();
			return true;
		}

		if (state == marketCustomerState.atCounter){
			AskForAssistance();
			return true;
		}

		if (state == marketCustomerState.canPay){
			PriceGood();
			return true;
		}

		if (state == marketCustomerState.needsPay){
			GivePayment();
			return true;
		}

		if (state == marketCustomerState.done){
			SayThanks();
			return true;
		}


		return false;
	}

	//ACTIONS*************************************************

	private void GoToMarket() {
		print("Going to market");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "Going to market", new Date()));
		getCustomerGui().DoGoToWaitingRoom();
		state = marketCustomerState.entered;
	}

	private void TellHost(){
		// DoEnterBank();
		print("I want service");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "I want service", new Date()));
		state = marketCustomerState.waiting;
		h.IWantService(this);
	}

	private void AskForAssistance(){
		state = marketCustomerState.waiting;
		
		if (item == "Steak")
			customerGui.setSpeechBubble("buysteak");
		if (item == "Chicken")
			customerGui.setSpeechBubble("buychicken");
		if (item == "Pizza")
			customerGui.setSpeechBubble("buypizza");
		if (item == "Salad")
			customerGui.setSpeechBubble("buysalad");
		if (item == "Car")
			customerGui.setSpeechBubble("buycar");
		if (item == "Juice")
			customerGui.setSpeechBubble("buyjuice");
		if (item == "Eggs")
			customerGui.setSpeechBubble("buyeggs");
		if (item == "Milk")
			customerGui.setSpeechBubble("buymilk");
	    
		timer.schedule( new TimerTask()
		{
			public void run()
			{				
				GiveRequest();
			}
		}, 4000);
	}

	private void GiveRequest(){
		print("I want to order " + quantityWanted + " of " + item);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "I want to order " + quantityWanted + " of " + item, new Date()));
		state = marketCustomerState.waiting;
		t.GiveOrder(this, item, quantityWanted);
	}

	private void PriceGood(){
		print("That price is good!");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "That price is good!", new Date()));
		state = marketCustomerState.waiting;
		t.PleaseFulfill(this);
	}

	private void GivePayment(){
		print("Here is payment of $" + amountOwed);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "Here is payment of $" + amountOwed, new Date()));

		balance -= amountOwed;
		state = marketCustomerState.waiting;
		t.GivePayment(this, amountOwed);
	}

	private void SayThanks(){
		state = marketCustomerState.exited;
		if (isHappy)
			customerGui.setSpeechBubble("thnxcust");
		else
			customerGui.setSpeechBubble("oksadcust");

		timer.schedule( new TimerTask()
		{
			public void run()
			{				
				LeaveMarket();
			}
		}, 2000);

	}

	private void LeaveMarket(){
		print("Thank you. I now have $" + balance);
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "Thank you. I now have $" + balance, new Date()));
		if (t != null){
			t.IAmLeaving();
			if (t.getTableNum() == 1){
				getCustomerGui().DoGoToWaitingRoom();
			}
			else if (t.getTableNum() == 2){
				getCustomerGui().DoGoToTopMiddle();
				getCustomerGui().DoGoToTopLeft();
				getCustomerGui().DoGoToBotLeft();
				getCustomerGui().DoGoToWaitingRoom();
			}
			else if (t.getTableNum() == 3){
				getCustomerGui().DoGoToTopRight();
				getCustomerGui().DoGoToBotRight();
				getCustomerGui().DoGoToWaitingRoom();
			}
		}
		getCustomerGui().DoExitRestaurant();
		state = marketCustomerState.exited;  
		this.myPerson.msgLeavingMarket(this, balance, item, quantityReceived);
		customerGui.finishedTransaction();
	}




	private void WalkToTeller() 
	{
		print("Directed to teller.");
		trackingWindow.tracker.alertOccurred(new Alert(AlertLevel.INFO, AlertTag.MARKET, "MarketCustomerRole", "Directed to teller.", new Date()));
		if (t.getTableNum() == 1){
			getCustomerGui().DoGoToSeat(t.getTableNum());
		}
		else if (t.getTableNum() == 2){
			getCustomerGui().DoGoToBotLeft();
			getCustomerGui().DoGoToTopLeft();
			getCustomerGui().DoGoToTopMiddle();
			getCustomerGui().DoGoToSeat(t.getTableNum());
		}
		else if (t.getTableNum() == 3){
			getCustomerGui().DoGoToBotRight();
			getCustomerGui().DoGoToTopRight();
			getCustomerGui().DoGoToSeat(t.getTableNum());
		}
		state = marketCustomerState.atCounter;
		//stateChanged();
	}




	//UTILITIES*************************************************



	public String toString() {
		return name;
	}

	public void setGui(MarketCustomerGui g) {
		setCustomerGui(g);
	}

	public MarketCustomerGui getGui() {
		return getCustomerGui();
	}

	public void WaitForAnimation()
	{
		try
		{
			this.animSemaphore.acquire();	
		} catch (InterruptedException e) {
			// no action - expected when stopping or when deadline changed
		} catch (Exception e) {
			print("Unexpected exception caught in Agent thread:", e);
		}
	}

	public void DoneWithAnimation()
	{
		this.animSemaphore.release();
	}

	public void setAnimPanel(MarketAnimationPanel panel)
	{
		copyOfAnimPanel = panel;
	}

	public MarketCustomerGui getCustomerGui() {
		return customerGui;
	}

	public void setCustomerGui(MarketCustomerGui customerGui) {
		this.customerGui = customerGui;
	}

	public String getName(){
		return name;
	}

	@Override
	public void setHost(MarketHostAgent host) {
		this.h = host;
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	}


