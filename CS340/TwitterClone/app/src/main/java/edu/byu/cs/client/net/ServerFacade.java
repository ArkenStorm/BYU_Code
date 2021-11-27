package edu.byu.cs.client.net;

import java.io.IOException;

import domain.User;
import request.*;
import response.*;


public class ServerFacade {
    private static ServerFacade instance;
    private static final String SERVER_URL = "https://623jswoo1b.execute-api.us-east-2.amazonaws.com/Dev";
    private String authToken = "authToken";
    private User currentUser;
    private User selectedUser;

    public static ServerFacade getInstance() {
        if (instance == null) {
            instance = new ServerFacade();
        }
        return instance;
    }

    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    public FollowersResponse getFollowers(FollowersRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);
    }

    public LoginResponse registerUser(RegisterRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    public LoginResponse loginUser(LoginRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    public StatusResponse getStatuses(StatusRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.setAuthToken(authToken);
        return clientCommunicator.doPost(urlPath, request, null, StatusResponse.class);
    }

    public PostResponse postStatus(PostRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.setAuthToken(authToken);
        return clientCommunicator.doPost(urlPath, request, null, PostResponse.class);
    }

    public GetUserResponse findUserByHandle(GetUserRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);
    }

    public FollowResponse updateFollow(FollowRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.setAuthToken(authToken);
        return clientCommunicator.doPost(urlPath, request, null, FollowResponse.class); // change to put?
    }

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.setAuthToken(authToken);
        return clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
