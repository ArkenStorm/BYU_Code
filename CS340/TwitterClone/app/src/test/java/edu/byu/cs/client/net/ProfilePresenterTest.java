package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.GetUserRequest;
import edu.byu.cs.client.net.response.GetUserResponse;
import edu.byu.cs.client.presenter.ProfilePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfilePresenterTest implements ProfilePresenter.View {
	private ProfilePresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new ProfilePresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void findValidUser() {
		GetUserResponse response = presenter.findUserByHandle(new GetUserRequest("@TestUser", user));
		assertEquals(user, response.getUser());
	}

	@Test
	void findInvalidUser() {
		GetUserResponse response = presenter.findUserByHandle(new GetUserRequest("@DarthVader", user));
		assertEquals("That user does not exist!", response.getMessage());
	}
}
