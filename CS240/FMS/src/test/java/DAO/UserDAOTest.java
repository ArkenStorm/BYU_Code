package DAO;

import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
	Database db;
	UserDAO udao;

	@BeforeEach
	void setUp() throws DataAccessException {
		db = Database.getInstance();
		db.openConnection();
		db.createTables();
		udao = UserDAO.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException {
		db.closeConnection(false);
	}

	@Test
	void insertUserSuccess() throws DataAccessException {
		User user = new User("billyjo", "hunter2", "billyjo@gmail.com", "Billy", "Joel", "m", "bj42");
		udao.insertUser(user);
		User expected = udao.getUserByUsername("billyjo");
		Assertions.assertEquals(expected, user);
	}

	@Test
	void insertUserFail() {
		User user = new User(null, null, null, null, null, null, null);
		Assertions.assertThrows(DataAccessException.class, () -> udao.insertUser(user));
	}

	@Test
	void getUserByUsernameSuccess() throws DataAccessException {
		User user = new User("billyjo", "hunter2", "billyjo@gmail.com", "Billy", "Joel", "m", "bj42");
		udao.insertUser(user);
		User expected = udao.getUserByUsername("billyjo");
		Assertions.assertEquals(expected, user);
	}

	@Test
	void getUserByUsernameFail() throws DataAccessException {
		User expected = udao.getUserByUsername("ledZeppelin");
		Assertions.assertNull(expected);
	}

	/**
	 * Clears a non-empty table
	 */
	@Test
	void clearUsers() throws DataAccessException, SQLException {
		User user = new User("billyjo", "hunter2", "billyjo@gmail.com", "Billy", "Joel", "m", "bj42");
		udao.insertUser(user);
		udao.clearUsers();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM User";
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
	void clearUsersEmpty() throws DataAccessException, SQLException {
		udao.clearUsers();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM User";
		PreparedStatement stmt = connect.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int count = -1;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		Assertions.assertEquals(0, count);
	}
}