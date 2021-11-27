package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.net.request.LogoutRequest;
import edu.byu.cs.client.net.response.LogoutResponse;
import edu.byu.cs.client.presenter.MainPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainPresenterTest implements MainPresenter.View {
	private MainPresenter presenter;
	private User user;

	@BeforeEach
	void setup() {
		presenter = new MainPresenter(this);
		user = LoginServiceProxy.getInstance().getCurrentUser();
	}

	@Test
	void successfulLogout() {
		LogoutResponse response = presenter.logout(new LogoutRequest(user));
		assertTrue(response.isSuccess());
	}
}
