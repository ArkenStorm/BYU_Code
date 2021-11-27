package response;

class Response {

    private boolean success;
    private String message;

    Response(boolean success) {
        this(success, null);
    }

    Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response() {}

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
