package com.example.demo.importer.data.pdf;


import com.example.demo.utils.Utils;
import com.example.demo.utils.mydate.DUtil;
import java.util.Iterator;

import com.example.demo.importer.IBase;
import com.example.demo.domain.Statement;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfDataAmz extends PBase {
private static final Logger log = LoggerFactory.getLogger(PdfDataAmz.class);
	
	public PdfDataAmz(IBase obj,IData data) throws BadDataException {
		super(obj,data);
		
	}
	
	protected String getLabel() { return "AMAZON"; }
	protected String transform(String str) { return str; }
	
	protected void doStartStop() throws BadDataException
	{
		Statement stmt = idata.getStmt();
		for (String s : lines) {
			if (s.startsWith("Opening/Closing Date")) {
				int idx = s.indexOf(' ');
				String str = s.substring(idx+1);
				idx = str.indexOf(' ');
				str = str.substring(idx+1);
				idx = str.indexOf('-');
				String str1 = str.substring(0,idx);
				String str2 = str.substring(idx+1);
				addMap(str1);
				addMap(str2);
			}
			if (s.startsWith("Previous Balance")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1).replaceAll(",", "");
				stmt.setSbalance(Double.parseDouble(str));
			}
			if (s.startsWith("New Balance:")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1).replaceAll(",", "");
				stmt.setFbalance(Double.parseDouble(str));
			}
			if (s.startsWith("Payment, Credit")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1).replaceAll(",", "");
				stmt.setIna(Double.parseDouble(str));
			}
			if (s.startsWith("Purchases +")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1).replaceAll(",", "");
				stmt.setOuta(Double.parseDouble(str));
			}
			if ((s.startsWith("Fees Charged")) || (s.startsWith("Interest Charged"))) {
				int idx = s.indexOf('$');
				String str = s.substring(idx + 1).replaceAll(",", "");
				if (stmt.getFee() != null) {
					double d = stmt.getFee();
					double ds = Double.parseDouble(str);
					stmt.setFee(Utils.convertDouble(d + ds));
				} else {
					stmt.setFee(Double.parseDouble(str));
				}
			}
		}
		
	}
	
	protected void doTransactions()
	{
		Iterator<String> iter = lines.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.startsWith("Transaction Merchant"))
				break;
		}
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.startsWith("Total fees charged")) 
				break;
			int idx = s.indexOf(' ');
			if (idx == -1)
				continue;
			String str = s.substring(0,idx).trim();
			String rest = s.substring(idx+1).trim();

			if (!DUtil.isValidMMDD(str))
				continue;

			int didx = str.indexOf('/');
			String mstr = str.substring(0,didx);
			String value = map.get(mstr);
			String dstr = str + "/" + value;
			
			addNData(dstr, rest);
		}
	}
}
