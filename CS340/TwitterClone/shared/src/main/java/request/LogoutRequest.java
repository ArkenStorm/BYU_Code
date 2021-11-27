package request;

import domain.User;

public class LogoutRequest {
	private User user;
	private String authToken;

	public LogoutRequest(User user) {
		this.user = user;
	}

	public LogoutRequest() {}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
