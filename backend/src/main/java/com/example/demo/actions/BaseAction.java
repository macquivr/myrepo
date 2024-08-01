package com.example.demo.actions;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;

public class BaseAction {
    protected Repos repos;

    public BaseAction(Repos r) {
        this.repos = r;
    }
}
