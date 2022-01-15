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
public class ReportImpl implements SessionI {
    private static final Logger logger=LoggerFactory.getLogger(ReportImpl.class);

    public ReportImpl()
    {
        /* nop */
    }

    public boolean validate(String data) {
        // make sure its a valid report type
        return true;
    }

    public void performAction(SessionUpdateDTO data) {
        Sessions sobj = Sessions.getObj();
        SessionDTO session = sobj.getSession(data.getSession());

        session.setReportType(data.getData());
    }
}
