package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import com.google.gson.Gson;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.AuthorizationResult;
import result.MessageResult;
import service.ClearService;
import service.LoginService;

import java.sql.SQLException;

public class LoginServiceTest {
	LoginService ls;
	Database db;
	String loginData = "{\n" +
			"  \"userName\": \"arken\",\n" +
			"  \"password\": \"storm\"\n" +
			"}\n";

	@BeforeEach
	void setUp() {
		ls = new LoginService();
		db = Database.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		ClearService cs = new ClearService();
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void loginSuccess() throws DataAccessException {
		UserDAO udao = new UserDAO(db);
		udao.insertUser(new User("arken", "storm", "stuff@gmail.com", "Taylor", "Whitlock", "m", "blargh"));
		AuthorizationResult result = (AuthorizationResult) ls.login(new Gson().fromJson(loginData, LoginRequest.class));
		Assertions.assertEquals("arken", result.getUserName());
	}

	@Test
	void loginFail() {
		MessageResult result = (MessageResult) ls.login(new Gson().fromJson(loginData, LoginRequest.class));
		Assertions.assertEquals("Error: There is no registered user with that username.", result.getMessage());
	}
}
