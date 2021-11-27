package response;

public class FollowResponse extends Response {
	private boolean isFollowed;

	public FollowResponse(String message) {
		super(false, message);
	}

	public FollowResponse(boolean update) {
		super(true);
		isFollowed = update;
	}

	public FollowResponse() {}

	public boolean isIsFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean followed) {
		isFollowed = followed;
	}
}
