package service;

import request.LoginRequest;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;

import java.io.IOException;

public interface LoginService {
	LoginResponse login(LoginRequest request) throws IOException;
	LogoutResponse logout(LogoutRequest request) throws IOException;
}
