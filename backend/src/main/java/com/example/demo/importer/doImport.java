package com.example.demo.importer;

import com.example.demo.actions.BudgetSetAction;
import com.example.demo.actions.MlBalanceAction;
import com.example.demo.dto.ImportDTO;
import com.example.demo.reports.postimport.balanceReport;
import com.example.demo.reports.postimport.ocReport;
import com.example.demo.reports.postimport.outReport;
import com.example.demo.repository.CsbtRepository;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.MltRepository;
import com.example.demo.repository.StatementsRepository;
import com.example.demo.state.importer.ImportState;
import com.example.demo.utils.Utils;
import com.example.demo.utils.runner.OcMaintenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.mydate.DUtil;
import com.example.demo.importer.iobj.*;
import com.example.demo.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class doImport extends importBase {
	private static final Logger log = LoggerFactory.getLogger(doImport.class);
	private List<Iimport> data = null;
	private Statements stmts = null;
	private final Repos repos;
	private final ImportDTO idto;

	public doImport(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid);

		this.idto = dto;
		this.repos = r;
		initStatements();
		data = new Vector<>();
		UtilImport util = new UtilImport(uuid,repos,idto);
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
			StatementsRepository sr = repos.getStatementsRepository();
			sr.save(stmts);
			ret = importData(true, err);
			if (ret) {
				doBudget();
				//doOc();
				//doBalance();
				//doOut();
				doMlBalance();
				doCsbBal();
				doMltBal();
			}
		}
		return ret;
	}

	private double calculateCsbIn(LocalDate start, LocalDate stop) {
		LedgerRepository lrepo = repos.getLedgerRepository();
		List<Ledger> data = lrepo.findAllByTransdateBetweenOrderByTransdateAsc(start, stop);
		double ret = 0;

		for (Ledger l : data) {
			if ((l.getLtype().getId() == 3) ||
					(l.getLtype().getId() == 5) ||
					(l.getLtype().getId() == 6) ||
					(l.getLtype().getId() == 12) ||
					(l.getLtype().getId() == 14)) {
				if (l.getAmount() > 0) {
					ret += l.getAmount();
				}
			}
		}
		return Utils.convertDouble(ret);
	}

	private double calculateCsbOut(LocalDate start, LocalDate stop) {
		LedgerRepository lrepo = repos.getLedgerRepository();
		List<Ledger> data = lrepo.findAllByTransdateBetweenOrderByTransdateAsc(start, stop);
		double ret = 0;

		for (Ledger l : data) {
			if ((l.getLtype().getId() == 3) ||
					(l.getLtype().getId() == 5) ||
					(l.getLtype().getId() == 6) ||
					(l.getLtype().getId() == 12) ||
					(l.getLtype().getId() == 14)) {
				if (l.getAmount() < 0) {
					ret += l.getAmount();
				}
			}
		}
		return Utils.convertDouble(ret);
	}

	private void doCsbBal() {
		CsbtRepository crepo = repos.getCsbtRepository();
		LocalDate start = this.stmts.getStmtdate().minusMonths(1);
		LocalDate stop = this.stmts.getStmtdate();

		List<Csbt> data = crepo.findAllByDtBetweenOrderByDtAsc(start,stop);
		Csbt prev = data.get(0);

		start = this.stmts.getStmtdate();
		stop = this.stmts.getStmtdate().plusMonths(1);

		Csbt newObj = new Csbt();
		double ina = calculateCsbIn(start,stop);
		double outa = calculateCsbOut(start,stop);

		newObj.setDt(this.stmts.getStmtdate());
		newObj.setIna(ina);
		newObj.setOuta(outa);

		double balance = ina + outa + prev.getBalance();
		newObj.setBalance(Utils.convertDouble(balance));

		crepo.save(newObj);
	}

	private double calculateMlIn(LocalDate start, LocalDate stop) {
		LedgerRepository lrepo = repos.getLedgerRepository();
		List<Ledger> data = lrepo.findAllByTransdateBetweenOrderByTransdateAsc(start, stop);
		double ret = 0;

		for (Ledger l : data) {
			if ((l.getLtype().getId() == 11) && (l.getAmount() > 0)) {
				ret += l.getAmount();
			}
		}
		return Utils.convertDouble(ret);
	}

	private double calculateMlOut(LocalDate start, LocalDate stop) {
		LedgerRepository lrepo = repos.getLedgerRepository();
		List<Ledger> data = lrepo.findAllByTransdateBetweenOrderByTransdateAsc(start, stop);
		double ret = 0;

		for (Ledger l : data) {
			if ((l.getLtype().getId() == 11) && (l.getAmount() < 0)) {
				ret += l.getAmount();
			}
		}
		return Utils.convertDouble(ret);
	}

	private void doMltBal() {
		MltRepository mrepo = repos.getMltRepository();
		LocalDate start = this.stmts.getStmtdate().minusMonths(1);
		LocalDate stop = this.stmts.getStmtdate();

		List<Mlt> data = mrepo.findAllByDtBetweenOrderByDtAsc(start,stop);
		Mlt prev = data.get(0);

		start = this.stmts.getStmtdate();
		stop = this.stmts.getStmtdate().plusMonths(1);

		Mlt newObj = new Mlt();
		double ina = calculateMlIn(start,stop);
		double outa = calculateMlOut(start,stop);

		newObj.setDt(this.stmts.getStmtdate());
		newObj.setIna(ina);
		newObj.setOuta(outa);

		double balance = ina + outa + prev.getBalance();
		newObj.setBalance(Utils.convertDouble(balance));

		mrepo.save(newObj);
	}

	private void doMlBalance() {
		MlBalanceAction obj = new MlBalanceAction(this.repos);

		try {
			obj.go(null);
		} catch (Exception ex) {
			// ignore
		}
	}

	private void doBudget() {
		BudgetSetAction bs = new BudgetSetAction(repos);
		bs.doBudgets(this.stmts);
	}

	private void doOc() {

		OcMaintenance ocSave = new OcMaintenance(repos,repos.getOcRepository());
		ocSave.go(stmts.getId());
		ocReport obj = new ocReport(repos, stmts.getId());
		try {
			obj.go();
		} catch (Exception ex) {
			// ignore
		}
	}

	private void doBalance() {
		balanceReport obj = new balanceReport(repos, stmts.getId());
		try {
			obj.go();
		} catch (Exception ex) {
			// ignore
		}
	}

	private void doOut() {
		outReport obj = new outReport(repos, stmts.getId());
		try {
			obj.go();
		} catch (Exception ex) {
			// ignore
		}
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

