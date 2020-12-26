/**
 * 
 */
package edu.ncsu.csc216.incident_management.model.incident;

import java.util.ArrayList;

import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident.xml.WorkNotes;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CancellationCode;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;

/**
 * Concrete class representing the State Pattern context class.
 * A ManagedIncident keeps track of all incident information including the current state.
 * The state is updated when a Command encapsulating a transition is given to the ManagedIncident. 
 * ManagedIncident encapsulates the six concrete *State classes and two enumerations:
 * NewState, InProgressState, OnHoldState, ResolvedState, ClosedState, CanceledState,
 * Category, and Priority.
 * 
 * @author Keaton Thurston
 * @author Bilal Mohamad
 */
public class ManagedIncident {

	/** A String used to represent the category of the incident */
	public static final String C_INQUIRY = "Inquiry";
	/** A String used to represent the category of the incident */
	public static final String C_SOFTWARE = "Software";
	/** A String used to represent the category of the incident */
	public static final String C_HARDWARE = "Hardware";
	/** A String used to represent the category of the incident */
	public static final String C_NETWORK = "Network";
	/** A String used to represent the category of the incident */
	public static final String C_DATABASE = "Database";
	/** A String used to represent the priority level of the incident */
	public static final String P_URGENT = "Urgent";
	/** A String used to represent the priority level of the incident */
	public static final String P_HIGH = "High";
	/** A String used to represent the priority level of the incident */
	public static final String P_MEDIUM = "Medium";
	/** A String used to represent the priority level of the incident */
	public static final String P_LOW = "Low";
	
	
	/** Represents the four possible priorities for ManagedIncidents */
	private Priority priority;
	
	/** Represents the three possible cancellation codes for an incident */
	private CancellationCode cancellationCode;
	
	/** Represents the four possible cancellation codes for an incident */
	private ResolutionCode resolutionCode;
	
	/** Represents the three possible on hold codes for an incident */
	private OnHoldReason onHoldReason;
	
	/** Represents the five possible categories of incidents */
	private Category category;
	
	
	/** Used to retrieve methods from the IncidentState interface for the current state */
	private IncidentState state;
	
	/** Used to retrieve methods from the IncidentState interface for the OnHold state */
	private final IncidentState onHoldState;
	
	/** Used to retrieve methods from the IncidentState interface for the Resolved state */
	private final IncidentState resolvedState;
	
	/** Used to retrieve methods from the IncidentState interface for the New state */
	private final IncidentState newState;
	
	/** Used to retrieve methods from the IncidentState interface for the Closed state */
	private final IncidentState closedState;
	
	/** Used to retrieve methods from the IncidentState interface for the InProgress state */
	private final IncidentState inProgressState;
	
	/** Used to retrieve methods from the IncidentState interface for the Canceled state */
	private final IncidentState canceledState;
	
	
	/** Used to represent the id number of the incident */
	private int incidentId;
	
	/** User id of person who reported the incident */
	private String caller;
	
	/** User id of the incident owner */
	private String owner;
	
	/** Incident’s name information from when the incident is created */
	private String name;
	
	/** Change request information for the incident */
	private String changeRequest;
	
	/** An ArrayList of notes */
	private ArrayList<String> notes;
	
	
	/** A String used to represent the name of the state of the incident */
	public static final String NEW_NAME = "New";
	/** A String used to represent the name of the state of the incident */
	public static final String IN_PROGRESS_NAME = "In Progress";
	/** A String used to represent the name of the state of the incident */
	public static final String ON_HOLD_NAME = "On Hold";
	/** A String used to represent the name of the state of the incident */
	public static final String RESOLVED_NAME = "Resolved";
	/** A String used to represent the name of the state of the incident */
	public static final String CLOSED_NAME = "Closed";
	/** A String used to represent the name of the state of the incident */
	public static final String CANCELED_NAME = "Canceled";
	
	
	/** A static field that keeps track of the id value that should be given to the next ManagedIncident created */
	private static int counter = 0;
	
	
	/**
	 * Constructor used to create a ManagedIncident object based on the entered parameters.
	 * If any of the parameters are null or empty strings (if a String type), then an IllegalArgumentException is thrown
	 * 
	 * @param caller	the user id of the person who reported the incident
	 * @param category	the category of the incident
	 * @param priority	the priority of the incident
	 * @param name		the name information of the incident
	 * @param workNote	the work note of the incident	
	 * 
	 * @throws IllegalArgumentException	if caller is empty or null
	 * 									if category is null
	 * 									if priority is null
	 * 									if name is empty or null
	 * 									if workNote is empty or null
	 */
	public ManagedIncident(String caller, Category category, Priority priority, String name, String workNote) {
		
		if (caller == null || caller.equals("")) {
			throw new IllegalArgumentException();
		}
		
		if (category == null) {
			throw new IllegalArgumentException();
		}
		
		if (priority == null) {
			throw new IllegalArgumentException();
		}
		
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException();
		}
		
