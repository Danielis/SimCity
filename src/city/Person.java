package city;

//Package Imports
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import restaurant.CustomerAgent;
import restaurant.Restaurant;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantAnimationPanel;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import restaurant.roles.*;
import agent.Agent;
import bank.BankCustomerRole;
import restaurant.gui.CustomerGui;
import city.PersonAgent.Item;
import city.PersonAgent.Job;
import city.PersonAgent.JobType;
import city.PersonAgent.PersonStatus;
import city.PersonAgent.WealthLevel;
import city.PersonAgent.bankStatus;
import city.PersonAgent.destination;
import city.PersonAgent.houseStatus;
import city.PersonAgent.location;
import city.PersonAgent.marketStatus;
import city.PersonAgent.nourishment;
import city.PersonAgent.workStatus;
import city.TimeManager.Day;
import city.guis.CityAnimationPanel;
import city.guis.PersonGui;
import bank.Bank;
import bank.Bank.Account;
import bank.Bank.Loan;
import bank.Bank.loanState;
import bank.BankHostRole;
import bank.interfaces.*;
import roles.Apartment;
import roles.Building;
import roles.Building.buildingType;
import roles.Role;
import logging.Alert;
import logging.AlertLevel;
import logging.AlertTag;
import logging.TrackerGui;
import market.*;
import market.interfaces.MarketCustomer;
import city.guis.PersonGui.Coordinate; //trans: added for trans
import transportation.BusStopAgent; // needed for BusStop variable
import housing.HousingCustomerRole;
import housing.interfaces.HousingCustomer;
import bank.*;
import transportation.TransportationCompanyAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

public interface Person
{
/*****************************************************************************
		Messages
******************************************************************************/
//	public void msgWakeUp() ;
//	
//	public void msgGetPaid() ;
//	
//	public void msgLeaveWork() ;
//
//	public void msgLeftWork(Role r, double balance) ;
//	
//	public void msgLeavingHome(Role r, double balance);
//
//	public void msgGoToWork() ;
//	
//	//Housing
//	public void msgGoToHome(String purpose);
//
//	//Restaurant
//	public void msgGoToRestaurant();
//
//	public void msgLeavingRestaurant(Role r, float myMoney);
//
//	public void msgGoToBank(String purpose, double amt);
//
//	public void msgLeavingBank(BankCustomerRole r, double balance);
//		
//	public void msgNewAccount(BankCustomerRole bankCustomerRole, Account acct);
//	
//	public void msgNewLoan(BankCustomerRole bankCustomerRole, Loan loan) ;
//		
//	public void msgAtBusStop();
//
//	
////Scheduler
//	public boolean pickAndExecuteAnAction() ;
//		
			
	
	
	
	public List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	public Vector<Building> buildings = new Vector<Building>(); //City Gui won't let me implement Lists
	public List<Item> inventory = Collections.synchronizedList(new ArrayList<Item>());
	public double setWealth();
	public double getCashThresholdUp();	
	public double getCashThresholdLow();
	public double getMoneyThreshold();
	public WealthLevel parseWealth(String wealth);
	public JobType parseJob(String job);	
	public void setThreshold(int q);
	public String getType();
	public int getQuantity();
	public void removeItem();
	public void setTrackerGui(TrackerGui t);
	public List<Item> getInvetory();
	public int getBound_leftx();
	public int getBound_rightx();
	public int getBound_topy();
	public int getBound_boty();
	public void addRole(Role r);
	public void removeRole(Role r);
	public String getName();
	public void setGui(PersonGui g);
	public void setAnimationPanel(CityAnimationPanel panel);
	public PersonGui getGui();
	public boolean hasCar();
	public void WaitForAnimation();
	public void DoneWithAnimation();
	public void WaitForBus();
	public void DoneWithBus();
	public void setMetro(TransportationCompanyAgent m);
	public BusStopAgent closestBusStop();
	public double checkBusStopDistance(BusStopAgent x);
	public void closestCheckpoint();
	public double checkPointDistance(int x, int y);
	public void transportationStatusBus();
	public void msgWakeUp();
	public void msgGetPaid();
	public void msgLeaveWork();
	public void msgLeftWork(Role r, double balance);	
	public void msgLeavingHome(Role r, double balance);
	public void msgGoToWork();
	public void msgGoToHome(String purpose);
	public void msgGoToRestaurant();
	public void msgLeavingRestaurant(Role r, float myMoney);
	public void msgAtBusStop();
	public void setDestinationStop(BusStopAgent P);
	public BusStopAgent getDestinationBusStop();
	public void setPosition(int X, int Y);
	public boolean pickAndExecuteAnAction();	
	public void GoToSleep();
	public boolean CheckRestOpen();
	public void GoEat();
	public boolean isHungry();
	public boolean isTired();
	public void WalkAimlessly();
	public boolean noRoleActive();
	public void DelayGoToWork();
	public boolean needsToBuy();
	public boolean canAfford(Item i);
	public double getTotalMoney();
	public void GoToWork();
	public void WorkAtRest();
	public void WorkAtBank();
	public void GoToRestaurant();
	public void GoToMarket();
	public void setBuildings(Vector<Building> buildings);
	public void setPersonList(Vector<PersonAgent> people);
	public void setAI(Boolean noAI);
}
