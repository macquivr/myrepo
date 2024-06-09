package com.example.demo.importw.data.csv;

import com.example.demo.importw.IBasew;
import com.example.demo.utils.mydate.DUtil;

import java.time.LocalDate;
import java.util.Iterator;
import com.example.demo.utils.Utils;
import com.example.demo.importer.data.NData;
import com.example.demo.importer.BadDataException;
import com.example.demo.importw.data.IData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataCapOne extends PBase {
private static final Logger log = LoggerFactory.getLogger(CsvDataCapOne.class);
	private double interest;
	
	public CsvDataCapOne(IBasew obj, IData data) throws BadDataException {
		super(obj,data);

		interest = 0;
	}
	
	protected String getLabel() { return "CapOne"; }

	private String makeValue(String s, int idx) {
		String ret = s.substring(idx+1).replaceAll(",", "");
		if (ret.endsWith("-"))
			ret = "-" + ret.substring(0,ret.length()-1);
		return ret;
	}

	private void payments()
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
	}
	
	public NData ledgerLine(String[] tokens)  throws BadDataException
	{
		/* Transaction Date,Posted Date,Card No.,Description,Category,Debit,Credit
		0 - tdate
		1 - fnord
		2 - fnord
		3 - label
		4 - fnord
		5 - debit
		6 - credit
		 */

		String dstr = DUtil.getCsvDate(tokens[0]);
		if (tokens[3].contains("PYMT")) {
			return null;
		}

		LocalDate ds = DUtil.getStdDate(dstr);
		if ((ds.isBefore(this.dates.getStart())) ||
				(ds.isAfter(this.dates.getStop()))) {
			return null;
		}

		NData ret = new NData();
		ret.setDate(dstr);

		String credit = (tokens.length == 6) ? "0" : tokens[6];
		String debit = tokens[5];

		if (!credit.equals("0")) {
			ret.setCredit(Double.parseDouble(credit));
		} else {
			ret.setDebit(Double.parseDouble(debit));
		}

		ret.setLabel(tokens[3]);

		return ret;
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
