package com.example.demo.state.msi;

import com.example.demo.state.Msi;
import com.example.demo.state.SessionI;
import com.example.demo.state.impl.UseDateImpl;
import com.example.demo.services.SessionService;

public class Udi implements Msi {
    public SessionI factory(SessionService service) { return new UseDateImpl(); }
}
