package com.example.demo.importw.iobj;

import com.example.demo.domain.*;
import com.example.demo.importw.IBasew;
import com.example.demo.repository.ChecksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.importw.data.IData;
import com.example.demo.importw.data.IMData;
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
	private ChecksRepository repo;
	private Payperiod pp = null;

	public SaveO(IBasew obj, int ltype, IData d, IMData im, List<String> err, ChecksRepository r, Payperiod pp)
	{
		super(obj.getUuid());

		this.pp = pp;
		this.repo = r;
		errs = err;
		ltypeo = obj.getLtype(ltype);
		cmap = new HashMap<>();
		data = d;
		imdata = im;
	}
	
	public boolean makeData()
	{
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
			Checks c = repo.findByLtypeAndCheckNum(ltypeo,n.getCheck());
	    	if (c == null) {
				c = new Checks();

				c.setCheckNum(n.getCheck());
				c.setCheckDate(DUtil.getStdDate(n.getDate()));
				c.setLtype(ltypeo);
				c.setPayee(n.getPayee());
			}
			cmap.put(n.getCheck(), c);
	    }
	    return true;
	}
	
	private boolean makeLedger()
	{
		List<NData> ndata = data.getData();
	    List<TLedger> ls = imdata.getLedger();

	    for (NData n : ndata) {
	    	TLedger l = new TLedger();
			l.setPayperiod(this.pp);
	    	String date = n.getDate().trim();

	    	Label label = n.getLbl();
	    	double amt = 0;
			if (n.getCredit() > 0)  {
				amt = n.getCredit();
			} else {
				if (n.getDebit() > 0) {
					amt = (n.getDebit() * -1);
				} else {
					amt = n.getDebit();
				}
			}
	    	if (amt == 0) {
	    		String str = "Bad Amount " + date + " " + label.getName();
	    		log.error(str);
	    		System.out.println(str);
	    		return false;
	    	}
			System.out.println("DSTR: " + date + " AMT: " + amt + " Credit: " + n.getCredit() + " Debit: " + n.getDebit());
	    	l.setTdate(DUtil.getStdDate(date));
	    	l.setLtype(ltypeo);
	    	l.setLabel(label);
	    	l.setAmount(amt);
	    	if (n.getCheck() != -1) 	
	    		l.setChecks(cmap.get(n.getCheck()));
	    	
	    	ls.add(l);
	    }
	    return true;
	}
	
}
