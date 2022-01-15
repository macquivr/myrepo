package com.example.demo.dto;

public class SessionUpdateDTO {
    private String session;
    private String type;
    private String data;

    public String getType() { return type; }
    public String getData() { return data; }

    public void setType(String s) { type = s; }
    public void setData(String s) { data = s; }

    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }
}
