package roles;

import agent.StringUtil;
import city.PersonAgent;

public class Role
{
	PersonAgent myPerson;
	Boolean active;
	
	//Utilities for Role
	public void setPerson(PersonAgent a)
	{
		myPerson = a;
	}
	
	public PersonAgent getPersonAgent()
	{
		return myPerson;
	}
	
	private void stateChanged()
	{
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
}
