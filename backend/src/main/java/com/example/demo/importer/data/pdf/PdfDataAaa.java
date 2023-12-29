package com.example.demo.importer.data.pdf;


import com.example.demo.utils.Utils;
import com.example.demo.utils.mydate.DUtil;

import java.io.BufferedReader;
import java.util.Iterator;
import java.io.FileReader;
import com.example.demo.importer.IBase;
import com.example.demo.domain.Statement;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfDataAaa extends PBase {
private static final Logger log = LoggerFactory.getLogger(PdfDataAaa.class);
	
	public PdfDataAaa(IBase obj,IData data) throws BadDataException {
		super(obj,data);

	}
	
	protected String getLabel() { return "AAA"; }
	protected void doStartStop() throws BadDataException
	{
		Statement stmt = idata.getStmt();
		for (String s : lines) {
			if (s.startsWith("Previous balance ")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1);
				stmt.setSbalance(Double.valueOf(str).doubleValue());
			}
			if (s.startsWith("New balance ")) {
				int idx = s.indexOf('$');
				String str = null;
				if (idx == -1) {
					str = s.substring(14);
				} else {
					str = s.substring(idx+1);
				}
				stmt.setFbalance(Double.valueOf(str).doubleValue());
			}
			if ((s.startsWith("Payments ")) || (s.startsWith("Other credit"))) {
				int idx = s.indexOf('-');
				if (idx != -1) {
					String str = s.substring(idx + 1);
					if (stmt.getIna() != null) {
						double d = stmt.getIna();
						double ds = Double.valueOf(str).doubleValue();
						stmt.setIna(Utils.convertDouble(d + ds));
					} else {
						stmt.setIna(Double.valueOf(str).doubleValue());
					}
				}
			}
			if (s.startsWith("Purchases ")) {
				int idx = s.indexOf('+');
				if (idx != -1) {
					String str = s.substring(idx+1);
					stmt.setOuta(Double.valueOf(str).doubleValue());
				}
			}
			if (s.startsWith("Fees charged")) {
				int idx = s.indexOf('+');
				String str = s.substring(idx+1);
				stmt.setFee(Double.valueOf(str).doubleValue());
			}
		}
		if (stmt.getSbalance() == null) {
			throw new BadDataException("Could not find aaa start...");
			/*
			double sbal = 0;
			try {
				String dir = System.getenv("MYFI_DATA_DIR");
				String sbalpath = "/aaaS.txt";
				FileReader f = new FileReader(dir + sbalpath);
				BufferedReader bf = new BufferedReader(f);
				String r = bf.readLine();
				sbal = Double.valueOf(r);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			stmt.setSbalance(sbal);
			*/
		}
	}

	protected void doTransactions() throws BadDataException
	{
		boolean credit = true;

		Iterator<String> iter = lines.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.equals("TRANS DATE TRANSACTION DESCRIPTION/LOCATION AMOUNT"))
				break;
		}
		if (!iter.hasNext()) {
			throw new BadDataException("AAA couldn't find Payments and Other Credits");
		}
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.equals("Purchases and Adjustments")) {
				credit = false;
			}

			if (s.startsWith("Interest charged"))
				break;

			int idx = s.indexOf(' ');
			if (idx == -1)
				continue;

			String str = s.substring(0,idx).trim();
			String rest = s.substring(idx+1).trim();
			if (!DUtil.isValidMMDDYYYY(str)) {
				System.out.println("Bad Date...");
				continue;
			}
			boolean err = false;
			try {
				double d = Double.valueOf(rest);
			} catch (NumberFormatException ex) {
				err = true;
			}
			if (!err) {
				rest = "BJS FUEL #9209 HUDSON MA " + rest;
			}
			/*
			int didx = str.indexOf('/');
			String mstr = str.substring(0,didx);
			String value = map.get(mstr);
			String dstr = str + "/" + value;

			//rest = transLabel(rest,credit);
            */

			addNData(str, rest);
		}
	}
	
	private String transLabel(String str, boolean credit)
	{
		int didx = str.indexOf(' ');
		String next = str.substring(didx+1);
		
		int idx = findValue(next);

		String amt = null;
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
		int i = 0;
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
			throw new BadDataException("Bad Date " + dstr);
		}
		addMap(dstr);

		String ndstr = DUtil.monthMinusOne(dstr);
	    if (ndstr == null) {
			throw new BadDataException("Bad Date " + dstr);
		}
		addMap(ndstr);
		
	}
}
