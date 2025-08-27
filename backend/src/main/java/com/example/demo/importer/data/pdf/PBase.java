package com.example.demo.importer.data.pdf;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.utils.Utils;
import com.example.demo.importer.IBase;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.IData;
import com.example.demo.importer.data.NData;

public abstract class PBase extends PLines {
	private static final Logger log = LoggerFactory.getLogger(PBase.class);
	protected IData idata;
	protected HashMap<String, String> map;
	
	public PBase(IBase obj,IData data) throws BadDataException {
		super(obj);

		idata = data;
		map = new HashMap<>();
	}
	
	protected abstract String transform(String str);
	protected abstract void doStartStop() throws BadDataException;
	protected abstract void doTransactions() throws BadDataException;
	
	public void go() throws BadDataException
	{
		doStartStop();
		doTransactions();
	}
	
	protected void addNData(String dstr, String rest)
	{
		int idx = findValue(rest);
		String amt = rest.substring(idx+1);
		String lbl = rest.substring(0,idx);
		
		if (amt.endsWith("-")) {
			amt = "-" + amt.substring(0,amt.length()-1);
		}
		
		NData nd = new NData();
		nd.setDate(dstr);
		nd.setLabel(lbl);
		
		Double d = Utils.dval(amt);
		if ((d == null) || (d == 0))
			return;
		
		if (d > 0)
			nd.setDebit(d);
		else
			nd.setCredit(d * -1);
        
		idata.getData().add(nd);
	}
		
	protected int findValue(String rest) {
		String str = rest.trim();
		int idx = str.length()-1;
		while (str.charAt(idx) != ' ')
			idx--;
		return idx;
	}	
	
	protected void addMap(String s) throws BadDataException
	{
		String str = s.trim();
		
		int idx = str.indexOf('/');
		if (idx == -1) {
			log.error("BAD DATE " + s);
			throw new BadDataException("bad date " + s);
		}
		
		String s1 = str.substring(0,idx);
		String rest = str.substring(idx+1);
		
		idx = rest.indexOf('/');
		String s2 = "20" + rest.substring(idx+1);
		log.info("DMAP:  " + s1 + " ==> " + s2);
		map.put(s1,s2);
	}
	
}
