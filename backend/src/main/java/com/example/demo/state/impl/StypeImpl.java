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
        Long id;
        try {
            id = Long.valueOf(data);
        } catch (Exception ex) {
            logger.error("Id not numeric " + data);
            return false;
        }

        return repository.findById(id.intValue()).isPresent();
    }

    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setStype(Integer.valueOf(data.getData()));
    }
}
