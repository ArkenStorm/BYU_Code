package DAO;

import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
	private Database db;
	EventDAO edao;

	@BeforeEach
	void setUp() throws DataAccessException {
		db = Database.getInstance();
		db.openConnection();
		db.createTables();
		edao = EventDAO.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException {
		db.closeConnection(false);
	}

	@Test
	void insertEventSuccess() throws DataAccessException {
		Event event = new Event("apocalypse", "Ragnarok", "korg", 7.6667, 23.6833, "Norway", "Hammerfest", "World Destruction", 2087);
		edao.insertEvent(event);
		Event expected = edao.getEventByID("apocalypse");
		Assertions.assertEquals(expected, event);
	}

	@Test
	void insertEventFail() {
		Event event = new Event(null, null, null, 0.0, 0.0, null, null, null, 0);
		Assertions.assertThrows(DataAccessException.class, () -> edao.insertEvent(event));
	}

	@Test
	void getEventByIDSuccess() throws DataAccessException {
		Event event = new Event("apocalypse", "Ragnarok", "korg", 7.6667, 23.6833, "Norway", "Hammerfest", "World Destruction", 2087);
		edao.insertEvent(event);
		Event expected = edao.getEventByID("apocalypse");
		Assertions.assertEquals(expected, event);
	}

	@Test
	void getEventByIDFail() throws DataAccessException {
		Event expected = edao.getEventByID("Hammerfall");
		Assertions.assertNull(expected);
	}

	@Test
	void getUserEventsSuccess() throws DataAccessException {
		Event birth = new Event("thursday", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Thor's day", 100);
		Event coronation = new Event("odinson", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Coronation", 2012);
		Event resignation = new Event("lokitrickedusall", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Renunciation of Kingship", 2019);
		edao.insertEvent(birth);
		edao.insertEvent(coronation);
		edao.insertEvent(resignation);
		List<Event> compare = new ArrayList<>();
		compare.add(birth);
		compare.add(coronation);
		compare.add(resignation);
		List<Event> userEvents = edao.getUserEvents("thor");
		Assertions.assertEquals(compare, userEvents);
	}

	@Test
	void getUserEventsFail() throws DataAccessException {
		Event birth = new Event("thursday", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Thor's day", 100);
		Event coronation = new Event("odinson", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Coronation", 2012);
		Event resignation = new Event("lokitrickedusall", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Renunciation of Kingship", 2019);
		edao.insertEvent(birth);
		edao.insertEvent(coronation);
		edao.insertEvent(resignation);
		List<Event> userEvents = edao.getUserEvents("stark");
		Assertions.assertTrue(userEvents.isEmpty());
	}

	/**
	 * Clears a non-empty table
	 */
	@Test
	void clearEvents() throws DataAccessException, SQLException {
		Event event = new Event("apocalypse", "Ragnarok", "korg", 7.6667, 23.6833, "Norway", "Hammerfest", "World Destruction", 2087);
		edao.insertEvent(event);
		edao.clearEvents();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM Event";
		PreparedStatement stmt = connect.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int count = -1;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		Assertions.assertEquals(0, count);
	}

	/**
	 * Clears an empty table
	 */
	@Test
	void clearEventsEmpty() throws DataAccessException, SQLException {
		edao.clearEvents();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM Event";
		PreparedStatement stmt = connect.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int count = -1;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		Assertions.assertEquals(0, count);
	}
}
