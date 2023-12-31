package com.example.demo.state.impl;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.repository.StypeRepository;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StypeImpl implements SessionI {
    private static final Logger logger=LoggerFactory.getLogger(StypeImpl.class);
    private final StypeRepository repository;

    public StypeImpl(StypeRepository r)
    {
        repository = r;
    }

    public boolean validate(String data) {
        if (repository == null) {
            logger.error("No Repo.....");
            return false;
        }
        long id;
        try {
            id = Long.parseLong(data);
        } catch (Exception ex) {
            logger.error("Id not numeric " + data);
            return false;
        }

        return repository.findById((int) id).isPresent();
    }

    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setStype(Integer.parseInt(data.getData()));
    }
}
