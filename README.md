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
|  5  | Aleena Byrne                 | aleenaby@usc.edu      | @[aleenabyrne](https://github.com/aleenabyrne)     |   Team Leader  |

####Team Meetings
|       Meeting       |           Time           |      Location      |
| :------------------ | :----------------------- | :----------------- |
| Lab                 | Tue. 02:00pm             | SAL 126            |
| Weekly Meeting 1    | Sun. 01:00pm to 05:00pm  | Century            |
| Weekly Meeting 2    | Thu. 05:00pm to 10:00pm  | Century            |

#### Breakdown of work done
Norman
* Original owner of source code, including ovearching architecture of the gui/panel/animation panels
* Working building model used to branching into individual tasks
* Along with Aleena modified source code for city semantics, two windows, and foundation for everyone to branch off and work on their own buildings and individual contributions
* Researched how to import images to the program, how to run image/state based animations, how to properly load them, how to run a sequence given source of sprite files, move them a certain rate, switch them based on states (eg direction). This system is used everywhere in the city.
* Delta Gui movement system that is implemented in every building and in city layout, a comperehensive movement system for everyone to navigate their gui
* Worked extensively on PersonAgent methodology, especially early, looking at the designs given in class to create the role/person interactions, Role class, interfaces (even for other buildings, created the main interfaces so roles are able to be passed around the city), integrating them and creating a system for people to use and modify for specific tasks (eg, transportation, work systems, timer systems, etc).
* Organized CityGui Layouts, CityLists layouts/Java interfaces, the City Animation Panel, implemented Person Gui
* Personal functioning restaurant with polished spriting and animations, and integrated in to city
* City animated environment (Transparent clouds that move about the city independently)
* Integrating everyone's individual contribution into the city involving personAgent and roles (Which involves fixing and adding functionality to interfaces for each of the agents that enter a building, creating role classes for every agent that is passed into the city, particularly the "customers", copying and deleting individual code so that roles run with the right interfaces, created Restaurant, Apartment, Bank classes with their respective panels for people to keep working on their implementations and send their people into their buildings, a person/money retainment system to keep track of money/inventories as well as returning to the city)
* Gui listener system using JObjects (With Aleena) that trigger functions that trigger events, messages, and actions.
* Lots of photoshop and parsing work for sprites, background images, specialized tile sets, for transportation, restaurant animations.
* How to click the buildings so that windows are displayed with what is inside in the city view rather than creating a new building each time, and a way for GUI not to pause itself when moving out of the window.
* Found spriting layout/themes/sprites for the city.
* Clicking people in the city opens up a new window with information about the person, closing this frame won't quit the program. Also closing any frame that is not the city won't quit the program.
* Hand-merging branches
* Figuring out a system for people to go in and out of work, setting buildings to open or closed based on whether there are enough workers and the proper roles are created, and let people in and out based on that. Workers in the restaurant are able to go in at properly. Buildings also now don't limit gui movements because they are replaced by the city rather than minimized. Added Guis for every role in the restaurant, because some did not need gui before and now they do because sometimes the restaurant has no cashier. Added images and animation also for those (host, cashier).
* People can now leave work given specific conditions for restaurant (eg cashier not having customers, and tables being empty) for work shifts
* Unit testing producer/consumer model in restaurant
* Unit tested people properly added into the city
* Unit tested basic roles added to person
* A lot of my role involved me creating new content for people to use as a basis to branch off and add functionality.

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
  + Exit the building window to go back to city
  + GUI controls for Bank, Market, Housing, Restaurant on left side only affect 'non AI' agents
  + Bus system is off by default (because some windows computers have some issues with it), you can turn it on by selecting 'Bus' under City Controls
  + Ignore the 'in-building' GUI, we will remove for v2

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
  + Although AI is turned off, they will still wander the streets if they have nothing to do

#### What doesn't work
  + We have a living system for apartments. Housing will be in v2.
  + Landlords do not automatically collect rent. The housing system for this interaction was built correctly (by Chris), but it was not integrated in time for v1 (by team).
  + Employees are not paid yet.
  + Market is fully implemented. Market workers are static right now (no shifts, roles, read disclaimer)*
  + People go to sleep and instantly wake up. (integration issue)
  + The housing worker is static.

* We've discussed with Professor Wilczynski about an issue with teammate work, so please take that into consideration for grading. We please ask you to contact him for the details. We are a four-person team creating a 6-person project with great emphasis on aesthetics and animation. We are down two people, one who dropped the course and one who submitted zero lines of code to the project and has not showing up to meetings. We were instructed to "include a simple market from one of your Restaurant deliveries, no animation" because of the missing team member, but we were able to add a fully functioning market to our city, but we unfortunately ran out of time for full integration. Again we are a small team, but please take a look at our long, extended and committed history on github. It would be a nice bump of extra credit if you could give us credit for extra deliverables despite a 4-man team.