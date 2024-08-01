package com.example.demo.state.impl;

import com.example.demo.controllers.ReportController;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PercentImpl implements SessionI {
    private static final Logger logger= LoggerFactory.getLogger(PercentImpl.class);

    @Override
    public boolean validate(String data) {
        try {
            Boolean.parseBoolean(data);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    @Override
    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        boolean c;
        try {
            c = Boolean.parseBoolean(data.getData());
        } catch (Exception ex) {
            return;
        }

        ReportController.flipAction();
        logger.info("percent updated to " + c);
        session.setPercent(c);
    }
}
