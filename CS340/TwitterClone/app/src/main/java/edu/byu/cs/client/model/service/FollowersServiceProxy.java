package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.FollowersRequest;
import response.FollowersResponse;
import service.FollowersService;

import java.io.IOException;


public class FollowersServiceProxy implements FollowersService {
	private static FollowersServiceProxy instance;
	private final ServerFacade serverFacade;

	public static FollowersServiceProxy getInstance() {
		if(instance == null) {
			instance = new FollowersServiceProxy();
		}

		return instance;
	}

	private FollowersServiceProxy() {
		serverFacade = ServerFacade.getInstance();
	}

	public FollowersResponse getFollowers(FollowersRequest request) throws IOException {
		return serverFacade.getFollowers(request, "/followers");
	}
}
