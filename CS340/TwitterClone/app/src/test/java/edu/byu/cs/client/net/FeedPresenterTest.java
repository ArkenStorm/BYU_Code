package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.StatusRequest;
import edu.byu.cs.client.net.response.StatusResponse;
import edu.byu.cs.client.presenter.FeedPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedPresenterTest implements FeedPresenter.View {
	private FeedPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new FeedPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void checkFeedLimit() {
		StatusResponse response = presenter.getFeed(new StatusRequest(user, 5, null));
		assertTrue(response.getStatuses().size() <= 5);
	}
}
