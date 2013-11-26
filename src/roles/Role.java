package roles;

import agent.StringUtil;
import city.PersonAgent;

public abstract class Role //why doesnt this extend agent?
{
	public PersonAgent myPerson;
	public Boolean active;
	
	public void startThread()
	{
		myPerson.startThread();
	}
	
	public void pauseAgent()
	{
		myPerson.pauseAgent();
	}
	
	public void resumeAgent()
	{
		myPerson.resumeAgent();
	}
	
	//Utilities for Role
	public void setPerson(PersonAgent a)
	{
		myPerson = a;
	}
	
	public PersonAgent getPersonAgent()
	{
		return myPerson;
	}
	
	protected void stateChanged()
	{
		//System.out.println(myPerson);
		myPerson.stateChanged();
	}
	
	public void setActivity(Boolean b)
	{
		active = b;
	}

	public Boolean getActivity()
	{
		return active;
	}
	
	public String getName()
	{
		return myPerson.getName();
		//return "test";
	}
	
    protected void print(String msg) {
        print(msg, null);
    }

    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }

	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	public abstract void msgLeaveWork();

	

}
