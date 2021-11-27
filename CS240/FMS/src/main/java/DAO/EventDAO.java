package DAO;

import model.Event;
import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for Event.
 */
public class EventDAO {
	private Database db;
	private static EventDAO instance;

	public static EventDAO getInstance() {
		if (instance == null) {
			instance = new EventDAO(Database.getInstance());
		}
		return instance;
	}

	public EventDAO(Database db) {
		this.db = db;
	}

	/**
	 * Inserts an Event.
	 *
	 * @param event the event
	 */
	public void insertEvent(Event event) throws DataAccessException {
		Connection connect = db.getConnection();
		// language=sql
		String sql = "INSERT INTO Event (EventID, Username, PersonID, Latitude, Longitude, " +
				"Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = connect.prepareStatement(sql)) {
			stmt.setString(1, event.getEventID());
			stmt.setString(2, event.getUsername());
			stmt.setString(3, event.getPersonID());
			stmt.setDouble(4, event.getLatitude());
			stmt.setDouble(5, event.getLongitude());
			stmt.setString(6, event.getCountry());
			stmt.setString(7, event.getCity());
			stmt.setString(8, event.getEventType());
			stmt.setInt(9, event.getYear());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Error encountered while inserting into the database");
		}
	}

	/**
	 * Deletes an Event by its ID.
	 *
	 * @param eventID the event id
	 */
	public void deleteEvent(String eventID) throws DataAccessException {
		PreparedStatement stmt = null;
		Connection connect = db.getConnection();
		try {
			// language=sql
			String sql = "DELETE FROM Event WHERE EventID = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, eventID);
		}
		catch(SQLException e) {
			throw new DataAccessException("SQL Error encountered while deleting from Event table");
		}
	}

	/**
	 * Gets an Event by ID.
	 *
	 * @param eventID the event ID.
	 * @return the Event by ID.
	 */
	public Event getEventByID(String eventID) throws DataAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connect = db.getConnection();
		try {
			//language=sql
			String sql = "SELECT * FROM Event WHERE EventID = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, eventID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new Event(
						rs.getString("EventID"),
						rs.getString("Username"),
						rs.getString("PersonID"),
						rs.getDouble("Latitude"),
						rs.getDouble("Longitude"),
						rs.getString("Country"),
						rs.getString("City"),
						rs.getString("EventType"),
						rs.getInt("Year")
				);
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when querying from Event");
		}
	}


	/**
	 * Gets a list of Events for a given username.
	 *
	 * @param username the username
	 */
	public List<Event> getUserEvents(String username) throws DataAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connect = db.getConnection();
		try {
			//language=sql
			String sql = "SELECT * FROM Event WHERE Username = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			List<Event> eventList = new ArrayList<>();
			while (rs.next()) {
				eventList.add(new Event(
						rs.getString("EventID"),
						rs.getString("Username"),
						rs.getString("PersonID"),
						rs.getDouble("Latitude"),
						rs.getDouble("Longitude"),
						rs.getString("Country"),
						rs.getString("City"),
						rs.getString("EventType"),
						rs.getInt("Year")
				));
			}
			return eventList;
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when querying from Person");
		}
	}

	/**
	 * Clears all Events from the database.
	 * @throws DataAccessException
	 */
	public void clearEvents() throws DataAccessException {
		Connection connect = db.getConnection();
		try (Statement stmt = connect.createStatement()){
			String sql = "DELETE FROM Event";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("SQL Error encountered while clearing tables");
		}
	}
}
