package com.example.demo.importer.data;

import com.example.demo.domain.*;
import java.util.List;
import java.util.Vector;

public class IMData {
	private Statement stmt = null;
	private List<Ledger> ledger = null;

	public IMData()
	{
		stmt = new Statement();
		ledger = new Vector<Ledger>();
	}
	
	public Statement getStatement() { return stmt; }
	public List<Ledger> getLedger() { return ledger; }
}
