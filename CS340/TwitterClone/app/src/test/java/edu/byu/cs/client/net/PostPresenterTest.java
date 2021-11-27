package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.Status;
import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.PostRequest;
import edu.byu.cs.client.net.response.PostResponse;
import edu.byu.cs.client.presenter.PostPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostPresenterTest implements PostPresenter.View {
	private PostPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new PostPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void validateNewStatus() {
		PostResponse response = presenter.postStatus(new PostRequest(user, "I have spoken"));
		assertEquals(new Status(user, "I have spoken"), response.getStatus());
	}
}
