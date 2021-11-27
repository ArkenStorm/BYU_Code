package request;

import domain.User;

public class GetUserRequest {
    private String handle;
    private User currentUser;

    public GetUserRequest(String handle, User currentUser) {
        this.handle = handle;
        this.currentUser = currentUser;
    }

    public GetUserRequest() {}

    public String getHandle() {
        return handle;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
