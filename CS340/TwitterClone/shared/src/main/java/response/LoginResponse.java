package response;

import domain.User;

public class LoginResponse extends Response {
    private User user;
    private String authtoken;

    public LoginResponse(boolean success, String message) {
        super(success, message);
    }

    public LoginResponse(boolean success, User user) {
        super(success);
        this.user = user;
    }

    public LoginResponse() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
