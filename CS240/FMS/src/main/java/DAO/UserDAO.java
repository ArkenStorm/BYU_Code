package DAO;

import model.User;

import java.sql.*;

/**
 * Database Access Object for User.
 */
public class UserDAO {
	private Database db;
	private static UserDAO instance;

	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO(Database.getInstance());
		}
		return instance;
	}

	public UserDAO(Database db) {
		this.db = db;
	}

	/**
	 * Inserts a User.
	 *
	 * @param user the user
	 */
	public void insertUser(User user) throws DataAccessException {
		PreparedStatement stmt = null;
		Connection connect = db.getConnection();
		try {
			// language=sql
			String sql = "INSERT INTO User (Username, Password, Email, FirstName, LastName, Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?)";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getFirstName());
			stmt.setString(5, user.getLastName());
			stmt.setString(6, user.getGender());
			stmt.setString(7, user.getPersonID());

			if (stmt.executeUpdate() != 1) {
				throw new DataAccessException("Error: Could not insert into table");
			}
		}
		catch(SQLException e) {
			throw new DataAccessException("SQL Error encountered while inserting into User table");
		}
	}

	/**
	 * Deletes a User.
	 *
	 * @param username the username
	 */
	public void deleteUser(String username) throws DataAccessException {
		PreparedStatement stmt = null;
		Connection connect = db.getConnection();
		try {
			// language=sql
			String sql = "DELETE FROM User WHERE Username = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, username);
		}
		catch(SQLException e) {
			throw new DataAccessException("SQL Error encountered while deleting from User table");
		}
	}

	/**
	 * Gets a User by username.
	 *
	 * @param username the username
	 * @return the user by username
	 */
	public User getUserByUsername(String username) throws DataAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connect = db.getConnection();
		try {
			//language=sql
			String sql = "SELECT * FROM User WHERE Username = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(
						rs.getString("Username"),
						rs.getString("Password"),
						rs.getString("Email"),
						rs.getString("FirstName"),
						rs.getString("LastName"),
						rs.getString("Gender"),
						rs.getString("PersonID")
				);
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when querying from User");
		}
	}

	/**
	 * Clears all Users from the Database.
	 */
	public void clearUsers() throws DataAccessException {
		Connection connect = db.getConnection();
		try (Statement stmt = connect.createStatement()){
			String sql = "DELETE FROM User";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered while clearing tables");
		}
	}
}
