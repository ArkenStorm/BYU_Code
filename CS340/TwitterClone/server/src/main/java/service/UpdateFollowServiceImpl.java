package service;

import dao.AuthTokenDAO;
import dao.FollowDAO;
import dao.UserDAO;
import request.FollowRequest;
import response.FollowResponse;

public class UpdateFollowServiceImpl implements UpdateFollowService {
	@Override
	public FollowResponse updateFollow(FollowRequest request) {
		if (!AuthTokenDAO.isValidToken(request.getAuthToken(), request.getCurrentUser().getAlias())) {
			throw new RuntimeException("Forbidden");
		}
		FollowDAO dao = new FollowDAO();
		if (request.isUpdate()) {
			return dao.addFollow(request);
		}
		return dao.deleteFollow(request);
	}
}
