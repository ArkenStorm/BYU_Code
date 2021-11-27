package request;

import domain.User;

public class FollowRequest {
	private boolean update;
	private User user; // The user who will be followed/unfollowed
	private User currentUser;
	private String authToken;

	public FollowRequest(boolean update, User user) {
		this.update = update;
		this.user = user;
	}

	public FollowRequest() {}

	public boolean isUpdate() {
		return update;
	}

	public User getUser() {
		return user;
	}

	public void setUpdate(boolean update) {
		this.update = update;
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

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
