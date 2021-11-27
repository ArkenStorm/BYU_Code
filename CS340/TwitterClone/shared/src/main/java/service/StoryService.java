package service;

import request.StatusRequest;
import response.StatusResponse;

import java.io.IOException;

public interface StoryService {
	StatusResponse getStory(StatusRequest request) throws IOException;
}
