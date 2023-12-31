package com.example.demo.importer.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.domain.*;
import com.example.demo.importer.IBase;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.pdf.*;
import com.example.demo.utils.Utils;
import com.example.demo.importer.CsbEType;
import com.example.demo.utils.mydate.DUtil;
import java.util.List;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Vector;

public class PdfData extends PLines {
	private static final Logger log = LoggerFactory.getLogger(PdfData.class);
	private final HashMap<String,String> map;

	private final List<String> checks;
	private final IData idata;
	private final CsbEType etype;

	public PdfData(IBase obj, IData data, CsbEType et) throws BadDataException {
		super(obj);

		map = new HashMap<>();
		etype = et;

		checks = new Vector<>();
		idata = data;
		initMap();
		populate();
	}

	private void initMap()
	{
		map.put("2104","Main");
		map.put("9792","Mortg");
		map.put("2859","Slush");
		map.put("8528","Annual");
		map.put("0756","Mainsave");
		map.put("8117","Emmasave");
	}
	public String getLabel() { return "CSB"; }
	private void populate()
	{
		boolean check = false;
		for (String l : lines)  {
			if (check) {
				checks.add(l);
				check = false;
			}
			if (l.equals("Check Nbr Date Amount")) 
				check = true;
		}
	}
	
	public void go() throws BadDataException
	{
		int l = stmt();
		ledger(l);
		processChecks();
	}
	
