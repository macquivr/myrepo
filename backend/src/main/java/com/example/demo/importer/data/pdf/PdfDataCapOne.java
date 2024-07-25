package com.example.demo.importer.data.pdf;

import com.example.demo.utils.mydate.DUtil;
import java.util.Iterator;
import com.example.demo.importer.IBase;
import com.example.demo.utils.Utils;
import com.example.demo.domain.Statement;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfDataCapOne extends PBase {
private static final Logger log = LoggerFactory.getLogger(PdfDataCapOne.class);
	private double interest;
	
	public PdfDataCapOne(IBase obj,IData data) throws BadDataException {
		super(obj,data);

		interest = 0;
	}
	
	protected String getLabel() { return "CapOne"; }
	protected void doStartStop() {
		Statement stmt = idata.getStmt();
		double fees = 0;
		for (String s : lines) {
			if (s.contains("days in Billing Cycle")) {
				getYear(s);
			}
			
			if (s.startsWith("Previous Balance")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					stmt.setSbalance(Double.parseDouble(str));
				}
			}
			if (s.startsWith("New Balance")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					stmt.setFbalance(Double.parseDouble(str));
				}
			}
			
			if (s.startsWith("Transactions +")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					stmt.setOuta(Double.parseDouble(str));
				}
			}
			if (s.startsWith("Fees Charged")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					fees = Double.parseDouble(str);
				}
			}
			
			if (s.startsWith("Interest Charged")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					interest = Double.parseDouble(str);
				}
			}
		}
		stmt.setFee(Utils.dvAdd(fees,interest));
		
	    payments(stmt);	
	}

	private String makeValue(String s, int idx) {
		String ret = s.substring(idx+1).replaceAll(",", "");
		if (ret.endsWith("-"))
			ret = "-" + ret.substring(0,ret.length()-1);
		return ret;
	}

	private void payments(Statement stmt)
	{
		boolean on = false;
		double amt = 0;
		for (String s : lines) {
			if (on) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = s.substring(idx+1).replace(",","");
					double dv = Double.parseDouble(str);
					amt = Utils.dvAdd(amt,dv);
				}
			}
			if (s.contains(" Payments, Credits and Adjustments")) {
				on = true;
			}
			if (on && s.contains("Transaction")) {
				break;
			}
		}
		stmt.setIna(amt);
	}
	
	protected void doTransactions() throws BadDataException
	{
		Iterator<String> iter = lines.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.contains("Date Description Amount"))
				break;
		}
		if (!iter.hasNext()) {
			throw new BadDataException("CapOne couldn't find Payments and Other Credits");
		}
		String dstr = null;
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.startsWith("Total Transactions for This Period")) 
				break;

			if (s.contains("days in Billing Cycle"))
			    continue;
			
			int idx = s.indexOf(' ');
			if (idx == -1)
				continue;
			int idx2 = s.indexOf(' ',idx+1);
			if (idx2 == -1)
				continue;
			
			String str = s.substring(0,idx2).trim();
			String rest = s.substring(idx2+1).trim();

			if (!DUtil.isValidDate(str,"MMM d"))
				continue;

			String nstr = findNext(iter,rest);

			int didx = extractDate(nstr);
			if (didx == -1)
				throw new BadDataException("Could not parse capone data " + nstr);

			str = nstr.substring(0,didx).trim();
			rest = nstr.substring(didx+1).trim();

			didx = str.indexOf(' ');
			String mstr = str.substring(0,didx);
			String value = map.get(mstr);
			if (value == null)
				value = DUtil.getDefaultYear();
			dstr = makeDate(str, value);

			log.info("DSTR: " + dstr);
			addNData(dstr, rest);
		}
		if (interest != 0) {
			String nstr = "Interest Charged on Purchases $" + interest;
			addNData(dstr,nstr);
		}
	}

	private int extractDate(String nstr)  {
		int idx = nstr.indexOf(' ');
		if (idx == -1)
			return -1;
		int idx2 = nstr.indexOf(' ',idx+1);
		if (idx2 == -1)
			return -1;

		String str = nstr.substring(0,idx2).trim();

		if (!DUtil.isValidDate(str,"MMM d"))
			return -1;

		return idx2;
	}
	private String makeDate(String dstr, String value)
	{
		String ret = dstr.concat(" " + value);
		
		return DUtil.translate(ret,"MMM d yyyy","MM/dd/yyyy");
	}
	
	private String findNext(Iterator<String> iter, String str)
	{
		String ret = "";
		String s = str;
		int idx;
		do {
			idx = s.indexOf('$');
			if (idx != -1) {
				String front = s.substring(0,idx);
				if (!front.endsWith(" ")) {
					front = front.concat(" ");
				}
				if (front.endsWith(" - ")) {
					front = front.replaceAll(" - "," -");
				}
				if (front.endsWith("- ")) {
					front = front.replaceAll("- "," -");
				}
				String back = s.substring(idx+1);
				ret = ret.concat(front.concat(back));
			} else {
				ret = ret.concat(s);
				s = iter.next();
			}
		} while ((s != null) && (idx == -1));
		return ret;
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
	
	private void getYear(String s)
	{
		int idx = s.indexOf('|');
		if (idx == -1)
			return;
		
		String str = s.substring(0,idx);
		
		idx = str.indexOf('-');
		if (idx == -1) {
			return;
		}
		String front = str.substring(0,idx);
		String back = str.substring(idx+1);
		
		int idx1 = findValue(front);
		int idx2 = findValue(back);
		
		String fstr = front.substring(idx1+1).trim();
		String bstr = back.substring(idx2+1).trim();

		if (fstr.startsWith(" ") ) {
			fstr = fstr.substring(1);
		}
		
		if (bstr.startsWith(" ") ) {
			bstr = bstr.substring(1);
		}
		
		String fm = front.substring(0,3);
		String bm = back.substring(1,4);
		
		map.put(fm, fstr);
		map.put(bm, bstr);
	}
}
