package com.example.demo.importw.iobj;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;

import java.util.UUID;

public class Annual extends CSBasew {

	public Annual(UUID uuid, Repos r, ImportDTO dto, Payperiod pp)
	{	
		super(uuid, r, dto, pp);

		etype = CsbEType.ANNUAL;
		fname = "Annual.csv";
		ltype = 14;
		credit = false;
	}
}
