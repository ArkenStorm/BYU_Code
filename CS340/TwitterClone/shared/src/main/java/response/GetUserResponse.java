package response;

import domain.User;

public class GetUserResponse extends Response {
    private User user;
    private boolean isFollowedByCurrentUser;

    public GetUserResponse(String message) {
        super(false, message);
    }

    public GetUserResponse(User user, boolean isFollowedByCurrentUser) {
        super(true);
        this.user = user;
        this.isFollowedByCurrentUser = isFollowedByCurrentUser;
    }

    public GetUserResponse() {}

    public User getUser() {
        return user;
    }

    public boolean isFollowedByCurrentUser() {
        return isFollowedByCurrentUser;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFollowedByCurrentUser(boolean followedByCurrentUser) {
        isFollowedByCurrentUser = followedByCurrentUser;
    }
}
