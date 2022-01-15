package com.example.demo.state.msi;

import com.example.demo.state.Msi;
import com.example.demo.state.SessionI;
import com.example.demo.state.impl.LtypeImpl;
import com.example.demo.services.SessionService;

public class Ltypei implements Msi {
    public SessionI factory(SessionService service) { return new LtypeImpl(service.getLtypeRepo()); }
}
