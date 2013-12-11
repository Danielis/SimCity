package city.test;

import junit.framework.TestCase;
import logging.TrackerGui;
import restaurant.Restaurant;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Waiter;
import restaurant.roles.HostRole;
import restaurant.roles.TraditionalWaiterRole;
import roles.Role;
import city.PersonAgent;
import city.guis.CityGui;
import city.test.mock.TestPerson;
import city.test.mock.TestPerson.JobType;
import city.test.mock.TestPerson.ProfessorState;
import city.test.mock.TestPerson.StudentState;
import city.test.mock.TestPerson.WealthLevel;
import city.test.mock.TestPerson.destination;
import city.test.mock.TestPerson.location;
import city.test.mock.TestPerson.workStatus;

public class WilczynskiHasHeartAttack extends TestCase{

	TestPerson person;
	CityGui gui;
	TrackerGui trackerWindow;
	Restaurant r;

	public void setUp() throws Exception{
		super.setUp();
	}
	
    public void test1_PersonIsCreatedProperly() throws Exception
    {
    	System.out.println("TEST 1");
		person = new TestPerson("TestPerson", "Waiter", "Average");
		
		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a waiter", person.job.type == JobType.waiter);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be TestPerson", person.getName() == "TestPerson");
    }
    
    public void test2_ProfessorCreatedProperly() throws Exception
    {
    	System.out.println("TEST 2");
		person = new TestPerson("Wilczynski", "Professor", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a professor", person.job.type == JobType.professor);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Wilczynski", person.getName() == "Wilczynski");
		
		person.msgHaveHeartAttack();
		
		assertTrue("Professor should get cue to have heart attack", person.professorState == ProfessorState.needsHeartAttack);
		assertTrue("The person should be running an action in the scheduler", person.pickAndExecuteAnAction());
		assertTrue("The professor should have changed its state", person.professorState != ProfessorState.needsHeartAttack);
		assertTrue("The professor should have changed its state", person.professorState == ProfessorState.isHavingHeartAttack);
    }
    
    public void test3_StudentsCreatedProperly() throws Exception
    {
    	System.out.println("TEST 3");
		person = new TestPerson("Student", "Student", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a professor", person.job.type == JobType.student);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Student", person.getName() == "Student");
		
		person.msgGoToWilczynski();

		assertTrue("The person should be outside", person.Status.loc == location.outside);
		assertTrue("The person should be headed towards Wilczynski", person.Status.des == destination.Wilczynski);
		assertTrue("The person should be running an action in the scheduler", person.pickAndExecuteAnAction());
		
		assertTrue(person.Status.loc == location.work);
		assertTrue(person.Status.work == workStatus.working);
    }
    
    public void test4_StudentsGoBotherCPs() throws Exception
    {
    	System.out.println("TEST 4");
		person = new TestPerson("Student", "Student", "Average");

		assertTrue("Person should have a job.", person.job != null);
		assertTrue("The person's job type should be a professor", person.job.type == JobType.student);
		assertTrue("Person should have a wealth type", person.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Student", person.getName() == "Student");
		
		person.msgGoBotherTheCPs();

		assertTrue("The person's student state should be triggered to leave", person.studentState == StudentState.needsToLeave);
		assertTrue("The person should be running an action in the scheduler", person.pickAndExecuteAnAction());
		assertTrue("The person's state should now be triggered to leaving", person.studentState == StudentState.leaving);
    }
    
    public void test5_StudentInterleave() throws Exception
    {
    	System.out.println("TEST 5");
		TestPerson student = new TestPerson("Student", "Student", "Average");
		TestPerson professor = new TestPerson("Wilczynski", "Professor", "Average");

		assertTrue("Person should have a job.", student.job != null);
		assertTrue("The person's job type should be a professor", student.job.type == JobType.student);
		assertTrue("Person should have a wealth type", student.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Student", student.getName() == "Student");
		assertTrue("Person should have a job.", professor.job != null);
		assertTrue("The person's job type should be a professor", professor.job.type == JobType.professor);
		assertTrue("Person should have a wealth type", professor.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Wilczynski", professor.getName() == "Wilczynski");
		
		student.msgGoBotherTheCPs();
		professor.msgHaveHeartAttack();

		assertTrue("The person's student state should be triggered to leave", student.studentState == StudentState.needsToLeave);
		assertTrue("The person should be running an action in the scheduler", student.pickAndExecuteAnAction());
		assertTrue("The person's state should now be triggered to leaving", student.studentState == StudentState.leaving);
		
		assertTrue("Professor should get cue to have heart attack", professor.professorState == ProfessorState.needsHeartAttack);
		assertTrue("The person should be running an action in the scheduler", professor.pickAndExecuteAnAction());
		assertTrue("The professor should have changed its state", professor.professorState != ProfessorState.needsHeartAttack);
		assertTrue("The professor should have changed its state", professor.professorState == ProfessorState.isHavingHeartAttack);
    }
    
    public void test6_MoreStudentInterleave() throws Exception
    {
    	System.out.println("TEST 6");
		TestPerson student1 = new TestPerson("Student", "Student", "Average");
		TestPerson professor = new TestPerson("Wilczynski", "Professor", "Average");
		TestPerson student2 = new TestPerson("Student", "Student", "Average");

		assertTrue("Person should have a job.", student1.job != null);
		assertTrue("The person's job type should be a professor", student1.job.type == JobType.student);
		assertTrue("Person should have a wealth type", student1.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Student", student1.getName() == "Student");

		
		student1.msgGoBotherTheCPs();

		assertTrue("The person's student state should be triggered to leave", student1.studentState == StudentState.needsToLeave);
		assertTrue("The person should be running an action in the scheduler", student1.pickAndExecuteAnAction());
		assertTrue("The person's state should now be triggered to leaving", student1.studentState == StudentState.leaving);

		assertTrue("Person should have a job.", professor.job != null);
		assertTrue("The person's job type should be a professor", professor.job.type == JobType.professor);
		assertTrue("Person should have a wealth type", professor.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Wilczynski", professor.getName() == "Wilczynski");
		
		professor.msgHaveHeartAttack();
		
		assertTrue("Professor should get cue to have heart attack", professor.professorState == ProfessorState.needsHeartAttack);
		assertTrue("The person should be running an action in the scheduler", professor.pickAndExecuteAnAction());
		assertTrue("The professor should have changed its state", professor.professorState != ProfessorState.needsHeartAttack);
		assertTrue("The professor should have changed its state", professor.professorState == ProfessorState.isHavingHeartAttack);
		
		assertTrue("Person should have a job.", student2.job != null);
		assertTrue("The person's job type should be a professor", student2.job.type == JobType.student);
		assertTrue("Person should have a wealth type", student2.wealthLevel == WealthLevel.average);
		assertTrue("Person's name should be Student", student2.getName() == "Student");
		
		student2.msgGoBotherTheCPs();

		assertTrue("The person's student state should be triggered to leave", student2.studentState == StudentState.needsToLeave);
		assertTrue("The person should be running an action in the scheduler", student2.pickAndExecuteAnAction());
		assertTrue("The person's state should now be triggered to leaving", student2.studentState == StudentState.leaving);
    }
}
