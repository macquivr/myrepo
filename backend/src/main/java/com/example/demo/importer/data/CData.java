package com.example.demo.importer.data;

public class CData {
	private int num = 0;
	private String date = null;
	private double amt = 0;
	private int payee = 0;
	
	public void setNum(int n) { num = n; }
	public void setDate(String dstr) { date = dstr; }
	public void setAmt(double a) { amt = a; }
	public void setPayee(int p) { payee = p; }
	public int getNum() { return num; }
	public String getDate() { return date; }
	public double getAmt() { return amt; }
	
	public void Print()
	{
		System.out.println("Check: " + num + " Date: " + date + " Amount: " + amt);
	}
}
