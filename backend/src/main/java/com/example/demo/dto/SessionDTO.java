package com.example.demo.dto;

import com.example.demo.state.Consolidate;
import com.example.demo.state.WhichDate;
import com.example.demo.utils.mydate.DUtil;
import java.time.LocalDate;

public class SessionDTO {
    private String session;
    private LocalDate start;
    private LocalDate stop;
    private WhichDate whichDate;
    private Consolidate consolidate;
    private int ltype;
    private int stype;
    private boolean percent;
    private String nlc;
    private String nlcv;
    private String reportType;

    public SessionDTO() {
        start = null;
        stop = null;
        whichDate = WhichDate.START;
        consolidate = Consolidate.NONE;
        ltype = 0;
        stype = 0;
        percent = false;
        nlc = "NONE";
        nlcv = null;
    }

    public String getSession() { return session; }
    public LocalDate getStart() {
        if (start == null)
            start = DUtil.startNow();
        return start;
    }
    public LocalDate getStop() {
        if (stop == null)
            stop = DUtil.stopNow();
        return stop;
    }
    public WhichDate getWhichDate() { return whichDate; }
    public Consolidate getConsolidate() { return consolidate; }
    public int getLtype() { return ltype; }
    public int getStype() { return stype; }
    public boolean isPercent() { return percent; }
    public String getNlc() { return nlc; }
    public String getNlcv() { return nlcv; }
    public String getReportType() { return reportType; }

    public void setSession(String session) { this.session = session; }
    public void setStart(LocalDate s) { start = s; }
    public void setStop(LocalDate s) { stop = s; }
    public void setWhichDate(WhichDate b) { whichDate = b; }
    public void setConsolidate(Consolidate c) { consolidate = c; }
    public void setLtype(int l) { ltype = l; }
    public void setStype(int s) { stype = s; }
    public void setPercent(boolean p) { percent = p; }
    public void setNlc(String n) { nlc = n; }
    public void setNlcv(String n) { nlcv = n; }
    public void setReportType(String r) { reportType = r; }
}
