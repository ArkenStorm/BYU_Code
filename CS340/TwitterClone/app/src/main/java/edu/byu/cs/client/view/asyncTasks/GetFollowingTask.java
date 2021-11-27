package edu.byu.cs.client.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;

import domain.User;
import edu.byu.cs.client.presenter.FollowingPresenter;
import edu.byu.cs.client.view.cache.ImageCache;
import edu.byu.cs.client.view.util.ImageUtils;
import request.FollowingRequest;
import response.FollowersResponse;
import response.FollowingResponse;

public class GetFollowingTask extends AsyncTask<FollowingRequest, Void, FollowingResponse> {

    private final FollowingPresenter presenter;
    private final GetFolloweesObserver observer;

    public interface GetFolloweesObserver {
        void followeesRetrieved(FollowingResponse followingResponse);
    }

    public GetFollowingTask(FollowingPresenter presenter, GetFolloweesObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected FollowingResponse doInBackground(FollowingRequest... followingRequests) {
        FollowingResponse response = null;
        try {
            response = presenter.getFollowing(followingRequests[0]);
            loadImages(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void loadImages(FollowingResponse response) {
        for(User user : response.getFollowees()) {

            Drawable drawable;

            try {
                drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.toString(), e);
                drawable = null;
            }

            ImageCache.getInstance().cacheImage(user, drawable);
        }
    }

    @Override
    protected void onPostExecute(FollowingResponse response) {
        if(observer != null) {
            if (response == null) {
                response = new FollowingResponse("Error retrieving followers");
            }
            observer.followeesRetrieved(response);
        }
    }
}
