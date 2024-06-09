package com.example.demo.dto;

import com.example.demo.domain.TLedger;
import com.example.demo.domain.Wdatamap;

import java.time.LocalDate;

public class WdatamapRowDTO {
    private LocalDate tdate;
    private String who;
    private String freq;
    private String credit;
    private String wid;
    private String tid;
    private String other;

    public WdatamapRowDTO() { /* npo */ }
    public WdatamapRowDTO(Wdatamap data) {
        try {
            this.tdate = data.getTdate();
            this.who = data.getWho().getName();
            this.freq = data.getFreq().getName();
            this.credit = (data.getCredit() == 1) ? "Yes" : "No";
            this.wid = data.getWid().getStart().toString() + " ==> " + data.getWid().getStop().toString();
            this.tid = data.getTid().getLabel().getName() + " " + data.getTid().getAmount();
            this.other = (data.getOther() == null) ? "None" : data.getOther().getName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTdate(LocalDate d) { this.tdate = d; }
    public void setWho(String w) { this.who = w; }
    public void setFreq(String f) { this.freq = f; }
    public void setCredit(String c) { this.credit = c; }
    public void setWid(String w) { this.wid = w; }
    public void setTid(String t) { this.tid = t; }
    public void setOther(String o) { this.other = o; }


    public LocalDate getTdate() { return this.tdate; }
    public String getWho() { return this.who; }
    public String getFreq() { return this.freq; }
    public String getCredit() { return this.credit; }
    public String getWid() { return this.wid; }
    public String getTid() { return this.tid; }
    public String getOther() { return this.other; }




}
