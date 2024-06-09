package com.example.demo.importw.data.csv;

import com.example.demo.importw.IBasew;
import com.example.demo.importw.data.IData;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.NData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Iterator;
import com.example.demo.utils.mydate.DUtil;

public class CsvDataUsaa extends PBase {
private static final Logger log = LoggerFactory.getLogger(CsvDataUsaa.class);

	public CsvDataUsaa(IBasew obj, IData data) throws BadDataException {
		super(obj,data);

	}
	
	protected String getLabel() { return "USAA"; }
	
	private String makeValue(String s, int idx) {
		String ret = s.substring(idx+1).replaceAll(",", "");
		if (ret.endsWith("-"))
			ret = "-" + ret.substring(0,ret.length()-1);
		return ret;
	}
	public NData ledgerLine(String[] tokens) throws BadDataException
	{
		/*
		Date,Description,Original Description,Category,Amount,Status
		0 - tdate
		1 - fnord
		2 - label
		3 - fnord
		4 - amount
		5 - fnord
		 */

		String dstr = DUtil.getCsvDate(tokens[0]);
		LocalDate ds = DUtil.getStdDate(dstr);
		if ((ds.isBefore(this.dates.getStart())) ||
				(ds.isAfter(this.dates.getStop()))) {
			return null;
		}

		if (tokens[3].equals("Credit Card Payment")) {
			return null;
		}
		NData ret = new NData();

		ret.setDate(dstr);
		ret.setLabel(tokens[2]);

		String amount = tokens[4];
		char t = amount.charAt(0);

		if (t != '-') {
			ret.setCredit(Double.parseDouble(amount));
		} else {
			ret.setDebit(Double.parseDouble(amount));
		}

		return ret;
	}
	
	protected String transform(String str) { return str; }

	private void makeMap(String dstr) throws BadDataException
	{
		addMap(dstr);

		String ndstr = DUtil.monthMinusOne(dstr);
		if (ndstr == null)
			throw new BadDataException("Bad Date " + dstr);

		addMap(ndstr);
	}
	
}
