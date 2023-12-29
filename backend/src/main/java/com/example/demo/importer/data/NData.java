package com.example.demo.importer.data;

import com.example.demo.utils.Utils;
import com.example.demo.domain.*;

public class NData {
	private String date;
	private String label;
	private double balance;
	private double credit;
	private double debit;
	private int check;
	private Payee payee;
	private Csbtype type;
	private Label lbl;
	private Stype stype;
	private String ndstr = null;
	
	public NData() {
		credit = 0;
		debit = 0;
		balance = 0;
		check = -1;
		payee = null;
		type = null;
		lbl = null;
		stype = null;
		ndstr = null;
	}
	
	public void setDate(String d) { date = d; }
	public void setLabel(String l) { label = l; }
	public void setCredit(double d) { credit = Utils.dv(d); } 
	public void setDebit(double d) { debit = Utils.dv(d); }
	public void setBalance(double d) { balance = Utils.dv(d); }
	public void setType(Csbtype t) { type = t; }
	public void setCheck(int c) { check = c; }
	public void setPayee(Payee p) { payee = p; }
	public void setLbl(Label l) { lbl = l; }
	public void setStype(Stype s) { stype = s; }
	public void setNDstr(String d) { ndstr = d; }
	
	public String getDate() { return date; }
	public String getLabel() { return label; }
	public double getCredit() { return credit; }
	public double getDebit() { return debit;  }
	public double getBalance() { return balance;  }
	public Csbtype getType() { return type; }
	public int getCheck() { return check; }
	public Payee getPayee() { return payee; }
	public Label getLbl() { return lbl; }
	public Stype getStype() { return stype; }
	public String getNDstr() { return ndstr; }

	public void P() {
		System.out.println("DATE: " + date);
		System.out.println("LABEL: " + label);
		System.out.println("CREDIT: " + credit);
		System.out.println("DEBIT: " + debit);
		if (type != null)
			System.out.println("TYPE: " + type.getId());
	}
}
