package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.FeedServiceProxy;
import request.StatusRequest;
import response.StatusResponse;

import java.io.IOException;

public class FeedPresenter extends Presenter {
	private final View view;

	public interface View {

	}

	public FeedPresenter(View view) {
		this.view = view;
	}

	public StatusResponse getFeed(StatusRequest request) throws IOException {
		return FeedServiceProxy.getInstance().getFeed(request);
	}
}
