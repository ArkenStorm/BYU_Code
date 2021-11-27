package edu.byu.cs.client.model.service;

import edu.byu.cs.client.net.ServerFacade;
import request.RegisterRequest;
import response.LoginResponse;
import service.RegisterService;

import java.io.IOException;


public class RegisterServiceProxy implements RegisterService {
    private static RegisterServiceProxy instance;
    private final ServerFacade facade;

    private RegisterServiceProxy() {
        facade = ServerFacade.getInstance();
    }

    public static RegisterServiceProxy getInstance() {
        if (instance == null) {
            instance = new RegisterServiceProxy();
        }
        return instance;
    }

    @Override
    public LoginResponse handleRequest(RegisterRequest request) throws IOException {
        return facade.registerUser(request, "/register");
    }
}
