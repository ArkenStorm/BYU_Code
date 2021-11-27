package response;

import domain.User;

import java.util.List;

public class FollowingResponse extends PagedResponse {
    private List<User> followees;

    public FollowingResponse(String message) {
        super(false, message, false);
    }

    public FollowingResponse(List<User> followees, boolean hasMorePages) {
        super(true, hasMorePages);
        this.followees = followees;
    }

    public FollowingResponse() {}

    public List<User> getFollowees() {
        return followees;
    }

    public void setFollowees(List<User> followees) {
        this.followees = followees;
    }
}
