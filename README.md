##Restaurant Project Repository

###Student Information
  + Name: Norman Chootong
  + USC Email: chootong@usc.edu
  + USC ID: 1734669092
  + Lecture: MW12pm
  + Lab: T2pm
  + Version: v2.2

##Information: Basic How To Run The Program
  + IMPORTANT: Images may not load depending on the machine environment.
    What I first recommend is moving the images from src/resources
    to src if images do not load. There may also be a seconadary issue:
    Sarah Wang (TA) noted that images may be loaded
    differently depending on the server where the server that
    is running the program: "I think Windows uses \ while Linux
    uses / for paths." If it does not work, you can try using / 
    or \ for paths. I've committed using the right folder 
    configuration so the structure should be correct, but the main
    issue may differ in a different machine. I've tried 3 machines
    and they work on all 3, but that sample size is small.
    Please don't take points off for this if it happens, it seems 
    out of my hands whether the environment alters the image loading.
  + To run the program, add a waiter and a customer. The order 
    does not matter. The Markets/Cashier/Host/Cook are already 
    intantiated.
  +  The customer is added by writing the customer name in the 
    "Enter Customer here" field, checking the checkbox, and 
    clicking the add button; For the waiter, just simply create
    it and the buttons for the breaks will appear below. Select
    that agent from the list, and below in the information 
    panels, you can check the checkboxes them if you have not
    done so.
  + The program will run with as many customers and waiters as 
    you wish. If you are looking at a particular customer, if 
    you want to set that customer hungry once more, if it is 
    the last customer you added, simply click refresh and the 
    hungry checkbox will be available once that customer 
    leaves. You may also select that customer again from the 
    list or a new one that you wish to re-set to hungry. This
    only works if the customer has left the restaurant.
  + To pause, there is a pause button below the information 
    panels.
  + You can look at eclipse to see print traces of all 
    ongoing/received messages, animation calls, as well as who
    the caller is, and what the restaurant is doing. I have 
    done this extensively so you can follow the process in 
    synchronization with the animation.
  + Notes on how to run scenarios are at the button.

##Notes
  + Everything works and has been implemented. There are no 
    known errors. I have looked at rubric to doublecheck all 
    requirements. All possible scenarios run fine. I ran them
    multiple times.
  + IMPORTANT: If you want to set a customer hungry again you 
    have to find and click that customer in the list of  
    customers, or click the refresh button. The gui needs 
    some sort of trigger. If the gui looks weird, you just drag 
    the window around so it refreshes itself.
  + I did enough printouts to the System.Out so that you can 
    follow along what is going on with the code as the animation 
    runs. There you can see what messages are being sent, who 
    receives them and when, when they decide to do those actions 
    and when an animation is complete or ongoing.

##Scenarios: Market/Cashier payments
   1 (One order, fulfilled by the market, bill paid in full)
   + Create waiter, Create customer. Cook will order, Cashier pays market.
   2 (One order, fulfilled by TWO markets, 2 bills paid in full)
   + Press the "Run Out" Button, so Gordon Ramsey throws a fit and
     throws all inventory away. All you have to do is create a customer
     and a waiter and the cook will order a bunch of things from the market
     and you can see the cashier fulfillping payments for multiple orders at
     once. The customer will see there is no food in the restaurant and leave.
   + Markets 1 and 2 will have only 1 item so you can see the multiple order
     scenarios. Market 3 has 100 items for each so you can play around if you
     want.
   + If you look at the printouts the cashier pays the order from
     multiple markets.
   3 (One order, fulfilled by the market, bill unable to be paid) Yay EC.
   + When you press the "Collection Day!" button, the Manager Mr. Krabs
     comes to the restaurant and takes all the cashier's money because
     he's just really greedy. Once you click it, you will see a message
     in the system output and the cashier will have no money.
   + Press the "Run Out" button so that there is no inventory.
   + The cashier will keep a note of how much he owes each market, and will
     compute a bill that adds that owed amount each time so the cashier
     pays the market. Normally the cashier gains revenue from the 
     customers as they leave the restaurant and loses it when markets fulfill
     orders. I handled the scenario by letting the cashier get customer 
     payments as they enter and get back some money, while stacking bills for
     orders it can't pay for markets.
##Scenarios: Unit Testing
   + Run the CashierTest.java file.
   + There's 7 tests, the comment before the test explains what it is testing
     and they test normative, complex, as well as interleaving scenarios.

##Scenarios: Animation Upgrades
   + You'll find all the upgrades there. Only the waiters require separate
     areas so I only did that for waiters. The customers still come on
     screen and get taken by waiters. The cook has a cooking/plating/fridge
     area (yay extra credit) and places the orders on the table as images.
     If you want to test the waiter positions just create a bunch. You 
     never really need more than 4 waiters though.