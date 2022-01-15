package com.example.demo.importer.iobj;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;

import java.util.UUID;

public class EmmaSave extends CSBase {
	
	public EmmaSave(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r, dto);

		etype = CsbEType.EMMASAVE;
		fname = "EmmaSave.pdf";
		ltype = 4;
		credit = false;
	}
	
	public void attachStatement(Statements stmts,Statement stmt) 
	{
		stmt.setStatements(stmts);
		stmts.addStatement(stmt);
	}
}
