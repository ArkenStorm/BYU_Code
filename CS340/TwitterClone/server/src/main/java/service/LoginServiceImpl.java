package service;

import dao.AuthTokenDAO;
import dao.UserDAO;
import request.LoginRequest;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;

public class LoginServiceImpl implements LoginService {
	@Override
	public LoginResponse login(LoginRequest request) {
		UserDAO dao = new UserDAO();
		LoginResponse response = dao.loginUser(request);
		if (response.isSuccess()) {
			String token = AuthTokenDAO.createToken(request.getHandle());
			response.setAuthtoken(token);
		}
		return response;
	}

	@Override
	public LogoutResponse logout(LogoutRequest request) {
		AuthTokenDAO dao = new AuthTokenDAO();
		LogoutResponse response = dao.logout(request);
		if (!response.isSuccess()) {
			throw new RuntimeException("Forbidden");
		}
		return response;
	}
}
