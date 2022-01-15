package com.example.demo.state.msi;

import com.example.demo.services.SessionService;
import com.example.demo.state.Msi;
import com.example.demo.state.SessionI;
import com.example.demo.state.impl.ConsolidateImpl;

public class Cdi implements Msi {
    public SessionI factory(SessionService service) { return new ConsolidateImpl(); }
}
