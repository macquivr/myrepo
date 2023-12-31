package com.example.demo.state.impl;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import com.example.demo.state.WhichDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseDateImpl implements SessionI {
    private static final Logger logger=LoggerFactory.getLogger(UseDateImpl.class);

    public boolean validate(String data) {

        try {
            WhichDate.valueOf(data);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        WhichDate w;
        try {
            w = WhichDate.valueOf(data.getData());
        } catch (Exception ex) {
            return;
        }

        logger.info("UseDate updated to " + w);
        session.setWhichDate(w);
    }
}
