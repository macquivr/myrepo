package com.example.demo.importer.data;

import com.example.demo.domain.*;
import java.util.List;
import java.util.Vector;

public class IMData {
	private final Statement stmt;
	private final List<Ledger> ledger;

	public IMData()
	{
		stmt = new Statement();
		ledger = new Vector<>();
	}
	
	public Statement getStatement() { return stmt; }
	public List<Ledger> getLedger() { return ledger; }
}
