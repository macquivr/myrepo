package com.example.demo.state.impl;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LtypeImpl implements SessionI {
    private static final Logger logger=LoggerFactory.getLogger(LtypeImpl.class);
    private final LtypeRepository repository;

    public LtypeImpl(LtypeRepository r)
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
        if (validateSpecial(id))
            return true;

        return repository.findById(id.intValue()).isPresent();
    }

    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setLtype(Integer.valueOf(data.getData()));
    }

    private boolean validateSpecial(Long id)
    {
        if ((id.longValue() == 0) || (id.longValue() == -1))
            return true;

        return false;
    }
}
