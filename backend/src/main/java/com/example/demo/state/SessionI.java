package com.example.demo.state;

import com.example.demo.dto.SessionUpdateDTO;

public interface SessionI {
    boolean validate(String data);
    void performAction(SessionUpdateDTO data);
}
