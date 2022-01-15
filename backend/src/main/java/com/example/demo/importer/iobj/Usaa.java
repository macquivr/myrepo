package com.example.demo.importer.iobj;


import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.Repos;
import com.example.demo.importer.data.pdf.*;
import java.util.UUID;

public class Usaa extends CBase {
	
	public Usaa(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid, r,dto);
		
		fname = "usaa.pdf";
		ltype = 8;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException { return new PdfDataUsaa(this,data); }
	
	public void setCStmt(Statements stmts,Statement stmt) 
	{
		stmts.addStatement(stmt);
	}
}
