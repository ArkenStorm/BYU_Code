package edu.byu.cs.client.presenter;

import domain.User;
import edu.byu.cs.client.model.service.GetUserServiceProxy;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.ServerFacade;
import request.GetUserRequest;
import response.GetUserResponse;

import java.io.IOException;


public abstract class Presenter {
    ServerFacade facade = ServerFacade.getInstance();

    public GetUserResponse findUserByHandle(GetUserRequest request) throws IOException {
        return GetUserServiceProxy.getInstance().findUserByHandle(request);
    }

    public User getSelectedUser() {
        return facade.getSelectedUser();
    }

    public User getCurrentUser() {
        return facade.getCurrentUser();
    }

    public void setCurrentUser(User user) {
        facade.setCurrentUser(user);
    }

    public void setSelectedUser(User selectedUser) {
        facade.setSelectedUser(selectedUser);
    }

    public void setAuthtoken(String token) {
        facade.setAuthToken(token);
    }
}
