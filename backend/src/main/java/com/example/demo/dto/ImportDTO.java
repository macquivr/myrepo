package com.example.demo.dto;

import com.example.demo.state.importer.ImportState;
import java.util.List;

public class ImportDTO {
    private String session;
    private ImportState state;
    private List<String> iresults;
    private boolean error = false;
    private String errMessage = null;
    private String mdata = null;

    public ImportDTO(String s) {
        session = s;
        state = ImportState.INIT;
    }

    public ImportState getImportState() { return state; }
    public void setImportState(ImportState s) { state = s; }

    public String getSession() { return session; }
    public void setSession(String s) { session = s; }

    public String getMdata() { return mdata; }
    public void setMdata(String mc) { mdata = mc; }

    public List<String> getIresults() { return iresults; }
    public void setIresults(List<String> r) { iresults = r; }

    public boolean getError() { return error; }
    public void setError(boolean b) {
        if (b)
            state = ImportState.ERROR;
        error = b;
    }

    public String getErrMessage() { return errMessage; }
    public void setErrMessage(String e) { errMessage = e; }
}
