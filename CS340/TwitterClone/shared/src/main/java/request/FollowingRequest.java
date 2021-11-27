package request;


import domain.User;

public class FollowingRequest {
    private User follower;
    private int limit;
    private User lastFollowee;

    public FollowingRequest(User follower, int limit, User lastFollowee) {
        this.follower = follower;
        this.limit = limit;
        this.lastFollowee = lastFollowee;
    }

    public FollowingRequest() {}

    public User getFollower() {
        return follower;
    }

    public int getLimit() {
        return limit;
    }

    public User getLastFollowee() {
        return lastFollowee;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastFollowee(User lastFollowee) {
        this.lastFollowee = lastFollowee;
    }
}
