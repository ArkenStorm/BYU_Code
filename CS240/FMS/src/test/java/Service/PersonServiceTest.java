package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import model.AuthToken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.MessageResult;
import result.PersonArrayResult;
import result.PersonResult;
import service.ClearService;
import service.PersonService;

import java.sql.SQLException;

public class PersonServiceTest {
	PersonService ps;
	Database db;

	@BeforeEach
	void setUp() throws DataAccessException {
		ps = new PersonService();
		db = Database.getInstance();
		PersonDAO pdao = new PersonDAO(db);
		Person person1 = new Person("bj42", "thor", "Billy", "Joel", "m", "bjsr", "mamaj", "");
		Person person2 = new Person("strongestAvenger", "thor", "Thor", "Odinson", "m", "odin", "freya", "");
		pdao.insertPerson(person1);
		pdao.insertPerson(person2);
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		ClearService cs = new ClearService();
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void getPersonByIDSuccess() throws DataAccessException {
		AuthToken token = new AuthToken("authorized", "thor");
		AuthTokenDAO atdao = new AuthTokenDAO(db);
		atdao.insertToken(token);
		PersonResult person = (PersonResult) ps.getPersonByID("authorized", "bj42");
		Assertions.assertEquals("bj42", person.getPersonID());
	}

	@Test
	void getPersonByIDFail() {
		MessageResult result = (MessageResult) ps.getPersonByID("authorized", "bj42");
		Assertions.assertEquals("Error: The current user is unauthorized to make that request", result.getMessage());
	}

	@Test
	void getPersonsSuccess() throws DataAccessException {
		AuthToken token = new AuthToken("authorized", "thor");
		AuthTokenDAO atdao = new AuthTokenDAO(db);
		atdao.insertToken(token);
		PersonArrayResult result = (PersonArrayResult) ps.getPersons("authorized");
		for (Person person : result.getData()) {
			Assertions.assertEquals("thor", person.getUsername());
		}
	}

	@Test
	void getPersonsFail() {
		MessageResult result = (MessageResult) ps.getPersons("authorized");
		Assertions.assertEquals("Error: The current user is not authorized to make this request", result.getMessage());
	}
}
