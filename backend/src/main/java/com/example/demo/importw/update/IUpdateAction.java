package com.example.demo.importw.update;

import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.importer.Repos;
import com.example.demo.state.importer.ImportDR;
import com.example.demo.dto.StatusDTO;

public interface IUpdateAction {
    public void performAction(Repos r, SessionUpdateDTO data, ImportDR dr, StatusDTO sts);
}
