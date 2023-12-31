package com.example.demo.state.impl;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.state.SessionI;
import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import com.example.demo.utils.mydate.DUtil;


public class EndDateImpl implements SessionI {

    private static final Logger logger=LoggerFactory.getLogger(EndDateImpl.class);

    public boolean validate(String data) {
        boolean b = DUtil.isValidDate(data, DUtil.FULL_FMT);
        if (!b) {
            logger.error("Could not parse date " + data);
            return false;
        }
        return true;
    }

    public void performAction(SessionUpdateDTO data) {
        SessionDTO session = Sessions.getObj().getSession(data.getSession());

        String sdate = DUtil.translate(data.getData(),DUtil.FULL_FMT,DUtil.MMDDYYYY);
        LocalDate dt = DUtil.getDate(sdate,DUtil.MMDDYYYY);
        if (dt == null) {
            logger.error("Could not parse date " + data);
            return;
        }
        dt = DUtil.lastOfMonth(dt);

        session.setStop(dt);
        logger.info("Updated end date to " + dt);
    }
}
