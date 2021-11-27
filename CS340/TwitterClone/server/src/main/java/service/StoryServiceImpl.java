package service;

import dao.AuthTokenDAO;
import dao.StoryDAO;
import request.StatusRequest;
import response.StatusResponse;

public class StoryServiceImpl implements StoryService {
	@Override
	public StatusResponse getStory(StatusRequest request) {
		if (!AuthTokenDAO.isValidToken(request.getAuthToken(), request.getOwner().getAlias())) {
			return new StatusResponse("Invalid Authtoken.");
		}
		StoryDAO dao = new StoryDAO();
		StatusResponse response = dao.getStory(request);
//		if (!response.isSuccess()) {
//			throw new RuntimeException("Internal Error. " + response.getMessage());
//		}
		return response;
	}
}
