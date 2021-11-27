package edu.byu.cs.client.net;

import edu.byu.cs.client.model.domain.User;
import edu.byu.cs.client.net.request.RegisterRequest;
import edu.byu.cs.client.net.response.LoginResponse;
import edu.byu.cs.client.presenter.RegisterPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterPresenterTest implements RegisterPresenter.View {
	private RegisterPresenter presenter;

	@BeforeEach
	void setup() {
		presenter = new RegisterPresenter(this);
	}

	@Test
	void checkUpdatedLogin() {
		LoginResponse response = presenter.register(new RegisterRequest("aa", "asdfas",
				"George", "Clooney", "@BigGC", null));
		User expected = new User("George", "Clooney", "@BigGC", "");
		assertEquals(expected, response.getUser());
	}
}
