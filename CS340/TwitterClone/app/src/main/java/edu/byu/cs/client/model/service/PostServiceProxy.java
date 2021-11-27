package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.PostRequest;
import response.PostResponse;
import service.PostService;

import java.io.IOException;

public class PostServiceProxy implements PostService {
    private static PostServiceProxy instance;
    private final ServerFacade facade;

    public static PostServiceProxy getInstance() {
        if (instance == null) {
            instance = new PostServiceProxy();
        }
        return instance;
    }

    public PostServiceProxy() {
        facade = ServerFacade.getInstance();
    }

    @Override
    public PostResponse postStatus(PostRequest request) throws IOException {
        return facade.postStatus(request, "/post");
    }
}
