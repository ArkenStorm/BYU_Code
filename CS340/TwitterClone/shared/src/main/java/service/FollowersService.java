package service;

import request.FollowersRequest;
import response.FollowersResponse;

import java.io.IOException;

public interface FollowersService {
	FollowersResponse getFollowers(FollowersRequest request) throws IOException;
}
