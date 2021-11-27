package request;


import domain.User;

public class FollowersRequest {
	private User followee;
	private int limit;
	private User lastFollower;

	public FollowersRequest(User followee, int limit, User lastFollower) {
		this.followee = followee;
		this.limit = limit;
		this.lastFollower = lastFollower;
	}

	public FollowersRequest() {
	}

	public User getFollowee() {
		return followee;
	}

	public int getLimit() {
		return limit;
	}

	public User getLastFollower() {
		return lastFollower;
	}

	public void setFollowee(User followee) {
		this.followee = followee;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLastFollower(User lastFollower) {
		this.lastFollower = lastFollower;
	}
}
