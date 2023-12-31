package com.example.demo.state;

import java.util.HashMap;
import java.util.UUID;
import com.example.demo.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sessions {
    private static final Logger logger=LoggerFactory.getLogger(Sessions.class);
    private final HashMap<String,SessionDTO> map;

    private static Sessions obj = null;

    private Sessions() {

        this.map = new HashMap<>();
    }

    public static Sessions getObj() {
        if (obj == null)
            obj = new Sessions();
        return obj;
    }

    public SessionDTO makeNewSession() {
        SessionDTO newSession = new SessionDTO();
        UUID uuid = UUID.randomUUID();

        String session = uuid.toString();
        
        newSession.setSession(session);
        map.put(session,newSession);

        logger.info("New Session: " + session + " " + map.size());

        return newSession;
    }

    public boolean findSession(String session) {
        return (map.get(session) != null);
    }

    public SessionDTO getSession(String session) {
        return map.get(session);
    }
}
