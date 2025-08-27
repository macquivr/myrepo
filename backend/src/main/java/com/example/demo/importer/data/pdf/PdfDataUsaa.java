package com.example.demo.importer.data.pdf;

import com.example.demo.importer.IBase;
import com.example.demo.utils.Utils;
import com.example.demo.domain.Statement;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import com.example.demo.utils.mydate.DUtil;

public class PdfDataUsaa extends PBase {
private static final Logger log = LoggerFactory.getLogger(PdfDataUsaa.class);

	public PdfDataUsaa(IBase obj,IData data) throws BadDataException {
		super(obj,data);

	}
	
	protected String getLabel() { return "USAA"; }
	
	protected void doStartStop() throws BadDataException
	{
		Statement stmt = idata.getStmt();
		double payments = -1;
		double credits = -1;
		double out1 = 0;
		double out2 = 0;
		double out;

		for (String s : lines) {
			if (s.startsWith("Statement Closing Date")) {
				int idx = findValue(s);
				String str = s.substring(idx+1);
				makeMap(str);
			}
			if (s.startsWith("Previous Balance")) {
				int idx = s.indexOf('$');
				String str = makeValue(s,idx);
				stmt.setSbalance(Double.parseDouble(str));
			}
			if (s.startsWith("New Balance $")) {
				int idx = s.indexOf('$');
				String str = makeValue(s,idx);
				stmt.setFbalance(Double.parseDouble(str));
			}
			
			if (s.startsWith("Payments - ") && (payments == -1)) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					payments = Double.parseDouble(str);
				}
			}
			
			if (s.startsWith("Other Credits") && (credits == -1)) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					credits = Double.parseDouble(str);
				}
			}
			
			if (s.startsWith("New Purchases +")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					out1 = Double.parseDouble(str);
					//stmt.setOuta(Double.parseDouble(str));
				}
			}
			if (s.startsWith("New Balance Transfers")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = makeValue(s,idx);
					out2 = Double.parseDouble(str);
					//stmt.setOuta(Double.parseDouble(str));
				}
			}
			if ((s.startsWith("Fees Charged +")) || (s.startsWith("Interest Charged"))) {
				int idx = s.indexOf('$');
				String str = makeValue(s,idx);
				if (stmt.getFee() != null) {
					double d = stmt.getFee();
					double ds = Double.parseDouble(str);
					stmt.setFee(Utils.convertDouble(d + ds));
				} else {
					stmt.setFee(Double.parseDouble(str));
				}

			}
			
		}
		out = Utils.convertDouble(out1 + out2);
		stmt.setOuta(out);
		stmt.setIna(Utils.dvAdd(payments, credits));
	}

	private String makeValue(String s, int idx) {
		String ret = s.substring(idx+1).replaceAll(",", "");
		if (ret.endsWith("-"))
			ret = "-" + ret.substring(0,ret.length()-1);
		return ret;
	}
	protected void doTransactions() throws BadDataException
	{
		Iterator<String> iter = lines.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.equals("Transactions"))
				break;
		}
		if (!iter.hasNext()) {
			throw new BadDataException("USAA couldn't find Payments and Other Credits");
		}
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.startsWith("Total Interest"))
				break;
			
			int idx = s.indexOf(' ');
			if (idx == -1)
				continue;
			String str = s.substring(0,idx).trim();
			String rest = s.substring(idx+1).trim();

			if (!DUtil.isValidMMDD(str))
				continue;
	
			idx = rest.indexOf(' ');
			rest = rest.substring(idx+1);
			
			idx = rest.indexOf(' ');
			rest = rest.substring(idx+1);
			
			int didx = str.indexOf('/');
			String mstr = str.substring(0,didx);
			String value = map.get(mstr);
			String dstr = str + "/" + value;
			
			idx = rest.indexOf('$');
			if (idx == -1) {
				String astr = null;
				while (iter.hasNext()) {
					String s2 = iter.next();
					if (s2.startsWith("Total Interest"))
						break;
					idx = s2.indexOf('$');
					if (idx != -1) {
						astr = s2.substring(idx);
						break;
					}
				}
				if (astr != null)
					rest = rest.concat(" " + astr);
			}
			log.info("STUFF: " + dstr + " " + rest);
			addNData(dstr, rest);
		}
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
