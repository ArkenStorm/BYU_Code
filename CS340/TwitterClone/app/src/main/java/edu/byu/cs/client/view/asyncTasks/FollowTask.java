package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.ProfilePresenter;
import request.FollowRequest;
import response.FollowResponse;

import java.io.IOException;

public class FollowTask extends AsyncTask<FollowRequest, Void, FollowResponse> {
	private ProfilePresenter presenter;
	private FollowTaskObserver observer;

	public interface FollowTaskObserver {
		void followUpdated(FollowResponse response);
	}

	public FollowTask(ProfilePresenter presenter, FollowTaskObserver observer) {
		this.presenter = presenter;
		this.observer = observer;
	}

	@Override
	protected FollowResponse doInBackground(FollowRequest... followRequests) {
		FollowResponse response = null;
		try {
			response = presenter.updateFollow(followRequests[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(FollowResponse response) {
		observer.followUpdated(response);
	}
}
