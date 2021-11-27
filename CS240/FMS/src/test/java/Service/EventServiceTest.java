package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import model.AuthToken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventArrayResult;
import result.EventResult;
import result.MessageResult;
import service.ClearService;
import service.EventService;

import java.sql.SQLException;

public class EventServiceTest {
	EventService es;
	Database db;

	@BeforeEach
	void setUp() throws DataAccessException, SQLException {
		es = new EventService();
		db = Database.getInstance();
		AuthTokenDAO atdao = new AuthTokenDAO(db);
		atdao.insertToken(new AuthToken("godofthunder", "thor"));
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		ClearService cs = new ClearService();
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void getEventByIDSuccess() throws DataAccessException {
		EventDAO edao = new EventDAO(db);
		Event event = new Event("odinson", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Coronation", 2012);
		edao.insertEvent(event);
		EventResult result = (EventResult) es.getEventByID("odinson", "godofthunder");
		Assertions.assertEquals(result.getEventID(), event.getEventID());
	}

	@Test
	void getEventByIDFail() {
		MessageResult result = (MessageResult) es.getEventByID("lokistabbedme", "godofthunder");
		Assertions.assertEquals("Error: No event with that eventID exists", result.getMessage());
	}

	@Test
	void getEventsByPersonSuccess() throws DataAccessException {
		EventDAO edao = new EventDAO(db);
		Event birth = new Event("thursday", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Thor's day", 100);
		Event coronation = new Event("odinson", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Coronation", 2012);
		Event resignation = new Event("lokitrickedusall", "thor", "strongestAvenger", 7.6667, 23.6833, "Norway", "Hammerfest", "Renunciation of Kingship", 2019);
		edao.insertEvent(birth);
		edao.insertEvent(coronation);
		edao.insertEvent(resignation);
		EventArrayResult result = (EventArrayResult) es.getEventsByPerson("godofthunder");
		for (Event event : result.getData()) {
			Assertions.assertEquals(event.getPersonID(), "strongestAvenger");
		}
	}

	@Test
	void getEventsByPersonFail() {
		MessageResult result = (MessageResult) es.getEventsByPerson("invalidToken");
		Assertions.assertEquals("Error: The current user is not authorized to make this request", result.getMessage());
	}
}
