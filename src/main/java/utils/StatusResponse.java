package utils;

public enum StatusResponse {
    SUCCESS(200, "Success - Mock for now!"),
    ERROR(404, "Error - Mock for now!"),
    NOT_MODIFIED(304, "Not modified!"),
    BAD_REQUEST(400, "Bad request!"),
    NOT_FOUND(404, "Not found!"),
    SERVER_ERROR(500, "Internal server error!");

    private int statusCode;
    private String message;

    StatusResponse() {

    }

    StatusResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
