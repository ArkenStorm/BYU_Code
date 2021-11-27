package request;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

/**
 * A Load Request.
 */
public class LoadRequest {
	/**
	 * The list of Users to be loaded
	 */
	private List<User> users;

	/**
	 * The list of Persons to be loaded
	 */
	private List<Person> persons;

	/**
	 * The list of Events to be loaded
	 */
	private List<Event> events;

	/**
	 * Instantiates a new Load request.
	 *
	 * @param users   the users
	 * @param persons the persons
	 * @param events  the events
	 */
	public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
		this.users = users;
		this.persons = persons;
		this.events = events;
	}

	/**
	 * Gets users.
	 *
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * Sets users.
	 *
	 * @param users the users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Gets persons.
	 *
	 * @return the persons
	 */
	public List<Person> getPersons() {
		return persons;
	}

	/**
	 * Sets persons.
	 *
	 * @param persons the persons
	 */
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	/**
	 * Gets events.
	 *
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * Sets events.
	 *
	 * @param events the events
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
