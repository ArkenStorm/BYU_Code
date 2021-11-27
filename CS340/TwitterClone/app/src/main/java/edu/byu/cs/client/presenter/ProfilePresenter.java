package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.UpdateFollowServiceProxy;
import edu.byu.cs.client.net.ServerFacade;
import request.FollowRequest;
import response.FollowResponse;

import java.io.IOException;

public class ProfilePresenter extends Presenter {
	private final View view;

	/**
	 * The interface by which this presenter communicates with its view.
	 */
	public interface View {
		// If needed, Specify methods here that will be called on the view in response to model updates
	}

	public ProfilePresenter(View view) {
		this.view = view;
	}

	public FollowResponse updateFollow(FollowRequest request) throws IOException {
		return UpdateFollowServiceProxy.getInstance().updateFollow(request);
	}
}
