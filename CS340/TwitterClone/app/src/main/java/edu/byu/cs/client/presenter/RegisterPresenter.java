package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.RegisterServiceProxy;
import request.RegisterRequest;
import response.LoginResponse;

import java.io.IOException;

public class RegisterPresenter extends Presenter {
    private final View view;

    public interface View{
        // nothing for now
    }

    public RegisterPresenter(View view) {
        this.view = view;
    }

    public LoginResponse register(RegisterRequest request) throws IOException {
        return RegisterServiceProxy.getInstance().handleRequest(request);
    }
}
