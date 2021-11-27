package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.Presenter;
import request.GetUserRequest;
import response.GetUserResponse;

import java.io.IOException;

public class GetUserTask extends AsyncTask<GetUserRequest, Void, GetUserResponse> {
    private final Presenter presenter;
    private final GetUserTaskObserver observer;

    public interface GetUserTaskObserver {
        void userRetrieved(GetUserResponse response);
    }

    public GetUserTask(Presenter presenter, GetUserTaskObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected GetUserResponse doInBackground(GetUserRequest... getUserRequests) {
        GetUserResponse response = null;
        try {
            response = presenter.findUserByHandle(getUserRequests[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(GetUserResponse response) {
        if(observer != null) {
            observer.userRetrieved(response);
        }
    }
}
