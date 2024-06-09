package com.example.demo.importw;

import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Iimport;
import com.example.demo.importer.Repos;
import com.example.demo.importer.importBase;
import com.example.demo.importw.iobj.*;
import com.example.demo.repository.StatementsRepository;
import com.example.demo.state.importer.ImportState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.domain.*;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class doImportw extends importBase {
	private static final Logger log = LoggerFactory.getLogger(doImportw.class);
	private List<Iimport> data = null;
	private Statements stmts = null;
	private final Repos repos;
	private final ImportDTO idto;

	public doImportw(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid);

		this.idto = dto;
		this.repos = r;
		data = new Vector<>();
		data.add(new MainAcct(uuid,repos,idto));
		data.add(new Slush(uuid,repos,idto));
		data.add(new Annual(uuid,repos,idto));
		data.add(new MerrilLynch(uuid,repos,idto));
		data.add(new Amazon(uuid,repos,idto));
		data.add(new Aaa(uuid,repos,idto));
		data.add(new Usaa(uuid,repos,idto));
		data.add(new CapitalOne(uuid,repos,idto));
	}
	
	public List<String> go()
	{
		List<String> ret = new Vector<>();
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
		
		if (!missingLabels.isEmpty()) {
			idto.setImportState(ImportState.MISSING_LABELS);
			ret.addAll(missingLabels);
			return;
		}

		doStypes(ret);
		if (!ret.isEmpty()) {
			idto.setImportState(ImportState.MISSING_STYPES);
			return;
		}

		idto.setImportState(ImportState.SAVING);

		log.info("Importing data...");
		boolean b = importData(ret);
		if (!b && (ret.isEmpty())) {
			ret.add("Could not import data.");
		}

		if (b && (ret.isEmpty()))
			idto.setImportState(ImportState.SAVED);

	}

	private void doStypes(List<String> ret) {
		for (Iimport I : data) {
			I.setStype(ret);
			if (!ret.isEmpty())
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
		if (!tmp.isEmpty()) {
			for (String s : tmp) 
				if (!ret.contains(s)) {
					if (!s.isEmpty()) {
						//log.info("TAG: Adding " + s);
						ret.add(s);
					}
				}
		}
	}
	
	private List<String> validateLabels()
	{
		List<String> tmp;
		List<String> ret = new Vector<>();

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
		boolean ret;
		
		ret = importData(false,err);
		if (ret) {
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

}

