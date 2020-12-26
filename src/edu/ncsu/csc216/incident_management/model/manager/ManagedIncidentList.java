 	/**
 * 
 */
package edu.ncsu.csc216.incident_management.model.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * Maintains a List of ManagedIncidents using ArrayList functionality.
 * 
 * @author Bilal Mohamad
 * @author Keaton Thurston
 */
public class ManagedIncidentList {

	/** ArrayList of ManagedIncidents */
	private ArrayList<ManagedIncident> incidents;
	
	/**
	 * Constructs a new ManagedIncidentList.
	 */
	public ManagedIncidentList() {
		ManagedIncident.setCounter(0);
		incidents = new ArrayList<ManagedIncident>();
	}

	/**
	 * Adds a ManagedIncident to the list.
	 * @param caller user id of person who reported the incident
	 * @param category Category of the incident
	 * @param priority Priority of the incident
	 * @param name incident’s name information
	 * @param workNote note about the incident
	 * @return id of added incident 
	 */
	public int addIncident(String caller, Category category, Priority priority, 
			String name, String workNote) {
		ManagedIncident incident = new ManagedIncident(caller, category, priority, name, workNote);
		incidents.add(incident);
		return incident.getIncidentId();
	}
	
	/**
	 * Adds ManagedIncident objects stored in an XML document.
	 * @param list list of XML incidents
	 */
	public void addXMLIncidents (List<Incident> list) {
//		ManagedIncident current;
		int maxId = 0;
		
		Iterator<Incident> it = list.iterator();
		
		while (it.hasNext()) {
			Incident inc = (Incident) it.next();
			ManagedIncident mi = new ManagedIncident(inc);
			
			maxId = inc.getId();
			incidents.add(mi);
		}
		
		/*for (int i = 0; i < list.size(); i++) {
			current = new ManagedIncident(list.get(i));
			incidents.add(current);
			maxId++;
		}	*/
		ManagedIncident.setCounter(maxId + 1);
	}
	
	/**
	 * Retrieves the list of ManagedIncidents.
	 * @return list of ManagedIncidents
	 */
	public List<ManagedIncident> getManagedIncidents() {
		return incidents;
	}
	
	/**
	 * Retrieves the list of ManagedIncidents of a given Category.
	 * @param category Category
	 * @return list of ManagedIncidents of a given Category
	 * @throws IllegalArgumentException if a null category parameter is given
	 */
	public List<ManagedIncident> getIncidentsByCategory(Category category) {
		if (category == null) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<ManagedIncident> categorizedIncidents = new ArrayList<ManagedIncident>();
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getCategory().equals(category)) {
				categorizedIncidents.add(incidents.get(i));
			}
		}
		return categorizedIncidents;
	}
	
	/**
	 * Retrieves the ManagedIncident with the given id.
	 * @param id identification number
	 * @return ManagedIncident with the given id
	 */
	public ManagedIncident getIncidentById(int id) {
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getIncidentId() == id) {
				return incidents.get(i);
			}
		}
		return null;
	}
	
	
	/**
	 * Executes on a given command of a Command object for the given ManagedIncident.
	 * @param id id of the incident
	 * @param c command
	 */
	public void executeCommand(int id, Command c) {
		int incidentIndex = 0;
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getIncidentId() == id) {
				incidentIndex = i;
			}
		}
		if (incidents.size() > 0) {
			incidents.get(incidentIndex).update(c);
		}
	}
	
	/**
	 * Removes a ManagedIncident from it's ManagedIncident list.
	 * @param id id of the managed incident
	 */
	public void deleteIncidentById(int id) {
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getIncidentId() == id) {
				incidents.remove(i);
			}
		}
	}
}
