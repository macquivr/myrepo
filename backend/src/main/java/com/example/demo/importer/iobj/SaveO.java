package com.example.demo.importer.iobj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Ltype;
import com.example.demo.domain.Statement;
import com.example.demo.importer.data.IData;
import com.example.demo.importer.data.IMData;
import com.example.demo.importer.data.NData;
import com.example.demo.importer.*;
import com.example.demo.utils.mydate.DUtil;
import java.util.HashMap;
import java.util.List;


public class SaveO extends importBase {
	private static final Logger log = LoggerFactory.getLogger(SaveO.class);
	private final Ltype ltypeo;
	private final HashMap<Integer,Checks> cmap;
	protected final IMData imdata;
	protected final IData data;
	protected final List<String> errs;

	public SaveO(IBase obj,int ltype, IData d, IMData im, List<String> err)
	{
		super(obj.getUuid());

		errs = err;
		ltypeo = obj.getLtype(ltype);
		cmap = new HashMap<>();
		data = d;
		imdata = im;
	}
	
	public boolean makeData()
	{
		if (!buildStatement())
			return false;
		boolean b = makeChecks();
		if (!b)
			return false;
		return makeLedger();
	}
	
	
	private boolean makeChecks()
	{
		List<NData> ndata = data.getData();
	  
		for (NData n : ndata) {
	    	if (n.getCheck() == -1)
	    		continue;
	    	Checks c = new Checks();
	    	c.setCheckNum(n.getCheck());
	    	c.setCheckDate(DUtil.getStdDate(n.getDate()));
	    	c.setLtype(ltypeo);
	    	c.setPayee(n.getPayee());
	    	
	    	cmap.put(n.getCheck(), c);
	    }
	    return true;
	}
	
	
	private boolean buildStatement()
	{
		Statement istmt = data.getStmt();
		Statement stmt = imdata.getStatement();
		stmt.setLtype(ltypeo);
	
		Double start = istmt.getSbalance();
		if (start == null) {
			errs.add("Sbalance not set " + istmt.getLtype().getName());
			return false;
		}

		Double stop = istmt.getFbalance();
		if (stop == null) {
			errs.add("Fbalance not set");
			return false;
		}

		Double ina = istmt.getIna();
		if (ina == null) {
			errs.add("Ina not set");
			return false;
		}

		Double outa = istmt.getOuta();
		if (outa == null) {
			errs.add("outa not set");
			return false;
		}

		Boolean credit = istmt.getCredit();
		if (credit == null) {
			errs.add("Credit not set");
			return false;
		}

		Double fee = istmt.getFee();
		if (fee == null) {
			errs.add("Fee not set");
			return false;
		}

		stmt.setSbalance(start);
		stmt.setFbalance(stop);
		stmt.setIna(ina);
		stmt.setOuta(outa);
		stmt.setCredit(credit);
		stmt.setFee(fee);

		return true;
	}
	
	private boolean makeLedger()
	{
		List<NData> ndata = data.getData();
	    List<Ledger> ls = imdata.getLedger();
	    
	    for (NData n : ndata) {
	    	Ledger l = new Ledger();
	    		    
	    	String date = n.getDate();
	    	Label label = n.getLbl();
	    	double amt = (n.getCredit() > 0) ? n.getCredit() : (n.getDebit() * -1);
	    	if (amt == 0) {
	    		String str = "Bad Amount " + date + " " + label.getName();
	    		log.error(str);
	    		System.out.println(str);
	    		return false;
	    	}
	    	l.setTransdate(DUtil.getStdDate(date));
	    	l.setLtype(ltypeo);
	    	l.setStype(n.getStype());
	    	l.setLabel(label);
	    	l.setAmount(amt);
	    	if (n.getCheck() != -1) 	
	    		l.setChecks(cmap.get(n.getCheck()));
	    	
	    	ls.add(l);
	    }
	    return true;
	}
	
}
