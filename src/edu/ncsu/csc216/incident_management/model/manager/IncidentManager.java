/**
 * 
 */
package edu.ncsu.csc216.incident_management.model.manager;

import java.util.List;

import edu.ncsu.csc216.incident.io.IncidentIOException;
import edu.ncsu.csc216.incident.io.IncidentReader;
import edu.ncsu.csc216.incident.io.IncidentWriter;
import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * Controls the creation and modification of ManagedIncidentLists. Implements the Singleton
 * design pattern, meaning only one instance of IncidentManager can ever be created. This 
 * ensures that all parts of the IncidentManagerGUI are interacting with the same IncidentManager 
 * at all times.
 * 
 * @author Keaton Thurston
 * @author Bilal Mohamad
 */
public class IncidentManager {

	/** ONLY instance of IncidentManager */
	private static IncidentManager singleton;

	/** List of managed incidents */
	private ManagedIncidentList incidentList;
	
	/** Number of columns in the 2D String arrays of ManagedIncidents */
	public static final int COLUMNS = 5;
	
	/** Index of incident ID in the 2D String arrays of ManagedIncidents */
	public static final int ID_INDEX = 0;

	/** Index of incident category in the 2D String arrays of ManagedIncidents */
	public static final int CATEGORY_INDEX = 1;
	
	/** Index of incident state name in the 2D String arrays of ManagedIncidents */
	public static final int STATE_NAME_INDEX = 2;
	
	/** Index of incident priority in the 2D String arrays of ManagedIncidents */
	public static final int PRIORITY_INDEX = 3;
	
	/** Index of incident name in the 2D String arrays of ManagedIncidents */
	public static final int NAME_INDEX = 4;
	
	
	/** Constructor for the one instance of IncidentManager */
	private IncidentManager() {
		incidentList = new ManagedIncidentList();
	}

	/** 
	 * Static method that returns the singleton instance of IncidentManager.
	 * @return singleton instance of IncidentManager
	 */
	public static IncidentManager getInstance() {
		
		if (IncidentManager.singleton == null) {
			IncidentManager.singleton = new IncidentManager();
		}
		return IncidentManager.singleton;
		
		
		/*if (singleton != null) {
			return singleton;
		}
		singleton = new IncidentManager();
		return singleton;*/
	}

	/** 
	 * Writes managed incidents to a file.
	 * @param fileName name of the file to write to
	 * @throws IllegalArgumentException if an IncidentIOException is thrown by IncidentWriter
	 */
	public void saveManagedIncidentsToFile(String fileName) {
		
		try {
			IncidentWriter writer = new IncidentWriter(fileName);
			for (int i = 0; i < incidentList.getManagedIncidents().size(); i++) {
				writer.addItem(incidentList.getManagedIncidents().get(i).getXMLIncident());
			}
		
			writer.marshal();
		} catch (IncidentIOException e) {
			throw new IllegalArgumentException();
		}
	}


	/**
	 * Creates a list of managed incidents from a given file
	 * @param fileName name of the file containing the managed incidents
	 * @throws IllegalArgumentException if an IncidentIOException is thrown by IncidentWriter
	 */
	public void loadManagedIncidentsFromFile(String fileName) {
//		singleton.createNewManagedIncidentList();
		IncidentReader reader = null;
		try {
			reader = new IncidentReader(fileName);
			List<Incident> incidents = reader.getIncidents();
			incidentList.addXMLIncidents(incidents);
		} catch (IncidentIOException e) {
			throw new IllegalArgumentException();
		}
	}

	
	/**
	 * Creates a new ManagedIncidentList.
	 */
	public void createNewManagedIncidentList() {
		incidentList = new ManagedIncidentList();
	}

