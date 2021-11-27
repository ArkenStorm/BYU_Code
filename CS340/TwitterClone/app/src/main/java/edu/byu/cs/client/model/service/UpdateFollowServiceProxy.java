package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.FollowRequest;
import response.FollowResponse;
import service.UpdateFollowService;

import java.io.IOException;

public class UpdateFollowServiceProxy implements UpdateFollowService {
	private static UpdateFollowServiceProxy instance;

	public static UpdateFollowServiceProxy getInstance() {
		if (instance == null) {
			instance = new UpdateFollowServiceProxy();
		}
		return instance;
	}

	@Override
	public FollowResponse updateFollow(FollowRequest request) throws IOException {
		return ServerFacade.getInstance().updateFollow(request, "/updatefollow");
	}
}
