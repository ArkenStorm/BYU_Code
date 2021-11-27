package service;

import dao.AuthTokenDAO;
import dao.FeedDAO;
import request.StatusRequest;
import response.StatusResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FeedServiceImpl implements FeedService {
	@Override
	public StatusResponse getFeed(StatusRequest request) {
		if (!AuthTokenDAO.isValidToken(request.getAuthToken(), request.getOwner().getAlias())) {
			return new StatusResponse("Invalid Authtoken.");
		}
		FeedDAO dao = new FeedDAO();
		StatusResponse response = dao.getFeed(request);
//		if (!response.isSuccess()) {
//			throw new RuntimeException("Internal Error. " + response.getMessage());
//		}
		return response;
	}
}
