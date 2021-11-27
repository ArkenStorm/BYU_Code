package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.StatusRequest;
import response.StatusResponse;
import service.StoryService;

import java.io.IOException;

public class StoryServiceProxy implements StoryService {
	private static StoryServiceProxy instance;
	private final ServerFacade facade;

	public static StoryServiceProxy getInstance() {
		if (instance == null) {
			instance = new StoryServiceProxy();
		}
		return instance;
	}

	private StoryServiceProxy() {
		facade = ServerFacade.getInstance();
	}

	@Override
	public StatusResponse getStory(StatusRequest request) throws IOException {
		return facade.getStatuses(request, "/story");
	}
}
