package DAO;

import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAOTest {
	private Database db;
	AuthTokenDAO atdao;

	@BeforeEach
	void setUp() throws DataAccessException {
		db = Database.getInstance();
		db.openConnection();
		db.createTables();
		atdao = AuthTokenDAO.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException {
		db.closeConnection(false);
	}

	@Test
	void insertTokenSuccess() throws DataAccessException {
		AuthToken token = new AuthToken("authorized", "JamesBond");
		atdao.insertToken(token);
		AuthToken expected = atdao.getToken("authorized");
		Assertions.assertEquals(expected, token);
	}

	@Test
	void insertTokenFail() throws DataAccessException {
		AuthToken token = new AuthToken(null, null);
		Assertions.assertThrows(DataAccessException.class, () -> atdao.insertToken(token));
	}

	@Test
	void getTokenSuccess() throws DataAccessException {
		AuthToken token = new AuthToken("authorized", "JamesBond");
		atdao.insertToken(token);
		AuthToken expected = atdao.getToken("authorized");
		Assertions.assertEquals(expected, token);
	}

	@Test
	void getTokenFail() throws DataAccessException {
		AuthToken token = atdao.getToken("brep");
		Assertions.assertNull(token);
	}

	/**
	 * Clears a non-empty table
	 */
	@Test
	void clearEvents() throws DataAccessException, SQLException {
		AuthToken token = new AuthToken("authorized", "TonyStark");
		atdao.insertToken(token);
		atdao.clearAuthTokens();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM AuthToken";
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
	void clearAuthTokensEmpty() throws DataAccessException, SQLException {
		atdao.clearAuthTokens();
		Connection connect = db.getConnection();
		String sql = "SELECT COUNT(*) FROM AuthToken";
		PreparedStatement stmt = connect.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int count = -1;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		Assertions.assertEquals(0, count);
	}
}
