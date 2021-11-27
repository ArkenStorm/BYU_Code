package edu.byu.cs.client.view.asyncTasks;

import android.os.AsyncTask;
import edu.byu.cs.client.presenter.MainPresenter;
import request.LogoutRequest;
import response.LogoutResponse;

import java.io.IOException;

public class LogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> {
	private final MainPresenter presenter;
	private final LogoutTaskObserver observer;

	public interface LogoutTaskObserver {
		void logoutFinish(LogoutResponse response);
	}

	public LogoutTask(MainPresenter presenter, LogoutTaskObserver observer) {
		this.presenter = presenter;
		this.observer = observer;
	}

	@Override
	protected LogoutResponse doInBackground(LogoutRequest... logoutRequests) {
		LogoutResponse response = null;
		try {
			response = presenter.logout(logoutRequests[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(LogoutResponse response) {
		observer.logoutFinish(response);
	}
}
