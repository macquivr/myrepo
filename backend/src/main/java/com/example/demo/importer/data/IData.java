package com.example.demo.importer.data;

import java.util.List;
import com.example.demo.domain.*;
import java.util.Vector;

public class IData {
	private String sdate = null;
	private Statement stmt;
	private List<NData> data;
	private List<CData> checks;
	
	public IData()
	{
		stmt = new Statement();
		data = new Vector<NData>();
		checks = new Vector<CData>();
	}
	
	public String getSDate() { return sdate; }
	public void setSDate(String date) { sdate = date; }
	
	public Statement getStmt() { return stmt; }
	public List<NData> getData() { return data; }
	public List<CData> getChecks() { return checks; }
	
	public void Print() {
		stmt.Print();
		
		for (NData nd : data) {
			System.out.println();
			nd.P();
		}
		
		for (CData c : checks)
			c.Print();
	}
}
