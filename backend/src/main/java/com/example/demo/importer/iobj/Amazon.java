package com.example.demo.importer.iobj;

import java.util.UUID;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.*;
import com.example.demo.importer.data.pdf.*;

public class Amazon extends CBase {
	
	public Amazon(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);
		
		fname = "amazon.pdf";
		ltype = 9;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException {
		return new PdfDataAmz(this,data);
	}
	
	public void setCStmt(Statements stmts,Statement stmt) 
	{
		stmts.addStatement(stmt);
	}
}
