package com.example.demo.importw;

import java.io.*;

import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.*;
import com.example.demo.importer.data.NData;
import com.example.demo.importw.data.IData;
import com.example.demo.importw.data.IMData;
import com.example.demo.repository.ChecksRepository;
import com.example.demo.repository.TLedgerRepository;
import com.example.demo.state.importer.ImportData;
import com.example.demo.state.importer.ImportState;
import com.example.demo.state.importer.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.Utils;
import com.example.demo.domain.*;
import com.example.demo.state.importer.ImportDR;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import com.example.demo.bean.NewLabelData;

public abstract class IBasew extends importBase implements Iimport {
	private static final Logger ilog = LoggerFactory.getLogger(IBasew.class);
	protected final IMData imdata;
	protected final IData data;
	protected String fname = null;
	protected int ltype = 0;
	protected boolean skip = false;
	protected boolean credit = false;
	protected Properties p = null;
	protected final Repos repos;
	protected final ImportDTO idto;
	protected Payperiod pp;

	public IBasew(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid);

		idto = dto;
		repos = r;
		imdata = new IMData();
		data = new IData();
	}

	public void setPayperiod(Payperiod pp) {
		this.pp = pp;
	}
	public Repos getRepos() { return repos; }
	public abstract boolean validateFile(List<String> err);

	public abstract boolean makeData(List<String> err);

	private void checkSave(TLedger t) {
		Ltype l = t.getLtype();
		Checks c = t.getChecks();
		int cnum = c.getCheckNum();
		ChecksRepository cr = repos.getChecksRepository();
		Checks e = cr.findByLtypeAndCheckNum(l, cnum);
		if (e == null) {
			cr.save(c);
		}
	}
	public boolean performSave()
	{
		TLedgerRepository lr = repos.getTLedgerRepository();
		List<TLedger> ls = imdata.getLedger();
		System.out.println("** SIZE: " + ls.size());
		for (TLedger l : ls) {
			Checks c = l.getChecks();
			if (c != null) {
				checkSave(l);
			}
			lr.save(l);
		}
		
		return true;
	}
	
	protected boolean initChecks() {
		p = checkUtil.getObj(true).readChecks();
		return (p != null);
	}
	
	protected boolean doChecks(List<String> ret)
	{
		boolean ok = initChecks();
		if (!ok) {
			ret.add("Could not parse check file");
			return false;
		} 
		for (NData d : data.getData()) {
			if (d.getCheck() > 0) {
				if (p == null) {
					ret.add("Missing check file.");
					return false;
				}
				int payee = getPayee(d.getCheck());
				if (payee == -1) {
					ChecksRepository r = repos.getChecksRepository();
					Checks tc = r.findByLtypeAndCheckNum(this.getLtype(this.ltype),d.getCheck());
					if (tc == null) {
						idto.setImportState(ImportState.MISSING_CHECKS);
						ret.add(fname.replace(".csv", "") + " " + d.getCheck());
					} else {
						d.setPayee(tc.getPayee());
					}
				} else {
					Optional<Payee> p = repos.getPayeeRepository().findById(payee);
			    	if (!p.isPresent()) {
						ret.add("Bad Payee " + payee);
						break;
					}
			    	else 
			    		d.setPayee(p.get());
				}
			}
		}
		return (ret.isEmpty());
	}
	
	private int getPayee(int cnum)
	{
		String cstr = String.valueOf(cnum);
	
		String payee = p.getProperty(cstr);
		if (payee == null) {
			return -1;
		}
		return Integer.parseInt(payee);
	}
	
	public boolean verifyFile(List<String> err)
	{
		String dir = checkUtil.getObj(true).getDir();
		File f = new File(dir + "/" + fname);
		boolean ret = f.exists();
		
		if (!ret)
			err.add("Could not find " + dir + "/" + fname);
		
		return ret;
	}
	
	public List<String> validateLabels(String session)
	{
		List<String> nfCache = new Vector<>();
		List<String> ret = new Vector<>();

		List<Label> lbls = repos.getLabelRepository().findAll();
		List<NData> nl = data.getData();
		for (NData n : nl) {
			String l = n.getLabel().trim();
			String tl = Utils.itrim(l);

			boolean found = false;
			for (Label lbl : lbls) {
				String ln = lbl.getName();
				ln = Utils.itrim(ln);

				if (tl.equals(ln)) {
					n.setLbl(lbl);
					found = true; 
					break;
				}
			}
			if (!found && !nfCache.contains(l)) {
				Dups d = repos.getDupsRepository().findByDupLabel(l);
				if (d == null) {
					if (!setNewLabelData(session,l,n))
						return null;
					ret.add(l);
					nfCache.add(l);
				} else {
					n.setLbl(d.getLabel());
				}
			}		
		}

		return ret;
	}

	private boolean setNewLabelData(String session, String l, NData n) {
		String type = fname.replace(".csv","");
		String td = n.getDate();
		ImportDR dr = Imports.getObj().getImportObj(session);
		if (dr == null) {
			System.out.println("Could not find dr object....");
			return false;
		}

		NewLabelData nd = dr.getHMapEntry(l);
		if (nd != null)
			return true;

		nd = new NewLabelData();
		nd.setLabel(l);
		nd.setDate(td);
		nd.setType(type);
		dr.putHMapEntry(l,nd);

		return true;
	}
	public boolean importData(Statements s, boolean doSave, List<String> err)
	{
		if (this.skip) {
			return true;
		}
		if (doSave) {
			return performSave();
		}
		boolean b = makeData(err);
		if (!b)
			return false;

		return validate(err);
	}
	
	private boolean validate(List<String> err)
	{
		return true;
	}
	
	public Ltype getLtype(int id) 
	{
		Optional<Ltype> obj = repos.getLtypeRepository().findById(id);
		return obj.orElse(null);
	}
	
	public String readFile() throws BadDataException {

	    ImportData dr = Imports.getObj().getData(getUuid().toString());
	    String data = dr.getData(fname);
	    if (data != null) {
			System.out.println("Cache Hit " + fname);
			return data;
	    }
	    System.out.println("Reading " + fname);
	    
	    String parsedText;
	    File file = new File(checkUtil.getObj(true).getDir() + "/" + fname);

	    if (!file.exists()) {
	    	ilog.error("NO FILE " + file.getPath());
	    	throw new BadDataException("NO FILE " + file.getPath());
	    }
	    try {
	    	parsedText = readCsv();
	    } catch (Exception e) {
	    	ilog.error("Couldn't read csv " + file.getPath());
	    	throw new BadDataException(e.getMessage());
	    }

	    dr.setData(fname,parsedText);
	    return parsedText;
	}

	private String readCsv() throws Exception {
		String fileName = checkUtil.getObj(true).getDir() + "/" + fname;
		return new String(Files.readAllBytes(Paths.get(fileName)));
	}
    
	public void setStype(List<String> ret)
	{
		for (NData d : data.getData()) {
			// lookup by label
			Stypemap stl = repos.getStypemapRepository().findByLabel(d.getLbl());
			if (stl != null) {
				d.setStype(stl.getStype());
				continue;
			}
			
			if (d.getType() != null) {
				// lookup by csbtype
				Stypemap s = repos.getStypemapRepository().findByCsbtype(d.getType());
				if (s != null) {
					d.setStype(s.getStype());
					continue;
				}	 
			}
			
			// lookup by payee
			if (d.getCheck() > 0) {
				Stype m = repos.getStypeRepository().findByName(d.getPayee().getCheckType().getName());
				d.setStype(m);
				continue;
			}
		
			// kick if not in label lookup 
			

			String str = d.getLbl().getName();
			if (!ret.contains(str))
				ret.add(str);

		}
	}


}
