package service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import model.AuthToken;
import model.Event;
import model.User;
import result.EventArrayResult;
import result.EventResult;
import result.MessageResult;
import result.Result;

import java.util.List;

/**
 * Service for finding Events.
 */
public class EventService {
	/**
	 * Gets event by id.
	 *
	 * @param eventID   the event id
	 * @param authToken the auth token
	 * @return the event by id
	 */
	public Result getEventByID(String eventID, String authToken) {
		Database db = Database.getInstance();
		EventDAO edao = EventDAO.getInstance();
		AuthTokenDAO atdao = AuthTokenDAO.getInstance();
		Event event;
		try {
			AuthToken token = atdao.getToken(authToken);
			if (token == null) {
				return new MessageResult("Error: The current user is unauthorized to make that request");
			}
			event = edao.getEventByID(eventID);
			if (event == null) {
				return new MessageResult("Error: No event with that eventID exists");
			}
			else if (!event.getUsername().equals(token.getUsername())) {
				return new MessageResult("Error: That event does not belong to the current user");
			}
			db.closeConnection(true);
		}
		catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		return new EventResult(event);
	}

	/**
	 * Gets ALL the Events for ALL family members of the current User, based on an AuthToken.
	 *
	 * @param authToken the auth token
	 * @return the events by person
	 */
	public Result getEventsByPerson(String authToken) {
		Database db = Database.getInstance();
		EventDAO edao = new EventDAO(db);
		AuthTokenDAO atdao = new AuthTokenDAO(db);
		List<Event> eventList;
		try {
			AuthToken token = atdao.getToken(authToken);
			if (token == null) {
				return new MessageResult("Error: The current user is not authorized to make this request");
			}
			eventList = edao.getUserEvents(token.getUsername());
			db.closeConnection(true);
		} catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		return new EventArrayResult(eventList);
	}
}
