package service;

import request.StatusRequest;
import response.StatusResponse;

import java.io.IOException;

public interface FeedService {
	StatusResponse getFeed(StatusRequest request) throws IOException;
}
