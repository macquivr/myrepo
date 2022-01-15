package com.example.demo.state.msi;

import com.example.demo.state.Msi;
import com.example.demo.state.SessionI;
import com.example.demo.state.impl.StartDateImpl;
import com.example.demo.services.SessionService;

public class Sdi implements Msi {
    public SessionI factory(SessionService service) { return new StartDateImpl(); }
}
