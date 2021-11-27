package service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import result.AuthorizationResult;
import result.MessageResult;
import result.Result;

import java.util.UUID;

/**
 * The Login service.
 */
public class LoginService {
	/**
	 * Logs in the User and returns an AuthToken.
	 *
	 * @param request the request
	 * @return the result
	 */
	public Result login(LoginRequest request) {
		Database db = Database.getInstance();
		UserDAO udao = UserDAO.getInstance();
		AuthTokenDAO atdao = AuthTokenDAO.getInstance();
		User user;
		String authToken = UUID.randomUUID().toString();
		try {
			if (request.getUserName().equals("")) {
				return new MessageResult("Error: You must provide a valid username.");
			}
			else if (request.getPassword().equals("")) {
				return new MessageResult("Error: You must provide a valid password.");
			}
			user = udao.getUserByUsername(request.getUserName());
			if (user == null) {
				return new MessageResult("Error: There is no registered user with that username.");
			}
			else if (!user.getPassword().equals(request.getPassword())) {
				return new MessageResult("Error: Invalid password.");
			}
			atdao.insertToken(new AuthToken(authToken, request.getUserName()));
			db.closeConnection(true);
		}
		catch (DataAccessException e ) {
			return new MessageResult(e.getMessage());
		}
		return new AuthorizationResult(authToken, request.getUserName(), user.getPersonID());
	}
}
