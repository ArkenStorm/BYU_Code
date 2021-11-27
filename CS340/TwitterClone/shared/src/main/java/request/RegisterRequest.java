package request;

import java.io.Serializable;

public class RegisterRequest implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String handle;
    private String avatar; // This will be a bitmap encoded as a base64 string

    public RegisterRequest(String email, String password, String firstName, String lastName, String handle, String avatar) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.handle = handle;
        this.avatar = avatar;
    }

    public RegisterRequest() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHandle() {
        return handle;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
