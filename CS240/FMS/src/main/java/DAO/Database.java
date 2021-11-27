package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private Connection connect;
	private static Database instance = null;

	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	public Connection openConnection() throws DataAccessException {
		try {
			final String CONNECTION_URL = "jdbc:sqlite:database.db";

			connect = DriverManager.getConnection(CONNECTION_URL);

			connect.setAutoCommit(false);
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Error: Unable to open connection to database");
		}
		return connect;
	}

	public Connection getConnection() throws DataAccessException {
		if (connect == null) {
			return openConnection();
		}
		else {
			return connect;
		}
	}

	public void closeConnection(boolean commit) throws DataAccessException {
		try {
			if (commit) {
				connect.commit();
			}
			else {
				connect.rollback();
			}

			connect.close();
			connect = null;
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Error: Unable to close database connection");
		}
	}

	public void createTables() throws DataAccessException {
		clearTables();
		try (Statement stmt = connect.createStatement()) {
			String sql = "create table if not EXISTS User" +
				"(" +
					"Username varchar(255) primary key," +
					"Password varchar(255) not NULL," +
					"Email varchar(255) not NULL," +
					"FirstName varchar(255) not NULL," +
					"LastName varchar(255) not NULL," +
					"Gender varchar(1) not NULL," +
					"PersonID varchar(255) not NULL," +
					"foreign key(PersonID) references Person(PersonID)" +
				");" +

				"create table if not EXISTS Person" +
				"(" +
					"PersonID varchar(255) primary key," +
					"Username varchar(255)," +
					"FirstName varchar(255) not NULL," +
					"LastName varchar(255) not NULL," +
					"Gender varchar(1) not NULL," +
					"FatherID varchar(255)," +
					"MotherID varchar(255)," +
					"SpouseID varchar(255)," +
					"foreign key(Username) references User(Username)," +
					"foreign key(FatherID) references Person(PersonID)," +
					"foreign key(MotherID) references Person(PersonID)," +
					"foreign key(SpouseID) references Person(PersonID)" +
				");" +

				"create table if not EXISTS Event" +
				"(" +
					"EventID varchar(255) primary key," +
					"Username varchar(255)," +
					"PersonID varchar(255) not NULL," +
					"Latitude varchar(255)," +
					"Longitude varchar(255)," +
					"Country varchar(255)," +
					"City varchar(255)," +
					"EventType varchar(255)," +
					"Year varchar(4)," +
					"foreign key(Username) references User(Username)," +
					"foreign key(PersonID) references Person(PersonID)" +
				");" +

				"create table if not EXISTS AuthToken" +
				"(" +
					"AuthToken varchar(255) primary KEY," +
					"Username varchar(255) not NULL," +
					"foreign key(Username) references User(Username)" +
				");";

			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered while creating tables");
		}
	}

	public void clearTables() throws DataAccessException {
		try (Statement stmt = connect.createStatement()){
			String sql = "DELETE FROM Person;" + "DELETE FROM User;" + "DELETE FROM Event;" + "DELETE FROM AuthToken;";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			throw new DataAccessException("SQL Error encountered while clearing tables");
		}
	}
}
