package edu.byu.cs.client.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import domain.User;
import edu.byu.cs.client.presenter.FollowersPresenter;
import edu.byu.cs.client.view.cache.ImageCache;
import edu.byu.cs.client.view.util.ImageUtils;
import request.FollowersRequest;
import response.FollowersResponse;

import java.io.IOException;

public class GetFollowersTask extends AsyncTask<FollowersRequest, Void, FollowersResponse> {
	private final FollowersPresenter presenter;
	private final GetFollowersObserver observer;

	public interface GetFollowersObserver {
		void followersRetrieved(FollowersResponse followingResponse);
	}

	public GetFollowersTask(FollowersPresenter presenter, GetFollowersObserver observer) {
		this.presenter = presenter;
		this.observer = observer;
	}

	@Override
	protected FollowersResponse doInBackground(FollowersRequest... followingRequests) {
		FollowersResponse response = null;
		try {
			response = presenter.getFollowers(followingRequests[0]);
			loadImages(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private void loadImages(FollowersResponse response) {
		for(User user : response.getFollowers()) {
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
	protected void onPostExecute(FollowersResponse response) {
		if(observer != null) {
			if (response == null) {
				response = new FollowersResponse("Error retrieving followers");
			}
			observer.followersRetrieved(response);
		}
	}
}
