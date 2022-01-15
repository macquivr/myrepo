package com.example.demo.importer.iobj;


import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Repos;
import com.example.demo.importer.data.pdf.*;
import com.example.demo.importer.BadDataException;
import java.util.UUID;

public class CapitalOne extends CBase {
	
	public CapitalOne(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);
		
		fname = "capone.pdf";
		ltype = 7;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException
	{
		return new PdfDataCapOne(this,data);
	}
	
	public void setCStmt(Statements stmts,Statement stmt) 
	{
		stmts.addStatement(stmt);
	}
}
