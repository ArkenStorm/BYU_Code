package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.FollowersServiceProxy;
import request.FollowersRequest;
import response.FollowersResponse;

import java.io.IOException;


public class FollowersPresenter extends Presenter {
	private final View view;

	public interface View {

	}

	public FollowersPresenter(View view) {
		this.view = view;
	}

	public FollowersResponse getFollowers(FollowersRequest request) throws IOException {
		return FollowersServiceProxy.getInstance().getFollowers(request);
	}
}
