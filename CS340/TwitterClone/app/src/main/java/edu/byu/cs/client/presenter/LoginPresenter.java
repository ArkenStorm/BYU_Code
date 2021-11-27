package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.LoginServiceProxy;
import request.LoginRequest;
import response.LoginResponse;

import java.io.IOException;

public class LoginPresenter extends Presenter {
	private final View view;

	public interface View{
		// nothing for now
	}

	public LoginPresenter(View view) {
		this.view = view;
	}

	public LoginResponse login(LoginRequest request) throws IOException {
		return LoginServiceProxy.getInstance().login(request);
	}
}
