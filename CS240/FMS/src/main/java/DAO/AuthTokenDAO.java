package DAO;

import model.AuthToken;

import java.sql.*;

/**
 * Database Access Object for AuthToken.
 */
public class AuthTokenDAO {
	private Database db;
	private static AuthTokenDAO instance;

	public static AuthTokenDAO getInstance() {
		if (instance == null) {
			instance = new AuthTokenDAO(Database.getInstance());
		}
		return instance;
	}

	public AuthTokenDAO(Database db) {
		this.db = db;
	}

	/**
	 * Inserts an AuthToken.
	 *
	 * @param token the token
	 */
	public void insertToken(AuthToken token) throws DataAccessException {
		PreparedStatement stmt = null;
		Connection connect = db.getConnection();
		try {
			// language=sql
			String sql = "INSERT INTO AuthToken (AuthToken, Username) VALUES(?, ?)";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, token.getAuthToken());
			stmt.setString(2, token.getUsername());

			if (stmt.executeUpdate() != 1) {
				throw new DataAccessException("Error encountered when inserting into table");
			}
		}
		catch(SQLException e) {
			throw new DataAccessException("SQL Error encountered while inserting into AuthToken table");
		}
	}

	/**
	 * Gets a specific AuthToken, not necessarily associated with a User.
	 *
	 * @param token the token
	 * @return the token
	 */
	public AuthToken getToken(String token) throws DataAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connect = db.getConnection();
		try {
			//language=sql
			String sql = "SELECT * FROM AuthToken WHERE AuthToken = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, token);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new AuthToken(
						rs.getString("AuthToken"),
						rs.getString("Username")
				);
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when querying from AuthToken");
		}
	}

	/**
	 * Clears all AuthTokens.
	 */
	public void clearAuthTokens() throws DataAccessException {
		Connection connect = db.getConnection();
		try (Statement stmt = connect.createStatement()){
			String sql = "DELETE FROM AuthToken";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered while clearing tables");
		}
	}
}
