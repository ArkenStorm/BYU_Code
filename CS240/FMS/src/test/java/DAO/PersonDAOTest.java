package DAO;

import model.Person;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {
	Database db;
	PersonDAO pdao;

	@BeforeEach
	void setUp() throws DataAccessException {
		db = Database.getInstance();
		db.openConnection();
		db.createTables();
		pdao = PersonDAO.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException {
		db.closeConnection(false);
	}

	/**
	 * Tests for correct insertion into the table
	 * @throws DataAccessException
	 */
	@Test
	void insertPersonSuccess() throws DataAccessException {
		Person person = new Person("bj42", "BillyJo", "Billy", "Joel", "m", "bjsr", "mamaj", "");
		pdao.insertPerson(person);
		Person expected = pdao.getPersonByID("bj42");
		Assertions.assertEquals(expected, person);
	}

	/**
	 * Tests if omitting a required field (personID) throws an exception
	 */
	@Test
	void insertPersonFail() {
		Person person = new Person(null, null, null, null, null, null, null, null);
		Assertions.assertThrows(DataAccessException.class, () -> pdao.insertPerson(person));
	}

	@Test
	void getPersonByIDSuccess() throws DataAccessException {
		Person person = new Person("bj42", "BillyJo", "Billy", "Joel", "m", "bjsr", "mamaj", "");
		pdao.insertPerson(person);
		Person expected = pdao.getPersonByID("bj42");
		Assertions.assertEquals(expected, person);
	}

	@Test
	void getPersonByIDFail() throws DataAccessException {
		Person expected = pdao.getPersonByID("nobody");
		Assertions.assertNull(expected);
	}

	/**
	 * Clear a non-empty table
	 */
	@Test
	void clearPersons() throws DataAccessException, SQLException {
		Person person = new Person("bj42", "BillyJo", "Billy", "Joel", "m", "bjsr", "mamaj", "");
		pdao.insertPerson(person);
		pdao.clearPersons();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM Person";
		PreparedStatement stmt = connect.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int count = -1;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		Assertions.assertEquals(0, count);
	}

	/**
	 * Clear an empty table
	 */
	@Test
	void clearPersonsEmpty() throws DataAccessException, SQLException {
		pdao.clearPersons();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM Person";
		PreparedStatement stmt = connect.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int count = -1;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		Assertions.assertEquals(count, 0);
	}
}