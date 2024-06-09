package com.example.demo.importw.data;

import java.util.List;
import com.example.demo.domain.*;
import com.example.demo.importer.data.CData;
import com.example.demo.importer.data.NData;

import java.util.Vector;

public class IData {
	private String sdate = null;
	private final List<NData> data;
	private final List<CData> checks;
	
	public IData()
	{
		data = new Vector<>();
		checks = new Vector<>();
	}
	
	public String getSDate() { return sdate; }
	public void setSDate(String date) { sdate = date; }

	public List<NData> getData() { return data; }
	public List<CData> getChecks() { return checks; }
	
	public void Print() {
		
		for (NData nd : data) {
			System.out.println();
			nd.P();
		}
		
		for (CData c : checks)
			c.Print();
	}
}
