package com.hexagonal.ms_foodcourt.domain.model.response;

public class MessageResult {
    private String message;

    public MessageResult(String message) {
        this.message = message;
    }

    public MessageResult() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
