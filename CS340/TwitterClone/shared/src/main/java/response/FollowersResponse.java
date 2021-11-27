package response;

import domain.User;

import java.util.List;

public class FollowersResponse extends PagedResponse {
	private List<User> followers;

	public FollowersResponse(String message) {
		super(false, message, false);
	}

	public FollowersResponse(List<User> followers, boolean hasMorePages) {
		super(true, hasMorePages);
		this.followers = followers;
	}

	public FollowersResponse() {}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
}
