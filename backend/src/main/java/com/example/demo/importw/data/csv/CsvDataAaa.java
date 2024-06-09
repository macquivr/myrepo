package com.example.demo.importw.data.csv;


import com.example.demo.importer.data.NData;
import com.example.demo.importw.IBasew;
import com.example.demo.importw.data.IData;
import com.example.demo.utils.mydate.DUtil;

import java.time.LocalDate;
import java.util.Iterator;
import com.example.demo.importer.BadDataException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataAaa extends PBase {
private static final Logger log = LoggerFactory.getLogger(CsvDataAaa.class);
	
	public CsvDataAaa(IBasew obj, IData data) throws BadDataException {
		super(obj,data);

	}
	
	protected String getLabel() { return "AAA"; }

	public NData ledgerLine(String[] tokens) throws BadDataException
	{
		/*
		Date,Description,Location,Category,Amount
			0 - tdate
			1 - label
			2 - fnord
			3 - fnord
			4 - amount
		 */

		if (tokens[3].equals("Payment")) {
			return null;
		}

		LocalDate ds = DUtil.getStdDate(tokens[0]);
		if ((ds.isBefore(this.dates.getStart())) ||
				(ds.isAfter(this.dates.getStop()))) {
			return null;
		}

		NData ret = new NData();
		ret.setDate(tokens[0]);
		ret.setLabel(tokens[1].trim() + " " + tokens[2].trim());

		String category = tokens[3];
		String amount = tokens[4];

		if (category.equals("Charge"))  {
			ret.setDebit(Double.parseDouble(amount));
		} else {
			ret.setCredit(Double.parseDouble(amount));
		}

		return ret;
	}
	
	private String transLabel(String str, boolean credit)
	{
		int didx = str.indexOf(' ');
		String next = str.substring(didx+1);
		
		int idx = findValue(next);

		String amt;
		if (credit)
			amt = "-" + next.substring(idx+2);
		else
			amt = next.substring(idx+1);


		String tmp = next.substring(0,idx);

		String lbl = getLabel(tmp);
	
		return lbl + " " + amt;
	}
		
	private String getLabel(String str)
	{
		int idx = findValue(str);
		String one = str.substring(0,idx+1).trim();
		idx = findValue(one);
		String rstr = one.substring(0,idx+1).trim();
		String rest = one.substring(idx+1).trim();
		
		try {
			Integer.valueOf(rest);
		} catch (Exception ex) {
			return one;
		}
		return rstr;
	}
	
	protected String transform(String str)
	{
		byte[] bytes = str.getBytes();
		int i;
		int j = 0;
		boolean on = false;
		for (i=0;i<bytes.length;i++) {
			if (bytes[i] == '\'')
				continue;
			if (bytes[i] == ' ') {
				if (on) 
					continue;
				else
					on = true;
			} else {
				on = false;
			}
			j++;
		}
		byte[] nbytes = new byte[j];
		j = 0;
		for (i=0;i<bytes.length;i++) {
			if (bytes[i] == '\'')
				continue;
			if (bytes[i] == ' ') {
				if (on) 
					continue;
				else
					on = true;
			} else {
				on = false;
			}
			nbytes[j++] = bytes[i];
		}
		return new String(nbytes);
	}
	
	private void getYear(String str) throws BadDataException
	{
		if (!str.startsWith("JOHN A TODD")) 
			return;
		
		int idx = str.indexOf('!');
		if (idx == -1)
			return;
		
		idx = str.indexOf('-');
		String tstr = str.substring(idx+2);

		String dstr = DUtil.translate(tstr, "MMMM d, yyyy","MM/dd/yy");
		if (dstr == null) {
			throw new BadDataException("Bad Date " + tstr);
		}
		addMap(dstr);

		String ndstr = DUtil.monthMinusOne(dstr);
	    if (ndstr == null) {
			throw new BadDataException("Bad Date " + dstr);
		}
		addMap(ndstr);
		
	}
}
