package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.GetUserRequest;
import response.GetUserResponse;
import service.GetUserService;

import java.io.IOException;

public class GetUserServiceProxy implements GetUserService {
	private static GetUserServiceProxy instance;

	public static GetUserServiceProxy getInstance() {
		if (instance == null) {
			instance = new GetUserServiceProxy();
		}
		return instance;
	}


	@Override
	public GetUserResponse findUserByHandle(GetUserRequest request) throws IOException {
		return ServerFacade.getInstance().findUserByHandle(request, "/find");
	}
}
