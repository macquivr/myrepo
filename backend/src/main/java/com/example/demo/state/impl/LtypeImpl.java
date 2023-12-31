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
        long id;
        try {
            id = Long.parseLong(data);
        } catch (Exception ex) {
            logger.error("Id not numeric " + data);
            return false;
        }
        if (validateSpecial(id))
            return true;

        return repository.findById((int) id).isPresent();
    }

    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setLtype(Integer.parseInt(data.getData()));
    }

    private boolean validateSpecial(Long id)
    {
        return ((id == 0) || (id == -1));
    }
}
