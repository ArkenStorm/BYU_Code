package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.LoginRequest;
import edu.byu.cs.client.net.response.LoginResponse;
import edu.byu.cs.client.presenter.LoginPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginPresenterTest implements LoginPresenter.View {
	private LoginPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new LoginPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void loginValidUser() {
		LoginResponse response = presenter.login(new LoginRequest("@TestUser", "fakepassword"));
		assertNotNull(response.getUser());
	}

	@Test
	void invalidLogin() {
		LoginResponse response = presenter.login(new LoginRequest("@GodzillaKingOfTheBeasts", "fakepassword"));
		assertEquals("That user does not exist!", response.getMessage());
	}
}
