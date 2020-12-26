package edu.ncsu.csc216.incident_management.model.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc216.incident_management.model.command.Command.CancellationCode;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;

/**
 * This class tests the Command class
 * 
 * @author Bilal Mohamad
 * @author Keaton Thurston
 */
public class CommandTest {


	/**
	 * Tests the Command constructor
	 */
	@Test
	public void testCommand() {
		Command c = new Command(CommandValue.INVESTIGATE, "owner", OnHoldReason.AWAITING_CALLER, ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "Owner likes food");
		
		assertEquals(CommandValue.INVESTIGATE, c.getCommand());
		assertEquals("owner", c.getOwnerId());
		assertEquals(OnHoldReason.AWAITING_CALLER, c.getOnHoldReason());
		assertEquals(ResolutionCode.PERMANENTLY_SOLVED, c.getResolutionCode());
		assertEquals(CancellationCode.DUPLICATE, c.getCancellationCode());
		assertEquals("Owner likes food", c.getWorkNote());
	}
	
	
	/**
	 * Tests the Command constructor with an invalid CommandValue value
	 */
	@Test
	public void testInvalidCommandValue() {
		
		try {
			Command c = new Command(null, "owner", OnHoldReason.AWAITING_CALLER, ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "Owner likes food");
			c.getCommand();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	

	/**
	 * Tests the Command constructor with an invalid ownerId value
	 */
	@Test
	public void testInvalidOwnerId() {
		
		//Tests empty ownerId
		try {
			Command c = new Command(CommandValue.INVESTIGATE, "", OnHoldReason.AWAITING_CALLER, ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "Owner likes food");
			c.getOwnerId();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		
		//Tests null ownerId
		try {
			Command c = new Command(CommandValue.INVESTIGATE, null, OnHoldReason.AWAITING_CALLER, ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "Owner likes food");
			c.getOwnerId();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests the Command constructor with an invalid OnHoldReason value 
	 */
	@Test
	public void testInvalidOnHoldReason() {
		
		try {
			Command c = new Command(CommandValue.HOLD, "id", null, ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "Owner likes food");
			c.getOnHoldReason();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests the Command constructor with an invalid ResolutionCode value 
	 */
	@Test
	public void testInvalidResolutionCode() {
		
		try {
			Command c = new Command(CommandValue.RESOLVE, "id", OnHoldReason.AWAITING_VENDOR, null, CancellationCode.DUPLICATE, "Owner likes food");
			c.getResolutionCode();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests the Command constructor with an invalid CancellationCode value 
	 */
	@Test
	public void testInvalidCancellationCode() {
		
		try {
			Command c = new Command(CommandValue.CANCEL, "id", OnHoldReason.AWAITING_VENDOR, ResolutionCode.WORKAROUND, null, "Owner likes food");
			c.getCancellationCode();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
	/**
	 * Tests the Command constructor with an invalid note value 
	 */
	@Test
	public void testInvalidNote() {
				
		//Tests empty note
		try {
			Command c = new Command(CommandValue.HOLD, "id", OnHoldReason.AWAITING_VENDOR, ResolutionCode.WORKAROUND, CancellationCode.UNNECESSARY, "");
			c.getWorkNote();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
		
		
		//Tests null note
		try {
			Command c = new Command(CommandValue.INVESTIGATE, "id", OnHoldReason.AWAITING_VENDOR, ResolutionCode.WORKAROUND, CancellationCode.UNNECESSARY, null);
			c.getWorkNote();
			fail();
		}
		//TODO The exception was changed to saitisfy tests. Could be wrong
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}
	
	
}