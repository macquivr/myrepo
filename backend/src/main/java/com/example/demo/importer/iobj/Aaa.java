package com.example.demo.importer.iobj;


import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Repos;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.data.pdf.PBase;
import com.example.demo.importer.data.pdf.PdfDataAaa;

import java.util.UUID;

public class Aaa extends CBase {
	
	public Aaa(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);
		
		fname = "aaa.pdf";
		ltype = 10;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException {
		return new PdfDataAaa(this,data);
	}
	
	public void setCStmt(Statements stmts,Statement stmt) 
	{
		stmts.addStatement(stmt);
	}
}