		if (workNote == null || workNote.equals("")) {
			throw new IllegalArgumentException();
		}
	
		incidentId = counter;
		incrementCounter();
		
		
		this.caller = caller;
		this.category = category;
		this.priority = priority;
		this.name = name;
		notes = new ArrayList<String>();
		notes.add(workNote);
		
		onHoldReason = null;
		resolutionCode = null;
		cancellationCode = null;
		
		onHoldState = new OnHoldState();
		resolvedState = new ResolvedState();
		newState = new NewState();
		closedState = new ClosedState();
		inProgressState = new InProgressState();
		canceledState = new CanceledState();
		state = newState;
		
//		owner = null;
//		changeRequest = null;
//		onHoldReason = null;
//		cancellationCode = null;
//		resolutionCode = null;
		
	}
	
	
	/**
	 * Constructor used to create a ManagedIncident object based on the values from the incident.
	 * The fields of the ManagedIncident are set to the values from the Incident.
	 * 
	 * @param i		the incident used to create the ManagedIncident
	 */
	public ManagedIncident(Incident i) {
		
		caller = i.getCaller(); 
		setCategory(i.getCategory());
		setPriority(i.getPriority());
		name = i.getName();
		notes = new ArrayList<String>();
		notes.addAll(i.getWorkNotes().getNotes());
		
		owner = i.getOwner();
		incidentId = i.getId();
		
		onHoldState = new OnHoldState();
		resolvedState = new ResolvedState();
		newState = new NewState();
		closedState = new ClosedState();
		inProgressState = new InProgressState();
		canceledState = new CanceledState();
		
		setOnHoldReason(i.getOnHoldReason());
		setResolutionCode(i.getResolutionCode());
		setCancellationCode(i.getCancellationCode());
		setState(i.getState());
		
		
		/*state = null;			//Use update methods from inner classes
		onHoldState = null;
		resolvedState = null;
		newState = null;
		closedState = null;
		inProgressState = null;
		canceledState = null;
		changeRequest = i.getChangeRequest();

		incrementCounter();
		incidentId = i.getId();
		
		try {
			setCategory(i.getCategory());
			setState(i.getState());
			setPriority(i.getPriority());
			
			if (i.getOnHoldReason() != null) {
				setOnHoldReason(i.getOnHoldReason());
			}
			
			if (i.getResolutionCode() != null) {
				setResolutionCode(i.getResolutionCode());
			}
			
			if (i.getCancellationCode() != null) {
				setCancellationCode(i.getCancellationCode());
			}
			
			this.caller = i.getCaller();
			this.name = i.getName();
			notes = (ArrayList<String>) (i.getWorkNotes().getNotes());		//May be a future problem
		}
		catch (Exception e) {	//May need to catch a different exception 
			throw new IllegalArgumentException("Could not convert to enumeration");
		}	*/	
	}
	
	/**
	 * This method is used to increment the counter
	 */
	public static void incrementCounter() {
		counter++;
	}
	
	
	
	/**
	 * Retrieves the incidentId of the incident
	 * 
	 * @return the incidentId
	 */
	public int getIncidentId() {
		return incidentId;
	}


	/**
	 * Retrieves the changeRequest of the incident
	 * 
	 * @return the changeRequest
	 */
	public String getChangeRequest() {
		return changeRequest;
	}


	/**
	 * Retrieves the category of the incident
	 * 
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}


	/**
	 * Retrieves the category String depending on the category
	 * 
	 * @return a string of the category
	 */
	public String getCategoryString() {
		if (category == Category.INQUIRY) {
			return C_INQUIRY;
		}
		
		else if (category == Category.SOFTWARE) {
			return C_SOFTWARE;
		}
		
		else if (category == Category.HARDWARE) {
			return C_HARDWARE;
		}
		
		else if (category == Category.NETWORK) {
			return C_NETWORK;
		}
		
		else if (category == Category.DATABASE) {
			return C_DATABASE;
		}
		
		else {
			return null;
		}
	}
	
	
	/**
	 * Changes the incident to be of a different category
	 * 
	 * @param category	the category to change the current category to
	 * 
	 * @throws IllegalArgumentException if the string could not be converted into an enumeration
	 */
	private void setCategory(String category) {
		if (category.equals(C_INQUIRY)) {
			this.category = Category.INQUIRY;
		}
		
		else if (category.equals(C_SOFTWARE)) {
			this.category = Category.SOFTWARE;
		}
		
		else if (category.equals(C_HARDWARE)) {
			this.category = Category.HARDWARE;
		}
		
		else if (category.equals(C_NETWORK)) {
			this.category = Category.NETWORK;
		}
		
		else if (category.equals(C_DATABASE)) {
			this.category = Category.DATABASE;
		}
		else {
			throw new IllegalArgumentException("Could not convert string to enumeration");
		}
	}
	

	/**
	 * Retrieves the priority String depending on the priority
	 * 
	 * @return a string of the priority
	 */
	public String getPriorityString() {
		if (priority == Priority.URGENT) {
			return P_URGENT;
		}
		
		else if (priority == Priority.HIGH) {
			return P_HIGH;
		}
		
		else if (priority == Priority.MEDIUM) {
			return P_MEDIUM;
		}
		
		else if (priority == Priority.LOW) {
			return P_LOW;
		}
		
		else {
			return null;
		}
	}


	/**
	 * Changes the priority to be of a different priority
	 * 
	 * @param priority	the priority to change the current priority to
	 * 
	 * @throws IllegalArgumentException if the string could not be converted into an enumeration
	 */
	private void setPriority(String priority) {
		if (priority.equals(P_URGENT)) {
			this.priority = Priority.URGENT;
		}
		
		else if (priority.equals(P_HIGH)) {
			this.priority = Priority.HIGH;
		}
		
		else if (priority.equals(P_MEDIUM)) {
			this.priority = Priority.MEDIUM;
		}
		
		else if (priority.equals(P_LOW)) {
			this.priority = Priority.LOW;
		}
		
		else {
			throw new IllegalArgumentException("Could not convert string to enumeration");
		}
	}


	/**
	 * Retrieves the onHoldReason String depending on the onHoldReason
	 * 
	 * @return a string of the onHoldReason
	 */
	public String getOnHoldReasonString() {
		if (onHoldReason == null) {
			return null;
		}
		
		else if (onHoldReason == OnHoldReason.AWAITING_CALLER) {
			return Command.OH_CALLER;
		}
		
		else if (onHoldReason == OnHoldReason.AWAITING_CHANGE) {
			return Command.OH_CHANGE;
		}
		
		else if (onHoldReason == OnHoldReason.AWAITING_VENDOR) {
			return Command.OH_VENDOR;
		}
		
		else {
			return "";
		}
	}


	/**
	 * Changes the onHoldReason to be of a different onHoldReason
	 * 
	 * @param onHoldReason	the onHoldReason to change the current onHoldReason to
	 */
	private void setOnHoldReason(String onHoldReason) {
		if (onHoldReason == null) {
			this.onHoldReason = null;
		}
		
		else if (onHoldReason.equals(Command.OH_CALLER)) {
			this.onHoldReason = OnHoldReason.AWAITING_CALLER;
		}
		
		else if (onHoldReason.equals(Command.OH_CHANGE)) {
			this.onHoldReason = OnHoldReason.AWAITING_CHANGE;
		}
		
		else if (onHoldReason.equals(Command.OH_VENDOR)) {
			this.onHoldReason = OnHoldReason.AWAITING_VENDOR;
		}
		
		else {
			this.onHoldReason = null;
		}
	}


	/**
	 * Retrieves the cancellationCode String depending on the cancellationCode
	 * 
	 * @return a string of the cancellationCode
	 */
	public String getCancellationCodeString() {
		if (cancellationCode == null) {
			return null;
		}
		
		else if (cancellationCode == CancellationCode.DUPLICATE) {
			return Command.CC_DUPLICATE;
		}
		
		else if (cancellationCode == CancellationCode.UNNECESSARY) {
			return Command.CC_UNNECESSARY;
		}
		
		else if (cancellationCode == CancellationCode.NOT_AN_INCIDENT) {
			return Command.CC_NOT_AN_INCIDENT;
		}
		
		else {
			return "";
		}
	}


	/**
	 * Changes the cancellationCode to be of a different cancellationCode
	 * 
	 * @param cancellationCode	the cancellationCode to change the current cancellationCode to
	 */
	private void setCancellationCode(String cancellationCode) {
		if (cancellationCode == null) {
			this.cancellationCode = null;
		}
		
		else if (cancellationCode.equals(Command.CC_DUPLICATE)) {
			this.cancellationCode = CancellationCode.DUPLICATE;
		}
		
		else if (cancellationCode.equals(Command.CC_UNNECESSARY)) {
			this.cancellationCode = CancellationCode.UNNECESSARY;
		}
		
		else if (cancellationCode.equals(Command.CC_NOT_AN_INCIDENT)) {
			this.cancellationCode = CancellationCode.NOT_AN_INCIDENT;
		}
		
		else {
			this.cancellationCode = null;
		}
	}


	/**
	 * Retrieves the state of the incident
	 * 
	 * @return the current state of the incident
	 */
	public IncidentState getState() {
		return state;
	}


	/**
	 * Changes the state of the incident to a new state
	 * 
	 * @param the state to set the current state to
	 */
	private void setState(String state) {
		if (state.equals(NEW_NAME)) {
			this.state = newState;
		}
		
		else if (state.equals(IN_PROGRESS_NAME)) {
			this.state = inProgressState;
		}
		
		else if (state.equals(ON_HOLD_NAME)) {
			this.state = onHoldState;
		}
		
		else if (state.equals(RESOLVED_NAME)) {
			this.state = resolvedState;
		}
		
		else if (state.equals(CLOSED_NAME)) {
			this.state = closedState;
		}
		
		else if (state.equals(CANCELED_NAME)) {
			this.state = canceledState;
		}
		
		else {
			throw new IllegalArgumentException("Could not convert string to state");
		}
	}


	/**
	 * Retrieves the resolutionCode of the incident
	 * 
	 * @return the resolutionCode
	 */
	public ResolutionCode getResolutionCode() {
		return resolutionCode;
	}

	
	
	/**
	 * Retrieves the resolutionCode String depending on the resolutionCode
	 * 
	 * @return a string of the resolutionCode
	 */
	public String getResolutionCodeString() {
		if (resolutionCode == null) {
			return null;
		}
		
		else if (resolutionCode == ResolutionCode.PERMANENTLY_SOLVED) {
			return Command.RC_PERMANENTLY_SOLVED;
		}
		
		else if (resolutionCode == ResolutionCode.WORKAROUND) {
			return Command.RC_WORKAROUND;
		}
		
		else if (resolutionCode == ResolutionCode.NOT_SOLVED) {
			return Command.RC_NOT_SOLVED;
		}
			
		else if (resolutionCode == ResolutionCode.CALLER_CLOSED) {
			return Command.RC_CALLER_CLOSED;
		}
		
		else {
			return "";
		}
	}
	

	/**
	 * Changes the resolutionCode to be of a different resolutionCode
	 * 
	 * @param resolutionCode	the resolutionCode to change the current resolutionCode to
	 */
	private void setResolutionCode(String resolutionCode) {
		
		if (resolutionCode == null) {
			this.resolutionCode = null;
		}
		
		else if (resolutionCode.equals("Permanently Solved")) {
			this.resolutionCode = ResolutionCode.PERMANENTLY_SOLVED;
		}
		
		else if (resolutionCode.equals("Workaround")) {
			this.resolutionCode = ResolutionCode.WORKAROUND;
		}
		
		else if (resolutionCode.equals("Not Solved")) {
			this.resolutionCode = ResolutionCode.NOT_SOLVED;
		}
		
		else if (resolutionCode.equals("Caller Closed")) {
			this.resolutionCode = ResolutionCode.CALLER_CLOSED;
		}
		
		else {
			this.resolutionCode = null;
		}
	}


	/**
	 * Retrieves the owner string of the incident
	 * 
	 * @return user id of the incident owner 
	 */
	public String getOwner() {
		return owner;
	}


	/**
	 * Retrieves the name string of the incident
	 * 
	 * @return the name of the incident
	 */
	public String getName() {
		return name;
	}


	/**
	 * Retrieves the caller string of the incident
	 * 
	 * @return the caller of the incident
	 */
	public String getCaller() {
		return caller;
	}


	/**
	 * Retrieves the ArrayList of work notes of the incident
	 * 
	 * @return the ArrayList of work notes
	 */
	public ArrayList<String> getNotes() {
		return notes;
	}
	
	
	/**
	 * Converts the work notes into a String
	 * 
	 * @return a string of the work notes
	 */
	public String getNotesString() {
		String s = "";
		for (int i = 0; i < notes.size(); i++) {
			s += notes.get(i) + "\n-------\n";
		}
		
		return s;
	}
	
	
	/**
	 * This method drives the finite state machine by delegating the Command to the current state and if successful adding non-null notes to the notes list
	 * 
	 * @param c		the command being used to delegate the current state to
	 */
	public void update(Command c) {
		state.updateState(c);
		
		switch (c.getCommand()) {
		case INVESTIGATE:
			state = inProgressState;
			owner = c.getOwnerId();
			notes.add(c.getWorkNote());
			break;
			
		case HOLD:
			state = onHoldState;
			onHoldReason = c.getOnHoldReason();
			notes.add(c.getWorkNote());
			resolutionCode = null;
			break;
			
		case RESOLVE:
			state = resolvedState;
			resolutionCode = c.getResolutionCode();
			notes.add(c.getWorkNote());
			
			if (onHoldReason == OnHoldReason.AWAITING_CHANGE) {
				changeRequest = c.getWorkNote();
			}
			
			onHoldReason = null;
			break;
		
		case CONFIRM:
			state = closedState;
			notes.add(c.getWorkNote());
			break;
			
		case REOPEN:						
			state = inProgressState;
			notes.add(c.getWorkNote());
			
			if (onHoldReason == OnHoldReason.AWAITING_CHANGE) {
				changeRequest = c.getWorkNote();
			}
			
			onHoldReason = null;
			resolutionCode = null;
			cancellationCode = null;
			break;
			
		case CANCEL:
			state = canceledState;
			cancellationCode = c.getCancellationCode();
			notes.add(c.getWorkNote());
			resolutionCode = null;
			onHoldReason = null;
			break;
		
		default:
		}
	}
	
	
	/**
	 * Retrieves an XML file of the incident
	 * 
	 * @return the incident that was converted
	 */
	public Incident getXMLIncident() {
		Incident incident = new Incident();
		
		incident.setCaller(caller);
		incident.setCategory(getCategoryString());
		incident.setPriority(getPriorityString());
		incident.setName(name);
		
		incident.setOwner(owner);
		incident.setId(incidentId);
		incident.setChangeRequest(changeRequest);
		
		incident.setOnHoldReason(getOnHoldReasonString());
		incident.setResolutionCode(getResolutionCodeString());
		incident.setCancellationCode(getCancellationCodeString());
		
		incident.setState(state.getStateName());		
				
		WorkNotes workNotes = new WorkNotes();
		
		for (int i = 0; i < this.getNotes().size(); i++) {
			workNotes.getNotes().add(getNotes().get(i));
		}
		
		incident.setWorkNotes(workNotes);
		
		return incident;
	}
	
	/**
	 * Sets the counter instance variable to the entered value
	 * @param num number the counter will be set to
	 */
	public static void setCounter(int num) {
		counter = num;
	}


	/** An enumeration contained in the ManagedIncident class. Contains five possible categories of incidents. */
	public enum Category { INQUIRY, SOFTWARE, HARDWARE, NETWORK, DATABASE }

	/** An enumeration contained in the ManagedIncident class. Contains four possible priorities for ManagedIncidents. */
	public enum Priority { URGENT, HIGH, MEDIUM, LOW }

	
	/**
	 * The inner class used for an incident in the New state. Implements methods from the IncidentState interface.
	 * 
	 * @author Bilal Mohamad
	 * @author Keaton Thurston
	 */
	public class NewState implements IncidentState {
		
		/**
		 * Update the ManagedIncident based on the given Command.
		 * An UnsupportedOperationException is thrown if the CommandValue
		 * is not a valid action for the given state.  
		 * 
		 * @param command Command describing the action that will update the ManagedIncident's state.
		 * @throws UnsupportedOperationException if the CommandValue is not a valid action for the given state.
		 */
		public void updateState (Command command) {
			if (command.getCommand() != CommandValue.INVESTIGATE && command.getCommand() != CommandValue.CANCEL) {
				throw new UnsupportedOperationException();
			}
		}
		
		
		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return NEW_NAME;
		}
	}

	
	/**
	 * The inner class used for an incident in the InProgress state. Implements methods from the IncidentState interface.
	 * 
	 * @author Bilal Mohamad
	 * @author Keaton Thurston
	 */
	public class InProgressState implements IncidentState {
		
		/**
		 * Update the ManagedIncident based on the given Command.
		 * An UnsupportedOperationException is thrown if the CommandValue
		 * is not a valid action for the given state.  
		 * 
		 * @param command Command describing the action that will update the ManagedIncident's state.
		 * @throws UnsupportedOperationException if the CommandValue is not a valid action for the given state.
		 */
		public void updateState (Command command) {
			
			if (command.getCommand() != CommandValue.HOLD && command.getCommand() != CommandValue.RESOLVE &&
					command.getCommand() != CommandValue.CANCEL) {
				throw new UnsupportedOperationException();
			}
		}
		
		
		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return IN_PROGRESS_NAME;
		}
	}



	/**
	 * The inner class used for an incident in the OnHold state. Implements methods from the IncidentState interface.
	 * 
	 * @author Bilal Mohamad
	 * @author Keaton Thurston
	 */
	public class OnHoldState implements IncidentState {
		
		/**
		 * Update the ManagedIncident based on the given Command.
		 * An UnsupportedOperationException is thrown if the CommandValue
		 * is not a valid action for the given state.  
		 * 
		 * @param command Command describing the action that will update the ManagedIncident's state.
		 * @throws UnsupportedOperationException if the CommandValue is not a valid action for the given state.
		 */
		public void updateState (Command command) {
			
			if (command.getCommand() != CommandValue.REOPEN && command.getCommand() != CommandValue.RESOLVE && 
					command.getCommand() != CommandValue.CANCEL) {
				throw new UnsupportedOperationException();
			}
		}
		
		
		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return ON_HOLD_NAME;
		}
	}
	
	
	
	/**
	 * The inner class used for an incident in the Resolved state. Implements methods from the IncidentState interface.
	 * 
	 * @author Bilal Mohamad
	 * @author Keaton Thurston
	 */
	public class ResolvedState implements IncidentState {
		
		/**
		 * Update the ManagedIncident based on the given Command.
		 * An UnsupportedOperationException is thrown if the CommandValue
		 * is not a valid action for the given state.  
		 * 
		 * @param command Command describing the action that will update the ManagedIncident's state.
		 * @throws UnsupportedOperationException if the CommandValue is not a valid action for the given state.
		 */
		public void updateState (Command command) {
			
			if (command.getCommand() != CommandValue.CONFIRM && command.getCommand() != CommandValue.CANCEL &&
					command.getCommand() != CommandValue.HOLD && command.getCommand() != CommandValue.REOPEN) {
				throw new UnsupportedOperationException();
			}
		}
		
		
		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return RESOLVED_NAME;
		}
	}
	
	
	/**
	 * The inner class used for an incident in the Closed state. Implements methods from the IncidentState interface.
	 * 
	 * @author Bilal Mohamad
	 * @author Keaton Thurston
	 */
	public class ClosedState implements IncidentState {
		
		/**
		 * Update the ManagedIncident based on the given Command.
		 * An UnsupportedOperationException is thrown if the CommandValue
		 * is not a valid action for the given state.  
		 * 
		 * @param command Command describing the action that will update the ManagedIncident's state.
		 * @throws UnsupportedOperationException if the CommandValue is not a valid action for the given state.
		 */
		public void updateState (Command command) {
			
			if (command.getCommand() != CommandValue.REOPEN) {
				throw new UnsupportedOperationException();
			}
		}
		
		
		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return CLOSED_NAME;
		}
	}

	
	/**
	 * The inner class used for an incident in the Canceled state. Implements methods from the IncidentState interface.
	 * 
	 * @author Bilal Mohamad
	 * @author Keaton Thurston
	 */
	public class CanceledState implements IncidentState {
		
		/**
		 * Update the ManagedIncident based on the given Command.
		 * An UnsupportedOperationException is thrown if the CommandValue
		 * is not a valid action for the given state.  
		 * 
		 * @param command Command describing the action that will update the ManagedIncident's state.
		 * @throws UnsupportedOperationException if the CommandValue is not a valid action for the given state.
		 */
		public void updateState (Command command) {
			throw new UnsupportedOperationException();
		}
		
		
		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return CANCELED_NAME;
		}
	}
	
}
