###Team 05
####Team Information

  + Team Name: Victorious Secret
  + Team E-Mail: usc-csci201-fall2013-team05-l@mymaillists.usc.edu
  + Team Mentor: Daniel Bloznalis <bloznali@usc.edu>

####Resources

  + [Code Repository](https://github.com/usc-csci201-fall2013/team05)
  + [Documentation](https://github.com/usc-csci201-fall2013/team05/wiki)

####Team Members
| No. | Name (First (Nickname) Last) |       USC Email       |                GitHub Username                     |      Role      |
| :-: | :--------------------------- | :-------------------- | :--------------------------------------------      | :------------- |
|  1  | Norman Chootong              | chootong@usc.edu      | @[chootong](https://github.com/chootong)           |                |
|  2  | Chris Daniels                | chdaniel@usc.edu      | @[chrisdaniels9](https://github.com/chrisdaniels9) |                |
|  3  | Daniel Silva                 | danielis@usc.edu      | @[danielis](https://github.com/danielis)           |                |
|  4  | Victor Sherman               | vsherman@usc.edu      | @[vicss103](https://github.com/vicss)              |                |
|  5  | Aleena Byrne                 | aleenaby@usc.edu      | @[aleenabyrne](https://github.com/aleenabyrne)     |   Team Leader   |

####Team Meetings
|       Meeting       |           Time           |      Location      |
| :------------------ | :----------------------- | :----------------- |
| Lab                 | Tue. 02:00pm             | SAL 126            |
| Weekly Meeting 1    | Sun. 01:00pm to 05:00pm  | Century            |
| Weekly Meeting 2    | Thu. 05:00pm to 10:00pm  | Century            |

#### Breakdown of work done
 Norman
 + item here

 Chris
 + item here

 Daniel 
 + item here

 Aleena
 + item here

#### How To run system

![](http://i.imgur.com/bKOvnio.png)
+ Build from ant file, compile, run, and choose an option below
+ Double-click on the building to open the window
+ GUI controls for Bank, Market, Housing, Restaurant on left side only affect 'non AI' agents
+ Bus system is off by default (because some windows computers have some issues with it), you can turn it on by selecting 'Bus' under City Controls

##### Press "Scenario 1" button to run the normative scenario
+ This creates: 1 bank host, 3 bank tellers, 1 cashier, 1 cook, 1 restaurant host, 2 waiters, and many different types of jobless citizens.
+ Before 3AM, everyone will shop at the store, filling their inventories, or buying cars. 
+ After 3AM the workers will head to work. 
+ The jobless citizens then may go to the bank to open an account and deposit money, or go home to eat or sleep, or go to the restaurant. 
+ The work shift ends at 8PM, but workers finish up remaining jobs before leaving. 
+ The default action (personAgent scheduler returns false) is to wander around the streets).

##### Press "Turn off AI" to test individual component functions
+ Add neccesary workers for a building (bank: 1 host, 1 teller; restaurant: 1 cashier, 1 cook, 1 waiter, 1 host)
+ Press "Work" after adding each worker.
+ Add a person with "None" for job. 
+ Use the GUI controls listed under Bank, Market, Housing, City (selection drop down options, input quantities, etc. Select 'Go')
(although AI is turned off, they will still wander the streets if they have nothing to do)

#### What doesn't work
+ Landlords do not automatically collect rent. The housing system for this interaction was built correctly (by Chris), but it was not integrated in time for v1 (by team).
+ Employees are not paid (?)
+ Market was half integrated: customers can enter and leave, but employees work entire time.
+ People go to sleep and instantly wake up. (integration issue)
+ Housing workers always work, do not yet follow work shifts.
+ When restaurant workers leave, their GUI is not removed.