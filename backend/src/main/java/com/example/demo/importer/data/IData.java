package com.example.demo.importer.data;

import java.util.List;
import com.example.demo.domain.*;
import java.util.Vector;

public class IData {
	private String sdate = null;
	private final Statement stmt;
	private final List<NData> data;
	private final List<CData> checks;
	
	public IData()
	{
		stmt = new Statement();
		data = new Vector<>();
		checks = new Vector<>();
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
