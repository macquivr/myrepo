package com.example.demo.importw.data;

import com.example.demo.domain.*;
import java.util.List;
import java.util.Vector;

public class IMData {
	private final List<TLedger> tledger;

	public IMData()
	{
		tledger = new Vector<>();
	}
	
	public List<TLedger> getLedger() { return tledger; }
}
