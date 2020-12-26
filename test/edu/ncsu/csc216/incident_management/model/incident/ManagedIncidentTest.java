package edu.ncsu.csc216.incident_management.model.incident;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.ncsu.csc216.incident.io.IncidentIOException;
import edu.ncsu.csc216.incident.io.IncidentReader;
import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CancellationCode;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * This class tests the ManagedIncident class
 * 
 * @author Bilal Mohamad
 * @author Keaton Thurston
 */
public class ManagedIncidentTest {

	/**
	 * Tests an invalid ManagedIncident's caller parameter
	 */
	@Test
	public void testInvalidManagedIncidentCaller() {
		
		//Tests with an empty caller String
		try {
			ManagedIncident mi = new ManagedIncident("", Category.DATABASE, Priority.HIGH, "Jeff", "My name Jeff");
			mi.getCaller();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		//Tests with a null caller String
		try {
			ManagedIncident mi = new ManagedIncident(null, Category.DATABASE, Priority.HIGH, "Jeff", "My name Jeff");
			mi.getCaller();
			fail();
		}
		catch (Exception e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests an invalid ManagedIncident's category parameter
	 */
	@Test
	public void testInvalidManagedIncidentCategory() {
		try {
			ManagedIncident mi = new ManagedIncident("melike5", null, Priority.LOW, "Bill Nye", "Science Rules");
			mi.getCategory();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests an invalid ManagedIncident's priority parameter
	 */
	@Test
	public void testInvalidManagedIncidentPriority() {
		try {
			ManagedIncident mi = new ManagedIncident("you", Category.SOFTWARE, null, "Dude", "Exteme stuff");
			mi.getCategory();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests an invalid ManagedIncident's name parameter
	 */
	@Test
	public void testInvalidManagedIncidentName() {
		//Tests with an empty name String
		try {
			ManagedIncident mi = new ManagedIncident("callme", Category.DATABASE, Priority.MEDIUM, "", "Random caller");
			mi.getName();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		//Tests with a null name String
		try {
			ManagedIncident mi = new ManagedIncident("lpost", Category.HARDWARE, Priority.URGENT, null, "I like lamp");
			mi.getName();
			fail();
		}
		catch (Exception e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests an invalid ManagedIncident's workNote parameter
	 */
	@Test
	public void testInvalidManagedIncidentWorkNote() {
		//Tests with an empty workNote String
		try {
			ManagedIncident mi = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "");
			mi.getNotesString();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		//Tests with a null workNote String
		try {
			ManagedIncident mi = new ManagedIncident("alincoln", Category.HARDWARE, Priority.URGENT, "Hamlet", null);
			mi.getNotesString();
			fail();
		}
		catch (Exception e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests a valid ManagedIncident object
	 */
	@Test
	public void testManagedIncident() {
		ManagedIncident mi = new ManagedIncident("melike9", Category.HARDWARE, Priority.MEDIUM, "Bill Nye", "Science Rules");
		
		assertEquals("melike9", mi.getCaller());
		assertEquals(Category.HARDWARE, mi.getCategory());
		assertEquals("Hardware", mi.getCategoryString());
		assertEquals("Medium", mi.getPriorityString());
		assertEquals("Bill Nye", mi.getName());
		assertEquals(1, mi.getNotes().size());
		assertEquals("Science Rules" + "\n-------\n", mi.getNotesString());
		assertNotNull(mi.getIncidentId());
		assertEquals(null, mi.getOwner());
		assertEquals(null, mi.getChangeRequest());
		assertNotNull(mi.getState());

		
	}
	
	
	/**
	 * Tests a valid ManagedIncident with the incident as a parameter
	 */
	@Test
	public void testManagedIncidentWithIncidentParameter() {
		IncidentReader reader = null;
		List<Incident> incidents = null;

		try {
			reader = new IncidentReader("test-files/exp_incident_all.xml");
			incidents = reader.getIncidents();
		} catch (IncidentIOException e) {
			fail();
		}
		
		ManagedIncident incident = new ManagedIncident(incidents.get(0));
		assertEquals(incidents.get(0).getCaller(), incident.getCaller());
		assertEquals(incidents.get(0).getCategory().toString(), incident.getCategoryString());
		assertEquals(incidents.get(0).getId(), incident.getIncidentId());
		assertEquals(incidents.get(0).getName(), incident.getName());
		assertEquals(incidents.get(0).getOwner(), incident.getOwner());
		assertEquals(incidents.get(0).getPriority().toString(), incident.getPriorityString());
		
	}
	
	
	
	/**
	 * Tests the setCategory method
	 */
	/*@Test
	public void testSetCategory() {
		ManagedIncident mi = new ManagedIncident(new Incident());		
		
	}*/
	
	/**
	 * Test for the update() method in ManagedIncident. Tests the various state transitions of the FSM.
	 */
	@Test
	public void testUpdate() {
		ManagedIncident incident = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		
		//Commands
		Command investigate = new Command(Command.CommandValue.INVESTIGATE, "owner", null, null, null, "note");
		Command hold = new Command(Command.CommandValue.HOLD, "owner", OnHoldReason.AWAITING_CALLER, null, null, "note");
		Command reopen = new Command(Command.CommandValue.REOPEN, "owner", null, null, null, "note");
		Command resolve = new Command(Command.CommandValue.RESOLVE, "owner", null, ResolutionCode.PERMANENTLY_SOLVED, null, "note");
		Command close = new Command(Command.CommandValue.CONFIRM, "owner", null, null, null, "note");
		Command cancel = new Command(Command.CommandValue.CANCEL, "owner", null, null, CancellationCode.UNNECESSARY, "note");
		
		//Test UnsupportedOperationException for the New to Resolved transition
		try {
			incident.update(resolve);
			fail();
		} catch (UnsupportedOperationException e) {
			assertNull(e.getMessage());
		}
		//Assuming this starts at new, tests the New to In Progress state transition
		incident.update(investigate);
		assertEquals("In Progress", incident.getState().getStateName());
		
		//Tests the In Progress to On Hold state transition and a transition to On Hold without a work note
		incident.update(hold);
		assertEquals("On Hold", incident.getState().getStateName());
		
		//Tests the On Hold to in Progress transition
		incident.update(reopen);
		assertEquals("In Progress", incident.getState().getStateName());
		
		//Tests the In Progress to Resolved state transition
		incident.update(resolve);
		assertEquals("Resolved", incident.getState().getStateName());
		
		//Tests the Resolved to In Progress state transition
		incident.update(reopen);
		assertEquals("In Progress", incident.getState().getStateName());
		
		//Tests the Resolved to Closed state transition
		incident.update(resolve);
		incident.update(close);
		assertEquals("Closed", incident.getState().getStateName());
		
		//Tests the Closed to In Progress state transition
		incident.update(reopen);
		assertEquals("In Progress", incident.getState().getStateName());
		
		//Tests the In Progress to Canceled state transition and a transition to canceled without a work note
		incident.update(cancel);
		assertEquals("Canceled", incident.getState().getStateName());
		
		ManagedIncident incident2 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		//Tests the New to Canceled state transition
		incident2.update(cancel);
		assertEquals("Canceled", incident2.getState().getStateName());
		
		ManagedIncident incident3 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		//Tests the On Hold to Canceled state transition
		incident3.update(investigate);
		incident3.update(hold);
		incident3.update(cancel);
		assertEquals("Canceled", incident3.getState().getStateName());
		
		ManagedIncident incident4 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		//Tests the On Hold to Resolved state transition
		incident4.update(investigate);
		incident4.update(hold);
		incident4.update(resolve);
		assertEquals("Resolved", incident4.getState().getStateName());
		//Tests the Resolved to Canceled state transition
		incident4.update(cancel);
		assertEquals("Canceled", incident4.getState().getStateName());
		
		ManagedIncident incident5 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		//Tests the UnsupportedOperationException for the transition from close to cancel
		incident5.update(investigate);
		incident5.update(resolve);
		incident5.update(close);
		try {
			incident5.update(cancel);
			fail();
		} catch (UnsupportedOperationException e) {
			assertNull(e.getMessage());
		}
		assertEquals("Closed", incident5.getState().getStateName());
		
		//Makes sure that attempting to update the state of a canceled incident throws an UnsupportedOperationException
		try {
			incident4.update(reopen);
			fail();
		} catch (UnsupportedOperationException e) {
			assertNull(e.getMessage());
		}
		
		
	}
	
	/**
	 * Tests getCancellatioCodeString() in ManagedIncident.
	 */
	@Test
	public void testGetCancellationCodeString() {
		Command cancelUnnecessary = new Command(Command.CommandValue.CANCEL, "owner", null, null, CancellationCode.UNNECESSARY, "note");
		Command cancelDuplicate = new Command(Command.CommandValue.CANCEL, "owner", null, null, CancellationCode.DUPLICATE, "note");
		Command cancelNotIncident = new Command(Command.CommandValue.CANCEL, "owner", null, null, CancellationCode.NOT_AN_INCIDENT, "note");
		
		ManagedIncident incident = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident.update(cancelUnnecessary);
		assertEquals("Unnecessary", incident.getCancellationCodeString());
		
		ManagedIncident incident2 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident2.update(cancelNotIncident);
		assertEquals("Not an Incident", incident2.getCancellationCodeString());

		ManagedIncident incident3 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident3.update(cancelDuplicate);
		assertEquals("Duplicate", incident3.getCancellationCodeString());
	}

	/**
	 * Tests getGetResolutionCodeString() in ManagedIncident
	 */
	@Test
	public void testGetGetResolutionCodeString() {
		Command investigate = new Command(Command.CommandValue.INVESTIGATE, "owner", null, null, null, "note");
		Command resolvePermanent = new Command(Command.CommandValue.RESOLVE, "owner", null, ResolutionCode.PERMANENTLY_SOLVED, null, "note");
		Command resolveCaller = new Command(Command.CommandValue.RESOLVE, "owner", null, ResolutionCode.CALLER_CLOSED, null, "note");
		Command resolveNotSolved = new Command(Command.CommandValue.RESOLVE, "owner", null, ResolutionCode.NOT_SOLVED, null, "note");
		Command resolveWorkAround = new Command(Command.CommandValue.RESOLVE, "owner", null, ResolutionCode.WORKAROUND, null, "note");
		
		ManagedIncident incident = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident.update(investigate);
		incident.update(resolveCaller);
		assertEquals("Caller Closed", incident.getResolutionCodeString());
		
		ManagedIncident incident2 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident2.update(investigate);
		incident2.update(resolvePermanent);
		assertEquals("Permanently Solved", incident2.getResolutionCodeString());

		ManagedIncident incident3 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident3.update(investigate);
		incident3.update(resolveNotSolved);
		assertEquals("Not Solved", incident3.getResolutionCodeString());
		
		ManagedIncident incident4 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident4.update(investigate);
		incident4.update(resolveWorkAround);
		assertEquals("Workaround", incident4.getResolutionCodeString());
	}
	
	/**
	 * Tests getGetOnHoldReasonString() in ManagedIncident
	 */
	@Test
	public void testGetOnHoldReasonString() {
		Command investigate = new Command(Command.CommandValue.INVESTIGATE, "owner", null, null, null, "note");
		Command holdAwaitingCaller = new Command(Command.CommandValue.HOLD, "owner", OnHoldReason.AWAITING_CALLER, null, null, "note");
		Command holdAwaitingChange = new Command(Command.CommandValue.HOLD, "owner", OnHoldReason.AWAITING_CHANGE, null, null, "note");
		Command holdAwaitingVendor = new Command(Command.CommandValue.HOLD, "owner", OnHoldReason.AWAITING_VENDOR, null, null, "note");

		ManagedIncident incident = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident.update(investigate);
		incident.update(holdAwaitingCaller);
		assertEquals("Awaiting Caller", incident.getOnHoldReasonString());
		
		ManagedIncident incident2 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident2.update(investigate);
		incident2.update(holdAwaitingChange);
		assertEquals("Awaiting Change", incident2.getOnHoldReasonString());

		ManagedIncident incident3 = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		incident3.update(investigate);
		incident3.update(holdAwaitingVendor);
		assertEquals("Awaiting Vendor", incident3.getOnHoldReasonString());
	}
	
	/**
	 * Tests getCategoryString() in ManagedIncident
	 */
	@Test
	public void testGetCategoryString() {
		ManagedIncident incident = new ManagedIncident("jsmith", Category.NETWORK, Priority.LOW, "Johnny", "lol");
		assertEquals("Network", incident.getCategoryString());
		
		ManagedIncident incident1 = new ManagedIncident("jsmith", Category.DATABASE, Priority.LOW, "Johnny", "lol");
		assertEquals("Database", incident1.getCategoryString());
		
		ManagedIncident incident2 = new ManagedIncident("jsmith", Category.INQUIRY, Priority.LOW, "Johnny", "lol");
		assertEquals("Inquiry", incident2.getCategoryString());
		
		ManagedIncident incident3 = new ManagedIncident("jsmith", Category.HARDWARE, Priority.LOW, "Johnny", "lol");
		assertEquals("Hardware", incident3.getCategoryString());
		
		ManagedIncident incident4 = new ManagedIncident("jsmith", Category.SOFTWARE, Priority.LOW, "Johnny", "lol");
		assertEquals("Software", incident4.getCategoryString());
		
	}
}
