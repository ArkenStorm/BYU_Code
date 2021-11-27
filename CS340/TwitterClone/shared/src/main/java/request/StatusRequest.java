package request;


import domain.Status;
import domain.User;

public class StatusRequest {
    private User owner;
    private int limit;
    private Status lastStatus;
    private String authToken;

    public StatusRequest(User owner, int limit, Status lastStatus) {
        this.owner = owner;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public StatusRequest() {}

    public User getOwner() {
        return owner;
    }

    public int getLimit() {
        return limit;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
