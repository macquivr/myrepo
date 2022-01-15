package com.example.demo.importer.iobj;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;
import java.util.UUID;

public class Slush extends CSBase {

	public Slush(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);

		etype = CsbEType.SLUSH;
		fname = "Slush.pdf";
		ltype = 12;
		credit = false;
	}
	
	public void attachStatement(Statements stmts,Statement stmt) 
	{
		stmt.setStatements(stmts);
		stmts.addStatement(stmt);
	}
}
