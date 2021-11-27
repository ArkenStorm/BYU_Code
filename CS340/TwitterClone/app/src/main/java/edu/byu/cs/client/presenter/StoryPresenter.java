package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.StoryServiceProxy;
import request.StatusRequest;
import response.StatusResponse;

import java.io.IOException;

public class StoryPresenter extends Presenter {
	private final View view;

	public interface View {

	}

	public StoryPresenter(View view) {
		this.view = view;
	}

	public StatusResponse getStory(StatusRequest request) throws IOException {
		return StoryServiceProxy.getInstance().getStory(request);
	}
}
