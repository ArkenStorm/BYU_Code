package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.FollowingRequest;
import edu.byu.cs.client.net.response.FollowingResponse;
import edu.byu.cs.client.presenter.FollowingPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FollowingPresenterTest implements FollowingPresenter.View {
	private FollowingPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new FollowingPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void checkFollowingLimit() {
		FollowingResponse response = presenter.getFollowing(new FollowingRequest(user, 5, null));
		assertTrue(response.getFollowees().size() <= 5);
	}
}