	/**
	 * Retrieves a two-dimensional String array of managed incidents. The array has 1 row for every 
	 * ManagedIncident that you need to return. The array has 5 columns: id number, category, 
	 * state name, priority, and name.
	 * @return two-dimensional String array of managed incidents
	 */
	public String[][] getManagedIncidentsAsArray() {
		
		//TODO CHANGE THIS TO BE MORE SIMPLE
		/*List<ManagedIncident> list = incidentList.getManagedIncidents();
		String[][] results = new String[list.size()][];
		int i = 0;
		for (Iterator<ManagedIncident> iterator = list.iterator(); iterator.hasNext();) {
			ManagedIncident incident = (ManagedIncident) iterator.next();
			
			String[] row = new String[5];
			row[0] = Integer.toString((incident.getIncidentId()));
			row[1] = incident.getCategoryString();
			row[2] = incident.getState().getStateName();
			row[3] = incident.getPriorityString();
			row[4] = incident.getName();
			results[i] = row;
			i++;
		}
		
		return results;*/
		
		
		
		
		String[][] managedIncidentsArray = new String[incidentList.getManagedIncidents().size()][COLUMNS];
		for (int i = 0; i < incidentList.getManagedIncidents().size(); i++) {
			managedIncidentsArray[i][ID_INDEX] = Integer.toString(incidentList.getManagedIncidents().get(i).getIncidentId());
			managedIncidentsArray[i][CATEGORY_INDEX] = incidentList.getManagedIncidents().get(i).getCategoryString();
			
			//The following line causes issues
			managedIncidentsArray[i][STATE_NAME_INDEX] = incidentList.getManagedIncidents().get(i).getState().getStateName();
			
			managedIncidentsArray[i][PRIORITY_INDEX] = incidentList.getManagedIncidents().get(i).getPriorityString();
			managedIncidentsArray[i][NAME_INDEX] = incidentList.getManagedIncidents().get(i).getName();
		}
		return managedIncidentsArray;
	}

	/**
	 * Retrieves a two-dimensional String array of managed incidents of a given category. The 
	 * array has 1 row for every ManagedIncident that you need to return. The array has 5 
	 * columns: id number, category, state name, priority, and name.
	 * @param category category of ManagedIncidents
	 * @throws IllegalArgumentException if the given category is null
	 * @return two-dimensional String array of managed incidents of the given category
	 *
	 */
	public String[][] getManagedIncidentsAsArrayByCategory(Category category) {
		if (category == null) {
			throw new IllegalArgumentException();
		}

		String[][] managedIncidentsArray = new String[incidentList.getIncidentsByCategory(category).size()][COLUMNS];
		for (int i = 0; i < incidentList.getIncidentsByCategory(category).size(); i++) {
			managedIncidentsArray[i][ID_INDEX] = Integer.toString(incidentList.getIncidentsByCategory(category).get(i).getIncidentId());
			managedIncidentsArray[i][CATEGORY_INDEX] = incidentList.getIncidentsByCategory(category).get(i).getCategoryString();
			managedIncidentsArray[i][STATE_NAME_INDEX] = incidentList.getIncidentsByCategory(category).get(i).getState().getStateName();
			managedIncidentsArray[i][PRIORITY_INDEX] = incidentList.getIncidentsByCategory(category).get(i).getPriorityString();
			managedIncidentsArray[i][NAME_INDEX] = incidentList.getIncidentsByCategory(category).get(i).getName();
		}
		return managedIncidentsArray;
	}

	/**
	 * Retrieves the ManagedIncident with the given id.
	 * @param id identification number
	 * @return ManagedIncident with the given id
	 */
	public ManagedIncident getManagedIncidentById(int id) {
		return incidentList.getIncidentById(id);
	}

	/**
	 * Executes on a given command of a Command object for the given ManagedIncident.
	 * @param id id of the managed incident
	 * @param c command
	 */
	public void executeCommand(int id, Command c) {
		incidentList.executeCommand(id, c);
		/*
		int incidentIndex = 0;
		for (int i = 0; i < incidentList.getManagedIncidents().size(); i++) {
			if (incidentList.getManagedIncidents().get(i).getIncidentId() == id) {
				incidentIndex = i;
			}
		}
		incidentList.getManagedIncidents().get(incidentIndex).update(c);*/
	}

	/**
	 * Removes a ManagedIncident from it's ManagedIncident list.
	 * @param id id of the managed incident
	 */
	public void deleteManagedIncidentById(int id) {
		incidentList.deleteIncidentById(id);
	}

	/**
	 * Adds a new ManagedIncident to the ManagedIncidentList.
	 * 
	 * @param caller user id of person who reported the incident
	 * @param category Category of the incident
	 * @param priority Priority of the incident
	 * @param name incident’s name information
	 * @param workNote work note for the incident
	 */
	public void addManagedIncidentToList(String caller, Category category, Priority priority, 
			String name, String workNote) {
		incidentList.addIncident(caller, category, priority, name, workNote);
	}

}
