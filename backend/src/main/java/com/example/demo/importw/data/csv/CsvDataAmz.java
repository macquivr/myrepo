package com.example.demo.importw.data.csv;


import com.example.demo.importw.IBasew;
import com.example.demo.utils.mydate.DUtil;

import java.time.LocalDate;
import java.util.Iterator;

import com.example.demo.importer.BadDataException;
import com.example.demo.importw.data.IData;
import com.example.demo.importer.data.NData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataAmz extends PBase {
private static final Logger log = LoggerFactory.getLogger(CsvDataAmz.class);
	
	public CsvDataAmz(IBasew obj, IData data) throws BadDataException {
		super(obj,data);
		
	}
	
	protected String getLabel() { return "AMAZON"; }
	protected String transform(String str) { return str; }
	
	public NData ledgerLine(String[] tokens) throws BadDataException
	{
		/*
		Transaction Date,Post Date,Description,Category,Type,Amount,Memo
		0 - tdate
		1 - fnord
		2 - label
		3 - fnord
		4 - fnord
		5 - amount
		6 - fnord
		 */
		LocalDate ds = DUtil.getStdDate(tokens[0]);
		if ((ds.isBefore(this.dates.getStart())) ||
				(ds.isAfter(this.dates.getStop()))) {
			return null;
		}


		NData ret = new NData();

		//if (tokens[4].equals("Payment")) {
		//	return null;
		//}

		ret.setDate(tokens[0]);
		ret.setLabel(tokens[2]);

		String amount = tokens[5];
		char t = amount.charAt(0);

		if (t != '-') {
			ret.setCredit(Double.parseDouble(amount));
		} else {
			ret.setDebit(Double.parseDouble(amount));
		}
		
		return ret;
	}

}
