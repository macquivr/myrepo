package com.example.demo.importw.iobj;


import com.example.demo.domain.Payperiod;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Repos;
import com.example.demo.importer.BadDataException;
import com.example.demo.importw.data.csv.PBase;
import com.example.demo.importw.data.csv.CsvDataAaa;

import java.util.UUID;

public class Aaa extends CBasew {
	
	public Aaa(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);
		
		fname = "aaa.csv";
		ltype = 10;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException {
		return new CsvDataAaa(this,data);
	}
	
}
