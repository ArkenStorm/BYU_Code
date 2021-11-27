package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.FollowingRequest;
import response.FollowingResponse;
import service.FollowingService;

import java.io.IOException;

public class FollowingServiceProxy implements FollowingService {

    private static FollowingServiceProxy instance;

    private final ServerFacade serverFacade;

    public static FollowingServiceProxy getInstance() {
        if(instance == null) {
            instance = new FollowingServiceProxy();
        }

        return instance;
    }

    private FollowingServiceProxy() {
        serverFacade = ServerFacade.getInstance();
    }

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) throws IOException {
        return serverFacade.getFollowees(request, "/following");
    }
}
