package com.example.demo.state.impl;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NlcImpl implements SessionI {
    private static final Logger logger= LoggerFactory.getLogger(NlcImpl.class);

    @Override
    public boolean validate(String data) {
        return ((data.equals("NAME")) ||
                (data.equals("LOCATION")) ||
                (data.equals("CATEGORY")));
    }

    @Override
    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setNlc(data.getData());

        logger.info("nlc updated to " + data.getData());
    }
}
