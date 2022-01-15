package com.example.demo.importer.iobj;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.domain.Stype;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.*;
import com.example.demo.importer.data.NData;
import com.example.demo.importer.data.pdf.*;
import java.util.UUID;
import java.util.List;

public abstract class CBase extends IBase {
	
	public abstract void setCStmt(Statements stmts,Statement stmt);
	public abstract PBase makePBase() throws BadDataException;
	public CBase(UUID uuid,Repos r, ImportDTO dto)
	{
		super(uuid,r, dto);
	}

	public boolean validateFile(List<String> ret) { 
		try {
			makePBase().go();
		} catch (BadDataException ex) {
			ret.add("Bad Data " + ex.getMessage());
			return false;
		}
		
		data.getStmt().setLtype(getLtype(ltype));
		data.getStmt().setCredit(credit);
		if (data.getStmt().getFee() == null)
			data.getStmt().setFee(0.0);
		return true;
	}
	
	@Override
	public void setStype(List<String> ret)
	{
		for (NData d : data.getData()) {
			Stype m = repos.getStypeRepository().findByName("CreditTrans");
			d.setStype(m);
		}
	}
	
	public boolean makeData(Statements stmts, List<String> err)
	{
		SaveO obj = new SaveO(this, ltype, data, imdata,err);
	
		return obj.makeData(stmts);
	}

	public void attachStatement(Statements stmts,Statement stmt)
	{
		stmt.setStatements(stmts);
		setCStmt(stmts,stmt);
	}
}
