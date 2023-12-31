package com.example.demo.dto;

public class StatusDTO {
    private boolean status;
    private String message;

    public StatusDTO() {
        status = false;
        message = "uninitialized";
    }

    public void setStatus(boolean b) { status = b; }
    public boolean getStatus() { return status; }

    public void setMessage(String m) { message = m; }
    public String getMessage() { return message; }
}
