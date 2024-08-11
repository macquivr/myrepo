package com.example.demo.importw.iobj;

import com.example.demo.domain.Payperiod;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.Repos;
import com.example.demo.importw.data.csv.CsvDataCapOne;
import com.example.demo.importer.BadDataException;
import com.example.demo.importw.data.csv.PBase;

import java.util.UUID;

public class CapitalOne extends CBasew {
	
	public CapitalOne(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);
		
		fname = "capone.csv";
		ltype = 7;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException
	{
		return new CsvDataCapOne(this,data);
	}
	
}
