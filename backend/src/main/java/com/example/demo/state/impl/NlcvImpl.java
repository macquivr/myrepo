package com.example.demo.state.impl;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NlcvImpl implements SessionI {
    private static final Logger logger= LoggerFactory.getLogger(NlcvImpl.class);

    @Override
    public boolean validate(String data) {
        return true;
    }

    @Override
    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setNlcv(data.getData());

        logger.info("nlcv updated to " + data.getData());
    }
}
