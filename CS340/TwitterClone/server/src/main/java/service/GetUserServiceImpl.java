package service;

import dao.FollowDAO;
import dao.UserDAO;
import request.GetUserRequest;
import response.GetUserResponse;

public class GetUserServiceImpl implements GetUserService {
	@Override
	public GetUserResponse findUserByHandle(GetUserRequest request) {
		UserDAO dao = new UserDAO();
		GetUserResponse response = dao.findUserByHandle(request);
		FollowDAO followDAO = new FollowDAO();
		response.setFollowedByCurrentUser(followDAO.checkFollow(request));
		return response;
	}
}
