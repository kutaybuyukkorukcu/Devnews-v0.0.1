package utils;

import com.google.gson.JsonElement;

public class StandardResponse {
    private StatusResponse status;
    private int statusCode;
    private String message;
    private JsonElement data;

    public StandardResponse(StatusResponse status, int statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public StandardResponse(StatusResponse status, int statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    public StandardResponse(StatusResponse status, int statusCode, String message, JsonElement data) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
