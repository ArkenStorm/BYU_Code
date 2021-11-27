package service;

import dao.FollowDAO;
import dao.UserDAO;
import internal.AliasResponse;
import request.FollowersRequest;
import response.FollowersResponse;

public class FollowersServiceImpl implements FollowersService {
	@Override
	public FollowersResponse getFollowers(FollowersRequest request) {
		FollowDAO dao = new FollowDAO();
		AliasResponse aliasResponse = dao.getFollowers(request);
		UserDAO userDAO = new UserDAO();
		return new FollowersResponse(userDAO.findListOfUsersByHandle(aliasResponse.getAliases()), aliasResponse.isHasMorePages());
	}
}
