package service;

import dao.FollowDAO;
import dao.UserDAO;
import internal.AliasResponse;
import request.FollowingRequest;
import response.FollowingResponse;

public class FollowingServiceImpl implements FollowingService {
	@Override
	public FollowingResponse getFollowees(FollowingRequest request) {
		FollowDAO dao = new FollowDAO();
		AliasResponse aliasResponse = dao.getFollowees(request);
		UserDAO userDAO = new UserDAO();
		return new FollowingResponse(userDAO.findListOfUsersByHandle(aliasResponse.getAliases()), aliasResponse.isHasMorePages());
	}
}
