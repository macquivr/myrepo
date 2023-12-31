package com.example.demo.services;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.repository.StypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.state.*;

@Service
public class SessionService {

    @Autowired
    LtypeRepository ltypeRepository;

    @Autowired
    StypeRepository stypeRepository;

    public StypeRepository getStypeRepo() { return stypeRepository; }
    public LtypeRepository getLtypeRepo() { return ltypeRepository; }
    public StatusDTO update(SessionUpdateDTO data) {
        StatusDTO status = new StatusDTO();
        validate(status,data);
        if (!status.getStatus()) {
            return status;
        }

        status.setMessage("Ok.");
        updateSession(data);
        return status;
    }

    public SessionDTO newSession() {
        return Sessions.getObj().makeNewSession();
    }

    public SessionDTO getSession(String session) {
        return Sessions.getObj().getSession(session);
    }

    private void validate(StatusDTO obj, SessionUpdateDTO data)
    {
        if (data.getSession() == null) {
            obj.setMessage("No Session.");
            return;
        }
        if (!validateSession(obj,data.getSession()))
            return;

        if (data.getType() == null) {
            obj.setMessage("No Type.");
            return;
        }
        if (!validateType(obj,data.getType()))
            return;

        if (data.getData() == null) {
            obj.setMessage("No data.");
            return;
        }

        if (!validateData(obj,data.getType(), data.getData()))
            return;

        obj.setStatus(true);
    }

    private boolean validateData(StatusDTO status, String type, String data)
    {
        SessionParameters sp = SessionParameters.valueOf(type);
        SessionI si = SessionP.getObj().get(sp,this);

        boolean valid =  si.validate(data);
        if (!valid) {
            status.setMessage("Invalid data " + data);
            return false;
        }
        return true;
    }

    private boolean validateType(StatusDTO status, String type)
    {
        SessionParameters sp;
        try {
            sp = SessionParameters.valueOf(type);
        } catch (Exception ex) {
            status.setMessage("Invalid type " + type);
            return false;
        }

        SessionI si = SessionP.getObj().get(sp,this);
        if (si == null) {
            status.setMessage("Couldn't find type map for " + type);
            return false;
        }

        return true;
    }

    private boolean validateSession(StatusDTO status, String session)
    {
        Sessions sobj = Sessions.getObj();
        if (!sobj.findSession(session)) {
            status.setMessage("Session " + session + " not found.");
            return false;
        }
        return true;
    }

    private void updateSession(SessionUpdateDTO data)
    {
        SessionParameters sp = SessionParameters.valueOf(data.getType());
        SessionI si = SessionP.getObj().get(sp,this);
        si.performAction(data);
    }


}
