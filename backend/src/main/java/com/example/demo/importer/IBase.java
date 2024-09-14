package com.example.demo.importer;

import java.io.*;

import com.example.demo.dto.ImportDTO;
import com.example.demo.repository.ChecksRepository;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.StatementRepository;
import com.example.demo.state.importer.ImportData;
import com.example.demo.state.importer.ImportState;
import com.example.demo.state.importer.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.persistence.NoResultException;

import com.example.demo.importer.data.*;
import com.example.demo.utils.Utils;
import com.example.demo.domain.*;
import com.example.demo.state.importer.ImportDR;
import java.util.Properties;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import com.example.demo.bean.NewLabelData;

public abstract class IBase extends importBase implements Iimport {
	private static final Logger ilog = LoggerFactory.getLogger(IBase.class);
	protected final IMData imdata;
	protected final IData data;
	protected String fname = null;
	protected int ltype = 0;
	
	protected boolean credit = false;
	protected Properties p = null;
	protected final Repos repos;
	protected final ImportDTO idto;

	public IBase(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid);

		idto = dto;
		repos = r;
		imdata = new IMData();
		data = new IData();
	}

	public void setPayperiod(Payperiod pp) { /* nop */ }
	public Repos getRepos() { return repos; }
	public abstract boolean validateFile(List<String> err);
	public abstract void attachStatement(Statements stmts,Statement stmt);
	public abstract boolean makeData(Statements stmts, List<String> err);
	
	public boolean performSave(Statements stmts)
	{ 
		Statement stmt = imdata.getStatement();
		StatementRepository sr = repos.getStatementRepository();
		LedgerRepository lr = repos.getLedgerRepository();
		ChecksRepository cr = repos.getChecksRepository();
		sr.save(stmt);
		attachStatement(stmts,stmt);
		List<Ledger> ls = imdata.getLedger();
		for (Ledger l : ls) {
			Checks c = l.getChecks();
			if (c != null) {
				Checks e = cr.findByLtypeAndCheckNum(l.getLtype(), c.getCheckNum());
				if (e == null) {
					cr.save(c);
				} else {
					l.setChecks(e);
				}
			}
			lr.save(l);
			l.setStatement(stmt);
		}
		
		return true;
	}
	
	protected boolean initChecks() {
		p = checkUtil.getObj(false).readChecks();
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
					idto.setImportState(ImportState.MISSING_CHECKS);
					ret.add(fname.replace(".pdf","") + " " + d.getCheck());
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
			System.out.println("Bad Payee " + cstr);
			return -1;
		}
		return Integer.parseInt(payee);
	}
	
	public boolean verifyFile(List<String> err)
	{
		String dir = checkUtil.getObj(false).getDir();
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
		String type = fname.replace(".pdf","");
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
	public boolean importData(Statements stmts, boolean doSave, List<String> err)
	{ 
		if (doSave) {
			return performSave(stmts);
		}
		boolean b = makeData(stmts,err);
		if (!b)
			return false;

		if (findStatement(stmts.getName()) ) {
			err.add("Statement already imported.");
			return false;
		}
		return validate(err);
	}
	
	private boolean findStatement(String name)
	{
		try {	
			Statements s = repos.getStatementsRepository().findByName(name);
			if (s != null)
				return true;
		} catch (NoResultException ex) {
			return false;
		}
		return false;
	}
	
	private boolean validate(List<String> err)
	{
		Statement stmt = imdata.getStatement();
		List<Ledger> data = imdata.getLedger();
		double in = 0;
		for (Ledger l : data) {
			if (l.getAmount() > 0) 
				in = Utils.dvAdd(in, l.getAmount());
		}
		if (in != stmt.getIna()) {
			err.add("IN WRONG FOR " + stmt.getLtype().getId() + " Statement: " + stmt.getIna() + " Ledger: " + in);
			return false;
		}
		
		double out = 0;
		for (Ledger l : data) {
			if (l.getAmount() < 0) 
				out = Utils.dvSub(out, l.getAmount());
		}
		if (out != Utils.dvAdd(stmt.getOuta(),stmt.getFee())) {
			err.add("OUT WRONG FOR " + stmt.getLtype().getId() + " Statement: " + stmt.getOuta() + " Ledger: " + out);
			stmt.lPrint();
			return false;
		}
		
		ilog.info("OK " + stmt.getLtype().getId());
		return true;
	}
	
	public Ltype getLtype(int id) 
	{
		Optional<Ltype> obj = repos.getLtypeRepository().findById(id);
		return (obj.isPresent()) ? obj.get() : null;
	}
	
	public String readPdf() throws BadDataException {

		ImportData dr = Imports.getObj().getData(getUuid().toString());
		String data = dr.getData(fname);
		if (data != null) {
			System.out.println("Cache Hit " + fname);
			return data;
		}
		System.out.println("Reading " + fname);

	    PDFParser parser;
	    PDDocument pdDoc = null;
	    COSDocument cosDoc = null;
	    PDFTextStripper pdfStripper;
	    
	    String parsedText;
	    File file = new File(checkUtil.getObj(false).getDir() + "/" + fname);

	    if (!file.exists()) {
	    	ilog.error("NO FILE " + file.getPath());
	    	throw new BadDataException("NO FILE " + file.getPath());
	    }
	    try {
	    	parser = new PDFParser(new RandomAccessBufferedFileInputStream(file));
	    	parser.parse();
	    	cosDoc = parser.getDocument();
	    	pdfStripper = new PDFTextStripper();
	    	pdDoc = new PDDocument(cosDoc);
	    	parsedText = pdfStripper.getText(pdDoc);
	    } catch (Exception e) {
	    	ilog.error("Couldn't read pdf " + file.getPath());
	    	throw new BadDataException(e.getMessage());
	    } finally {
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	    dr.setData(fname,parsedText);
	    return parsedText;
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
