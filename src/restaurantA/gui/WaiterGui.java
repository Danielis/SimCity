package restaurantA.gui;


import restaurantA.Table;
import restaurantA.WaiterAgent;
import restaurantA.CustomerAgent.AgentEvent;
import restaurantA.interfaces.Customer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
public class WaiterGui implements Gui {


    private WaiterAgent agent = null;
    private int xOrigin= 20, yOrigin = 0;
    public int xHome, yHome;
    private int xPos = xOrigin, yPos = yOrigin;//default waiter position orignally -20
    private int xDestination = xOrigin, yDestination = yOrigin;//default start position originally -20
    private int xCook = 380, yCook = 50;
    private boolean foodOn = false;
    private JLabel[][] buttons;
    private String foodDeliver = null;
    private boolean isOnBreak = false;
    public ArrayList<Table> tables;
    Timer timer;
    
    public WaiterGui(WaiterAgent agent, ArrayList<Table> tables, int xHome, int yHome) {
        this.agent = agent;
    	this.tables = tables;    	
    	buttons   =   new JLabel[0][0];
    	this.xHome = xHome;
    	this.yHome = yHome;
    	//System.out.println("test" + xHome + " " + yHome);
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        //System.out.println(xPos + " " + yPos);
        //System.out.println(xDestination + " " + yDestination);
       // System.out.println(tables);
        
        if (xPos == xCook && yPos == yCook){
        	agent.msgAtCook();
       
        }
        
        else if ((xPos == xDestination && yPos == yDestination) && (xPos != xOrigin && yPos != yOrigin) && (tables != null)) {
     
        	agent.msgAtTable();
        	foodOn = false;
//        	for (Table t : tables){
//        		System.out.println(xPos +  " " + t.getxPos() + " " + yPos + " " + t.getyPos());
//        		if ((xPos == t.getxPos() + 20 ) & (t.getyPos() == yPos - 20 )){
//        		agent.msgAtTable();
//        		System.out.println("***********msgAtTable() called");
//        		DoLeaveCustomer(); //HACKKkkykyyy 4am hack ;_;
//        		}
//        	}
        }
        
        if (xPos == xOrigin && yPos == yOrigin) {
        	agent.msgAtOrigin();
        	
   
        }
      
    }

  

	public void draw(Graphics2D g) {

        	g.setColor(Color.MAGENTA);
            g.fillRect(xPos, yPos, 20, 20);
  
            if (foodOn){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(xPos, yPos+20, 10, 10);
            g.drawString(foodDeliver, xPos, yPos+20);
            }
            
            
       
     
    }


    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(Customer customer, Table table) {
        xDestination = table.getxPos() + 20;
        yDestination = table.getyPos() - 20;
    }
    
    public void DoDeliverOrder(Customer customer, Table table, String choice) {
        xDestination = table.getxPos() + 20;
        yDestination = table.getyPos() - 20;
        foodOn = true;
        
        if (choice.length() > 3)
        foodDeliver = choice.substring(0,3);
        else
        foodDeliver = choice;
       // buttons[xPos][yPos+50].setText("test");
    }
    
    public void DoSendOrder() {
        xDestination = xCook;
        yDestination = yCook;
    }

    public void DoReturnOrigin() {
        xDestination = xOrigin;
        yDestination = yOrigin;
    }
    

  
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public boolean isOnBreak(){
        	return isOnBreak;
        }
        
        public void WantsBreak(){
        	agent.host.msgIWantBreak(agent);
        }
        
        public void SetBreak(){
        	isOnBreak = true;
        }
        
        public void SetOffBreak(){
        	isOnBreak = false;
        	agent.host.msgDoneWithBreak(agent);
        }
        
        private void enjoyBreak() {
        	timer.schedule(new TimerTask() {
				Object cookie = 1;
				public void run() {
					
					SetOffBreak();
				}
			},
			5000);
    	}
     
    	 public void DoReturnHome(){
    		  xDestination = xHome;
    	      yDestination = yHome; 
    	 }
    
    public boolean atOrigin(){
    	return (xPos == xOrigin && yPos == yOrigin);
    }
    public boolean atHome(){
    	return (xPos == xHome && yPos == yHome);
    }
}
