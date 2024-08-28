package com.example.demo.actions;


import com.example.demo.dto.SessionDTO;

public interface ActionI {
    boolean go(SessionDTO session) throws Exception;
}
