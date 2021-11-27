package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.FollowersRequest;
import edu.byu.cs.client.net.response.FollowersResponse;
import edu.byu.cs.client.presenter.FollowersPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FollowersPresenterTest implements FollowersPresenter.View {
	private FollowersPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new FollowersPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void checkFollowerLimit() {
		FollowersResponse response = presenter.getFollowers(new FollowersRequest(user, 5, null));
		assertTrue(response.getFollowers().size() <= 5);
	}
}
