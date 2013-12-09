package restaurantA;
 
import java.util.Vector;

import restaurantA.interfaces.Cook;
import restaurantA.interfaces.Customer;
import restaurantA.interfaces.Waiter;

 
public class ProducerConsumerMonitor extends Object {
    private final int N = 15;
    private int count = 0;
    private Vector theData;
    
    synchronized public void insert(Ticket data) {
        while (count == N) {
            try{ 
                System.out.println("\tFull, waiting");
                wait(5000);                         // Full, wait to add
            } catch (InterruptedException ex) {};
        }
            
        insert_item(data);
        count++;
        if(count == 1) {
            System.out.println("\tNot Empty, notify");
          //  data.c.notify();                               // Not empty, notify a 
            data.c.msgNotEmpty();                                        // waiting consumer
        }
    }
    
    synchronized public Ticket remove() {
    	Ticket data;
        while(count == 0)
            try{ 
                System.out.println("\tEmpty, waiting");
                wait(5000);                         // Empty, wait to consume
            } catch (InterruptedException ex) {};
 
        data = remove_item();
        count--;
        if(count == N-1){ 
            System.out.println("\tNot full, notify");
           // data.w.notify();                               // Not full, notify a 
                                                    // waiting producer
        }
        return data;
    }
    
    private void insert_item(Ticket data){
        theData.addElement(data);
    }
    
    private Ticket remove_item(){
    	Ticket data = (Ticket) theData.firstElement();
        theData.removeElementAt(0);
        return data;
    }
    
    public ProducerConsumerMonitor(){
        theData = new Vector();
    }
    
    public int getCount(){
    	return count;
    }
    
    
    public class Ticket
    {
    	public Waiter w;
    	public String choice;
    	public Cook c;
    	public Customer cu;
    	public Table table;
    	
    	//Constructor
    	public Ticket(Waiter waiter, Cook cook, String newChoice, Table newTable, Customer cust)
    	{
    		w = waiter;
    		this.c = cook;
    		this.cu = cust;
    		choice = newChoice;
    		table = newTable;
    	}
    	
    	//Class Methods
    	Waiter getWaiter()
    	{
    		return w;
    	}
    	
    	String getChoice()
    	{
    		return choice;
    	}
    	
    	Table getTable(){
    		return table;
    	}
    	
    
    }
}

