package Service;

import DAO.DataAccessException;
import DAO.Database;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.AuthorizationResult;
import result.MessageResult;
import service.ClearService;
import service.RegisterService;

import java.sql.SQLException;

public class RegisterServiceTest {
	RegisterService rs;
	Database db;
	String registerData = "{\n" +
			"  \"userName\": \"arken\",\n" +
			"  \"password\": \"storm\",\n" +
			"  \"email\": \"stuff@gmail.com\",\n" +
			"  \"firstName\": \"Taylor\",\n" +
			"  \"lastName\": \"Whitlock\",\n" +
			"  \"gender\": \"m\"\n" +
			"}\n";

	@BeforeEach
	void setUp() throws DataAccessException {
		rs = new RegisterService();
		db = Database.getInstance();
	}

	@AfterEach
	void tearDown() throws DataAccessException, SQLException {
		ClearService cs = new ClearService();
		cs.clear();
		db.getConnection().commit();
	}

	@Test
	void registerSuccess() {
		AuthorizationResult result = (AuthorizationResult) rs.register(new Gson().fromJson(registerData, RegisterRequest.class));
		Assertions.assertEquals("arken", result.getUserName());
	}

	@Test
	void registerFail() {
		rs.register(new Gson().fromJson(registerData, RegisterRequest.class));
		MessageResult result = (MessageResult) rs.register(new Gson().fromJson(registerData, RegisterRequest.class));
		Assertions.assertEquals("Error: That username is already taken.", result.getMessage());
	}
}
