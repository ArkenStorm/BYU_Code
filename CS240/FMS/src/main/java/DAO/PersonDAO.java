package DAO;

import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for Person.
 */
public class PersonDAO {
	private Database db;
	private static PersonDAO instance;

	public static PersonDAO getInstance() {
		if (instance == null) {
			instance = new PersonDAO(Database.getInstance());
		}
		return instance;
	}

	public PersonDAO(Database db) {
		this.db = db;
	}

	/**
	 * Inserts a Person.
	 *
	 * @param person the person
	 */
	public void insertPerson(Person person) throws DataAccessException {
		PreparedStatement stmt = null;
		Connection connect = db.getConnection();
		try {
			// language=sql
			String sql = "INSERT INTO Person (PersonID, Username, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, person.getPersonID());
			stmt.setString(2, person.getUsername());
			stmt.setString(3, person.getFirstName());
			stmt.setString(4, person.getLastName());
			stmt.setString(5, person.getGender());
			stmt.setString(6, person.getFatherID());
			stmt.setString(7, person.getMotherID());
			stmt.setString(8, person.getSpouseID());

			if (stmt.executeUpdate() != 1) {
				throw new DataAccessException("Error inserting into Person table");
			}
		}
		catch(SQLException e) {
			throw new DataAccessException("SQL Error encountered while inserting into Person table");
		}
	}

	/**
	 * Deletes all person data associated with the user
	 *
	 * @param username
	 * @throws DataAccessException
	 */
	public void deleteUserPersons(String username) throws DataAccessException {
		PreparedStatement stmt = null;
		Connection connect = db.getConnection();
		// language=sql
		String sql = "DELETE FROM Person WHERE Username = ?";
		try {
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when deleting data associated with the user.");
		}
	}

	/**
	 * Gets a Person by ID.
	 *
	 * @param personID the person id
	 * @return the person by id
	 */
	public Person getPersonByID(String personID) throws DataAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connect = db.getConnection();
		try {
			//language=sql
			String sql = "SELECT * FROM Person WHERE PersonID = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, personID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new Person(
					rs.getString("PersonID"),
					rs.getString("Username"),
					rs.getString("FirstName"),
					rs.getString("LastName"),
					rs.getString("Gender"),
					rs.getString("FatherID"),
					rs.getString("MotherID"),
					rs.getString("SpouseID")
				);
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when querying from Person");
		}
	}

	/**
	 * Gets a list of Persons associated with a User.
	 *
	 * @param username the username
	 * @return the user persons
	 */
	public List<Person> getUserPersons(String username) throws DataAccessException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection connect = db.getConnection();
		try {
			//language=sql
			String sql = "SELECT * FROM Person WHERE Username = ?";
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			List<Person> personList = new ArrayList<>();
			while (rs.next()) {
				personList.add(new Person(
						rs.getString("PersonID"),
						rs.getString("Username"),
						rs.getString("FirstName"),
						rs.getString("LastName"),
						rs.getString("Gender"),
						rs.getString("FatherID"),
						rs.getString("MotherID"),
						rs.getString("SpouseID")
				));
			}
			return personList;
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered when querying from Person");
		}
	}

	/**
	 * Clears all Persons from the database.
	 * @throws DataAccessException
	 */
	public void clearPersons() throws DataAccessException {
		Connection connect = db.getConnection();
		try (Statement stmt = connect.createStatement()){
			String sql = "DELETE FROM Person";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("SQL Error encountered while clearing tables");
		}
	}
}
