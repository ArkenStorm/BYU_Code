package request;

import domain.User;

public class PostRequest {
    private User owner;
    private String statusText;
    private String authToken;

    public PostRequest(User owner, String statusText) {
        this.owner = owner;
        this.statusText = statusText;
    }

    public PostRequest() {}

    public User getOwner() {
        return owner;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
