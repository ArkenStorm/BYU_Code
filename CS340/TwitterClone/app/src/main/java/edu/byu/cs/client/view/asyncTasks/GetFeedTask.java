package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.FeedPresenter;
import request.StatusRequest;
import response.StatusResponse;

public class GetFeedTask extends AsyncTask<StatusRequest, Void, StatusResponse> {
	private final FeedPresenter presenter;
	private final GetFeedObserver observer;

	public interface GetFeedObserver {
		void feedRetrieved(StatusResponse response);
	}

	public GetFeedTask(FeedPresenter presenter, GetFeedObserver observer) {
		this.presenter = presenter;
		this.observer = observer;
	}

	@Override
	protected StatusResponse doInBackground(StatusRequest... StatusRequests) {
		StatusResponse response = null;
		try {
			response = presenter.getFeed(StatusRequests[0]);
		}
		catch (Exception e) {
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
			observer.feedRetrieved(response);
		}
	}
}
