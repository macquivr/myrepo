package com.example.demo.importer.iobj;


import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Repos;
import com.example.demo.importer.data.pdf.*;
import com.example.demo.importer.BadDataException;
import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.List;

public class UtilImport extends CBase {
	private static final Logger log = LoggerFactory.getLogger(UtilImport.class);
	private PdfDataCiti obj = null;
	private UtilitiesRepository urepo = null;

	public UtilImport(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);

		fname = "citi.pdf";
		ltype = 13;
		credit = true;
	}

	public void setRepo(UtilitiesRepository u) { urepo = u; }

	public PBase makePBase() throws BadDataException
	{
		obj = new PdfDataCiti(this,data);
		return obj;
	}

	@Override
	public boolean importData(Statements stmts, boolean doSave, List<String> err) {
		if (doSave)
			updateUtilRepo(stmts);

		return super.importData(stmts,doSave,err);
	}

	public void setCStmt(Statements stmts, Statement stmt)
	{
		stmts.addStatement(stmt);
	}

	private void updateUtilRepo(Statements stmts)
	{
		Utilities util = new Utilities();
		util.setCable(obj.getCable() * (-1));
		util.setElectric(obj.getElectric() * (-1));
		util.setCell(obj.getCell() * (-1));
		util.setDstr(stmts.getName());
		util.setDate(stmts.getStmtdate());
		urepo.save(util);
	}
}
