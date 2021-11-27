package service;

import DAO.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.MessageResult;

/**
 * Service for loading the database.
 */
public class LoadService {
	/**
	 * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
	 *
	 * @param request the request
	 * @return the message result
	 */
	public MessageResult loadDatabase(LoadRequest request) {
		ClearService cs = new ClearService();
		cs.clear(); // make sure clear succeeded?

		Database db = Database.getInstance();
		UserDAO udao = UserDAO.getInstance();
		PersonDAO pdao = PersonDAO.getInstance();
		EventDAO edao = EventDAO.getInstance();

		try {
			for (User user : request.getUsers()) {
				udao.insertUser(user);
			}
			for (Person person : request.getPersons()) {
				pdao.insertPerson(person);
			}
			for (Event event : request.getEvents()) {
				edao.insertEvent(event);
			}
			db.closeConnection(true);
		}
		catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		String result = "Successfully added " + request.getUsers().size() + " users, " +
						request.getPersons().size() + " persons, and " +
						request.getEvents().size() + " events to the database.";
		return new MessageResult(result);
	}
}
