package com.example.demo.state;

import com.example.demo.services.SessionService;

public interface Msi {
    SessionI factory(SessionService service);
}
