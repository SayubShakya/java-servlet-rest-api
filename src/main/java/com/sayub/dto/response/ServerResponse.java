package com.sayub.dto.response;

public class ServerResponse {
    private Object data;
    private String message;
    private int statusCode;
    private String detail;

    public ServerResponse(Object data, String message, int statusCode, String detail) {
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
        this.detail = detail;
    }

    public static ServerResponse success(Object data, String message) {
        return new ServerResponse(data, message, 200, "");
    }

    public static ServerResponse error(String message) {
        return new ServerResponse(null, message, 500, "");
    }

    public static ServerResponse error(String message, int statusCode) {
        return new ServerResponse(null, message, statusCode, "");
    }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}