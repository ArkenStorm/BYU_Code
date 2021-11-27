package service;

import request.PostRequest;
import response.PostResponse;

import java.io.IOException;

public interface PostService {
	PostResponse postStatus(PostRequest request) throws IOException;
}
