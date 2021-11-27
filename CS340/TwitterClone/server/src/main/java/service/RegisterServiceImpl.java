package service;

import dao.AuthTokenDAO;
import dao.UserDAO;
import request.RegisterRequest;
import response.LoginResponse;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterServiceImpl implements RegisterService {
	@Override
	public LoginResponse handleRequest(RegisterRequest request) {
		UserDAO dao = new UserDAO();
		LoginResponse response = dao.registerUser(request);
		if (response.isSuccess()) {
			response.setAuthtoken(AuthTokenDAO.createToken(request.getHandle()));
		}

		return response;
	}
}
