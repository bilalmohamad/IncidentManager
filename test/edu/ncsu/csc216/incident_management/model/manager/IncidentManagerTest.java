package edu.ncsu.csc216.incident_management.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


import org.junit.Test;

import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * This class tests the IncidentManager class
 * 
 * @author Bilal Mohamad
 * @author Keaton Thurston
 */
public class IncidentManagerTest {
	
	/** File containing incidents for testing */
	private static final String READING_FILE = "test-files/exp_incident_all.xml";
	
	/** File to write incidents to for testing */
	private static final String WRITING_FILE = "test-files/written_incidents.xml";

	/** Caller of the incident */
	private static final String CALLER = "Caller";
	
	/** Incident category */
	private static final Category CATEGORY = Category.INQUIRY;
	
	/** Incident priority */
	private static final Priority PRIORITY = Priority.HIGH;
	
	/** Name of incident */
	private static final String NAME = "Jeff";
	
	/** Incident work note */
	private static final String WORK_NOTE = "Everything is broken";

	/**
	 * Test for the constructor of IncidentManager.
	 */
	@Test
	public void testIncidentManager() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray().length, 0);
	}
	
	/**
	 * Test for saveManagedIncidentsToFile() in IncidentManager.
	 */
	@Test
	public void testSaveManagedIncidentsToFile() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().loadManagedIncidentsFromFile(READING_FILE);
		assertNotNull(IncidentManager.getInstance());
		IncidentManager.getInstance().saveManagedIncidentsToFile(WRITING_FILE);
		assertEquals(6, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		
		try {
			IncidentManager.getInstance().saveManagedIncidentsToFile("tester-files/fakefile");
		} catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
//		IncidentManager.getInstance().loadManagedIncidentsFromFile(WRITING_FILE);
//		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray().length, 6);
	}
	
	/**
	 * Test for loadManagedIncidentsFromFile() in IncidentManager.
	 */
	@Test
	public void testLoadManagedIncidentsFromFile() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		//Test invalid file
		try {
			IncidentManager.getInstance().loadManagedIncidentsFromFile("tester-files/fakefile");
		} catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		//Test valid file
		IncidentManager.getInstance().loadManagedIncidentsFromFile(READING_FILE);
		assertNotNull(IncidentManager.getInstance());
		assertEquals(6, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		
		
	}

	/**
	 * Test for getManagedIncidentsAsArray() in IncidentManager.
	 */
	@Test
	public void testGetManagedIncidentsAsArray() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().loadManagedIncidentsFromFile(READING_FILE);
		//Checking IDs
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[0][0], "1");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[1][0], "2");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[2][0], "3");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[3][0], "4");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[4][0], "5");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[5][0], "6");
		//Checking category
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[0][1], "Software");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[1][1], "Hardware");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[2][1], "Hardware");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[3][1], "Software");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[4][1], "Network");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[5][1], "Inquiry");
		
		//Checking state name causes ERRORS (other tests work fine)
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[0][2], "New");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[1][2], "In Progress");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[2][2], "On Hold");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[3][2], "Resolved");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[4][2], "Closed");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[5][2], "Canceled");
		
		//Checking priority
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[0][3], "Urgent");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[1][3], "High");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[2][3], "High");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[3][3], "Medium");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[4][3], "Medium");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[5][3], "Low");
		//Checking name
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[0][4], "Jenkins installation");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[1][4], "Jenkins VM Hard Drive");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[2][4], "Mount data drive to VM");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[3][4], "Permissions issue with reading files");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[4][4], "Unable to use SSH on NCSU wireless");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray()[5][4], "What's the best pizza place near Centenntial Campus?");
	}
	
	/**
	 * Test for getManagedIncidentsAsArrayByCategory() in IncidentManager.
	 */
	@Test
	public void testGetManagedIncidentsAsArrayByCategory() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		//Tests an invalid parameter
		try {
			IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		//Tests a valid parameter
		IncidentManager.getInstance().loadManagedIncidentsFromFile(READING_FILE);
		//Checking IDs
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[0][0], "1");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[1][0], "4");
		//Checking Category
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[0][1], "Software");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[1][1], "Software");
		//Checking state name
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[0][2], "New");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[1][2], "Resolved");
		//Checking priority
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[0][3], "Urgent");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[1][3], "Medium");
		//Checking name
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[0][4], "Jenkins installation");
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.SOFTWARE)[1][4], "Permissions issue with reading files");
	}

	/**
	 * Test for getManagedIncidentById() in IncidentManager.
	 */
	@Test
	public void testGetManagedIncidentById() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().loadManagedIncidentsFromFile(READING_FILE);
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray().length, 6);
		assertEquals(IncidentManager.getInstance().getManagedIncidentById(3).getCaller(), "student1");
		
	}
	
	/**
	 * Test for executeCommand() in IncidentManager.
	 */
	//TODO Fix test to function properly
	@Test
	public void testExecuteCommand() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		try {
			IncidentManager.getInstance().loadManagedIncidentsFromFile("test-files/incident1.xml");
			assertEquals(6, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
			
			IncidentManager.getInstance().executeCommand(2, new Command(CommandValue.HOLD, "tgpucket", OnHoldReason.AWAITING_CALLER, null, null, "Increase the hard drive capacity to 500 GB for Jenkins servers."));
		}
		catch (IllegalArgumentException e) {
			fail();
		}
	}
	
	/**
	 * Test for deleteManagedIncidentById() in IncidentManager.
	 */
	@Test
	public void testDeleteManagedIncidentById() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().loadManagedIncidentsFromFile(READING_FILE);
		IncidentManager.getInstance().deleteManagedIncidentById(3);
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray().length, 5);
	}
	
	/**
	 * Test for addManagedIncidentToList() in IncidentManager.
	 */
	@Test
	public void testAddManagedIncidentToList() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray().length, 1);
		IncidentManager.getInstance().addManagedIncidentToList(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		assertEquals(IncidentManager.getInstance().getManagedIncidentsAsArray().length, 2);
	}
}
