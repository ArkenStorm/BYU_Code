package request;

public class LoginRequest {
    private String handle;
    private String password;

    public LoginRequest(String handle, String password) {
        this.handle = handle;
        this.password = password;
    }

    public LoginRequest() {}

    public String getHandle() {
        return handle;
    }

    public String getPassword() {
        return password;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
