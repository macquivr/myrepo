package com.example.demo.state;

import java.util.HashMap;
import com.example.demo.services.SessionService;
import com.example.demo.state.msi.*;

public class SessionP {
    private HashMap<SessionParameters,Msi> map = null;
    private static SessionP obj;

    private SessionP()
    {
        map = new HashMap<SessionParameters,Msi>();
        register();
    }

    public static SessionP getObj() {
        if (obj == null)
            obj = new SessionP();
        return obj;
    }

    private void register()
    {
        map.put(SessionParameters.START_DATE,new Sdi());
        map.put(SessionParameters.END_DATE,new Edi());
        map.put(SessionParameters.USE_DATE,new Udi());
        map.put(SessionParameters.LTYPE,new Ltypei());
        map.put(SessionParameters.STYPE,new Stypei());
        map.put(SessionParameters.CONSOLIDATE,new Cdi());
        map.put(SessionParameters.PERCENT,new Pdi());
        map.put(SessionParameters.NLC,new Nlci());
        map.put(SessionParameters.NLCV,new Nlcvi());
        map.put(SessionParameters.REPORT,new Reporti());
    }

    public SessionI get(SessionParameters p,SessionService service) { return map.get(p).factory(service); }
}
