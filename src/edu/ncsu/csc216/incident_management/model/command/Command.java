/**
 * 
 */
package edu.ncsu.csc216.incident_management.model.command;

/**
 * The object representation of a command that a user would make in the Incident Management system that might initiate a state change.
 * Encapsulates the information about a user command that would lead to a transition. Contains four inner enumeration:
 * CommandValue, OnHoldReason, CancellationCode, and ResolutionCode
 * 
 * @author Keaton Thurston
 * @author Bilal Mohamad
 */
public class Command {
	
	/** String used to indicate the reason for an incident in the OnHold state */
	public static final String OH_CALLER = "Awaiting Caller";
	/** String used to indicate the reason for an incident in the OnHold state */
	public static final String OH_CHANGE = "Awaiting Change";
	/** String used to indicate the reason for an incident in the OnHold state */
	public static final String OH_VENDOR = "Awaiting Vendor";
	/** String used to indicate the resolution code for an incident in the Closed state */
	public static final String RC_PERMANENTLY_SOLVED = "Permanently Solved";
	/** String used to indicate the resolution code for an incident in the Closed state */
	public static final String RC_WORKAROUND = "Workaround";
	/** String used to indicate the resolution code for an incident in the Closed state */
	public static final String RC_NOT_SOLVED = "Not Solved";
	/** String used to indicate the resolution code for an incident in the Closed state */
	public static final String RC_CALLER_CLOSED = "Caller Closed";
	/** String used to indicate the cancellation code for an incident in the Cancelled state */
	public static final String CC_DUPLICATE = "Duplicate";
	/** String used to indicate the cancellation code for an incident in the Cancelled state */
	public static final String CC_UNNECESSARY = "Unnecessary";
	/** String used to indicate the cancellation code for an incident in the Cancelled state */
	public static final String CC_NOT_AN_INCIDENT = "Not an Incident";
	
	/** String containing the owner id */
	private String ownerId;
	
	/** String containing the custom note entered by the user */
	private String note;
	
	/** Represents one of the six possible commands that a user can make for the Incident Management FSM. */
	private CommandValue c;
	
	/** Represents the three possible cancellation codes. */
	private CancellationCode cancellationCode;
	
	/** Represents the four possible cancellation codes. */
	private ResolutionCode resolutionCode;
	
	/** Represents the three possible on hold codes.*/
	private OnHoldReason onHoldReason;
	
	
	/**
	 * The constructor for creating a Command object
	 * 
	 * @param c					Represents one of the six possible commands that a user can make for the Incident Management FSM.
	 * @param ownerId			The id of the owner
	 * @param onHoldReason		The reason the incident is on hold
	 * @param resolutionCode	The resolution code for the incident
	 * @param cancellationCode	The cancellation code for the incident
	 * @param note				The note the user added about the incident
	 * 
	 * @throws IllegalArgumentException	if the CommandValue entered is null
	 * 									if the CommandValue is INVESTIGATE and the ownerId is empty or null
	 * 									if the CommandValue is HOLD and the onHoldReason is null
	 * 									if the CommandValue is RESOLVE and the resolutionCode is null
	 * 									if the CommandValue is CANCEL and the cancellationCode is null
	 * 									if the note is empty or null
	 */
	public Command(CommandValue c, String ownerId, OnHoldReason onHoldReason, ResolutionCode resolutionCode, CancellationCode cancellationCode, String note) {
		
		if (c == null) {
			throw new IllegalArgumentException();
		}
		
		if (c.equals(CommandValue.INVESTIGATE) && (ownerId == null || ownerId.equals(""))) {
			throw new IllegalArgumentException();
		}
		
		if (c.equals(CommandValue.HOLD) && onHoldReason == null) {
			throw new IllegalArgumentException();
		}
		
		if (c.equals(CommandValue.RESOLVE) && resolutionCode == null) {
			throw new IllegalArgumentException();
		}
		
		if (c.equals(CommandValue.CANCEL) && cancellationCode == null) {
			throw new IllegalArgumentException();
		}
		
		if (note == null || note.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.c = c;
		this.ownerId = ownerId;
		this.onHoldReason = onHoldReason;
		this.resolutionCode = resolutionCode;
		this.cancellationCode = cancellationCode;
		this.note = note;
	}
	
	
	/**
	 * Retrieves the command's command value
	 * 
	 * @return the command's CommandValue
	 */
	public CommandValue getCommand() {
		return c;
	}
	
	
	/**
	 * Retrieves the command's owner id
	 * 
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}
	
	
	/**
	 * Retrieves the command's resolution code
	 * 
	 * @return the command's ResolutionCode
	 */
	public ResolutionCode getResolutionCode() {
		return resolutionCode;
	}
	
	
	/**
	 * Retrieves the command's work note 
	 * 
	 * @return the command's work note
	 */
	public String getWorkNote() {
		return note;
	}
	
	
	/**
	 * Retrieves the command's reason for being on hold
	 * 
	 * @return the command's OnHoldReason
	 */
	public OnHoldReason getOnHoldReason() {
		return onHoldReason;
	}
	
	
	/**
	 * Retrieves the command's cancellation code
	 * 
	 * @return the command's CancellationCode
	 */
	public CancellationCode getCancellationCode() {
		return cancellationCode;
	}
	
	
	/** An enumeration contained in the Command class. Contains six possible commands that a user can make for the Incident Management FSM. */
	public enum CommandValue { INVESTIGATE, HOLD, RESOLVE, CONFIRM, REOPEN, CANCEL }

	/** An enumeration contained in the Command class. Contains three possible on hold codes. */
	public enum OnHoldReason { AWAITING_CALLER, AWAITING_CHANGE, AWAITING_VENDOR }

	/** An enumeration contained in the Command class. Contains four possible resolution codes. */
	public enum ResolutionCode { PERMANENTLY_SOLVED, WORKAROUND, NOT_SOLVED, CALLER_CLOSED }

	/** An enumeration contained in the Command class. Contains the three possible on cancellation codes. */
	public enum CancellationCode { DUPLICATE, UNNECESSARY, NOT_AN_INCIDENT };
}
