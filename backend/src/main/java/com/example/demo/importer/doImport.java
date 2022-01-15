package com.example.demo.importer;

import com.example.demo.dto.ImportDTO;
import com.example.demo.repository.StatementsRepository;
import com.example.demo.state.importer.ImportState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.mydate.DUtil;
import com.example.demo.importer.iobj.*;
import com.example.demo.domain.*;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class doImport extends importBase {
	private static final Logger log = LoggerFactory.getLogger(doImport.class);
	private List<Iimport> data = null;
	private Statements stmts = null;
	private Repos repos = null;
	private ImportDTO idto = null;
	private UtilImport util = null;
	public doImport(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid);

		idto = dto;
		repos = r;
		initStatements();
		data = new Vector<Iimport>();
		util = new UtilImport(uuid,repos,idto);
		util.setRepo(repos.getUtilitiesRepository());

		data.add(new MainAcct(uuid,repos,idto));
		data.add(new Mortg(uuid,repos,idto));
		data.add(new Slush(uuid,repos,idto));
		data.add(new MainSave(uuid,repos,idto));
		data.add(new EmmaSave(uuid,repos,idto));
		data.add(new Annual(uuid,repos,idto));
		data.add(new MerrilLynch(uuid,repos,idto));
		data.add(new Amazon(uuid,repos,idto));
		data.add(new Aaa(uuid,repos,idto));
        data.add(util);
		data.add(new Usaa(uuid,repos,idto));
   		data.add(new CapitalOne(uuid,repos,idto));
	}
	
	private void initStatements()
	{
		stmts = new Statements();
		stmts.setName(null);
		stmts.setCreated(DUtil.stopNow());
	}
	
	public List<String> go()
	{
		List<String> ret = new Vector<String>();
		process(ret);
		return ret;		
	}
	
	public void process(List<String> ret) {
		
		if (!verifyFiles(ret))
			return;

		if (!validateFiles(ret)) {
			System.out.println("EEK!");
			return;
		}

		List<String> missingLabels = validateLabels();
		if (missingLabels == null) {
			idto.setImportState(ImportState.ERROR);
			ret.add("Could not validate Labels.");
			return;
		}
		
		if (missingLabels.size() > 0) {
			idto.setImportState(ImportState.MISSING_LABELS);
			for (String s : missingLabels)
				ret.add(s);
			return;
		}

		doStypes(ret);
		if (ret.size() > 0) {
			idto.setImportState(ImportState.MISSING_STYPES);
			return;
		}

		idto.setImportState(ImportState.SAVING);

		log.info("Importing data...");
		boolean b = importData(ret);
		if (!b && (ret.size() == 0)) {
			ret.add("Could not import data.");
		}

		if (b && (ret.size() == 0))
			idto.setImportState(ImportState.SAVED);

	}

	private void doStypes(List<String> ret) {
		for (Iimport I : data) {
			I.setStype(ret);
			if (ret.size() > 0)
				return;
		}
	}
	
	public boolean verifyFiles(List<String> ret)
	{
		for (Iimport I : data) {
			if (!I.verifyFile(ret))
			return false;
		}
		
		return true;
	}
	
	private boolean validateFiles(List<String> err)
	{
		for (Iimport I : data) {
			if (!I.validateFile(err))
				return false;
		}
		
		return true;
	}
	
	private void addStuff(List<String> ret,List<String> tmp) 
	{
		if (tmp.size() > 0) {
			for (String s : tmp) 
				if (!ret.contains(s)) {
					if (s.length() > 0) {
						//log.info("TAG: Adding " + s);
						ret.add(s);
					}
				}
		}
	}
	
	private List<String> validateLabels()
	{
		List<String> tmp = null;
		List<String> ret = new Vector<String>();

		for (Iimport I : data) {
			tmp = I.validateLabels(getUuid().toString());
			if (tmp == null) {
				return null;
			}
			addStuff(ret,tmp);
		}

		return ret;
	}
	

	private boolean importData(List<String> err)
	{
		boolean ret = true;
		
		ret = importData(false,err);
		if (ret) {
			StatementsRepository sr = repos.getStatementsRepository();
			sr.save(stmts);
			ret = importData(true, err);
		}
		
		return ret;
	}

	private boolean importData(boolean doSave, List<String> err)
	{
		for (Iimport I : data) {
			if (!I.importData(stmts,doSave,err))
				return false;
		}
		
		return true;
	}
	
	public static void main(String[] args)
	{
		doImport obj = new doImport(null,null, null);
	    
		obj.go();
	}
}

