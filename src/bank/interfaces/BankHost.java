package bank.interfaces;

import java.awt.image.ImageObserver;

import javax.swing.Icon;

import bank.BankCustomerRole;
import bank.TellerAgent;
import bank.gui.BankAnimationPanel;
import bank.gui.HostGui;

public interface BankHost {
	ImageObserver copyOfAnimPanel = null;

	public void IWantService(BankCustomerRole c);
		public void msgNewTeller(TellerAgent t);
		public void IAmFree(TellerAgent tell);
		public void msgIdLikeToGoOnBreak(TellerAgent t);
		public void msgIdLikeToGetOffBreak(TellerAgent t);
		public void WaitForAnimation();

		public void DoneWithAnimation();
		
		public abstract void setAnimPanel(BankAnimationPanel animationPanel);
		public String getName();
		public void setGui(HostGui g);
}
