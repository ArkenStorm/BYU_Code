package service;

import request.GetUserRequest;
import response.GetUserResponse;

import java.io.IOException;

public interface GetUserService {
	GetUserResponse findUserByHandle(GetUserRequest request) throws IOException;
}
