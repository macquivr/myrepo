package com.example.demo.importw.iobj;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;
import java.util.UUID;

public class MainAcct extends CSBasew {
	
	public MainAcct(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);

		etype = CsbEType.MAIN;
		fname = "Main.csv";
		ltype = 3;
		credit = false;
	}
	
}
