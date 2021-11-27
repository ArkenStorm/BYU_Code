package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.FollowingServiceProxy;
import request.FollowingRequest;
import response.FollowingResponse;

import java.io.IOException;

public class FollowingPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public FollowingPresenter(View view) {
        this.view = view;
    }

    public FollowingResponse getFollowing(FollowingRequest request) throws IOException {
        return FollowingServiceProxy.getInstance().getFollowees(request);
    }
}
