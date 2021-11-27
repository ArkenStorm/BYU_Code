package response;

import domain.Status;

public class PostResponse extends Response {
    private Status status;

    public PostResponse(String message) {
        super(false, message);
    }

    public PostResponse(Status status) {
        super(true);
        this.status = status;
    }

    public PostResponse() {}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
