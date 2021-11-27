package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.LoginPresenter;
import request.LoginRequest;
import response.LoginResponse;

import java.io.IOException;
import java.util.function.Consumer;

public class LoginTask extends AsyncTask <LoginRequest, Void, LoginResponse> {
	private Consumer<LoginResponse> listener;
	private final LoginPresenter presenter;

	public LoginTask(LoginPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	protected LoginResponse doInBackground(LoginRequest... loginRequests) {
		LoginResponse response = null;
		try {
			response = presenter.login(loginRequests[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(LoginResponse result) {
		listener.accept(result);
	}

	public LoginTask setListener(Consumer<LoginResponse> listener) {
		this.listener = listener;
		return this;
	}
}
