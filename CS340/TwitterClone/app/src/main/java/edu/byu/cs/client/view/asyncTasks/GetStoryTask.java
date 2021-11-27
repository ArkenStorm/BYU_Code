package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.StoryPresenter;
import request.StatusRequest;
import response.StatusResponse;

import java.io.IOException;

public class GetStoryTask extends AsyncTask<StatusRequest, Void, StatusResponse> {
	private final StoryPresenter presenter;
	private final GetStoryObserver observer;

	public interface GetStoryObserver {
		void storyRetrieved(StatusResponse response);
	}

	public GetStoryTask(StoryPresenter presenter, GetStoryObserver observer) {
		this.presenter = presenter;
		this.observer = observer;
	}

	@Override
	protected StatusResponse doInBackground(StatusRequest... StatusRequests) {
		StatusResponse response = null;
		try {
			response = presenter.getStory(StatusRequests[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(StatusResponse response) {
		if(observer != null) {
			if (response == null) {
				response = new StatusResponse("Error retrieving feed");
			}
			observer.storyRetrieved(response);
		}
	}
}
