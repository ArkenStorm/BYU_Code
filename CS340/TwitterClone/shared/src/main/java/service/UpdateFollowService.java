package service;

import request.FollowRequest;
import response.FollowResponse;

import java.io.IOException;

public interface UpdateFollowService {
	FollowResponse updateFollow(FollowRequest request) throws IOException;
}
