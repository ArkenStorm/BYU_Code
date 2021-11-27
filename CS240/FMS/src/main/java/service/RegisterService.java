package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import result.MessageResult;
import result.Result;

import java.util.UUID;

/**
 * The User Registration service
 */
public class RegisterService {
	/**
	 * Attempts to register a User.
	 *
	 * @param request the request
	 * @return the result
	 */
	public Result register(RegisterRequest request) {
		Database db = Database.getInstance();
		UserDAO udao = UserDAO.getInstance();
		PersonDAO pdao = PersonDAO.getInstance();
		LoginService ls = new LoginService();
		User user;
		try {
			if (request.getUserName().equals("")) {
				return new MessageResult("Error: You must provide a valid username.");
			}
			else if (request.getPassword().equals("")) {
				return new MessageResult("Error: You must provide a valid password.");
			}
			else if (request.getEmail().equals("")) {
				return new MessageResult("Error: You must provide a valid email.");
			}
			else if (request.getFirstName().equals("")) {
				return new MessageResult("Error: You must provide a valid first name.");
			}
			else if (request.getLastName().equals("")) {
				return new MessageResult("Error: You must provide a valid last name.");
			}
			else if (request.getGender().equals("") || (!request.getGender().equals("m") && !request.getGender().equals("f"))) {
				return new MessageResult("Error: You must provide a valid gender.");
			}
			if (udao.getUserByUsername(request.getUserName()) != null) {
				return new MessageResult("Error: That username is already taken.");
			}
			String personID = UUID.randomUUID().toString();
			user = new User(request.getUserName(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), personID);
			udao.insertUser(user);

			FillService fs = new FillService();
			fs.fill(request.getUserName(), 4);
			//db.closeConnection(true);
		}
		catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}

		return ls.login(new LoginRequest(request.getUserName(), request.getPassword()));
	}
}
