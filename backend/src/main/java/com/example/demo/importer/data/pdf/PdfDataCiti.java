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

public class PdfDataCiti extends PBase {
private static final Logger log = LoggerFactory.getLogger(PdfDataCiti.class);
	private double cell = 0;
	private double cable = 0;
	private double electric = 0;

	public PdfDataCiti(IBase obj,IData data) throws BadDataException {
		super(obj,data);

	}

	public double getCell() { return cell; }
	public double getCable() { return cable; }
	public double getElectric() { return electric; }

	protected String getLabel() { return "Citi"; }
	
	protected void doStartStop() throws BadDataException
	{
		Statement stmt = idata.getStmt();
		double payments = -1;
		double credits = -1;
		boolean y = false;
		
		for (String s : lines) {
			if (y) {
				yearMap(s);
				y = false;
			}
			if (s.startsWith("JOHN A TODD")) 
				y = true;
		
			if (s.startsWith("Payments") && (payments == -1)) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = s.substring(idx+1);
					payments = Double.valueOf(str).doubleValue();
				}
			}
			
			if (s.startsWith("Credits") && (credits == -1)) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = s.substring(idx+1);
					credits = Double.valueOf(str).doubleValue();
				}
			}
			
			if (s.startsWith("Previous balance")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1);
				stmt.setSbalance(Double.valueOf(str).doubleValue());
			}
			
			if (s.startsWith("New balance")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1);
				stmt.setFbalance(Double.valueOf(str).doubleValue());
			}
	
			if (s.startsWith("Purchases +")) {
				int idx = s.indexOf('$');
				if (idx != -1) {
					String str = s.substring(idx+1);
					stmt.setOuta(Double.valueOf(str).doubleValue());
				}
			}
			if (s.startsWith("Fees +")) {
				int idx = s.indexOf('$');
				String str = s.substring(idx+1);
				stmt.setFee(Double.valueOf(str).doubleValue());
			}
		}
		stmt.setIna(Utils.dvAdd(payments, credits));
	}
	
	protected void doTransactions() throws BadDataException
	{
		boolean autopay = false;
		String mdstr = null;

		Iterator<String> piter = lines.iterator();
		while (piter.hasNext()) {
			String s = piter.next();
			log.info("CITIL: " + s);
		}

		Iterator<String> iter = lines.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.equals("date Description Amount"))
				break;
		}
		if (!iter.hasNext()) {
			throw new BadDataException("Citi couldn't find next");
		}
		while (iter.hasNext()) {
			String s = iter.next();

			if (autopay) {
				if (s.equals("AUTO-PMT"))
					continue;

				String dstr = makeDstr(mdstr);
				String rest = "AUTO-PMT " + s;

				log.info("PSTR: " + dstr + " REST: " + rest);
				addNData(dstr, rest);
				autopay = false;
				continue;
			}
			if (s.startsWith("Days in billing cycle"))
				break;
		
			int idx = s.indexOf(' ');
			if (idx == -1)
				continue;
			String str = s.substring(0,idx).trim();
			String rest = s.substring(idx+1).trim();

			if (!DUtil.isValidMMDD(str))
				continue;
	
			if (rest.contains("AUTOPAY")) {
				mdstr = str;
				autopay = true;
				continue;
			}
			idx = rest.indexOf(' ');
			rest = rest.substring(idx+1);
			
			String dstr = makeDstr(str);

			setUtils(rest);
			log.info("DSTR: " + dstr + " REST: " + rest);
			addNData(dstr, rest);
		}
		
	}

	private String makeDstr(String str) {
		int didx = str.indexOf('/');
		String mstr = str.substring(0,didx);
		String value = map.get(mstr);

		return  str + "/" + value;
	}
	private void setUtils(String rest)
	{
		int idx = rest.indexOf('$');
		String amount = rest.substring(idx+1);
		if (rest.contains("NATIONAL GRID")) {
			electric += Double.valueOf(amount);
		}
		if (rest.contains("SOLAR")) {
			electric += Double.valueOf(amount);
		}
		if (rest.contains("COMCAST")) {
			cable = Double.valueOf(amount);
		}
		if (rest.contains("XFINITY")) {
			cell = Double.valueOf(amount);
		}

	}
	protected String transform(String str)	{ return str; }	
	
	private void yearMap(String line) throws BadDataException
	{
		int idx = line.indexOf('-');
		if (idx == -1)
			return;
	    int check = line.indexOf('/');
	    if (check == -1)
	    	return;
	    
		String one = line.substring(0,idx);
		String two = line.substring(idx+1);
		addMap(one);
		addMap(two);
	}

}
