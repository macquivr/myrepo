package com.example.demo.importw;

import com.example.demo.actions.InAction;
import com.example.demo.actions.OutAction;
import com.example.demo.bean.StartStop;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Iimport;
import com.example.demo.importer.Repos;
import com.example.demo.importer.checkUtil;
import com.example.demo.importer.importBase;
import com.example.demo.importw.iobj.*;
import com.example.demo.repository.IntableRepository;
import com.example.demo.repository.OuttableRepository;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.repository.StatementsRepository;
import com.example.demo.state.importer.ImportState;
import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.domain.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;

public class doImportw extends importBase {
	private static final Logger log = LoggerFactory.getLogger(doImportw.class);
	private List<Iimport> data = null;
	private Statements stmts = null;
	private final Repos repos;
	private final ImportDTO idto;
	private Payperiod pp = null;

	public doImportw(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid);

		this.repos = r;
		this.pp = getPayperiod();
		this.idto = dto;

		data = new Vector<>();
		data.add(new MainAcct(uuid,repos,idto,this.pp));
		data.add(new Slush(uuid,repos,idto,this.pp));
		data.add(new Annual(uuid,repos,idto,this.pp));
		data.add(new MerrilLynch(uuid,repos,idto,this.pp));
		data.add(new Amazon(uuid,repos,idto,this.pp));
		data.add(new Aaa(uuid,repos,idto,this.pp));
		data.add(new Usaa(uuid,repos,idto,this.pp));
		data.add(new CapitalOne(uuid,repos,idto,this.pp));
	}

	private Payperiod getPayperiod() {
		StartStop dates = initStartStop();
		LocalDate start = dates.getStart();
		LocalDate stop = dates.getStop();
		PayperiodRepository r = repos.getPayPeriod();
		List<Payperiod> p = r.findAllByStartBetweenOrderByStartAsc(start,stop);
		Payperiod pobj = null;
		if (p != null) {
			if (p.isEmpty()) {
				Payperiod np = new Payperiod();
				np.setStart(start);
				np.setStop(stop);

				Intable inobj = new Intable();
				try {
					IntableRepository inr = repos.getIntable();
					inr.saveAndFlush(inobj);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				np.setIna(inobj);

				Outtable oobj = new Outtable();
				try {
					OuttableRepository outr = repos.getOuttable();
					outr.saveAndFlush(oobj);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				np.setOuta(oobj);

				r.save(np);
				pobj = np;
			} else {
				pobj = p.get(0);
			}
		}
		return pobj;
	}
	public List<String> go()
	{
		List<String> ret = new Vector<>();
		if (this.pp == null) {
			ret.add("Couldn't get dates.");
		} else {
			process(ret);
		}
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

	private void updatePp() {
		InAction ina = new InAction(repos);
		OutAction outa = new OutAction(repos);

		System.out.println("Updating in and out...");
		ina.performAction(this.pp);
		outa.performAction(this.pp);
	}

	private boolean importData(boolean doSave, List<String> err)
	{
		for (Iimport I : data) {
			if (!I.importData(stmts,doSave,err))
				return false;
		}

		if (doSave) {
			updatePp();
		}
		return true;
	}
	protected StartStop initStartStop() {
		StartStop ret = new StartStop();
		try {
			String fileName = checkUtil.getObj(true).getDir() + "/" + "dates.txt";
			String ds = new String(Files.readAllBytes(Paths.get(fileName)));

			List<String> dsl = new Vector<>();
			StringTokenizer st = new StringTokenizer(ds,"\n");
			while (st.hasMoreTokens()) {
				String l = st.nextToken();
				dsl.add(sanitize(l));
			}
			System.out.println("** START " +  dsl.get(0) + " **");
			System.out.println("** STOP " +  dsl.get(1) + " **");
			ret.setStart(DUtil.getStdDate(dsl.get(0)));
			ret.setStop(DUtil.getStdDate(dsl.get(1)));

			if (ret.getStart() == null) {
				System.out.println("*** COULD NOT SET START DATE!!!");
				return null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return ret;
	}
	private String sanitize(String str) {
		String ret = "";
		boolean on = false;
		int len = str.length();
		int i = 0;
		for (i=0;i<len;i++) {
			char c = str.charAt(i);
			if (c == '"') {
				on = !on;
			} else {
				if (!on || (c != ',')) {
					ret = ret.concat(String.valueOf(c));
				}
			}
		}

		return ret;
	}
}

