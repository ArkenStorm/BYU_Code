package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.StatusRequest;
import edu.byu.cs.client.net.response.StatusResponse;
import edu.byu.cs.client.presenter.StoryPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoryPresenterTest implements StoryPresenter.View {
	private StoryPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new StoryPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void checkStoryLimit() {
		StatusResponse response = presenter.getStory(new StatusRequest(user, 5, null));
		assertTrue(response.getStatuses().size() <= 5);
	}
}
