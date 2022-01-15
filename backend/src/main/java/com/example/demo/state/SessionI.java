package com.example.demo.state;

import com.example.demo.dto.SessionUpdateDTO;

public interface SessionI {
    public boolean validate(String data);
    public void performAction(SessionUpdateDTO data);
}
