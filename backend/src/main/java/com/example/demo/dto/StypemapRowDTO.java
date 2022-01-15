package com.example.demo.dto;

import com.example.demo.domain.Stypemap;

public class StypemapRowDTO {
    private String csbtype;
    private String label;
    private String payee;
    private String stype;

    public StypemapRowDTO() { /* npo */ }
    public StypemapRowDTO(Stypemap data) {
        csbtype = (data.getCsbType() != null) ? data.getCsbType().getName() : "N/A";
        label = (data.getLabel() != null) ? data.getLabel().getName() : "N/A";
        payee = (data.getPayee() != null) ? data.getPayee().getName() : "N/A";
        stype = data.getStype().getName();
    }

    public void setCsbType(String n) { csbtype = n; }
    public void setLabel(String s) { label = s; }
    public void setStype(String s) { stype = s; }
    public void setPayee(String d) { payee = d; }

    public String getCsbType() { return csbtype; }
    public String getLabel() { return label;}
    public String getStype() { return stype; }
    public String getPayee() { return payee; }
}
