package bank.interfaces;

import java.awt.image.ImageObserver;
import java.util.List;

import javax.swing.Icon;


import logging.TrackerGui;

import bank.Bank;

import bank.BankCustomerRole;
import bank.gui.BankAnimationPanel;
import bank.gui.HostGui;

public interface BankHost {
	ImageObserver copyOfAnimPanel = null;

	public void IWantService(BankCustomerRole c);
		public abstract void msgNewTeller(Teller t);
		public abstract void IAmFree(Teller tell);
		public abstract void msgIdLikeToGoOnBreak(Teller t);
		public abstract void msgIdLikeToGetOffBreak(Teller t);
		public abstract void WaitForAnimation();

		public abstract void DoneWithAnimation();
		
		public abstract void setAnimPanel(BankAnimationPanel animationPanel);
		public abstract String getName();
		public abstract void setGui(HostGui g);

		public void setTellers(List<Teller> tellers);
		public void setBank(Bank b);

}
