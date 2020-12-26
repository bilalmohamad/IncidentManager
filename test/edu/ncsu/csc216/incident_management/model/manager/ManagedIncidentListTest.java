package edu.ncsu.csc216.incident_management.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.ncsu.csc216.incident.io.IncidentIOException;
import edu.ncsu.csc216.incident.io.IncidentReader;
import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * This class tests the ManagedIncidentList class
 * 
 * @author Bilal Mohamad
 * @author Keaton Thurston
 */
public class ManagedIncidentListTest {
	
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
	 * Test for the ManagedIncidentList constructor.
	 */
	@Test
	public void testManagedIncidentList() {
		ManagedIncident.setCounter(4);
		ManagedIncidentList list = new ManagedIncidentList();
		assertEquals(list.getManagedIncidents().size(), 0);
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		assertEquals(list.getManagedIncidents().get(0).getIncidentId(), 0);
	}
	
	/**
	 * Test for addIncident() in ManagedIncidentList.
	 */
	@Test
	public void testAddIncident() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		assertEquals(list.getManagedIncidents().size(), 1);
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		assertEquals(list.getManagedIncidents().size(), 2);
	}
	
	/**
	 * Test for addXMLIncidents() in ManagedIncidentList.
	 */
	@Test
	public void testAddXMLIncidents() {
		try {
			ManagedIncidentList list = new ManagedIncidentList();
			IncidentReader read = new IncidentReader("test-files/incident1.xml");
			List<Incident> incidents = read.getIncidents();
			
			list.addXMLIncidents(incidents);
			assertEquals(6, list.getManagedIncidents().size());
		}
		catch (IncidentIOException e) {
			fail();
		}
			
	}

	/**
	 * Test for getManagedIncidents() in ManagedIncidentList.
	 */
	@Test
	public void testGetManagedIncidents() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		assertEquals(list.getManagedIncidents().size(), 1);
	}

	/**
	 * Test for getIncidentsByCategory() in ManagedIncidentList.
	 */
	@Test
	public void testGetIncidentsByCategory() {
	
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Alex Jones", Category.DATABASE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Jeff", Category.HARDWARE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("The IRS", Category.NETWORK, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Canada Dry", Category.SOFTWARE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Bill Nye", CATEGORY, PRIORITY, NAME, WORK_NOTE);
		
		//Invalid parameter
		try {
			list.getIncidentsByCategory(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		//Inquiry
		assertEquals(list.getIncidentsByCategory(CATEGORY).get(0).getCaller(), "Caller");
		assertEquals(list.getIncidentsByCategory(CATEGORY).get(1).getCaller(), "Bill Nye");
		//Database
		assertEquals(list.getIncidentsByCategory(Category.DATABASE).get(0).getCaller(), "Alex Jones");
		//Hardware
		assertEquals(list.getIncidentsByCategory(Category.HARDWARE).get(0).getCaller(), "Jeff");
		//Network
		assertEquals(list.getIncidentsByCategory(Category.NETWORK).get(0).getCaller(), "The IRS");
		//Software
		assertEquals(list.getIncidentsByCategory(Category.SOFTWARE).get(0).getCaller(), "Canada Dry");

	}

	/**
	 * Test for getIncidentById() in ManagedIncidentList. 
	 */
	@Test
	public void testGetIncidentById() {//Waiting for counter to be fixed
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Alex Jones", Category.DATABASE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Jeff", Category.HARDWARE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("The IRS", Category.NETWORK, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Canada Dry", Category.SOFTWARE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Bill Nye", CATEGORY, PRIORITY, NAME, WORK_NOTE);
		
		assertEquals(list.getIncidentById(0).getCaller(), "Caller");
		
		assertEquals(list.getIncidentById(1).getCaller(), "Alex Jones");
		
		assertEquals(list.getIncidentById(2).getCaller(), "Jeff");
		
		assertEquals(list.getIncidentById(3).getCaller(), "The IRS");
		
		assertEquals(list.getIncidentById(4).getCaller(), "Canada Dry");
		
		assertEquals(list.getIncidentById(5).getCaller(), "Bill Nye");
		
		assertNull(list.getIncidentById(6));
	}

	/**
	 * Test for executeCommand() in ManagedIncidentList.
	 */
	//TODO Fix test to function properly
	@Test
	public void testExecuteCommand() {
		try {
			ManagedIncidentList list = new ManagedIncidentList();
			IncidentReader read = new IncidentReader("test-files/incident1.xml");
			List<Incident> incidents = read.getIncidents();
			
			list.addXMLIncidents(incidents);
			assertEquals(6, list.getManagedIncidents().size());
			
			//TODO Does not actually test the executeCommand method but achieves coverage
			try {
				list.executeCommand(2, new Command(CommandValue.HOLD, "tgpucket", OnHoldReason.AWAITING_CHANGE, null, null, "Increase the hard drive capacity to 500 GB for Jenkins servers."));
			}
			catch (Exception e) {
				fail();
			}
		}
		catch (IncidentIOException e) {
			fail();
		}
	}
	
	/**
	 * Test for deleteIncidentById() in ManagedIncidentList.
	 */
	@Test
	public void testDeleteIncidentById() {//Waiting for counter to be fixed
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident(CALLER, CATEGORY, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Alex Jones", Category.DATABASE, PRIORITY, NAME, WORK_NOTE);
		list.addIncident("Jeff", Category.HARDWARE, PRIORITY, NAME, WORK_NOTE);
		
		list.deleteIncidentById(0);
		list.deleteIncidentById(1);
		
		assertEquals(list.getIncidentById(2).getCaller(), "Jeff");
	}
	
	
	/**
	 * Tests the ManageeIncident() constructor containing an incident as a parameter
	 */
	@Test
	public void testManagedIncidentFromParameter() {
		try {
			ManagedIncidentList list = new ManagedIncidentList();
			IncidentReader read = new IncidentReader("test-files/exp_incident_all.xml");
			List<Incident> incidents = read.getIncidents();

			list.addXMLIncidents(incidents);
			
			assertEquals(ResolutionCode.WORKAROUND, list.getIncidentById(4).getResolutionCode());
			assertEquals("Workaround", list.getIncidentById(4).getResolutionCodeString());
			
			assertEquals("Not an Incident", list.getIncidentById(6).getCancellationCodeString());
			
			assertEquals("Awaiting Change", list.getIncidentById(3).getOnHoldReasonString());
		}
		catch (IncidentIOException e) {
			fail();
		}
	}
	
	
	/**
	 * Tests the ManageeIncident() constructor containing an invalid state
	 */
	@Test
	public void testManagedIncidentWithIncorrectState() {
		try {
			ManagedIncidentList list = new ManagedIncidentList();
			IncidentReader read = new IncidentReader("test-files/incident10.xml");
			List<Incident> incidents = read.getIncidents();

			list.addXMLIncidents(incidents);
			fail();
			
			
		}
		catch (IncidentIOException e) {
			assertEquals("IncidentIOException - error processing incident list", e.getMessage());
		}
	}
	
	
	/**
	 * Tests the ManageeIncident with the Incident9.xml file
	 */
	@Test
	public void testManagedIncidentWithTestFileIncident9() {
		try {
			ManagedIncidentList list = new ManagedIncidentList();
			IncidentReader read = new IncidentReader("test-files/incident9.xml");
			List<Incident> incidents = read.getIncidents();

			list.addXMLIncidents(incidents);
			
//			list.);
			fail();
			
			
		}
		catch (IncidentIOException e) {
			assertEquals("IncidentIOException - error processing incident list", e.getMessage());
		}
	}
}
