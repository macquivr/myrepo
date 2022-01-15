package com.example.demo.dto.ui;

import com.example.demo.utils.RowDTOI;
import com.example.demo.utils.Utils;

public class StypeRowDTO implements RowDTOI {
    private String label;
    private double bills;
    private double pos;
    private double atm;
    private double misc;
    private double deposit;
    private double annual;
    private double credit;
    private double total;

    public String getLabel() { return label; }
    public double getBills() { return bills; }
    public double getPos() { return pos; }
    public double getAtm() { return atm; }
    public double getMisc() { return misc; }
    public double getDeposit() { return deposit; }
    public double getAnnual() { return annual; }
    public double getCredit() { return credit; }
    public double getTotal() { return total; }

    public void setLabel(String l) { label = l; }
    public void setBills(double d) { bills = d; }
    public void setPos(double d) { pos = d; }
    public void setAtm(double d) { atm = d; }
    public void setMisc(double d) { misc = d; }
    public void setDeposit(double d) { deposit = d; }
    public void setAnnual(double d) { annual = d; }
    public void setCredit(double d) { credit = d; }
    public void setTotal(double t) { total = t; }
    public String toString()
    {
        return "label " + label + ",bills " + bills + ",pos " + pos + ",atm " + atm + ",misc " + misc + ",annual " + annual + ",credit " + credit + "\n";
    }

    public void makePercent() {
        double ltotal = Utils.convertDouble(bills + pos + atm + misc + annual + credit);
        ltotal = ltotal * (-1);

        bills = Utils.convertDouble((bills / ltotal) * 100);
        pos = Utils.convertDouble((pos / ltotal) * 100);
        atm = Utils.convertDouble((atm / ltotal) * 100);
        misc = Utils.convertDouble((misc / ltotal) * 100);
        annual = Utils.convertDouble((annual / ltotal) * 100);
        credit = Utils.convertDouble((credit / ltotal) * 100);
        total = 100;
    }
}
