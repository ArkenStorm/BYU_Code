package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.RegisterPresenter;
import request.RegisterRequest;
import response.LoginResponse;

import java.io.IOException;
import java.util.function.Consumer;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, LoginResponse> {
    private Consumer<LoginResponse> listener;
    private final RegisterPresenter presenter;

    public RegisterTask(RegisterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected LoginResponse doInBackground(RegisterRequest... registerRequests) {
        LoginResponse response = null;
        try {
            response = presenter.register(registerRequests[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(LoginResponse result) {
        listener.accept(result);
    }

    public RegisterTask setListener(Consumer<LoginResponse> listener) {
        this.listener = listener;
        return this;
    }
}
