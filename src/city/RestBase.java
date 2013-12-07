package city;

import roles.*;
public abstract class RestBase extends Building {

	public abstract boolean needsHost();

	public abstract void sethost();
	

	public abstract boolean needsCook();

	public abstract void setCook();
	

	public abstract boolean needsCashier();

	public abstract void setCashier();

	public abstract boolean needsWaiter();

	public abstract void setWaiter();
	
	

}
