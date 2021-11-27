package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.MessageResult;
import service.ClearService;
import service.FillService;

import java.sql.SQLException;

public class FillServiceTest {
	FillService fs;
	Database db;

	@BeforeEach
	void setUp() {
		fs = new FillService();
		db = Database.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		ClearService cs = new ClearService();
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void fillSuccess() throws DataAccessException {
		UserDAO udao = new UserDAO(db);
		udao.insertUser(new User("thor", "mjolnir", "computerwhatfor@mail.com", "Thor", "Odinson", "m", "strongestAvenger"));
		MessageResult result = fs.fill("thor", 2);
		Assertions.assertEquals("Successfully added 7 persons and 18 events to the database.", result.getMessage());
	}

	@Test
	void fillFail() {
		MessageResult result = fs.fill("nobodyhere", 7);
		Assertions.assertEquals("Error: No user exists with that username.", result.getMessage());
	}
}
