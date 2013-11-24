package market;

import market.gui.MarketAnimationPanel;
import market.gui.MarketCustomerGui;
import market.interfaces.*;
import agent.Agent;

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
	double balance = 1000;
	
	private MarketCustomerGui customerGui;
	public Semaphore animSemaphore = new Semaphore(0, true);
	public String name;
	Timer timer = new Timer();
	
	Boolean isHappy = true;
	
	String item;
	int quantityWanted;
	int quantityReceived; 
	double amountOwed;
	
	
	//Constructor
	public MarketCustomerRole(String name, MarketHost h){
		super();
		this.name = name;
		this.h = h;
		state = marketCustomerState.outside;
	}
	
	public MarketCustomerRole(String name, String string, int i, double money) {
		super();
		this.name = name;
		state = marketCustomerState.outside;
	}
//UTILITIES**************************************************

	

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
	
public void msgWantsToBuy(String type, int temp){ //called from gui
		item = type;
		state = marketCustomerState.outside;
		quantityWanted = temp;
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
		//print("reached sched");
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
		getCustomerGui().DoGoToWaitingRoom();
		state = marketCustomerState.entered;
}
	
private void TellHost(){
	   // DoEnterBank();
		print("I want service");
	    state = marketCustomerState.waiting;
	    h.IWantService(this);
}

private void AskForAssistance(){
	state = marketCustomerState.waiting;
	
	timer.schedule( new TimerTask()
	{
		public void run()
		{				
			GiveRequest();
		}
	}, 4000);
}

private void GiveRequest(){
	print("I want to order this...");
	state = marketCustomerState.waiting;
	t.GiveOrder(this, item, quantityWanted);
}

private void PriceGood(){
	print("That price is good!");
	state = marketCustomerState.waiting;
	t.PleaseFulfill(this);
}

private void GivePayment(){
	print("Here is payment of $" + amountOwed);
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
	    
	    getCustomerGui().finishedTransaction();
}
	
	
	
	
	private void WalkToTeller() 
	{
		print("Directed to teller.");
		
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

	
}


