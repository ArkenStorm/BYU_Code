package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.PostPresenter;
import request.PostRequest;
import response.PostResponse;

import java.io.IOException;

public class PostTask extends AsyncTask<PostRequest, Void, PostResponse> {
    private final PostPresenter presenter;
    private final PostTaskObserver observer;

    @Override
    protected PostResponse doInBackground(PostRequest... postRequests) {
        PostResponse response = null;
        try {
            response = presenter.postStatus(postRequests[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public interface PostTaskObserver {
        void postResult(PostResponse postResponse);
    }

    public PostTask(PostPresenter presenter, PostTaskObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(PostResponse postResponse) {
        if(observer != null) {
            observer.postResult(postResponse);
        }
    }
}
