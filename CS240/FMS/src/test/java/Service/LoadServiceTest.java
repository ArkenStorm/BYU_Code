package Service;

import DAO.DataAccessException;
import DAO.Database;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.MessageResult;
import service.ClearService;
import service.LoadService;

import java.sql.SQLException;

public class LoadServiceTest {
	LoadService ls;
	Database db;
	String data = "{\n" +
			"  \"users\": [\n" +
			"    {\n" +
			"      \"userName\": \"sheila\",\n" +
			"      \"password\": \"parker\",\n" +
			"      \"email\": \"sheila@parker.com\",\n" +
			"      \"firstName\": \"Sheila\",\n" +
			"      \"lastName\": \"Parker\",\n" +
			"      \"gender\": \"f\",\n" +
			"      \"personID\": \"Sheila_Parker\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"persons\": [\n" +
			"    {\n" +
			"      \"firstName\": \"Sheila\",\n" +
			"      \"lastName\": \"Parker\",\n" +
			"      \"gender\": \"f\",\n" +
			"      \"personID\": \"Sheila_Parker\",\n" +
			"      \"fatherID\": \"Patrick_Spencer\",\n" +
			"      \"motherID\": \"Im_really_good_at_names\",\n" +
			"      \"associatedUsername\": \"sheila\"\n" +
			"    },\n" +
			"    {\n" +
			"      \"firstName\": \"Patrick\",\n" +
			"      \"lastName\": \"Spencer\",\n" +
			"      \"gender\": \"m\",\n" +
			"      \"personID\":\"Patrick_Spencer\",\n" +
			"      \"spouseID\": \"Im_really_good_at_names\",\n" +
			"      \"associatedUsername\": \"sheila\"\n" +
			"    },\n" +
			"    {\n" +
			"      \"firstName\": \"CS240\",\n" +
			"      \"lastName\": \"JavaRocks\",\n" +
			"      \"gender\": \"f\",\n" +
			"      \"personID\": \"Im_really_good_at_names\",\n" +
			"      \"spouseID\": \"Patrick_Spencer\",\n" +
			"      \"associatedUsername\": \"sheila\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"events\": [\n" +
			"    {\n" +
			"      \"eventType\": \"started family map\",\n" +
			"      \"personID\": \"Sheila_Parker\",\n" +
			"      \"city\": \"Salt Lake City\",\n" +
			"      \"country\": \"United States\",\n" +
			"      \"latitude\": 40.7500,\n" +
			"      \"longitude\": -110.1167,\n" +
			"      \"year\": 2016,\n" +
			"      \"eventID\": \"Sheila_Family_Map\",\n" +
			"      \"associatedUsername\":\"sheila\"\n" +
			"    },\n" +
			"    {\n" +
			"      \"eventType\": \"fixed this thing\",\n" +
			"      \"personID\": \"Patrick_Spencer\",\n" +
			"      \"city\": \"Provo\",\n" +
			"      \"country\": \"United States\",\n" +
			"      \"latitude\": 40.2338,\n" +
			"      \"longitude\": -111.6585,\n" +
			"      \"year\": 2017,\n" +
			"      \"eventID\": \"I_hate_formatting\",\n" +
			"      \"associatedUsername\": \"sheila\"\n" +
			"    }\n" +
			"  ]\n" +
			"}\n";
	String invalidData = "{\n" +
			"  \"users\": [\n" +
			"    {}\n" +
			"  ],\n" +
			"  \"persons\": [\n" +
			"  ],\n" +
			"  \"events\": [\n" +
			"  ]\n" +
			"}\n";

	@BeforeEach
	void setUp() {
		ls = new LoadService();
		db = Database.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		ClearService cs = new ClearService();
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void loadSuccess() {
		MessageResult result = ls.loadDatabase(new Gson().fromJson(data, LoadRequest.class));
		Assertions.assertEquals("Successfully added 1 users, 3 persons, and 2 events to the database.", result.getMessage());
	}

	@Test
	void loadFail() {
		MessageResult result = ls.loadDatabase(new Gson().fromJson(invalidData, LoadRequest.class));
		Assertions.assertEquals("SQL Error encountered while inserting into User table", result.getMessage());
	}
}
