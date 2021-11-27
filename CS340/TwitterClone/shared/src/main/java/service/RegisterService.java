package service;

import request.RegisterRequest;
import response.LoginResponse;

import java.io.IOException;

public interface RegisterService {
	LoginResponse handleRequest(RegisterRequest request) throws IOException;
}
