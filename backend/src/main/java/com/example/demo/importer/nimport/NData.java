package com.example.demo.importer.nimport;

import com.example.demo.utils.Utils;

public class NData {
	private String date;
	private String label;
	private double amount;
	private double balance;
	
	private double credit;
	private double debit;
	private int check;
	
	public NData() {
		credit = 0;
		debit = 0;
		amount = 0;
		balance = 0;
		check = -1;
	}
	
	public void setDate(String d) { date = d; }
	public void setLabel(String l) { label = l; }
	public void setCredit(double d) { credit = Utils.dv(d); } 
	public void setDebit(double d) { debit = Utils.dv(d); }
	
	public void setAmount(double d) { amount = Utils.dv(d); } 
	public void setBalance(double d) { balance = Utils.dv(d); }
	
	public void setCheck(int c) { check = c; }
	
	public String getDate() { return date; }
	public String getLabel() { return label; }
	public double getCredit() { return credit; }
	public double getDebit() { return debit;  }
	
	public double getAmount() { return amount; }
	public double getBalance() { return balance;  }
	
	public int getCheck() { return check; }
	public void Print() { 
		System.out.println(date + " " + label + " " + credit + " " + debit + " " + check);
	}
	public void P() {
		System.out.println("DATE: " + date);
		System.out.println("LABEL: " + label);
		System.out.println("CREDIT: " + credit);
		System.out.println("DEBIT: " + debit);
	}
}