	private void processChecks() throws BadDataException 
	{
		int w;
		for (String c : checks) {
			w = 0;
			CData ch = new CData();
			StringTokenizer st = new StringTokenizer(c," ");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				switch (w) {
				case 0:
					addCheck(token,ch);
					break;
				case 1:
					addDate(token,ch);
					break;
				case 2:
					addAmount(token,ch);
					break;
				}
				w++;
			}
			idata.getChecks().add(ch);
		}
	}

	private void addCheck(String token,CData ch) throws BadDataException
	{
		if (token.charAt(token.length()-1) == '*') {
			token = token.substring(0,token.length()-1);
		}
		int num;
		try {
			num = Integer.parseInt(token);
		} catch (Exception ex) {
			throw new BadDataException("Couldn't parse check number " + token);
		}
		ch.setNum(num);
	}
	
	private void addDate(String token,CData ch)
	{
		ch.setDate(token);
	}
	private void addAmount(String token,CData ch) throws BadDataException
	{
		String amts = token.substring(1);
		double amt = 0;
		try {
			Double d = Utils.dval(amts);
			if (d != null)
				amt = d;
		} catch (Exception ex) {
			throw new BadDataException("Couldn't parse amount " + token);
		}
		ch.setAmt(amt);		
	}
	
	private void stmtLine(String line,int l) 
	{
		int idx;
		String str;
		double amt = 0;
		if (l < 4) {
			idx = line.indexOf('$');
			str = line.substring(idx+1).replaceAll(",","");
			Double d = Utils.dval(str);
			if (d != null)
				amt = d;
		}
		Statement stmt = idata.getStmt();
		switch(l) {
		case 0:
			stmt.setSbalance(amt);
			break;
		case 1:
			stmt.setIna(amt);
			break;
		case 2:
			stmt.setOuta(amt);
			break;
		case 3:
			stmt.setFbalance(amt);
			break;
		}
		
	}
	private int stmt() 
	{
		int ln = 0;
		int l = 0;
		boolean on = false;
		for (String s : lines) {
			if (on) {
				stmtLine(s,l);
				l++;
			}
			if (s.startsWith("Date Description Amount")) 
				on = true;
			if (s.startsWith("Account Activity"))
				break;
			ln++;
		}
		return ln;
	}

	private String changeLabelToTransfer(String data)
	{
		String str = data.replace("Internet Transfer ", "");
		int idx = str.indexOf(' ');
		String dw = str.substring(0,idx);
		String rest = str.substring(idx+1);
		idx = rest.indexOf(' ');
		String tof = rest.substring(0,idx);
		rest = rest.substring(idx+1);
		idx = rest.indexOf(' ');
		String tgt = rest.substring(idx+1,idx+5);

		String target = map.get(tgt);
		if (target == null) {
			log.info("TAG: bad target " + data + " TGT: #" + tgt + "#");
			target = "BAD";
		}
		String estr = etype.toString().charAt(0) + etype.toString().substring(1).toLowerCase();

		return dw + " " + estr + " " + tof + " " + target;
	}

	private NData ledgerLine(String line,NData prev) throws BadDataException {
		if (prev == null) {
			throw new BadDataException("Could not determine starting balance");
		}
		NData nd = new NData();
		int idx = line.indexOf(' ');
		String dstr = line.substring(0,idx);
		nd.setDate(dstr);
		String rest = line.substring(idx+1);
		idx = rest.indexOf('$');
		String label = rest.substring(0,idx);
		while (label.charAt(0) == ' ') {
			label = label.substring(1);
		}

		if (label.contains("Internet Transfer") && !label.contains("3031"))
			label = changeLabelToTransfer(label);

		nd.setLabel(label);
		String amts = rest.substring(idx+1);

		idx = amts.indexOf(' ');
		if (idx == -1) {
			throw new BadDataException("Could not parse amount for line " + line);
		}
		String amt = amts.substring(0,idx).replaceAll(",", "");
		String bals = amts.substring(idx+1).replaceAll(",", ""); // .substring(1);

		if (!bals.startsWith("-"))
			bals = bals.substring(1);

		double newb = 0;
		try {
			Double d = Utils.dval(bals);
			if (d != null) {
				newb = d;
				nd.setBalance(newb);
			}
		} catch (Exception ex) {
			throw new BadDataException("Could not parse balance for line " + line);
		}
		
		double amta = 0;
		try {
			Double d =  Utils.dval(amt);
			if (d != null)
				amta = d;
		} catch (Exception ex) {
			throw new BadDataException("Could not parse amount for line " + line);
		}
		
		double oldb = prev.getBalance();
		if (newb > oldb) {
			nd.setCredit(amta);
		} else {
			nd.setDebit(amta);
		}

		idata.getData().add(nd);
		return nd;
	}
	
	private String findDate(Iterator<String> iter)
	{
		boolean done = false;
		String ret = null;
		while (!done) {
			if (!iter.hasNext()) {
				done = true;
				continue;
			}
		
			String dstr;
			String str = iter.next();
			
			int idx = str.indexOf(' ');
			if (idx == -1) 
				dstr = str;
			else
				dstr = str.substring(0,idx);
		
			if (DUtil.isValidDate(dstr,DUtil.MMDDYYYY))  {
				done = true;
				ret = str;
			}
		}
		
		return ret;
	}
	private String getNext(Iterator<String> iter)
	{
		if (!iter.hasNext()) 
			return null;
		
		String dstr = findDate(iter);
		if (dstr == null)
			return null;
		
		if (dstr.contains("Beginning Balance")) {
			int idx = dstr.indexOf(' ');
			idata.setSDate(dstr.substring(0,idx));
				
			dstr = findDate(iter);
			if (dstr == null)
				return null;
		}
		
		int idx = dstr.indexOf(' ');
		if (idx == -1) { 
			String tstr = iter.next();
			dstr = dstr.concat(" " + tstr);
		} else {
			int idx2 = dstr.indexOf('$');
			if (idx2 != -1) {
				String nstr = dstr.substring(idx2+1);
				int idx3 = nstr.indexOf('$');
				if (idx3 != -1) {
					return dstr;
				}
			}
		}
		
		boolean done = false;
		
		while (!done) {
			if (!iter.hasNext())
				return null;
			String nstr = iter.next();
			int idx2 = nstr.indexOf('$');
			if (idx2 != -1) {
				String nstr2 = nstr.substring(idx2+1);
				int idx3 = nstr2.indexOf('$');
				if (idx3 != -1) {
					done = true;
				} else {
					nstr = nstr.replace("$","");
				}
			}
			dstr = dstr.concat(" " + nstr);
		}
		
		if (dstr.contains("Ending Balance"))
			return null;
	
		return dstr;
	}
	
	private void ledger(int start) throws BadDataException
	{
		NData prev = new NData();
		prev.setBalance(idata.getStmt().getSbalance());
		int l = 0;
		boolean done = false;
		Iterator<String> iter = lines.iterator();
		
		while (l < start) {
			l++;
			if (iter.hasNext())
				iter.next();
		}
		if (!iter.hasNext())
			return;
		
		
		while (!done) {
			String istr = getNext(iter);
			if (istr != null)
				prev = ledgerLine(istr,prev);
			else
				done = true;
		}
	}
}
