package Service;

import DAO.DataAccessException;
import DAO.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.MessageResult;
import service.ClearService;

import java.sql.SQLException;

public class ClearServiceTest {
	ClearService cs;
	Database db;

	@BeforeEach
	void setUp() throws DataAccessException, SQLException {
		cs = new ClearService();
		db = Database.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void clearTest() {
		MessageResult result = cs.clear();
		Assertions.assertEquals("Clear succeeded.", result.getMessage());
	}

	@Test
	void clearTwice() {
		MessageResult firstResult = cs.clear();
		Assertions.assertEquals("Clear succeeded.", firstResult.getMessage());
		MessageResult secondResult = cs.clear();
		Assertions.assertEquals("Clear succeeded.", secondResult.getMessage());
	}
}
