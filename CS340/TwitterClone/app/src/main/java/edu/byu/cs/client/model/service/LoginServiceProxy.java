package edu.byu.cs.client.model.service;

import domain.User;
import edu.byu.cs.client.net.ServerFacade;
import request.LoginRequest;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;
import service.LoginService;

import java.io.IOException;


public class LoginServiceProxy implements LoginService {
    private static LoginServiceProxy instance;
    private final ServerFacade facade;

    public static LoginServiceProxy getInstance() {
        if(instance == null) {
            instance = new LoginServiceProxy();
        }

        return instance;
    }

    private LoginServiceProxy() {
        facade = ServerFacade.getInstance();
    }

    @Override
    public LoginResponse login(LoginRequest request) throws IOException {
        return facade.loginUser(request, "/login");
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IOException {
        return facade.logout(request, "/logout");
    }

    public User getCurrentUser() {
        return facade.getCurrentUser();
    }
}
