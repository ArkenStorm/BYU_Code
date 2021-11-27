package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.StatusRequest;
import response.StatusResponse;
import service.FeedService;

import java.io.IOException;

public class FeedServiceProxy implements FeedService {
	private static FeedServiceProxy instance;
	private final ServerFacade facade;

	public static FeedServiceProxy getInstance() {
		if (instance == null) {
			instance = new FeedServiceProxy();
		}
		return instance;
	}

	private FeedServiceProxy() {
		facade = ServerFacade.getInstance();
	}

	@Override
	public StatusResponse getFeed(StatusRequest request) throws IOException {
		return facade.getStatuses(request, "/feed");
	} // add user parameter?
}
