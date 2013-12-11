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


#### How to run system
![City Map](http://i.imgur.com/Hx6EuxO.png)
  + Build from ant file, compile, run 'CityGui', and choose an option below
  + Double-click on the building to open the window
  + Exit the building window to go back to city
  + GUI controls for Bank, Market, Housing, Restaurant on left side only affect 'non AI' agents
  + Bus system is off by default (because some computers have some issues with it), you can turn it on by selecting 'Bus' under City Controls

### Notes
We've discussed with Professor Wilczynski about an issue with teammate work, so please take that into consideration for grading. We please ask you to contact him for the details. We are a four-person team creating a 6-person project with great emphasis on aesthetics and animation. We are down two people, one who dropped the course and one who submitted zero lines of code to the project and has not showing up to meetings. 

For V1, we were instructed to "include a simple market from one of your Restaurant deliveries, no animation" because of the no-contribution team member, but we were able to add a fully functioning market to our city, but we unfortunately ran out of time for full integration. 

For V2, the professor told us to use our discretion in deciding what parts of the project we can and can't do with our smaller group. 

Our no-contribution team member was assigned to work on:
+ Market (which we fully implemented two of, but not fully integrated)
+ Transportation (assigned to do with Daniel, who would take over the entire segment)
+ Person role and AI design 
(in addition to contributing to smaller details of the project)

Because we had to spend time working on these missing core features, we missed some of the minute details outlined on the rubric.

Additionally, one of our team members, Chris, was sick and then hospitalized during key integration period (he has talked with the professor about this). Despite being ill, Chris was able to make full contributions to the project, but we were unable to address some smaller details and integration requirements for the project due to further stressed time constraints and inability to meet as a full team. 

// NORMAN WRITE A NOTE HERE !!


##Normative Scenarios – Baseline, i.e., little or no interleaving

####A. Scenario: [Tests all the behaviors.]
1.  Press button 'A'
2.  North bank, Norman's restaurant, and both markets will be employed, one non-worker will go about his day.

####B. Scenario: [Tests all the behaviors.]
1.  Press button 'B'
2.  North bank, Norman's restaurant, X's restaurant, and both markets will be employed, three non-workers will go about their day.

####C. Scenario: [Tests cook, cashier, market interaction.]
  N/A: Team member responsible dropped course (see note above)

####D. Scenario: For large teams [Tests party behavior.]
  N/A: Small team

####E. Scenario: [Shows bus-stop behavior]
1.  Do XYZ

##Non-Normative Scenarios – Baseline, i.e., minimum interleaving

####F. Scenario: [Shows that people know they can't visit certain workplaces.]
1.  Press button 'F'. North bank, Norman's restaurant, and both markets will be employed.
For each building B:
1. Create a 'No AI' person, click 'Go' under category B. This person will go to B and interact.
2. Click 'Close B' button, workers will leave
3. Create another 'No AI' person, click 'Go' under category B. This person will not go to B because it is closed.
This scenario works for: banks, markets, and restaurants.
  
####G. Scenario: [Tests market behavior]
  N/A: Team member responsible dropped course

####H. Scenario: For large teams [Tests party behavior.]
  N/A: Small team

####I. Scenario: For large teams [Tests party behavior.]
  N/A: Small team

##Normative Scenarios – Normal operation, i.e., normal interleaving

####J. Scenario:

1.  Bank: 3 tellers, 1 host (x2)
2.  Restaurants: 1 host, 1 cashier, 1 cook, 3 waiters (x4)
3.  Markets: 3 tellers, 1 host (x2)
4.  Housing: 1 landlord, 3 repairman (x1)
5.  No Job: 6+
Total: 50+

People eat at home based upon their wealth class and inventory. People decide which building to visit based on distance.

##Non-Normative Scenarios

####O. Scenario: Bank Robbery
1.  Press button 'O', creates workers for 1 bank, and 1 robber
2.  After employees go to bank, robber will enter.
3.  Each worker has a 20% chance of having a gun on them, if they do the robbery will fail. Otherwise the robbery will succeed.
4.  The robber will try to rob a bank once a day, otherwise he will go about his day like a normal person (though he does not try to go to the bank to do 'normal' activities so that he is not recognized).
5.  Since the town has no police, robberies are a usual occurrence and so the tellers, hosts, and other customers are unfazed by a nearby robbery.

####P. Scenario: Vehicle accident





####Q. Scenario: Vehicle hits pedestrian





####R. Scenario: Weekend behavior is different
1.  Press button 'R', which fully employees the city and move the clock time to Saturday at the normal work shift.
2.  The banks are closed on weekends (and so bank workers go about their day-to-day business) and two restaurants are closed (Aleena's, Chris'). 

####S. Scenario: Job Changing/Firing/Shifts
  N/A: Team member responsible dropped course

##Rubric for Individual Grades

####T. Proper implementation of producer-consumer code
1.  Aleena: implementation for Restaurant, testing for RestaurantA
2.  Norman: implementation for RestaurantA, testing for RestaurantC
3.  Chris: implementation RestaurantC, testing for RestaurantC
4.  Daniel: implementation RestaurantD, testing for RestaurantD

#### What doesn't work
1.  We have a living system for apartments. We did not have time to add in housing (See notes)
2.  Landlords do not automatically collect rent. The housing system for this interaction was built correctly (by Chris), but it was not integrated in time for v2 (by team).
3.  Market is fully implemented, but employees are robots right now, working 24/7 (See notes above)
4.  RestaurantA (Aleena's restaurant) has animation issues that arose during integration.

#### Breakdown of work done
Norman
* Created source code gui and panels
* Helped modify source code for city semantics, two windows, and foundation for everyone to branch off and work on their own buildings and individual contributions
* Image importing, loading, displaying
* State-based image animations in Guis
* Gui movement system based on coordinates in all guis
* Worked extensively on PersonAgent methodology,looking at the designs given in class to create the role/person interactions, Role class, interfaces (even for other buildings, created the main interfaces so roles are able to be passed around the city), integrating them and creating a system for people to use and modify for specific tasks (eg, transportation, work systems, timer systems, etc).
* Helped with CityGui Layout, CityLists layouts/Java interfaces, the City Animation Panel
* Implemented Person Gui to move about
* Personal functioning restaurant with polished spriting and animations
* Animated city environment (Transparent Clouds!)
* Integrating others individual contributions into the city involving personAgent and roles (fixing/adding functionality to interfaces for building agents that enter a building, creating base role classes for all agents passed from person)
* Money retainment system from person to role
* Partial gui listener system using JObjects that trigger functions that trigger events, messages, and actions.
* Photoshop and parsing work for some sprites, background images, specialized tile sets, for transportation, restaurant animations, etc.
* How to click the buildings so that windows are displayed with what is inside in the city
* Created system to move windows around rather than closing to prevent gui from stopping the messaging
* Helped with spriting layout/themes/sprites for the city.
* Clicking people in the city opens up a new window with information about the person, closing this frame won't quit the program.
* Preventing unimportant frames from quitting the program
* System for people going in and out of work
* Setting buildings to open/closed based on available roles
* Figured out a system for people to go in and out of work, setting buildings to open or closed based on whether there are enough workers and the proper roles are 
* Added extra guis for every role in the restaurant, (some did not need gui before because people did not leave) and animated them
* People can now leave work given specific conditions for restaurant (eg cashier not having customers, and tables being empty) for work shifts
* Unit testing producer/consumer model in restaurant
* Unit tested people properly added into the city
* Unit tested basic roles added to person
* Lots of random bug fixes everywhere, always anticipated but usually not taken into credit
* Worked on government class that funds buildings with payment funds for their workers
* Salary system for people to get paid in all working restaurants
* Salary system for people to get paid in Banks
* System for workers to enter/leave restaurants
* Cleaned up Restaurant Gui/Panels
* Cleaned up Bank Guis/Panels
* Cleaned up Market Guis/Panels
* Added image labels to replace waiter panels in market/banks
* Gui Clean up, removing irrelevant buttons/panels and keeping the restaurant panel consitent with the customers
* producer/consumer for restaurant A
* A lot of the rubric case hard-coding along with everyone else
* Set up TestPerson for everyone to create classes and test their parts
* Testing Wilczynski has a heart attack
* Testing Going to Restaurant AI and MockRestaurant
* Testing Going to Bank AI and MockMank
* Testing Going to Market AI and MockBank
* Testing basic person addition
* Testing all restaurant working roles
* Testing all bank working roles
* NOTE: On github, I had two accounts for some reason (one that has a picture and one that doesn't, it created a second one and only recorded my work for the second one once I added an icon for my picture) and so my added lines and number of commits is split between them :(

Chris
  + Restaurant Design Doc
  + Housing: v1 has an apartment complex but no houses
  + Many people stay in the same complex in different rooms/beds with a common kitchen area
  + Issue tracker: created a new window that took each console output and created an alert and displayed it appropriately.  This involved working with each file and tailoring it to this new system.
  + Developed some restaurant-market interactions before we knew it wasn't going to be required of us.  This will be utilized in v2.
  + Unit testing for LandlordAgent in housing

Daniel 
  + PersonAgent Design Doc
  + Buses
  ++ Must be able to pick people up and take them to destinations
  + BusStops
  + + hold people as they wait for the bus to pick them up
  + TransportationCompany
  + + Handles all decision making for bus as in when it is at a stop pick people up and drop others off
  + + This will eventually also handle any collision interactions
  + PersonAgent decision in walking to locations based on start position and end position
  + + People should walk when they don't have a car or want to take bus, they should take a bus only when they want to but have no car and walking is done as a default. A random function will decide if a person is always going to take a bus or walk unless they have a car,
  + Payment system so that everyone gets paid at a certain time and only if working at that time. Payment will later on be based upon job role and pay according to how much they've worked.
  + Unit Testing BusStopAgent
  + Unit Testing BusAgent
  + the two unit tests are the main components used in transportation.

Aleena
  + PersonAgent AI design & implementation
  + Timers for eating, calling for repairs, going to sleep, going to work, leaving work
  + Housing, market, bank design document
  + Market, bank implementation
  + City GUI controls
  + Time management singleton
  + Clock class
  + Animation for bank, market
  + Integrating banks, markets
  + Inventory management
  + Shared data monitor in restaurant
  + In-building GUI clean-up
  + Bank unit testing
  + (some of) personAgent AI testing
  + Sprites for city