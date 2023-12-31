package com.example.demo.importer.iobj;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.*;
import com.example.demo.importer.data.PdfDataM;
import java.util.UUID;
import java.util.List;

public class MerrilLynch extends IBase {
	
	public MerrilLynch(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);
		
		fname = "ml.pdf";
		ltype = 11;
		credit = false;
	}
	
	public boolean validateFile(List<String> ret) { 
		try {
			new PdfDataM(this,data,checkUtil.getObj().getDir()).go();
		} catch (BadDataException ex) {
			ret.add("Bad Data " + ex.getMessage());
			return false;
		}
		boolean c = doChecks(ret);
		if (!c) 
			return false;
		
		data.getStmt().setLtype(getLtype(ltype));
		data.getStmt().setCredit(credit);
		if (data.getStmt().getFee() == null)
			data.getStmt().setFee(0.0);
		return true;
	}
	
	public boolean makeData(Statements stmts, List<String> err)
	{
		SaveO obj = new SaveO(this, ltype, data, imdata,err);
	
		return obj.makeData();
	}

	public void attachStatement(Statements stmts,Statement stmt)
	{
		stmt.setStatements(stmts);
		stmts.addStatement(stmt);
	}
}
